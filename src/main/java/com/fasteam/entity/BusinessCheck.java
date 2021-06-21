package com.fasteam.entity;

import com.fasteam.dto.InoutBusiness;
import com.fasteam.dto.MoveBusiness;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Description:  藏品出入库审核表
 * Copyright: © 2020 Foxconn. All rights reserved.
 * Company: Foxconn
 *
 * @author FL
 * @version 1.0
 * @timestamp 2020/2/20
 */
@Data
@Entity
@Table(name = "business_check")
public class BusinessCheck implements Serializable {
    @Id
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "uuid2")
    private String id;
    private String cid;
    @Column(name = "store_id")
    private String storeId;
    @Column(name = "target_store_id")
    private String targetStoreId;
    private Integer count;
    private String backup;
    @Column(name = "inout_type")
    private String inoutType;
    @Column(name = "check_type")
    private String checkType;
    @Column(name = "check_role")
    private String checkRole;
    @Column(name = "create_time", nullable = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @Transient
    private String cname;
    @Transient
    private String ccode;
    @Transient
    private String storeName;
    @Transient
    private String targetStoreName;
    private String createBy;
    private String outReason;
    private String outPerson;
    private String outWhere;
    private String outOwner;

    public InoutBusiness toInoutBean() {
        InoutBusiness inoutBean = new InoutBusiness();
        inoutBean.setCid(this.cid);
        inoutBean.setStoreId(this.storeId);
        inoutBean.setBackup(this.backup);
        inoutBean.setCounter(this.count);
        inoutBean.setOutOwner(this.outOwner);
        inoutBean.setOutPerson(this.outPerson);
        inoutBean.setOutReason(this.outReason);
        inoutBean.setOutWhere(this.outWhere);
        return inoutBean;
    }

    public MoveBusiness toMoveBean() {
        MoveBusiness moveBean = new MoveBusiness();
        moveBean.setCid(this.cid);
        moveBean.setOriginStoreId(this.storeId);
        moveBean.setTargetStoreId(this.targetStoreId);
        moveBean.setCounter(this.count);
        return moveBean;
    }

    public Destroy toDestroyBean() {
        Destroy destroy = new Destroy();
        destroy.setCid(this.cid);
        destroy.setStoreId(this.storeId);
        destroy.setCounter(this.count);
        destroy.setBackup(this.backup);
        return destroy;
    }

}
