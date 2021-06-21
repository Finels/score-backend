package com.fasteam.service;

import com.fasteam.bean.Namespace;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Description:  com.fasteam.service
 * Copyright: © 2021 Foxconn. All rights reserved.
 * Company: Foxconn
 *
 * @author FL
 * @version 1.0
 * @timestamp 2021/6/21
 */
@Service
public class NamespaceManager {
    @Value("${score.namespace.default.root}")
    private String scoreNamespace;

    //全局namespace列表
    private volatile Map<String, Namespace> namespaceList = new LinkedHashMap<>();

    //默认过期时间
    public final Long DEFAULT_EXPIRE = -1L;

    /**
     * 申请命名空间
     */
    public synchronized void apply(String qname) {
        Namespace namespace = namespaceList.getOrDefault(qname, defaultNamespace(qname));
    }

    /**
     * 申请临时命名空间
     */
    public void apply(String namespace, Long expire) {

    }

    /**
     * 默认的namespace对象
     *
     * @return
     */
    public Namespace defaultNamespace(String qname) {
        String room = UUID.randomUUID().toString();
        Namespace namespace = new Namespace();
        namespace.setQname(qname);
        namespace.setRootRoom(room);
        namespace.setExpire(DEFAULT_EXPIRE);
        return namespace;
    }
}
