package com.fasteam.service.intf;


import com.fasteam.dto.PaginationResult;
import com.fasteam.entity.Accident;
import com.fasteam.entity.Destroy;

/**
 * Description:  com.crow32.market.appservice.service.intf
 * Copyright: © 2020 Foxconn. All rights reserved.
 * Company: Foxconn
 *
 * @author FL
 * @version 1.0
 * @timestamp 2020/2/20
 */
public interface DestroyServiceIntf {

    PaginationResult getByPagination(String conditionLike, int page, int limit);

    void persist(Destroy destroy) throws Exception;


}
