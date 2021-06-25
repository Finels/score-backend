package com.fasteam.service;

import com.fasteam.bean.Namespace;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description:  com.fasteam.service
 * Copyright: © 2021 Foxconn. All rights reserved.
 * Company: Foxconn
 *
 * @author FL
 * @version 1.0
 * @timestamp 2021/6/24
 */
@Service
public class CacheService {
    @Autowired
    private CacheManager cacheManager;

    public void putBlockCode(String key, Object value) {
        Cache cache = cacheManager.getCache("blockCodeCache");
        Element element = new Element(key, value);
        cache.put(element);
        cache.flush();
    }

    public Object getBlockCode(String key) {
        Cache cache = cacheManager.getCache("blockCodeCache");
        Element element = cache.get(key);
        return element.getObjectValue();
    }

}
