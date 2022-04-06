package com.southsmart.geo.manager.improve;

import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;

/**
 * 增强版的 GeoServerRESTPublisher 设置 restUrl、用户名、密码
 *
 * @author huquanzhi
 * @version 1.0.0
 * @class ImproveGeoServerPublisher
 * @date 2021/7/7  11:23
 */

public class ImproveGeoServerPublisher extends GeoServerRESTPublisher {
    private String restURL;
    private String username;
    private String password;

    public ImproveGeoServerPublisher(String restURL, String username, String password) {
        super(restURL, username, password);
        this.restURL = restURL;
        this.username = username;
        this.password = password;
    }

    public String getRestURL() {
        return restURL;
    }

    public void setRestURL(String restURL) {
        this.restURL = restURL;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogInStr() {
        return this.username + ":" + this.password;
    }
}
