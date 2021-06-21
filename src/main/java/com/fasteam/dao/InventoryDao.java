package com.fasteam.dao;

import com.fasteam.entity.Inventory;
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
public interface InventoryDao extends JpaRepository<Inventory, String> {
    @Query(value = "select * from inventory  limit ?1,?2 ", nativeQuery = true)
    List<Inventory> getByPagnation(int start, int end);

    @Query(value = "select a.* from inventory a left join collection b on b.id = a.cid where b.`code` like concat('%',:condition,'%') or b.`name` like concat('%',:condition,'%') limit :start,:end ", nativeQuery = true)
    List<Inventory> getByWherePagination(@Param("condition") String condition, @Param("start") int start, @Param("end") int end);

    @Query(value = "select count(1) from inventory a left join collection b on b.id = a.cid where b.`code` like concat('%',:condition,'%') or b.`name` like concat('%',:condition,'%') ", nativeQuery = true)
    int countByWherePagination(@Param("condition") String condition);

}
