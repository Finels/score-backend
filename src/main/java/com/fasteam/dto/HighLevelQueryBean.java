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
public class HighLevelQueryBean {
    private String condition;
    private String modal;
    private String value;
    private List<String> values;
    private String relation;
}
