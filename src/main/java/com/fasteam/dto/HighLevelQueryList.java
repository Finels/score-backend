package com.fasteam.dto;

import lombok.Data;

import java.util.List;

/**
 * Description:  com.fasteam.dto
 * Copyright: © 2020 Foxconn. All rights reserved.
 * Company: Foxconn
 *
 * @author FL
 * @version 1.0
 * @timestamp 2020/11/4
 */
@Data
public class HighLevelQueryList {
    private List<HighLevelQueryBean> queryBean;
    private int page;
    private int limit;
}
