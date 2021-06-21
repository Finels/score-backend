package com.fasteam.dao;

import com.fasteam.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
public interface UserDao extends JpaRepository<User, String> {
    User findByUsernameAndEnable(String username, int enable);

    User findFirtByUsername(String username);

    @Query(value = "select * from sys_user where role_code = :role limit :start,:end", nativeQuery = true)
    List<User> findByRole(@Param("role") String role, @Param("start") int start, @Param("end") int end);

    @Query(value = "select count(1) from sys_user where role_code = :role ", nativeQuery = true)
    int countByRole(@Param("role") String role);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update sys_user set enable = :enable where id = :id", nativeQuery = true)
    void updateEnableById(@Param("enable") int enable, @Param("id") String id);
}
