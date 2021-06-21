package com.fasteam.dao;

import com.fasteam.entity.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Description:  com.fasteam.dao
 * Copyright: © 2020 Foxconn. All rights reserved.
 * Company: Foxconn
 *
 * @author FL
 * @version 1.0
 * @timestamp 2020/11/4
 */
@Repository
public interface HighLevelQueryCollectionDao extends JpaRepository<Collection, String>, JpaSpecificationExecutor<Collection> {
}
