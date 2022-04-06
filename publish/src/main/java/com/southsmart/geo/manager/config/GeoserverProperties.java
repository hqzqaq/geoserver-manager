package com.southsmart.geo.manager.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * geoserver 配置信息
 *
 * @author huquanzhi
 * @version 1.0.0
 * @date 2022/3/17  14:48
 */
@ConfigurationProperties(prefix = "geoserver")
public class GeoserverProperties {
    /**
     * geoserver 地址
     */
    private String url;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
