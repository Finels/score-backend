package com.fasteam.service;

import com.fasteam.dao.AccidentDao;
import com.fasteam.dao.CollectionDao;
import com.fasteam.dao.RepairDao;
import com.fasteam.dto.PaginationResult;
import com.fasteam.entity.Accident;
import com.fasteam.entity.Collection;
import com.fasteam.entity.Repair;
import com.fasteam.service.intf.AccidentServiceIntf;
import com.fasteam.service.intf.RepairServiceIntf;
import com.fasteam.tools.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
public class RepairService implements RepairServiceIntf {
    @Autowired
    private RepairDao repairDao;
    @Autowired
    private CollectionDao collectionDao;

    @Override
    public PaginationResult getByPagination(String conditionLike, int page, int limit) {
        int start = (page - 1) * limit;
        int total = 0;
        List<Repair> repairList = null;
        if (StringUtils.isEmpty(conditionLike)) {
            repairList = repairDao.getByPagnation(start, limit);
            total = Integer.valueOf(repairDao.count() + "");
        } else {
            repairList = repairDao.getByWherePagination(conditionLike, start, limit);
            total = repairDao.countByWherePagination(conditionLike);
        }
        repairList.forEach(r -> {
            Collection collection = collectionDao.findAllById(r.getCid());
            r.setCname(collection.getName());
            r.setCcode(collection.getCode());
        });

        return PaginationResult.set(repairList, total);
    }

    @Override
    public void persist(Repair repair) {
        //需要查出藏品信息
        repairDao.save(repair);
    }


}
