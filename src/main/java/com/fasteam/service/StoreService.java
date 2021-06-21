package com.fasteam.service;

import com.fasteam.dao.BusinessDao;
import com.fasteam.dao.StoreDao;
import com.fasteam.dto.PaginationResult;
import com.fasteam.entity.Business;
import com.fasteam.entity.Store;
import com.fasteam.service.intf.StoreServiceIntf;
import com.fasteam.tools.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

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
public class StoreService implements StoreServiceIntf {
    @Autowired
    private StoreDao storeDao;
    @Autowired
    private BusinessDao businessDao;

    @Override
    public PaginationResult getByPagination(String code, String floor, String location, int page, int limit) {
        int start = (page - 1) * limit;
        int total = 0;
        List<Store> storeList = null;
        if (StringUtils.isEmpty(code) && StringUtils.isEmpty(floor) && StringUtils.isEmpty(location)) {
            storeList = storeDao.getByPagnation(start, limit);
            total = Integer.valueOf(storeDao.count() + "");
        } else if (StringUtils.isEmpty(floor) && StringUtils.isEmpty(location)) {
            storeList = storeDao.getByCodePagination(code, start, limit);
            total = storeDao.countByCodePagination(code);
        } else if (StringUtils.isEmpty(location)) {
            storeList = storeDao.getByCodeAndFloorPagination(code, floor, start, limit);
            total = storeDao.countByCodeAndFloorPagination(code, floor);
        } else {
            storeList = storeDao.getByCodeAndFloorAndLocationPagination(code, floor, location, start, limit);
            total = storeDao.countByCodeAndFloorAndLocationPagination(code, floor, location);
        }
        storeList.forEach(r -> {
            r.setName(r.getCode() + "-" + r.getFloor() + "-" + r.getLocation());
        });
        return PaginationResult.set(storeList,total);
    }

    @Override
    public void persist(Store store) throws Exception {
        //校验是否已存在
        Store exist = storeDao.findFirstByCodeAndFloorAndLocation(store.getCode(), store.getFloor(), store.getLocation());
        if (StringUtils.isEmpty(store.getId()) && !ObjectUtils.isEmpty(exist)) {
            throw new Exception("仓库信息已存在");
        }
        store.setCreateTime(new Date());
        store.setCreateBy(AuthUtil.getLoginUser().getUsername());
        storeDao.save(store);
    }

    @Override
    public Store getById(String id) {
        Store store = storeDao.findAllById(id);
        if (!ObjectUtils.isEmpty(store)) {
            store.setName(store.getCode() + "-" + store.getFloor() + "-" + store.getLocation());
        }
        return store;
    }

    @Override
    public void delete(String id) throws Exception {
        //判断该仓库是否有库存
        Business business = businessDao.findFirstByStoreId(id);
        if (!ObjectUtils.isEmpty(business)) {
            throw new Exception("无法删除，请先清空该库存");
        }
        storeDao.deleteById(id);
    }


}
