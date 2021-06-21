package com.fasteam.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Description:  com.fasteam.entity
 * Copyright: © 2020 Foxconn. All rights reserved.
 * Company: Foxconn
 *
 * @author FL
 * @version 1.0
 * @timestamp 2020/5/19
 */
@Data
@Entity
@Table(name = "sys_menu")
public class SystemMenu {
    @Id
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "uuid2")
    private String id;
    private String code; //库房编号
    private String floor; //楼层编号
    private String administrator;
    private String location; //橱柜编号
    private String backup;
    @Transient
    private String name;
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "create_by")
    private String createBy;
}
