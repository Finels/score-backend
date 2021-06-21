package com.fasteam.service;

import com.fasteam.dao.*;
import com.fasteam.dto.PaginationResult;
import com.fasteam.entity.*;
import com.fasteam.service.intf.AccidentServiceIntf;
import com.fasteam.service.intf.DestroyServiceIntf;
import com.fasteam.tools.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

import static com.fasteam.constant.BusinessConstant.*;
import static com.fasteam.constant.BusinessConstant.TAKE_OUT_STORE;

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
public class DestroyService implements DestroyServiceIntf {
    @Autowired
    private DestroyDao destroyDao;
    @Autowired
    private CollectionDao collectionDao;
    @Autowired
    private BusinessDao businessDao;
    @Autowired
    private BusinessCheckDao checkDao;

    @Override
    public PaginationResult getByPagination(String conditionLike, int page, int limit) {
        if (page > 0) {
            page = page - 1;
        }
        Page<Destroy> destroyList = null;
        if (StringUtils.isEmpty(conditionLike)) {
            destroyList = destroyDao.findAll(PageRequest.of(page, limit));
        } else {
            destroyList = destroyDao.getByWherePagination(conditionLike, PageRequest.of(page, limit));
        }
        destroyList.getContent().forEach(r -> {
            Collection collection = collectionDao.findAllById(r.getCid());
            r.setCname(collection.getName());
            r.setCcode(collection.getCode());
        });
        return PaginationResult.set(destroyList.getContent(), Integer.valueOf(destroyList.getTotalElements() + ""));
    }

    @Override
    public void persist(Destroy destroy) throws Exception {
        //操作库存
        Business business = businessDao.findByCidAndStoreId(destroy.getCid(), destroy.getStoreId());
        if (ObjectUtils.isEmpty(business)) {
            throw new Exception("对应库存信息不存在");
        }
        if (business.getCounter() < destroy.getCounter()) {
            throw new Exception("注销失败，注销数量不能大于库存数量");
        }
        Integer count = business.getCounter() - destroy.getCounter();
        if (count > 0) {
            business.setStatus(1); //部分注销
        } else {
            business.setStatus(2); //已注销
        }
        business.setCounter(count);
        businessDao.save(business);
        destroy.setCreateTime(new Date());
        destroy.setCreateBy(AuthUtil.getLoginUser().getUsername());
        destroyDao.save(destroy);
    }

    //提交注销申请，需通过审核才能执行注销操作
    public void submitDestroyRequest(Destroy destroy) throws Exception {
        //操作库存
        Business business = businessDao.findByCidAndStoreId(destroy.getCid(), destroy.getStoreId());
        if (ObjectUtils.isEmpty(business)) {
            throw new Exception("对应库存信息不存在");
        }
        if (business.getCounter() < destroy.getCounter()) {
            throw new Exception("注销失败，注销数量不能大于库存数量");
        }
        //已存在出库或移库申请的时候不能提交注销申请
        BusinessCheck existMove = checkDao.findFirstByCidAndStoreIdAndInoutTypeAndCheckType(destroy.getCid(), destroy.getStoreId(), MOVE_TO_STORE, NOT_REVIEWED);
        BusinessCheck existOut = checkDao.findFirstByCidAndStoreIdAndInoutTypeAndCheckType(destroy.getCid(), destroy.getStoreId(), TAKE_OUT_STORE, NOT_REVIEWED);
        if (!ObjectUtils.isEmpty(existMove) || !ObjectUtils.isEmpty(existOut)) {
            throw new Exception("存在一条待审批记录，请先审核后再操作该库存");
        }
        BusinessCheck check = new BusinessCheck();
        check.setCid(destroy.getCid());
        check.setStoreId(destroy.getStoreId());
        check.setCount(destroy.getCounter());
        check.setBackup(destroy.getBackup());
        //设置出入库标识
        check.setInoutType(DESTROY_TO_STORE);
        //设置状态为未审核
        check.setCheckType(NOT_REVIEWED);
        //todo 设置该条数据可由哪个审核角色审核，目前审核角色只有一个，暂定为1
        check.setCheckRole("1");
        check.setCreateTime(new Date());
        check.setCreateBy(AuthUtil.getLoginUser().getRealName());
        checkDao.save(check);
    }
}
