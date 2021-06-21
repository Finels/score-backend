package com.fasteam.dto;

import lombok.Data;

import java.util.List;

/**
 * Created by Administrator on 2020/5/3.
 */
@Data
public class PaginationResult {
    private List lst;
    private int total;

    public static PaginationResult set(List data,int total) {
        PaginationResult result = new PaginationResult();
        result.setLst(data);
        result.setTotal(total);
        return result;
    }
}
