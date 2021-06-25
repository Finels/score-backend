package com.fasteam.service;

import com.fasteam.bean.Namespace;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
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
@EnableScheduling
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
    public Namespace apply(String qname) {
        return this.apply(qname, DEFAULT_EXPIRE);
    }

    /**
     * 申请临时命名空间
     */
    public synchronized Namespace apply(String qname, Long expire) {
        Namespace namespace = namespaceList.getOrDefault(qname, defaultNamespace(qname));
        String id = UUID.randomUUID().toString();
        namespace.setRootRoom(scoreNamespace + File.separator + id);
        namespace.setExpire(expire);
        namespace.doingMk(scoreNamespace);
        return namespace;
    }

    public Namespace getNamespace(String qname) {
        return namespaceList.get(qname);
    }

    public Namespace getOrApplyNamespace(String qname) {
        if (namespaceList.get(qname) == null) {
            return apply(qname);
        }
        return namespaceList.get(qname);
    }

    public Namespace getOrApplyNamespace(String qname, Long expire) {
        if (namespaceList.get(qname) == null) {
            return apply(qname, expire);
        }
        return namespaceList.get(qname);
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

    @Scheduled(fixedDelay = 10000L)
    public void clearExpire() {

    }
}
