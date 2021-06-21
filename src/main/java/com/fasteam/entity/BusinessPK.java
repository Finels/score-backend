package com.fasteam.entity;

import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Objects;

/**
 * Description:  com.fasteam.entity
 * Copyright: © 2020 Foxconn. All rights reserved.
 * Company: Foxconn
 *
 * @author FL
 * @version 1.0
 * @timestamp 2020/5/6
 */
@Data
public class BusinessPK implements Serializable {
    private String cid;
    @Column(name = "store_id")
    private String storeId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BusinessPK that = (BusinessPK) o;
        return Objects.equals(cid, that.cid) &&
                Objects.equals(storeId, that.storeId);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cid == null) ? 0 : cid.hashCode());
        result = prime * result
                + ((storeId == null) ? 0 : storeId.hashCode());
        return result;
    }
}
