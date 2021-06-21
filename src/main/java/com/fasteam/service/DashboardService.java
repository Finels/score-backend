package com.fasteam.service;

import com.fasteam.dao.*;
import com.fasteam.entity.*;
import com.fasteam.service.intf.AccidentServiceIntf;
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
public class DashboardService {
    @Autowired
    private AccidentDao accidentDao;
    @Autowired
    private CollectionDao collectionDao;
    @Autowired
    private BusinessDao businessDao;
    @Autowired
    private InoutRecordDao recordDao;
    @Autowired
    private StoreDao storeDao;

    public Dashboard getNums() {
        Dashboard dashboard = new Dashboard();
        dashboard.setCountNum1(String.valueOf(collectionDao.count()));
        dashboard.setCountNum2(String.valueOf(businessDao.sumCounter()));
        dashboard.setCountNum3(String.valueOf(recordDao.count()));
        dashboard.setCountNum4(String.valueOf(accidentDao.count()));
        return dashboard;
    }

    public Dashboard getList() {
        Dashboard dashboard = new Dashboard();
        dashboard.setLst(recordDao.getByOptTimeDesc10());
        return dashboard;
    }

    public Dashboard getTops() {
        Dashboard dashboard = new Dashboard();
        List<Business> result = businessDao.getByCounterDesc10();
        result.forEach(r -> {
            Store store = storeDao.findAllById(r.getStoreId());
            r.setStoreName(store.getCode() + "库-" + store.getFloor() + "柜-" + store.getLocation()+"层");
        });
        dashboard.setTops(result);
        return dashboard;
    }


}
