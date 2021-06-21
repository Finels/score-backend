package com.fasteam.dao;

import com.fasteam.entity.Collection;
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
public interface CollectionDao extends JpaRepository<Collection, String> {
    @Query(value = "select * from collection where is_del is null  order by create_time desc limit :start,:end ", nativeQuery = true)
    List<Collection> getByPagnation(@Param("start") int start, @Param("end") int end);

    @Query(value = "select * from collection where is_del is null and (`name` like concat('%',:condition,'%') or `code` like concat('%',:condition,'%') or `zcode` like concat('%',:condition,'%')) order by create_time desc limit :start,:end ", nativeQuery = true)
    List<Collection> getByWherePagination(@Param("condition") String condition, @Param("start") int start, @Param("end") int end);

    @Query(value = "select count(1) from collection where is_del is null and (`name` like concat('%',:condition,'%') or `code` like concat('%',:condition,'%') or `zcode` like concat('%',:condition,'%'))", nativeQuery = true)
    int countByWhere(@Param("condition") String condition);

    Collection findAllById(String id);

    Collection findFirstByZcode(String zcode);

    Collection findFirstByCodeAndIsDelIsNull(String code);
    Collection findFirstByCode(String code);

}
