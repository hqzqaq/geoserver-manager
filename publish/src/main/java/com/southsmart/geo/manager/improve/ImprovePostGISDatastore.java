package com.southsmart.geo.manager.improve;

import it.geosolutions.geoserver.rest.encoder.datastore.GSPostGISDatastoreEncoder;

/**
 * PostGIS 数据源加强类，帮助构建PostGIS数据源
 *
 * @author huquanzhi
 * @version 1.0.0
 * @class ImprovePostGISDatastore
 * @date 2021/7/7  11:27
 */

public class ImprovePostGISDatastore {
    //geoserver 中数据存储的名字
    private final String dataStoreName;
    private final String host;
    //默认的postgresql的端口号
    private int port = 5432;
    //默认的postgresql的用户名
    private String user = "postgres";
    private final String password;
    private final String database;
    //数据库中的模式
    private final String schema;

    public ImprovePostGISDatastore(String host, String password, String database,String schema,String dataStoreName) {
        this.host = host;
        this.password = password;
        this.database = database;
        this.schema = schema;
        this.dataStoreName = dataStoreName;
    }

    public ImprovePostGISDatastore(String host, int port, String user, String password, String database,String schema,String dataStoreName) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.database = database;
        this.schema = schema;
        this.dataStoreName = dataStoreName;
    }

    /**
     * 构建 GSPostGISDatastoreEncoder 对象，并将其返回
     * @return GSPostGISDatastoreEncoder 对象
     */
    public GSPostGISDatastoreEncoder builder () {
        GSPostGISDatastoreEncoder build = new GSPostGISDatastoreEncoder(dataStoreName);
        build.setHost(host);
        build.setPort(port);
        build.setUser(user);
        build.setPassword(password);
        build.setDatabase(database);
        build.setSchema(schema);
        build.setExposePrimaryKeys(true);
        return build;
    }
}
