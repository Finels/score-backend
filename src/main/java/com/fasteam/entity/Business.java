package com.fasteam.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Description:  藏品
 * Copyright: © 2020 Foxconn. All rights reserved.
 * Company: Foxconn
 *
 * @author FL
 * @version 1.0
 * @timestamp 2020/2/20
 */
@Data
@Entity
@Table(name = "business")
@IdClass(BusinessPK.class)
public class Business implements Serializable {
    @Id
    private String cid;
    @Transient
    private String cName;
    @Transient
    private String ccode;
    private Integer counter;
    private Integer status;
    @Id
    @Column(name = "store_id")
    private String storeId;
    @Transient
    @Column(name = "store_name")
    private String storeName;
    @Transient
    private String administrator;
    @Column(name = "create_time", nullable = true)
    private String createTime;
    @Column(name = "create_by", nullable = true)
    private String createBy;
    @Transient
    private String backup; //出入库备注，存入record表中

    //以下是出库时需要填写的内容
    @Transient
    private String outPerson; //出库经办人
    @Transient
    private String outReason; //出库原因
    @Transient
    private String outWhere; //借出方
    @Transient
    private String outOwner; //借出负责人

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setCreateTime(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.setCreateTime(formatter.format(date));
    }
}
