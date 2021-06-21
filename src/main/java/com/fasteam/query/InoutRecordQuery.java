package com.fasteam.query;

import lombok.Data;

/**
 * Created by Administrator on 2020/5/3.
 */
@Data
public class InoutRecordQuery {
    private String type;
    private String name;
    private String startTime;
    private String endTime;
    private int page;
    private int limit;
}
