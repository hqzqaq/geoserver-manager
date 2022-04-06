package com.southsmart.geo.manager.test;//package com.southsmart.geo.manager.test;
//
//import it.geosolutions.geoserver.rest.GeoServerRESTManager;
//import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
//import it.geosolutions.geoserver.rest.decoder.RESTDataStore;
//import it.geosolutions.geoserver.rest.decoder.RESTLayer;
//import it.geosolutions.geoserver.rest.encoder.GSLayerEncoder;
//import it.geosolutions.geoserver.rest.encoder.datastore.GSPostGISDatastoreEncoder;
//import it.geosolutions.geoserver.rest.encoder.feature.GSFeatureTypeEncoder;
//
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.List;
//
///**
// * @author huquanzhi
// * @version 1.0.0
// * @class Test
// * @date 2021/7/6  17:10
// * @Desc Todo
// */
//
//public class Test {
//    public static void main(String[] args) throws MalformedURLException {
//        //GeoServer的连接配置
//        String url = "http://localhost:8080/geoserver";
//        String username = "admin";
//        String passwd = "geoserver";
//
//        //postgis连接配置
//        String postgisHost = "localhost";
//        int postgisPort = 5432;
//        String postgisUser = "postgres";
//        String postgisPassword = "postgresql";
//        String postgisDatabase = "geoserver_publish";
//        String postgisSchema = "test";
//        String postgisSrs = "EPSG:4326";
//
//        String ws = "test";     //待创建和发布图层的工作区名称workspace
//        String store_name = "cities"; //待创建和发布图层的数据存储名称store
//        String table_name = "city"; // 数据库要发布的表名称,后面图层名称和表名保持一致
//        String layerName = "myLayer";
//
//
//
//        //判断工作区（workspace）是否存在，不存在则创建
//        URL u = new URL(url);
//        GeoServerRESTManager manager = new GeoServerRESTManager(u, username, passwd);
//        GeoServerRESTPublisher publisher = manager.getPublisher();
//        List<String> workspaces = manager.getReader().getWorkspaceNames();
//        if (!workspaces.contains(ws)) {
//            boolean createws = publisher.createWorkspace(ws);
//            System.out.println("create ws : " + createws);
//        } else {
//            System.out.println("workspace已经存在了,ws :" + ws);
//        }
//
//        //判断数据存储（datastore）是否已经存在，不存在则创建
//        RESTDataStore restStore = manager.getReader().getDatastore(ws, store_name);
//        if (restStore == null) {
//            GSPostGISDatastoreEncoder store = new GSPostGISDatastoreEncoder(store_name);
//            store.setHost(postgisHost);//设置url
//            store.setPort(postgisPort);//设置端口
//            store.setUser(postgisUser);// 数据库的用户名
//            store.setPassword(postgisPassword);// 数据库的密码
//            store.setDatabase(postgisDatabase);// 哪个数据库;
//            store.setSchema(postgisSchema); //设置Schema
//            store.setConnectionTimeout(20);// 超时设置
//            //store.setName(schema);
//            store.setMaxConnections(20); // 最大连接数
//            store.setMinConnections(1);     // 最小连接数
//            store.setExposePrimaryKeys(true);
//            boolean createStore = manager.getStoreManager().create(ws, store);
//            System.out.println("create store : " + createStore);
//        } else {
//            System.out.println("数据存储已经存在了,store:" + store_name);
//        }
//
//
//        //判断图层是否已经存在，不存在则创建并发布
//        RESTLayer layer = manager.getReader().getLayer(ws, table_name);
//        if (layer == null) {
//            GSFeatureTypeEncoder pds = new GSFeatureTypeEncoder();
//            //geoserver中图层的名字
//            pds.setTitle(layerName);
//            //数据库中表的名字
//            pds.setName(table_name);
//            pds.setSRS(postgisSrs);
//            // pds.setNativeName(layerName);
//            GSLayerEncoder layerEncoder = new GSLayerEncoder();
//            boolean publish = manager.getPublisher().publishDBLayer(ws, store_name, pds, layerEncoder);
//            System.out.println("publish : " + publish);
//        } else {
//            System.out.println("表已经发布过了,table:" + table_name);
//        }
//    }
//}
