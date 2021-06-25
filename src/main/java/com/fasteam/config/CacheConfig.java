package com.fasteam.config;

import net.sf.ehcache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description:  com.fasteam.config
 * Copyright: © 2021 Foxconn. All rights reserved.
 * Company: Foxconn
 *
 * @author FL
 * @version 1.0
 * @timestamp 2021/6/24
 */
@Configuration
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        CacheManager cacheManager = CacheManager.create("./src/main/resources/ehcache.xml");
        return cacheManager;
    }
}
