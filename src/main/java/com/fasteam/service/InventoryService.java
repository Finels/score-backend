package com.fasteam.service;

import com.fasteam.dao.CollectionDao;
import com.fasteam.dao.InventoryDao;
import com.fasteam.dao.RepairDao;
import com.fasteam.dto.PaginationResult;
import com.fasteam.entity.Collection;
import com.fasteam.entity.Inventory;
import com.fasteam.entity.Repair;
import com.fasteam.service.intf.InventoryServiceIntf;
import com.fasteam.service.intf.RepairServiceIntf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
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
public class InventoryService implements InventoryServiceIntf {
    @Autowired
    private InventoryDao inventoryDao;
    @Autowired
    private CollectionDao collectionDao;

    @Override
    public PaginationResult getByPagination(String conditionLike, int page, int limit) {
        int start = (page - 1) * limit;
        int total = 0;
        List<Inventory> inventoryList = null;
        if (StringUtils.isEmpty(conditionLike)) {
            inventoryList = inventoryDao.getByPagnation(start, limit);
            total = Integer.valueOf(inventoryDao.count() + "");
        } else {
            inventoryList = inventoryDao.getByWherePagination(conditionLike, start, limit);
            total = inventoryDao.countByWherePagination(conditionLike);
        }
        inventoryList.forEach(r -> {
            Collection collection = collectionDao.findAllById(r.getCid());
            r.setCname(collection.getName());
            r.setCcode(collection.getCode());
        });

        return PaginationResult.set(inventoryList, total);
    }

    @Override
    public void persist(Inventory inventory) {
        //需要查出藏品信息
        inventoryDao.save(inventory);
    }


}
