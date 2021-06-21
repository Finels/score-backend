package com.fasteam.dto;

import io.swagger.models.auth.In;
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
public class MoveBusiness {
    private String cid;
    private String originStoreId;
    private Integer counter;
    private String targetStoreId;
    private String backup;
}
