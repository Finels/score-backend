package com.fasteam.service;

import com.fasteam.dao.*;
import com.fasteam.dto.BusinessCollection;
import com.fasteam.dto.InoutBusiness;
import com.fasteam.dto.MoveBusiness;
import com.fasteam.dto.PaginationResult;
import com.fasteam.entity.*;
import com.fasteam.entity.Collection;
import com.fasteam.query.BusinessQuery;
import com.fasteam.service.intf.BusinessServiceIntf;
import com.fasteam.tools.AuthUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;

import static com.fasteam.constant.BusinessConstant.*;

/**
 * Description:  com.crow32.market.appservice.service
 * Copyright: © 2020 Foxconn. All rights reserved.
 * Company: Foxconn
 *
 * @author FL
 * @version 1.0
 * @timestamp 2020/2/20
 */
@Service
@Transactional
public class BusinessService implements BusinessServiceIntf {
    @Autowired
    private BusinessDao businessDao;
    @Autowired
    private CollectionDao collectionDao;
    @Autowired
    private StoreDao storeDao;
    @Autowired
    private InoutRecordDao recordDao;
    @Autowired
    private BusinessCheckDao checkDao;
    @Autowired
    private DestroyService destroyService;

    @Override
    public PaginationResult getByPagination(BusinessQuery query) {
        int page = query.getPage();
        int limit = query.getLimit();
        int start = (page - 1) * limit;
        int total = 0;
        List<Business> result = null;
        if (!isConditionEmpty(query) && StringUtils.isEmpty(query.getStoreId())) {
            result = businessDao.findByWhereNamePagination(query.getName(), query.getTexture(), query.getYears(), query.getCategory(), query.getLevel(), query.getOrigin(), start, limit);
            total = businessDao.countByWhereNamePagination(query.getName(), query.getTexture(), query.getYears(), query.getCategory(), query.getLevel(), query.getOrigin());
        } else if (isConditionEmpty(query) && !StringUtils.isEmpty(query.getStoreId())) {
            result = businessDao.findByWhereStorePagination(query.getStoreId(), start, limit);
            total = businessDao.countByWhereStorePagination(query.getStoreId());
        } else if (!isConditionEmpty(query) && !StringUtils.isEmpty(query.getStoreId())) {
            result = businessDao.findByWhereNameAndStorePagination(query.getName(), query.getTexture(), query.getYears(), query.getCategory(), query.getLevel(), query.getOrigin(), query.getStoreId(), start, limit);
            total = businessDao.countByWhereNameAndStorePagination(query.getName(), query.getTexture(), query.getYears(), query.getCategory(), query.getLevel(), query.getOrigin(), query.getStoreId());
        } else {
            result = businessDao.findByPagination(start, limit);
            total = Integer.valueOf(businessDao.count() + "");
        }
        result.forEach(r -> {
            Collection collection = collectionDao.findAllById(r.getCid());
            r.setCName(collection.getName());
            r.setCcode(collection.getCode());
            Store store = storeDao.findAllById(r.getStoreId());
            r.setStoreName(store.getCode() + "库-" + store.getFloor() + "柜-" + store.getLocation() + "层");
            r.setAdministrator(store.getAdministrator() == null ? "" : store.getAdministrator());
        });
        return PaginationResult.set(result, total);
    }


