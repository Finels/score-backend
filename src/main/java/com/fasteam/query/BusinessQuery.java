package com.fasteam.query;

import lombok.Data;

/**
 * Created by Administrator on 2020/5/3.
 */
@Data
public class BusinessQuery {
    private String name;
    private String texture;
    private String years;
    private String category;
    private String level;
    private String origin;
    private String storeId;
    private int page;
    private int limit;
}
