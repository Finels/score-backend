package com.fasteam.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

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
@Table(name = "collection")
public class Collection implements Serializable {
    @Id
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "uuid2")
    private String id;
    private String code; //总登记号
    private String zcode; //文物编号
    private String wcode; //藏品编号
    private String ycode; //原始凭证号
    private String name; //藏品名称
    @Column(name = "old_name")
    private String oldName;
    private String detail; //备注
    private String years; //年代
    @Column(name = "years_date")
    private String yearsDate;
    private String category; //类型
    private String texture; //质地
    private String texture1; //质地
    private String texture2; //质地
    private float length;
    private float width;
    private float height;
    private float mass; //质量
    @Column(name = "mass_range")
    private String massRange;
    @Column(name = "size_text")
    private String sizeText; //尺寸描述
    private String level; //文物级别
    private String origin; //文物来源
    private String completeness; //完残程度
    @Column(name = "complete_status")
    private String completeStatus; //完残状况
    private String restore; //保存状况
    @Column(name = "in_time_range")
    private String inTimeRange; //入藏时间范围
    @Column(name = "in_year")
    private String inYear; //入藏年度信息
    private String author;
    @Column(name = "create_time")
    private String createTime;
    private String createBy;
    @Column(name = "counter_type")
    private String counterType;
    private String counter;

    private String version; //版本
    private String saved; //存卷
    private String times; //时间（一个随便输入的字段）
    private String address; //地点
    private String research; //收集者
    private String pno; //照相号
    private String tno; //拓片号
    private String jstatus; //鉴定情况
    private String sign; //标识
    //库存相关信息
    @Column(name = "warehouse_code")
    private String warehouseCode;
    @Column(name = "warehouse_floor")
    private String warehouseFloor;//橱柜信息
    @Column(name = "warehouse_location")
    private String warehouseLocation;//层位信息
    @Column(name = "c_total")
    private String businessTotal; //库存套数信息
    @Column(name = "is_business")
    private Integer isBusiness;
    @Column(name = "is_del")
    private Boolean isDel;

    @Transient
    private List<CollectionImg> imgUrls;
    @Transient
    private String storeId;

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setCreateTime(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.setCreateTime(formatter.format(date));
    }

    public static List<Map<String, String>> getConditionData() {
        List<Map<String, String>> data = new ArrayList<>();
        Map<String, String> conditionData = new HashMap<>();
        conditionData.put("id", "code");
        conditionData.put("text", "总登记号");
        data.add(conditionData);
        conditionData = new HashMap<>();
        conditionData.put("id", "zcode");
        conditionData.put("text", "文物编号");
        data.add(conditionData);
        conditionData = new HashMap<>();
        conditionData.put("id", "wcode");
        conditionData.put("text", "入馆登记号");
        data.add(conditionData);
        conditionData = new HashMap<>();
        conditionData.put("id", "ycode");
        conditionData.put("text", "原始凭证号");
        data.add(conditionData);
        conditionData = new HashMap<>();
        conditionData.put("id", "oldName");
        conditionData.put("text", "原名称");
        data.add(conditionData);
        conditionData = new HashMap<>();
        conditionData.put("id", "level");
        conditionData.put("text", "文物级别");
        data.add(conditionData);
        conditionData = new HashMap<>();
        conditionData.put("id", "origin");
        conditionData.put("text", "来源方式");
        data.add(conditionData);
        conditionData = new HashMap<>();
        conditionData.put("id", "category");
        conditionData.put("text", "文物类别");
        data.add(conditionData);
        conditionData = new HashMap<>();
        conditionData.put("id", "years");
        conditionData.put("text", "年代");
        data.add(conditionData);
        conditionData = new HashMap<>();
        conditionData.put("id", "yearsDate");
        conditionData.put("text", "具体年代");
        data.add(conditionData);
        conditionData = new HashMap<>();
        conditionData.put("id", "inTimeRange");
        conditionData.put("text", "入藏时间范围");
        data.add(conditionData);
        conditionData = new HashMap<>();
        conditionData.put("id", "inYear");
        conditionData.put("text", "入藏时间");
        data.add(conditionData);
        conditionData = new HashMap<>();
        conditionData.put("id", "counterType");
        conditionData.put("text", "数量类型");
        data.add(conditionData);
        conditionData = new HashMap<>();
        conditionData.put("id", "length");
        conditionData.put("text", "通长");
        data.add(conditionData);
        conditionData = new HashMap<>();
        conditionData.put("id", "height");
        conditionData.put("text", "通高");
        data.add(conditionData);
        conditionData = new HashMap<>();
        conditionData.put("id", "width");
        conditionData.put("text", "通宽");
        data.add(conditionData);
        data.add(conditionData);
        conditionData = new HashMap<>();
        conditionData.put("id", "massRange");
        conditionData.put("text", "质量范围");
        data.add(conditionData);
        conditionData = new HashMap<>();
        conditionData.put("id", "mass");
        conditionData.put("text", "具体质量");
        data.add(conditionData);
        return data;
    }
}