    /**
     * 移库
     *
     * @param business
     * @throws Exception
     */
    @Override
    public void handleMove(MoveBusiness business) throws Exception {
        Integer counter = business.getCounter();
        Integer beforeInCounter = 0;
        Integer beforeOutCounter = 0;
        Business origin = businessDao.findByCidAndStoreId(business.getCid(), business.getOriginStoreId());
        Business target = businessDao.findByCidAndStoreId(business.getCid(), business.getTargetStoreId());
        beforeOutCounter = origin.getCounter();
        //需要将现有的business记录减去库存数,如果源仓库库存数为0，则删掉这个business记录
        //然后再新增到目标仓库中，如果有记录则更新库存数，如果没有则新增记录
        if (origin.getCounter() < counter) {
            throw new Exception("移库失败，移库数量'" + counter + "' 大于原仓库存数'" + origin.getCounter() + "'");
        } else if (origin.getCounter().equals(counter)) {
            businessDao.delete(origin);
        } else {
            origin.setCounter(origin.getCounter() - counter);
            businessDao.save(origin);
        }
        if (ObjectUtils.isEmpty(target)) {
            beforeInCounter = 0;
            target = new Business();
            target.setCid(business.getCid());
            target.setStoreId(business.getTargetStoreId());
            target.setCounter(counter);
            target.setCreateTime(new Date());
            target.setCreateBy(AuthUtil.getLoginUser().getUsername());
        } else {
            beforeInCounter = target.getCounter();
            target.setCounter(target.getCounter() + counter);
        }
        businessDao.save(target);
        //todo 增加移库记录，即一条入库 一条出库
        //查出藏品信息
        String cName = collectionDao.findAllById(business.getCid()).getName();
        //查出仓库信息
        Store originStore = storeDao.findAllById(origin.getStoreId());
        Store targetStore = storeDao.findAllById(target.getStoreId());
        String originStoreName = originStore.getCode() + "-" + originStore.getFloor() + "-" + originStore.getLocation();
        String targetStoreName = targetStore.getCode() + "-" + targetStore.getFloor() + "-" + targetStore.getLocation();


        //增加入库记录
//        InoutRecord record = new InoutRecord();
//        record.setRecordType("0"); //0入库
//        record.setCollectionId(target.getCid());
//        record.setWarehouseId(target.getStoreId());
//        record.setCounter(counter);
//        record.setBeforeCounter(beforeInCounter);
//        record.setAfterCounter(beforeInCounter + counter);
//        record.setBackup("移库：藏品'" + cName + "' 由仓库'" + originStoreName + "' 移动到仓库'" + targetStoreName + "' ");
//        record.setOptTime(new Date());
//        record.setOptUser(AuthUtil.getLoginUser().getUsername());
//        recordDao.save(record);
//        //增加出库记录
//        InoutRecord record1 = new InoutRecord();
//        record1.setRecordType("1"); //1出库
//        record1.setCollectionId(target.getCid());
//        record1.setWarehouseId(origin.getStoreId());
//        record1.setCounter(counter);
//        record1.setBeforeCounter(beforeOutCounter);
//        record1.setAfterCounter(beforeOutCounter - counter);
//        record1.setBackup("移库：藏品'" + cName + "' 由仓库'" + originStoreName + "' 移动到仓库'" + targetStoreName + "' ");
//        record1.setOptTime(new Date());
//        record1.setOptUser(AuthUtil.getLoginUser().getUsername());
//        recordDao.save(record1);
        //增加移库记录 2021-05-06需求更改，移库不是一条入库一条出库了，变为一条移库记录
        InoutRecord record = new InoutRecord();
        record.setRecordType("2"); //移库
        record.setCollectionId(target.getCid());
        record.setWarehouseId(target.getStoreId());
        record.setCounter(counter);
        record.setBeforeCounter(beforeInCounter);
        record.setAfterCounter(beforeInCounter + counter);
        record.setBackup("移库：藏品'" + cName + "' 由仓库'" + originStoreName + "' 移动到仓库'" + targetStoreName + "' ");
        record.setOptTime(new Date());
        record.setOptUser(AuthUtil.getLoginUser().getUsername());
        recordDao.save(record);
        //更新藏品信息页面的仓库编号
        Collection collection = collectionDao.getOne(business.getCid());
        collection.setWarehouseCode(targetStore.getCode());
        collection.setWarehouseFloor(targetStore.getFloor());
        collection.setWarehouseLocation(targetStore.getLocation());
        collectionDao.save(collection);
    }

    /**
     * 归库，将出库记录重新归库
     *
     * @param record
     * @throws Exception
     */
    @Override
    public void handReturn(InoutRecord record) throws Exception {
        Integer counter = record.getCounter();
        Integer beforeCounter = 0;
        Business target = businessDao.findByCidAndStoreId(record.getCollectionId(), record.getWarehouseId());
        if (!ObjectUtils.isEmpty(target)) {
            beforeCounter = target.getCounter();
            target.setCounter(target.getCounter() + counter);
            businessDao.save(target);
        } else {
            target = new Business();
            target.setCid(record.getCollectionId());
            target.setCounter(counter);
            target.setStoreId(record.getWarehouseId());
            target.setCreateTime(new Date());
            target.setCreateBy(AuthUtil.getLoginUser().getUsername());
            businessDao.save(target);
        }
        //藏品信息更新归库字段，同时更新藏品位置
        Store targetStore = storeDao.findAllById(record.getWarehouseId());
        Collection collection = collectionDao.findAllById(record.getCollectionId());
        collection.setWarehouseCode(targetStore.getCode());
        collection.setWarehouseFloor(targetStore.getFloor());
        collection.setWarehouseLocation(targetStore.getLocation());
        collectionDao.save(collection);
        //增加归库记录
        InoutRecord returnRecord = new InoutRecord();
        returnRecord.setRecordType("3"); //3归库
        returnRecord.setCollectionId(record.getCollectionId());
        returnRecord.setWarehouseId(record.getWarehouseId());
        returnRecord.setCounter(counter);
        returnRecord.setBeforeCounter(beforeCounter);
        returnRecord.setAfterCounter(target.getCounter());
        returnRecord.setOptTime(new Date());
        returnRecord.setOptUser(AuthUtil.getLoginUser().getUsername());
        recordDao.save(returnRecord);
        //原来的那条出库记录的归库字段需要更新
        record.setIsReturn(true);
        recordDao.save(record);
    }

