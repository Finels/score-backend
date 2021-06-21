package com.fasteam.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Description:  仓库
 * Copyright: © 2020 Foxconn. All rights reserved.
 * Company: Foxconn
 *
 * @author FL
 * @version 1.0
 * @timestamp 2020/2/20
 */
@Data
@Entity
@Table(name = "warehouse")
public class Store implements Serializable {
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
    @Transient
    private List<String> imgUrls;

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setCreateTime(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.setCreateTime(formatter.format(date));
    }
}
