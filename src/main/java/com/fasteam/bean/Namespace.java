package com.fasteam.bean;

import lombok.Data;

import java.io.File;

/**
 * Description:  com.fasteam.bean
 * Copyright: © 2021 Foxconn. All rights reserved.
 * Company: Foxconn
 *
 * @author FL
 * @version 1.0
 * @timestamp 2021/6/21
 */
@Data
public class Namespace {
    //此命名空间的标识名
    private String qname;
    //命名空间根路径
    private String rootRoom;
    //过期时间，-1表示永不过期
    private Long expire;

    public void doingMk(String root) {
        synchronized (this) {
            File file = new File(root + File.separator + rootRoom);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
    }
}
