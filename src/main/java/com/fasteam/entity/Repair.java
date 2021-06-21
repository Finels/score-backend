package com.fasteam.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Description:  事故记录
 * Copyright: © 2020 Foxconn. All rights reserved.
 * Company: Foxconn
 *
 * @author FL
 * @version 1.0
 * @timestamp 2020/2/20
 */
@Data
@Entity
@Table(name = "repair")
public class Repair implements Serializable {
    @Id
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "uuid2")
    private String id;
    private String cid;
    @Transient
    private String cname;
    @Transient
    private String ccode;
    @Column(name = "start_time")
    private String startTime;
    @Column(name = "end_time")
    private String endTime;
    private String department;
    private String person;
    private String location;
    private String reason;
    private String teck;
    private String plan;
    private String result;
    private String backup;
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "create_by")
    private String createBy;

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setCreateTime(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.setCreateTime(formatter.format(date));
    }
}
