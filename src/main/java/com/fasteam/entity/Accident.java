package com.fasteam.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
@Table(name = "accident")
public class Accident implements Serializable {
    @Id
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "uuid2")
    private String id;
    private String cid;
    private String cname;
    private String ccode;
    private String location;
    private String reason;
    private String responsibility;
    private String detail;
    private String result;
    @Column(name = "happen_time")
    private String happenTime;
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
