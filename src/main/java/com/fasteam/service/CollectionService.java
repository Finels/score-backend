package com.fasteam.service;

import com.fasteam.dao.*;
import com.fasteam.dto.HighLevelQueryBean;
import com.fasteam.dto.PaginationResult;
import com.fasteam.entity.*;
import com.fasteam.service.intf.CollectionServiceIntf;
import com.fasteam.tools.AuthUtil;
import com.fasteam.tools.FileUtil;
import io.swagger.models.auth.In;
import org.csource.common.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description:  com.crow32.market.appservice.service
 * Copyright: © 2020 Foxconn. All rights reserved.
 * Company: Foxconn
 *
 * @author FL
 * @version 1.0
 * @timestamp 2020/2/20
 */
@SuppressWarnings("Duplicates")
@Service
@Transactional
public class CollectionService implements CollectionServiceIntf {
    @Autowired
    private CollectionDao collectionDao;
    @Autowired
    private CollectionImgDao imgDao;
    @Autowired
    private FileUtil fileUtil;
    @Value("${fastdfs.nginx.url}")
    private String nginxUrl;
    @Autowired
    private BusinessDao businessDao;
    @Autowired
    private StoreDao storeDao;
    @Autowired
    private InoutRecordDao recordDao;
    @Autowired
    private HighLevelQueryCollectionDao highLevelDao;

    @Override
    public PaginationResult getByPagination(String conditionLike, int page, int limit) {
        int start = (page - 1) * limit;
        int total = 0;
        List<Collection> collectionList = null;
        if (StringUtils.isEmpty(conditionLike)) {
            collectionList = collectionDao.getByPagnation(start, limit);
            total =     Integer.valueOf(collectionDao.count() + "");
        } else {
            collectionList = collectionDao.getByWherePagination(conditionLike, start, limit);
            total = collectionDao.countByWhere(conditionLike);
        }
        collectionList.forEach(r -> {
            List<CollectionImg> imgs = imgDao.findUrlByCidOrderByCreateTimeDesc(r.getId());
            r.setImgUrls(imgs);
        });
        return PaginationResult.set(collectionList, total);
    }

    @Override
    public void persist(Collection collection) throws Exception {
        final String userName = AuthUtil.getLoginUser().getUsername();
        if (collection.getLength() <= 0 || collection.getWidth() <= 0 || collection.getHeight() <= 0 || collection.getMass() <= 0) {
            throw new Exception("录入失败，尺寸必须大于0");
        }
        String cid = null;
        //如果是修改，则需要核对图片数量，是否需要删除图片
        if (!StringUtils.isEmpty(collection.getId())) {
            cid = collection.getId();
            //先把旧数据删除
            imgDao.deleteByCid(collection.getId());
            List<CollectionImg> editNewImgs = collection.getImgUrls();
            //然后依次插入新数据
            editNewImgs.forEach(r -> {
                r.setCid(collection.getId());
                r.setCreateBy(userName);
                r.setCreateTime(new Date());
                imgDao.save(r);
            });
            collectionDao.save(collection);
        } else {
            //根据总登记号判断是否重复录入
            Collection exist = collectionDao.findFirstByCode(collection.getCode());
            if (!ObjectUtils.isEmpty(exist) && !exist.getIsDel()) {
                throw new Exception("该藏品已存在：总登记号重复");
            }
            collection.setCreateTime(new Date());
            collection.setCreateBy(userName);
            Collection pojo = collectionDao.save(collection);
            cid = pojo.getId();
            List<CollectionImg> imgs = collection.getImgUrls();
            if (imgs != null && !imgs.isEmpty()) {
                imgs.forEach(r -> {
                    r.setCid(pojo.getId());
                    r.setCreateBy(userName);
                    r.setCreateTime(new Date());
                    imgDao.save(r);
                });
            }
        }
        if (collection.getIsBusiness() != 1) {
            //根据是否已入库字段来判断是否继续下面的操作
            //保存完成之后需要再保存一份到库存表business，相当于初始入库操作
            //需要根据是否有输入库存信息再决定是否入库
            if (!StringUtils.isEmpty(collection.getWarehouseCode().trim())
                    && !StringUtils.isEmpty(collection.getWarehouseFloor().trim())
                    && !StringUtils.isEmpty(collection.getWarehouseLocation().trim())) {
                //先判断是否有仓库信息，如果没有则新增，如果有则取出这条仓库信息
                Store store = storeDao.findFirstByCodeAndFloorAndLocation(collection.getWarehouseCode(), collection.getWarehouseFloor(), collection.getWarehouseLocation());
                if (ObjectUtils.isEmpty(store)) {
                    store = new Store();
                    store.setCode(collection.getWarehouseCode());
                    store.setFloor(collection.getWarehouseFloor());
                    store.setLocation(collection.getWarehouseLocation());
                    store.setCreateTime(new Date());
                    store.setCreateBy(AuthUtil.getLoginUser().getUsername());
                    storeDao.save(store);
                }
                Business business = new Business();
                business.setCid(cid);
                business.setCounter(Integer.valueOf(collection.getBusinessTotal()));
                business.setStoreId(store.getId());
                business.setCreateTime(new Date());
                business.setCreateBy(AuthUtil.getLoginUser().getUsername());
                businessDao.save(business);
                //增加出入库记录
                InoutRecord record = new InoutRecord();
                record.setRecordType("0"); //0入库
                record.setCollectionId(business.getCid());
                record.setWarehouseId(business.getStoreId());
                record.setCounter(business.getCounter());
                record.setBeforeCounter(0);
                record.setAfterCounter(business.getCounter());
                record.setBackup(business.getBackup());
                record.setOptTime(new Date());
                record.setOptUser(AuthUtil.getLoginUser().getUsername());
                recordDao.save(record);

                //同时再更新一下是否入库这个字段
                collection.setIsBusiness(1);
                collection.setId(cid);
                collectionDao.save(collection);
            }
        }

//        Business business = new Business();
//        business.setCid(pojo.getId());
//        business.setCounter(collection.getCounter());
//        business.setStoreId(collection.getStoreId());
//        business.setCreateTime(new Date());
//        business.setCreateBy(AuthUtil.getLoginUser().getUsername());
//        businessDao.save(business);
    }

