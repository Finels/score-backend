package com.fasteam.dao;

import com.fasteam.entity.BusinessCheck;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Description:  com.crow32.market.appservice.dao
 * Copyright: © 2020 Foxconn. All rights reserved.
 * Company: Foxconn
 *
 * @author FL
 * @version 1.0
 * @timestamp 2020/2/20
 */
@Repository
public interface BusinessCheckDao extends JpaRepository<BusinessCheck, String> {

    Page<BusinessCheck> findByCheckTypeOrderByCreateTimeDesc(String checkType, Pageable pageable);

    BusinessCheck findFirstByCidAndStoreIdAndInoutTypeAndCheckType(String cid, String storeId, String inoutType,String checkType);
}
