package com.southsmart.geo.manager.test;//package com.southsmart.geo.manager.test;
//
//import it.geosolutions.geoserver.rest.decoder.RESTCoverageStore;
//import it.geosolutions.geoserver.rest.encoder.GSLayerEncoder;
//import it.geosolutions.geoserver.rest.encoder.GSResourceEncoder;
//import it.geosolutions.geoserver.rest.encoder.coverage.GSCoverageEncoder;
//import it.geosolutions.geoserver.rest.encoder.datastore.GSGeoTIFFDatastoreEncoder;
//
//import java.net.MalformedURLException;
//
///**
// * Todo
// *
// * @author huquanzhi
// * @version 1.0.0
// * @date 2021/8/25  10:13
// */
//public class TiffTest {
//    /**
//     * 创建geotiff的存储，已存在的直接返回true
//     *
//     * 这里需要注意路径，保证文件已经存在到geoserver所在的服务器中
//     *
//     * @param workspaceName 工作空间名称
//     * @param dataStoreName 数据存储
//     * @return the boolean
//     */
//    //@Override
//    public boolean createTiffDataStore(String workspaceName, String dataStoreName, String filePath) {
//        if (!exitWorkspace(workspaceName)) {
//            log.error(String.format("工作空间 workspaceName：%s 不存在", workspaceName));
//            return false;
//        }
//        if (!reader.existsCoveragestore(workspaceName, dataStoreName)) {
//            try {
//                GSGeoTIFFDatastoreEncoder gsGeoTiffDatastoreEncoder = new GSGeoTIFFDatastoreEncoder(dataStoreName);
//                gsGeoTiffDatastoreEncoder.setWorkspaceName(workspaceName);
//                gsGeoTiffDatastoreEncoder.setUrl(new URL("file:" + filePath));
//                return storeManager.create(workspaceName, gsGeoTiffDatastoreEncoder);
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//                return false;
//            }
//        }
//        return true;
//    }
//    public static void main(String[] args) {
//        GeoServerExtendImpl geoServerExtend = new GeoServerExtendImpl();
//        geoServerExtend.init();
//        String workspaceName = "cite";
//        String dataStoreName = "tiff-test";
//        String coverageName = "tiff-test";
////        String srs = "EPSG:4326";
//        String srs = "EPSG:32633";
//        String filePath = "coverages/img_sample/Pk50095.tif";
//        geoServerExtend.publishGeoTIFFByLocalFile(workspaceName, dataStoreName, filePath, coverageName, srs, "default");
//    }
//    @Override
//    public boolean publishGeoTIFFByLocalFile(String workspace, String storeName,
//                                             String tiffPath, String coverageName, String srs, String defaultStyle) {
//        GSCoverageEncoder coverageEncoder = new GSCoverageEncoder();
//        coverageEncoder.setName(coverageName);
//        coverageEncoder.setTitle(coverageName);
//        coverageEncoder.setSRS(srs);
//        coverageEncoder.setProjectionPolicy(GSResourceEncoder.ProjectionPolicy.NONE);
//        GSLayerEncoder layerEncoder = new GSLayerEncoder();
//
//        layerEncoder.setDefaultStyle(defaultStyle);
//
//        return publishExternalGeoTIFF(workspace, storeName, tiffPath, coverageEncoder, layerEncoder) != null;
//    }
//
//    /**
//     * 部署外部文件（本地文件）的tiff图层服务，如果图层服务存在，则先删除在创建新的
//     *
//     * @param workspace       工作区
//     * @param storeName       存储名称
//     * @param tiffPath        tiff文件路径
//     * @param coverageEncoder coverage封装参数
//     * @param layerEncoder    layer封装参数
//     * @return the rest coverage store
//     * @throws IllegalArgumentException the illegal argument exception
//     */
//    private RESTCoverageStore publishExternalGeoTIFF(String workspace, String storeName, String tiffPath, GSCoverageEncoder coverageEncoder, GSLayerEncoder layerEncoder) throws IllegalArgumentException {
//        if (workspace != null && tiffPath != null && storeName != null && layerEncoder != null && coverageEncoder != null) {
//            String coverageName = coverageEncoder.getName();
//            if (coverageName.isEmpty()) {
//                throw new IllegalArgumentException("Unable to run: empty coverage store name");
//            }
//            //由于 rest api 不支持直接删除geotiff生成的图层，只能通过删除geotiff存储来递归删除其发布的图层
//            if (reader.existsCoverage(workspace, storeName, coverageName)) {
//                publisher.removeCoverageStore(workspace,storeName,true);
//            }
//            //创建geotiff存储，如果存在直接返回true
//            boolean store = createTiffDataStore(workspace, storeName, tiffPath);
//            if (!store) {
//                log.error("创建geotiff存储失败！");
//                return null;
//            }
//            //判定服务是否存在,不存在创建
//            if (!publisher.createCoverage(workspace, storeName, coverageEncoder)) {
//                log.error("创建tiff Coverage 失败！");
//                return null;
//
//            }
//            //获取图层信息
//            if (publisher.configureLayer(workspace, coverageName, layerEncoder)) {
//                return reader.getCoverageStore(workspace, storeName);
//            } else {
//                return null;
//            }
//        } else {
//            throw new IllegalArgumentException("Unable to run: null parameter");
//        }
//    }
//
//}
