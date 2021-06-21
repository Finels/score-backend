package com.fasteam.dao;

import com.fasteam.entity.Accident;
import com.fasteam.entity.Destroy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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
public interface DestroyDao extends JpaRepository<Destroy, String> {
    @Query(value = "select * from accident where `ccode` like concat('%',:condition,'%') or `cname` like concat('%',:condition,'%') limit :start,:end ", countQuery = "select count(1) from accident where `ccode` like concat('%',:condition,'%') or `cname` like concat('%',:condition,'%') ", nativeQuery = true)
    Page<Destroy> getByWherePagination(@Param("condition") String condition, Pageable pageable);
}