    @Override
    public void handIn(InoutBusiness business) throws Exception {
        Integer counter = business.getCounter();
        Integer beforeCounter = 0;
        Business target = businessDao.findByCidAndStoreId(business.getCid(), business.getStoreId());
        if (!ObjectUtils.isEmpty(target)) {
            beforeCounter = target.getCounter();
            target.setCounter(target.getCounter() + counter);
            businessDao.save(target);
        } else {
            target = new Business();
            target.setCid(business.getCid());
            target.setCounter(counter);
            target.setStoreId(business.getStoreId());
            target.setCreateTime(new Date());
            target.setCreateBy(AuthUtil.getLoginUser().getUsername());
            businessDao.save(target);
        }
        //藏品信息更新是否入库字段，同时更新藏品位置
        Store targetStore = storeDao.findAllById(target.getStoreId());
        Collection collection = collectionDao.findAllById(target.getCid());
        collection.setIsBusiness(1);
        collection.setWarehouseCode(targetStore.getCode());
        collection.setWarehouseFloor(targetStore.getFloor());
        collection.setWarehouseLocation(targetStore.getLocation());
        collectionDao.save(collection);

        //增加入库记录
        InoutRecord record = new InoutRecord();
        record.setRecordType("0"); //0入库
        record.setCollectionId(target.getCid());
        record.setWarehouseId(target.getStoreId());
        record.setCounter(counter);
        record.setBeforeCounter(beforeCounter);
        record.setAfterCounter(target.getCounter());
        record.setBackup(business.getBackup());
        record.setOptTime(new Date());
        record.setOptUser(AuthUtil.getLoginUser().getUsername());
        recordDao.save(record);
    }

    @Override
    public void handOut(InoutBusiness business) throws Exception {
        Integer counter = business.getCounter();
        Integer beforeCounter = 0;
        Business target = businessDao.findByCidAndStoreId(business.getCid(), business.getStoreId());
        if (ObjectUtils.isEmpty(target)) {
            throw new Exception("无法出库，该库存不存在");
        }
        if (counter > target.getCounter()) {
            throw new Exception("出库失败，出库数量大于库存数量" + target.getCounter());
        }
        beforeCounter = target.getCounter();
        target.setCounter(target.getCounter() - counter);
        businessDao.save(target);
        //修改藏品信息的仓库字段为已出库
        Collection collection = collectionDao.findAllById(business.getCid());
        collection.setWarehouseCode("已出库");
        collection.setWarehouseFloor("已出库");
        collection.setWarehouseLocation("已出库");
        collectionDao.save(collection);
        //增加出库记录
        InoutRecord record = new InoutRecord();
        record.setRecordType("1"); //1 出库
        record.setCollectionId(target.getCid());
        record.setWarehouseId(target.getStoreId());
        record.setCounter(counter);
        record.setBeforeCounter(beforeCounter);
        record.setAfterCounter(target.getCounter());
        record.setBackup(business.getBackup());
        record.setOptTime(new Date());
        record.setOptUser(AuthUtil.getLoginUser().getUsername());
        record.setOutOwner(business.getOutOwner());
        record.setOutPerson(business.getOutPerson());
        record.setOutWhere(business.getOutWhere());
        record.setOutReason(business.getOutReason());
        recordDao.save(record);

    }

    @Override
    public Business getById(String id) {
        return null;
    }

    @Override
    public List<Store> getCurrentStore(String cid, String name) {
        return storeDao.findCurrentStore(cid, name);
    }