    @Override
    public Collection getById(String id) {
        Collection result = collectionDao.findAllById(id);
        result.setImgUrls(imgDao.findUrlByCidOrderByCreateTimeDesc(result.getId()));
        return result;
    }

    @Override
    public void delete(String id) throws Exception {
        //先查询该藏品是否有库存
        Business business = businessDao.findFirstByCid(id);

        if (business != null && business.getCounter() > 0) {
            throw new Exception("无法删除，请先清空该藏品库存");
        } else if (business != null) {
            //删除库存表记录
            businessDao.delete(business);
        }
        //20210506改为逻辑删除
        Collection collection = collectionDao.findAllById(id);
        collection.setIsDel(true);
        collectionDao.save(collection);
        List<CollectionImg> imgs = imgDao.findAllByCid(id);
        imgDao.deleteByCid(id);
        //去文件服务器执行删除
        imgs.forEach(r -> {
            try {
                fileUtil.delete(r.getUrl().replaceAll(nginxUrl, ""));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (MyException e) {
                e.printStackTrace();
            }
        });
    }

    //动态查询
    public PaginationResult dynamicQuery(List<HighLevelQueryBean> queryBeans, int page, int limit) {
        if (page > 0) {
            page = page - 1;
        }
        //构建查询条件
        Specification<Collection> specification = (Specification<Collection>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> andPredicates = new ArrayList<Predicate>(); //and的条件数组
            List<Predicate> orPredicates = new ArrayList<Predicate>(); //or的条件数组
            for (HighLevelQueryBean queryBean : queryBeans) {
                Predicate condition = null;
                switch (queryBean.getModal()) {
                    case "=":
                        condition = criteriaBuilder.equal(root.<String>get(queryBean.getCondition()), queryBean.getValue());
                        break;
                    case ">":
                        condition = criteriaBuilder.gt(root.<Float>get(queryBean.getCondition()), Float.parseFloat(queryBean.getValue()));
                        break;
                    case "<":
                        condition = criteriaBuilder.lt(root.<Float>get(queryBean.getCondition()), Float.parseFloat(queryBean.getValue()));
                        break;
                    case ">=":
                        condition = criteriaBuilder.ge(root.<Float>get(queryBean.getCondition()), Float.parseFloat(queryBean.getValue()));
                        break;
                    case "<=":
                        condition = criteriaBuilder.le(root.<Float>get(queryBean.getCondition()), Float.parseFloat(queryBean.getValue()));
                        break;
                    case "in": {
                        CriteriaBuilder.In<Object> in = criteriaBuilder.in(root.get(queryBean.getCondition()));
                        for (String value : queryBean.getValues()) {
                            in.value(value);//存入值
                        }
                        condition = in;
                    }
                    break;
                    case "not in": {
                        CriteriaBuilder.In<Object> in = criteriaBuilder.in(root.get(queryBean.getCondition()));
                        for (String value : queryBean.getValues()) {
                            in.value(value);//存入值
                        }
                        condition = criteriaBuilder.not(in);
                    }
                    break;
                }
                if (queryBean.getRelation().equals("or")) {
                    orPredicates.add(condition);
                } else {
                    andPredicates.add(condition);
                }
            }
            if (!orPredicates.isEmpty()) {
                Predicate or = criteriaBuilder.or(orPredicates.toArray(new Predicate[orPredicates.size()]));
                andPredicates.add(or);
            }
            return criteriaBuilder.and(andPredicates.toArray(new Predicate[andPredicates.size()]));
        };
        Page<Collection> collections = highLevelDao.findAll(specification, PageRequest.of(page, limit));
        List<Collection> collectionList = collections.getContent();
        collectionList.forEach(r -> {
            List<CollectionImg> imgs = imgDao.findUrlByCidOrderByCreateTimeDesc(r.getId());
            r.setImgUrls(imgs);
        });
        return PaginationResult.set(collectionList, Integer.valueOf(collections.getTotalElements() + ""));
    }
}
