package com.fasteam.dao;

import com.fasteam.entity.InoutRecord;
import com.fasteam.entity.Store;
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
public interface InoutRecordDao extends JpaRepository<InoutRecord, String> {
    @Query(value = "select a.id,a.record_type,a.is_return,a.collection_id,a.warehouse_id,a.before_counter,a.counter,a.after_counter,d.real_name as opt_user,a.opt_time,a.backup,a.out_reason,a.out_person,a.out_where,a.out_owner,b.name as collection_name,b.code as collection_code,concat(c.code,'-',c.floor,'-',c.location) as warehouse from inout_record a left join collection b on b.id =a.collection_id left join warehouse c on c.id = a.warehouse_id left join sys_user d on d.username = a.opt_user order by a.opt_time desc limit ?1,?2 ", nativeQuery = true)
    List<InoutRecord> getByPagnation(int start, int end);

    @Query(value = "select a.id,a.record_type,a.collection_id,a.is_return,a.warehouse_id,a.before_counter,a.counter,a.after_counter,d.real_name as opt_user,a.opt_time,a.`backup`,a.out_reason,a.out_person,a.out_where,a.out_owner,b.`name` as collection_name,b.`code` as collection_code,concat(c.`code`,'-',c.floor,'-',c.location) as warehouse from inout_record a left join collection b on b.id =a.collection_id left join warehouse c on c.id = a.warehouse_id left join sys_user d on d.username = a.opt_user " +
            "where " +
            "case when :conditionType is not null then `record_type` = :conditionType else 1=1 end " +
            "and case when :condition is not null then (b.`code` like concat('%',:condition,'%') or b.`name` like concat('%',:condition,'%')) else 1=1 end " +
            "and (`opt_time` between :startTime and :endTime) " +
            "order by a.opt_time desc " +
            "limit :start,:end ", nativeQuery = true)
    List<InoutRecord> getByWherePagination(@Param("conditionType") String conditionType, @Param("condition") String condition, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("start") int start, @Param("end") int end);

    @Query(value = "select count(1) from inout_record a left join collection b on b.id =a.collection_id left join warehouse c on c.id = a.warehouse_id left join sys_user d on d.username = a.opt_user " +
            "where " +
            "case when :conditionType is not null then `record_type` = :conditionType else 1=1 end " +
            "and case when :condition is not null then (b.`code` like concat('%',:condition,'%') or b.`name` like concat('%',:condition,'%')) else 1=1 end " +
            "and (`opt_time` between :startTime and :endTime) ", nativeQuery = true)
    int countByWherePagination(@Param("conditionType") String conditionType, @Param("condition") String condition, @Param("startTime") String startTime, @Param("endTime") String endTime);

    @Query(value = "select a.id,a.record_type,a.collection_id,a.is_return,a.warehouse_id,a.before_counter,a.counter,a.after_counter,d.real_name as opt_user,a.opt_time,a.`backup`,a.out_reason,a.out_person,a.out_where,a.out_owner,b.`code` as collection_code,b.`name` as collection_name,concat(c.`code`,'-',c.floor,'-',c.location) as warehouse from inout_record a left join collection b on b.id =a.collection_id left join warehouse c on c.id = a.warehouse_id left join sys_user d on d.username = a.opt_user order by a.opt_time desc limit 15", nativeQuery = true)
    List<InoutRecord> getByOptTimeDesc10();


    @Query(value = "select " +
            "a.id,a.collection_id,a.record_type,a.is_return,a.warehouse_id,a.before_counter,a.counter," +
            "a.after_counter,d.real_name as opt_user,a.opt_time,a.`backup`,a.out_reason,a.out_person,a.out_where,a.out_owner," +
            "b.`name` as collection_name," +
            "b.`code` as collection_code,concat(c.`code`,'-',c.floor,'-',c.location) as warehouse " +
            "from inout_record a " +
            "left join collection b on b.id =a.collection_id " +
            "left join warehouse c on c.id = a.warehouse_id " +
            "left join sys_user d on d.username = a.opt_user " +
            "where " +
            "record_type = 1 and is_return is null " +
            "and case when :condition is not null then (b.`code` like concat('%',:condition,'%') or b.`name` like concat('%',:condition,'%')) else 1=1 end " +
            "order by a.opt_time desc ",
            countQuery = "select count(1) " +
                    "from inout_record a " +
                    "left join collection b on b.id =a.collection_id " +
                    "left join warehouse c on c.id = a.warehouse_id " +
                    "left join sys_user d on d.username = a.opt_user " +
                    "where " +
                    "record_type = 1 and is_return is null " +
                    "and case when :condition is not null then (b.`code` like concat('%',:condition,'%') or b.`name` like concat('%',:condition,'%')) else 1=1 end "
            , nativeQuery = true)
    Page<InoutRecord> getReturnList(@Param("condition") String condition, Pageable pageable);
}
