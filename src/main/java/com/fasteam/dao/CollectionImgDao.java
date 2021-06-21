package com.fasteam.dao;

import com.fasteam.entity.CollectionImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description:  com.crow32.market.appservice.dao
 * Copyright: © 2020 Foxconn. All rights reserved.
 * Company: Foxconn
 *
 * @author FL
 * @version 1.0
 * @timestamp 2020/2/20
 */
@Repository
public interface CollectionImgDao extends JpaRepository<CollectionImg, String> {
    List<CollectionImg> findUrlByCidOrderByCreateTimeDesc(String cid);
    void deleteByCid(String cid);
    List<CollectionImg> findAllByCid(String cid);
}
