package com.fasteam.service;

import com.fasteam.dao.AccidentDao;
import com.fasteam.dao.CollectionDao;
import com.fasteam.dao.StoreDao;
import com.fasteam.dto.PaginationResult;
import com.fasteam.entity.Accident;
import com.fasteam.entity.Collection;
import com.fasteam.entity.Store;
import com.fasteam.service.intf.AccidentServiceIntf;
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
public class AccidentService implements AccidentServiceIntf {
    @Autowired
    private AccidentDao accidentDao;
    @Autowired
    private CollectionDao collectionDao;

    @Override
    public PaginationResult getByPagination(String conditionLike, int page, int limit) {
        int start = (page - 1) * limit;
        int total = 0;
        List<Accident> accidentList = null;
        if (StringUtils.isEmpty(conditionLike)) {
            accidentList = accidentDao.getByPagnation(start, limit);
            total = Integer.valueOf(accidentDao.count() + "");
        } else {
            accidentList = accidentDao.getByWherePagination(conditionLike, start, limit);
            total = accidentDao.countByWherePagination(conditionLike);
        }

        return PaginationResult.set(accidentList, total);
    }

    @Override
    public void persist(Accident accident) throws Exception {
        //需要查出藏品信息
        Collection collection = collectionDao.findAllById(accident.getCid());
        accident.setCcode(collection.getCode());
        accident.setCname(collection.getName());
        accident.setCreateTime(new Date());
        accident.setCreateBy(AuthUtil.getLoginUser().getUsername());
        accidentDao.save(accident);
    }


}
