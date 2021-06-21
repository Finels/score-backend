package com.fasteam.dao;

import com.fasteam.entity.Collection;
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
public interface StoreDao extends JpaRepository<Store, String> {
    @Query(value = "select * from warehouse order by `code` desc,floor desc,location desc limit ?1,?2 ", nativeQuery = true)
    List<Store> getByPagnation(int start, int end);

    @Query(value = "select * from warehouse where `code` like concat('%',:code,'%') limit :start,:end ", nativeQuery = true)
    List<Store> getByCodePagination(@Param("code") String code, @Param("start") int start, @Param("end") int end);

    @Query(value = "select count(1) from warehouse where `code` like concat('%',:code,'%') ", nativeQuery = true)
    int countByCodePagination(@Param("code") String code);

    @Query(value = "select * from warehouse where `code` like concat('%',:code,'%') and `floor` like concat('%',:floor,'%') limit :start,:end ", nativeQuery = true)
    List<Store> getByCodeAndFloorPagination(@Param("code") String code, @Param("floor") String floor, @Param("start") int start, @Param("end") int end);

    @Query(value = "select count(1) from warehouse where `code` like concat('%',:code,'%') and `floor` like concat('%',:floor,'%')", nativeQuery = true)
    int countByCodeAndFloorPagination(@Param("code") String code, @Param("floor") String floor);

    @Query(value = "select * from warehouse where `code` like concat('%',:code,'%') and `floor` like concat('%',:floor,'%') and `location` like concat('%',:location,'%') limit :start,:end ", nativeQuery = true)
    List<Store> getByCodeAndFloorAndLocationPagination(@Param("code") String code, @Param("floor") String floor, @Param("location") String location, @Param("start") int start, @Param("end") int end);

    @Query(value = "select count(1) from warehouse where `code` like concat('%',:code,'%') and `floor` like concat('%',:floor,'%') and `location` like concat('%',:location,'%')", nativeQuery = true)
    int countByCodeAndFloorAndLocationPagination(@Param("code") String code, @Param("floor") String floor, @Param("location") String location);

    Store findAllById(String id);

    @Query(value = "select b.* from business a left join warehouse b on b.id = a.store_id where a.cid = :cid and b.name like concat('%',:condition,'%') ", nativeQuery = true)
    List<Store> findCurrentStore(@Param("cid") String cid, @Param("condition") String name);

    Store findFirstByCodeAndFloorAndLocation(String code, String floor, String location);

}
