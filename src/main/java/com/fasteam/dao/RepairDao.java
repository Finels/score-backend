package com.fasteam.dao;

import com.fasteam.entity.Accident;
import com.fasteam.entity.Repair;
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
public interface RepairDao extends JpaRepository<Repair, String> {
    @Query(value = "select a.*,b.name as cname,b.code as ccode from repair a left join collection b on b.id = a.cid limit ?1,?2 ", nativeQuery = true)
    List<Repair> getByPagnation(int start, int end);

    @Query(value = "select a.*,b.name as cname,b.code as ccode from repair a left join collection b on b.id = a.cid where `ccode` like concat('%',:condition,'%') or `cname` like concat('%',:condition,'%') limit :start,:end ", nativeQuery = true)
    List<Repair> getByWherePagination(@Param("condition") String condition, @Param("start") int start, @Param("end") int end);

    @Query(value = "select count(1) from repair a left join collection b on b.id = a.cid where b.`code` like concat('%',:condition,'%') or b.`name` like concat('%',:condition,'%') ", nativeQuery = true)
    int countByWherePagination(@Param("condition") String condition);

}
