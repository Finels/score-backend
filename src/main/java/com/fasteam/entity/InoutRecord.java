package com.fasteam.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Description:  出入库记录
 * Copyright: © 2020 Foxconn. All rights reserved.
 * Company: Foxconn
 *
 * @author FL
 * @version 1.0
 * @timestamp 2020/2/20
 */
@Data
@Entity
@Table(name = "inout_record")
public class InoutRecord implements Serializable {
    @Id
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "uuid2")
    private String id;
    @Column(name = "record_type")
    private String recordType;
    @Column(name = "collection_name")
    private String collectionName;
    @Column(name = "collection_code")
    private String collectionCode;
    @Column(name = "collection_id")
    private String collectionId;
    private String warehouse;
    @Column(name = "warehouse_id")
    private String warehouseId;
    @Column(name = "before_counter")
    private Integer beforeCounter;
    private Integer counter;
    @Column(name = "after_counter")
    private Integer afterCounter;
    @Column(name = "opt_user")
    private String optUser;
    @Column(name = "opt_time")
    private String optTime;
    private String backup;
    @Column(name = "is_return")
    private Boolean isReturn;
    //以下字段仅出库记录使用
    private String outReason; //出库原因
    private String outPerson; //经办人
    private String outWhere; //借出方
    private String outOwner; //借出负责人

    public void setOptTime(String createTime) {
        this.optTime = createTime;
    }

    public void setOptTime(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.setOptTime(formatter.format(date));
    }
}
