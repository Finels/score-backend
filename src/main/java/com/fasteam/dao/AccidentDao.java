package com.fasteam.dao;

import com.fasteam.entity.Accident;
import com.fasteam.entity.Store;
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
public interface AccidentDao extends JpaRepository<Accident, String> {
    @Query(value = "select * from accident limit ?1,?2 ", nativeQuery = true)
    List<Accident> getByPagnation(int start, int end);

    @Query(value = "select * from accident where `ccode` like concat('%',:condition,'%') or `cname` like concat('%',:condition,'%') limit :start,:end ", nativeQuery = true)
    List<Accident> getByWherePagination(@Param("condition") String condition, @Param("start") int start, @Param("end") int end);

    @Query(value = "select count(1) from accident where `ccode` like concat('%',:condition,'%') or `cname` like concat('%',:condition,'%') ", nativeQuery = true)
    int countByWherePagination(@Param("condition") String condition);

}