    @Override
    public List<BusinessCollection> getExportList(BusinessQuery query) {
        List<BusinessCollection> returnData = new ArrayList<>();
        PaginationResult result = this.getByPagination(query);
        List<Business> dataList = result.getLst();
        dataList.stream().forEach(b -> {
            BusinessCollection businessCollection = new BusinessCollection();
            Collection collection = collectionDao.findAllById(b.getCid());
            try {
                Map coBeanMap = new HashMap();
                BeanInfo info = Introspector.getBeanInfo(collection.getClass(), Object.class);
                PropertyDescriptor[] pds = info.getPropertyDescriptors();
                for (PropertyDescriptor pd : pds) {
                    String propertyName = pd.getName();
                    Method m = pd.getReadMethod();
                    Object properValue = m.invoke(collection);
                    coBeanMap.put(propertyName, properValue);
                }
                BeanUtils.populate(businessCollection, coBeanMap);
                businessCollection.setTotal(b.getCounter() + "");
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!ObjectUtils.isEmpty(businessCollection)) {
                Store store = storeDao.findAllById(b.getStoreId());
                businessCollection.setLocation(store.getCode() + "库-" + store.getFloor() + "柜-" + store.getLocation() + "层");
                returnData.add(businessCollection);
            }
        });
        return returnData;
    }

    private boolean isConditionEmpty(BusinessQuery query) {
        boolean tag = false;
        if (StringUtils.isEmpty(query.getName())
                && StringUtils.isEmpty(query.getCategory())
                && StringUtils.isEmpty(query.getLevel())
                && StringUtils.isEmpty(query.getOrigin())
                && StringUtils.isEmpty(query.getTexture())
                && StringUtils.isEmpty(query.getYears())) {
            tag = true;
        } else {
            if (StringUtils.isEmpty(query.getName())) {
                query.setName(null);
            }
            if (StringUtils.isEmpty(query.getCategory())) {
                query.setCategory(null);
            }
            if (StringUtils.isEmpty(query.getLevel())) {
                query.setLevel(null);
            }
            if (StringUtils.isEmpty(query.getOrigin())) {
                query.setOrigin(null);
            }
            if (StringUtils.isEmpty(query.getTexture())) {
                query.setTexture(null);
            }
            if (StringUtils.isEmpty(query.getYears())) {
                query.setYears(null);
            }
        }
        return tag;
    }

    //根据条件查询所有 未审核/审核 的记录
    public PaginationResult listChecked(String checkType, int page, int limit) {
        if (page > 0) {
            page = page - 1;
        }
        Page<BusinessCheck> unchecks = checkDao.findByCheckTypeOrderByCreateTimeDesc(checkType, PageRequest.of(page, limit));
        List<BusinessCheck> uncheckList = unchecks.getContent();
        uncheckList.forEach(r -> {
            Collection collection = collectionDao.findAllById(r.getCid());
            Store store = storeDao.getOne(r.getStoreId());
            r.setCcode(collection.getCode());
            r.setCname(collection.getName());
            String code = StringUtils.isEmpty(store.getCode()) ? "" : store.getCode();
            String floor = StringUtils.isEmpty(store.getFloor()) ? "" : store.getFloor();
            String location = StringUtils.isEmpty(store.getLocation()) ? "" : store.getLocation();
            r.setStoreName(code + "-" + floor + "-" + location);
            if (!StringUtils.isEmpty(r.getTargetStoreId())) {
                Store target = storeDao.getOne(r.getTargetStoreId());
                String targeCode = StringUtils.isEmpty(target.getCode()) ? "" : target.getCode();
                String targetFloor = StringUtils.isEmpty(target.getFloor()) ? "" : target.getFloor();
                String targetFocation = StringUtils.isEmpty(target.getLocation()) ? "" : target.getLocation();
                r.setTargetStoreName(targeCode + "-" + targetFloor + "-" + targetFocation);
            }
        });

        return PaginationResult.set(uncheckList, Integer.valueOf(unchecks.getTotalElements() + ""));
    }

