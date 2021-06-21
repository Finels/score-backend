package com.fasteam.service.intf;


import com.fasteam.dto.PaginationResult;
import com.fasteam.entity.Collection;

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
public interface CollectionServiceIntf {

    PaginationResult getByPagination(String conditionLike , int page, int limit);

    void persist(Collection collection) throws Exception;

    Collection getById(String id);

    void delete(String id) throws Exception;

}
