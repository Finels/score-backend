package com.fasteam.dto;

import lombok.Data;

/**
 * Description:  com.fasteam.dto
 * Copyright: © 2020 Foxconn. All rights reserved.
 * Company: Foxconn
 *
 * @author FL
 * @version 1.0
 * @timestamp 2020/5/6
 */
@Data
public class InoutBusiness {
    private String cid;
    private String storeId;
    private Integer counter;
    private String backup;
    private String outReason;
    private String outPerson;
    private String outWhere;
    private String outOwner;
}
