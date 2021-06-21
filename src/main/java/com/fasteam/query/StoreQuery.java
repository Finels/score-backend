package com.fasteam.query;

import lombok.Data;

/**
 * Created by Administrator on 2020/5/3.
 */
@Data
public class StoreQuery {
    private String code;
    private String floor;
    private String location;
    private int page;
    private int limit;
}
