package com.fasteam.dto;

import lombok.Data;

import javax.persistence.Column;

/**
 * Description:  com.fasteam.dto
 * Copyright: © 2020 Foxconn. All rights reserved.
 * Company: Foxconn
 *
 * @author FL
 * @version 1.0
 * @timestamp 2020/6/9
 */
@Data
public class BusinessCollection {
    private String id;
    private String code; //馆内编号
    private String zcode; //总登记号
    private String ycode;
    private String wcode; //文物编号
    private String name; //藏品名称
    private String oldName;
    private String detail; //备注
    private String years; //年代
    private String yearsDate;
    private String category; //类型
    private String texture; //质地
    private String texture1; //质地
    private String texture2; //质地
    private float length;
    private float width;
    private float height;
    private float mass; //质量
    private String massRange;
    private String sizeText; //尺寸描述
    private String level; //文物级别
    private String origin; //文物来源
    private String completeness; //完残程度
    private String completeStatus; //完残状况
    private String restore; //保存状况
    private String inTimeRange; //入藏时间范围
    private String inYear; //入藏年度信息
    private String author;
    private String createTime;
    private String createBy;
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
    private String location;
    private String total;
}
