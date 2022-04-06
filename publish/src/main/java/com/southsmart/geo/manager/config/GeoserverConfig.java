package com.southsmart.geo.manager.config;


import com.southsmart.geo.manager.GeoServerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.MalformedURLException;

/**
 * geoserver 配置类
 *
 * @author huquanzhi
 * @version 1.0.0
 * @date 2022/3/17  14:54
 */
@Configuration
@EnableConfigurationProperties(GeoserverProperties.class)
@ConditionalOnProperty(prefix = "geoserver",name = "isOpen",havingValue = "true")
public class GeoserverConfig {
    @Autowired
    private GeoserverProperties geoserverProperties;

    @Bean(name = "geoserverManager")
    public GeoServerManager geoServerManager() throws MalformedURLException {
        return new GeoServerManager(geoserverProperties.getUrl(),geoserverProperties.getUserName(),geoserverProperties.getPassword());
    }
}
