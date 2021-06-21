package com.fasteam.service.intf;


import com.fasteam.dto.PaginationResult;
import com.fasteam.entity.Business;
import com.fasteam.entity.Store;

import java.util.List;

/**
 * Description:  com.crow32.market.appservice.service.intf
 * Copyright: © 2020 Foxconn. All rights reserved.
 * Company: Foxconn
 *
 * @author FL
 * @version 1.0
 * @timestamp 2020/2/20
 */
public interface StoreServiceIntf {

    PaginationResult getByPagination(String code, String floor, String location, int page, int limit);

    void persist(Store store) throws Exception;

    Store getById(String id);

    void delete(String id) throws Exception;

}