    //提交出入库审核申请
    public void submitInoutCheckRequest(InoutBusiness inoutBusiness, String inoutType) throws Exception {
        //出库时需要验证数量和重复申请
        if (TAKE_OUT_STORE.equals(inoutType)) {
            Business target = businessDao.findByCidAndStoreId(inoutBusiness.getCid(), inoutBusiness.getStoreId());
            if (ObjectUtils.isEmpty(target)) {
                throw new Exception("无法出库，该库存不存在");
            }
            if (inoutBusiness.getCounter() > target.getCounter()) {
                throw new Exception("出库失败，出库数量大于库存数量" + target.getCounter());
            }
            //根据cid和storeId和出库类型去判断是否存在重复操作（比如误点两次导致两笔相同的出库）
            BusinessCheck exist = checkDao.findFirstByCidAndStoreIdAndInoutTypeAndCheckType(inoutBusiness.getCid(), inoutBusiness.getStoreId(), TAKE_OUT_STORE, NOT_REVIEWED);
            if (!ObjectUtils.isEmpty(exist)) {
                throw new Exception("存在一条待审批记录，请先审核后再操作该库存");
            }
        }
        //入库时则不需要判断重复
        BusinessCheck check = new BusinessCheck();
        check.setCid(inoutBusiness.getCid());
        check.setStoreId(inoutBusiness.getStoreId());
        check.setCount(inoutBusiness.getCounter());
        check.setBackup(inoutBusiness.getBackup());
        //设置出入库标识
        check.setInoutType(inoutType);
        //设置状态为未审核
        check.setCheckType(NOT_REVIEWED);
        //todo 设置该条数据可由哪个审核角色审核，目前审核角色只有一个，暂定为1
        check.setCheckRole("1");
        check.setCreateTime(new Date());
        check.setCreateBy(AuthUtil.getLoginUser().getRealName());
        check.setOutOwner(inoutBusiness.getOutOwner());
        check.setOutPerson(inoutBusiness.getOutPerson());
        check.setOutReason(inoutBusiness.getOutReason());
        check.setOutWhere(inoutBusiness.getOutWhere());
        checkDao.save(check);
    }

    //提交移库审核申请
    public void submitMoveCheckRequest(MoveBusiness moveBusiness) throws Exception {
        //移库时需要验证数量
        Business origin = businessDao.findByCidAndStoreId(moveBusiness.getCid(), moveBusiness.getOriginStoreId());
        if (origin.getCounter() < moveBusiness.getCounter()) {
            throw new Exception("移库失败，移库数量'" + moveBusiness.getCounter() + "' 大于原仓库存数'" + origin.getCounter() + "'");
        }
        //根据cid和storeId和出入库类型去判断是否存在重复操作（比如误点两次导致两笔相同的出库）
        BusinessCheck existMove = checkDao.findFirstByCidAndStoreIdAndInoutTypeAndCheckType(moveBusiness.getCid(), moveBusiness.getOriginStoreId(), MOVE_TO_STORE, NOT_REVIEWED);
        BusinessCheck existOut = checkDao.findFirstByCidAndStoreIdAndInoutTypeAndCheckType(moveBusiness.getCid(), moveBusiness.getOriginStoreId(), TAKE_OUT_STORE, NOT_REVIEWED);
        if (!ObjectUtils.isEmpty(existMove) || !ObjectUtils.isEmpty(existOut)) {
            throw new Exception("存在一条待审批记录，请先审核后再操作该库存");
        }
        BusinessCheck check = new BusinessCheck();
        check.setCid(moveBusiness.getCid());
        check.setStoreId(moveBusiness.getOriginStoreId());
        check.setTargetStoreId(moveBusiness.getTargetStoreId());
        check.setCount(moveBusiness.getCounter());
        check.setBackup(moveBusiness.getBackup());
        //设置移库标识
        check.setInoutType(MOVE_TO_STORE);
        //设置状态为未审核
        check.setCheckType(NOT_REVIEWED);
        //todo 设置该条数据可由哪个审核角色审核，目前审核角色只有一个，暂定为1
        check.setCheckRole("1");
        check.setCreateTime(new Date());
        check.setCreateBy(AuthUtil.getLoginUser().getRealName());
        checkDao.save(check);
    }

    //执行审核操作
    @Transactional
    public void doCheck(String id) throws Exception {
        BusinessCheck check = checkDao.getOne(id);
        if (PUT_IN_STORE.equals(check.getInoutType())) {
            this.handIn(check.toInoutBean());
        } else if (TAKE_OUT_STORE.equals(check.getInoutType())) {
            this.handOut(check.toInoutBean());
        } else if (MOVE_TO_STORE.equals(check.getInoutType())) {
            this.handleMove(check.toMoveBean());
        } else if (DESTROY_TO_STORE.equals(check.getInoutType())) {
            destroyService.persist(check.toDestroyBean());
        }
        //执行完毕后更新这条记录为已审核状态
        check.setCheckType(REVIEWED);
        check.setCreateTime(new Date());
        checkDao.save(check);
    }

    //删除审批操作
    @Transactional
    public void doDel(String id) throws Exception {
        BusinessCheck check = checkDao.getOne(id);
        if (!ObjectUtils.isEmpty(check)) {
            check.setCheckType(REJECT);
            check.setCreateTime(new Date());
            checkDao.save(check);
        }
    }
}
