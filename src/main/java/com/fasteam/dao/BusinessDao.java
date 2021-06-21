package com.fasteam.dao;

import com.fasteam.entity.Business;
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
public interface BusinessDao extends JpaRepository<Business, String> {

    @Query(value = "select a.status,a.cid,b.name as cName,a.counter,a.store_id,c.code as store_name,c.administrator,a.create_time,a.create_by from business a left join collection b on b.id = a.cid left join warehouse c on c.id = a.store_id order by b.code desc limit ?1,?2", nativeQuery = true)
    List<Business> findByPagination(int start, int end);

    @Query(value = "" +
            "select " +
            "a.status,a.cid,b.name as cName," +
            "a.counter,a.store_id,c.code as store_name," +
            "c.administrator,a.create_time,a.create_by " +
            "from business a " +
            "left join collection b on b.id = a.cid " +
            "left join warehouse c on c.id = a.store_id " +
            "where " +
            "case when :name is not null then (b.`name` like concat('%',:name,'%') or b.`code` like concat('%',:name,'%')) " +
            "else 1=1 end " +
            "and " +
            "case when :texture is not null then " +
            "(b.`texture` like concat('%',:texture,'%') " +
            "or b.`texture1` like concat('%',:texture,'%')" +
            "or b.`texture2` like concat('%',:texture,'%')) else 1=1 end " +
            "and case when :years is not null then b.`years` like concat('%',:years,'%') else 1=1 end " +
            "and case when :category is not null then b.`category` like concat('%',:category,'%') else 1=1 end " +
            "and case when :level is not null then b.`level` like concat('%',:level,'%') else 1=1 end " +
            "and case when :origin is not null then b.`origin` like concat('%',:origin,'%') else 1=1 end " +
            "order by b.code desc limit :start,:end", nativeQuery = true)
    List<Business> findByWhereNamePagination(@Param("name") String name,@Param("texture") String texture,@Param("years") String years,@Param("category") String category,@Param("level") String level,@Param("origin") String origin, @Param("start") int start, @Param("end") int end);

    @Query(value = "" +
            "select " +
            "count(1) " +
            "from business a " +
            "left join collection b on b.id = a.cid " +
            "left join warehouse c on c.id = a.store_id " +
            "where " +
            "case when :name is not null then (b.`name` like concat('%',:name,'%') or b.`code` like concat('%',:name,'%')) " +
            "else 1=1 end " +
            "and " +
            "case when :texture is not null then " +
            "(b.`texture` like concat('%',:texture,'%') " +
            "or b.`texture1` like concat('%',:texture,'%')" +
            "or b.`texture2` like concat('%',:texture,'%')) else 1=1 end " +
            "and case when :years is not null then b.`years` like concat('%',:years,'%') else 1=1 end " +
            "and case when :category is not null then b.`category` like concat('%',:category,'%') else 1=1 end " +
            "and case when :level is not null then b.`level` like concat('%',:level,'%') else 1=1 end " +
            "and case when :origin is not null then b.`origin` like concat('%',:origin,'%') else 1=1 end "+
            "", nativeQuery = true)
    int countByWhereNamePagination(@Param("name") String name,@Param("texture") String texture,@Param("years") String years,@Param("category") String category,@Param("level") String level,@Param("origin") String origin);

    @Query(value = "" +
            "select a.status,a.cid,b.name as cName,a.counter," +
            "a.store_id,c.code as store_name,c.administrator," +
            "a.create_time,a.create_by " +
            "from business a " +
            "left join collection b on b.id = a.cid " +
            "left join warehouse c on c.id = a.store_id " +
            "where " +
            "a.store_id = :storeId " +
            "order by a.create_time desc limit :start,:end", nativeQuery = true)
    List<Business> findByWhereStorePagination(@Param("storeId") String storeId, @Param("start") int start, @Param("end") int end);
    @Query(value = "" +
            "select count(1) " +
            "from business a " +
            "left join collection b on b.id = a.cid " +
            "left join warehouse c on c.id = a.store_id " +
            "where a.store_id = :storeId", nativeQuery = true)
    int countByWhereStorePagination(@Param("storeId") String storeId);

    @Query(value = "" +
            "select " +
            "a.status,a.cid,b.name as cName,a.counter,a.store_id,c.code as store_name," +
            "c.administrator,a.create_time,a.create_by " +
            "from business a " +
            "left join collection b on b.id = a.cid " +
            "left join warehouse c on c.id = a.store_id " +
            "where " +
            "a.store_id = :storeId " +
            "and " +
            "case when :name is not null then (b.`name` like concat('%',:name,'%') or b.`code` like concat('%',:name,'%')) " +
            "else 1=1 end " +
            "and " +
            "case when :texture is not null then " +
            "(b.`texture` like concat('%',:texture,'%') " +
            "or b.`texture1` like concat('%',:texture,'%')" +
            "or b.`texture2` like concat('%',:texture,'%')) else 1=1 end " +
            "and case when :years is not null then b.`years` like concat('%',:years,'%') else 1=1 end " +
            "and case when :category is not null then b.`category` like concat('%',:category,'%') else 1=1 end " +
            "and case when :level is not null then b.`level` like concat('%',:level,'%') else 1=1 end " +
            "and case when :origin is not null then b.`origin` like concat('%',:origin,'%') else 1=1 end "+
            "order by b.code desc limit :start,:end", nativeQuery = true)
    List<Business> findByWhereNameAndStorePagination(@Param("name") String name,@Param("texture") String texture,@Param("years") String years,@Param("category") String category,@Param("level") String level,@Param("origin") String origin, @Param("storeId") String storeId, @Param("start") int start, @Param("end") int end);
    @Query(value = "" +
            "select count(1) " +
            "from business a " +
            "left join collection b on b.id = a.cid " +
            "left join warehouse c on c.id = a.store_id " +
            "where a.store_id = :storeId " +
            "and " +
            "case when :name is not null then (b.`name` like concat('%',:name,'%') or b.`code` like concat('%',:name,'%'))" +
            "else 1=1 end " +
            "and " +
            "case when :texture is not null then " +
            "(b.`texture` like concat('%',:texture,'%') " +
            "or b.`texture1` like concat('%',:texture,'%')" +
            "or b.`texture2` like concat('%',:texture,'%')) else 1=1 end " +
            "and case when :years is not null then b.`years` like concat('%',:years,'%') else 1=1 end " +
            "and case when :category is not null then b.`category` like concat('%',:category,'%') else 1=1 end " +
            "and case when :level is not null then b.`level` like concat('%',:level,'%') else 1=1 end " +
            "and case when :origin is not null then b.`origin` like concat('%',:origin,'%') else 1=1 end " +
            "", nativeQuery = true)
    int countByWhereNameAndStorePagination(@Param("name") String name,@Param("texture") String texture,@Param("years") String years,@Param("category") String category,@Param("level") String level,@Param("origin") String origin, @Param("storeId") String storeId);

    Business findByCidAndStoreId(String cid, String storeId);

    Business findFirstByCid(String cid);

    Business findFirstByStoreId(String storeId);

    @Query(value = "select ifnull(sum(counter),0) from business", nativeQuery = true)
    long sumCounter();

    @Query(value = "select '' as cid,a.store_id,sum(a.counter) as counter,min(status) as status,min(a.create_by) as create_by,min(a.create_time) create_time from business a left join warehouse c on c.id = a.store_id group by a.store_id order by counter desc limit 15", nativeQuery = true)
    List<Business> getByCounterDesc10();

}
