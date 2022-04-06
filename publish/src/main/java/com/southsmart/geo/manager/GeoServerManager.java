package com.southsmart.geo.manager;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.southsmart.geo.manager.constant.ContentType;
import com.southsmart.geo.manager.entity.TilesParamsJsonRoot;
import com.southsmart.geo.manager.error.ogc.StyleServiceNotFoundException;
import com.southsmart.geo.manager.error.ogc.WorkSpaceNotFoundException;
import com.southsmart.geo.manager.constant.TaskKillType;
import com.southsmart.geo.manager.error.ErrorException;
import com.southsmart.geo.manager.error.ExistedException;
import com.southsmart.geo.manager.error.ogc.GwcLayerNotFoundException;
import com.southsmart.geo.manager.improve.ImproveGeoServerPublisher;
import com.southsmart.geo.manager.reader.GeoServerReader;
import it.geosolutions.geoserver.rest.GeoServerRESTManager;
import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.HTTPUtils;
import it.geosolutions.geoserver.rest.decoder.RESTCoverageStore;
import it.geosolutions.geoserver.rest.encoder.GSLayerEncoder;
import it.geosolutions.geoserver.rest.encoder.coverage.GSCoverageEncoder;
import it.geosolutions.geoserver.rest.encoder.datastore.GSPostGISDatastoreEncoder;
import it.geosolutions.geoserver.rest.encoder.feature.GSFeatureTypeEncoder;
import org.apache.commons.httpclient.NameValuePair;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;

import static it.geosolutions.geoserver.rest.encoder.GSResourceEncoder.ProjectionPolicy.REPROJECT_TO_DECLARED;

/**
 * geoserver 管理器
 *
 * @author huquanzhi
 * @version 1.0.0
 * @class GeoServerManager
 * @date 2021/7/7  11:43
 */

public class GeoServerManager {
    //  加强geoserver publisher 发布服务
    private final ImproveGeoServerPublisher geoServerRESTPublisher;
    //  geoserver REST 管理者
    private final GeoServerRESTManager geoServerRESTManager;
    //  geoserver REST 阅读者
    private final GeoServerReader reader;

    private static final Log log = LogFactory.get();

    /**
     * 直接提供 geoserver 地址，用户名，密码为默认的： admin/geoserver
     *
     * @param restUrl geoserver 服务地址
     * @throws MalformedURLException 服务地址错误
     */
    public GeoServerManager(String restUrl) throws MalformedURLException {
        this(restUrl, "admin", "geoserver");
    }

    /**
     * 提供 geoserver 服务地址和用户名、密码
     *
     * @param restUrl  geoserver 服务地址
     * @param userName geoserver登录用户名
     * @param password geoserver 密码
     * @throws MalformedURLException 服务地址或登录失败错误
     */
    public GeoServerManager(String restUrl, String userName, String password) throws MalformedURLException {
        geoServerRESTPublisher = new ImproveGeoServerPublisher(restUrl, userName, password);
        geoServerRESTManager = new GeoServerRESTManager(new URL(restUrl), userName, password);
        reader = new GeoServerReader(restUrl, userName, password);
    }

    ///============== 发布postGis中的表 ===============================

    /**
     * 发布PostGIS 中存在的表图层
     *
     * @param gsPostGISDatastoreEncoder PostGIS DataStore 配置对象
     * @param workspaceName             工作空间名称
     * @param tableName                 要发布的表名，表名也是geoserver中数据源名称和图层名称
     * @param crsCode                   坐标系代码
     * @return 是否发布成功
     * @throws WorkSpaceNotFoundException 工作空间不存在
     * @throws ExistedException           数据源已存在、图层已存在
     * @throws ErrorException             数据源发布失败
     */
    public Boolean createPostGISLayer(
            GSPostGISDatastoreEncoder gsPostGISDatastoreEncoder,
            String workspaceName,
            String tableName,
            int crsCode
    ) throws ExistedException, WorkSpaceNotFoundException, ErrorException {
        GSLayerEncoder gsLayerEncoder = new GSLayerEncoder();
        return createPostGISLayer(gsPostGISDatastoreEncoder, workspaceName, tableName, crsCode, gsLayerEncoder);
    }

    /**
     * 发布PostGIS 中存在的表图层
     *
     * @param gsPostGISDatastoreEncoder PostGIS DataStore 配置对象
     * @param workspaceName             工作空间名称
     * @param tableName                 要发布的表名
     * @param crsCode                   坐标系代码
     * @param styleName                 style 样式服务名称
     * @return 是否发布成功
     * @throws WorkSpaceNotFoundException 工作空间不存在
     * @throws ExistedException           数据源已存在、图层已存在
     * @throws ErrorException             数据源发布失败
     */
    public Boolean createPostGISLayer(
            GSPostGISDatastoreEncoder gsPostGISDatastoreEncoder,
            String workspaceName,
            String tableName,
            int crsCode,
            String styleName
    ) throws ExistedException, WorkSpaceNotFoundException, ErrorException {
        GSLayerEncoder gsLayerEncoder = new GSLayerEncoder();
        gsLayerEncoder.setDefaultStyle(styleName);
        return createPostGISLayer(gsPostGISDatastoreEncoder, workspaceName, tableName, crsCode, gsLayerEncoder);
    }

    /**
     * 发布PostGIS 中存在的表图层，指定在工作空间中的样式服务
     *
     * @param gsPostGISDatastoreEncoder PostGIS DataStore 配置对象
     * @param workspaceName             工作空间名称
     * @param tableName                 要发布的表名
     * @param crsCode                   坐标系代码
     * @param styleWorkspace            style 样式服务工作空间名称
     * @param styleName                 style 样式服务名称
     * @return 是否发布成功
     * @throws WorkSpaceNotFoundException    工作空间不存
     * @throws StyleServiceNotFoundException 样式服务不存在
     * @throws ExistedException              数据源已存在、图层已存在
     * @throws ErrorException                数据源发布失败
     */
    public Boolean createPostGISLayer(
            GSPostGISDatastoreEncoder gsPostGISDatastoreEncoder,
            String workspaceName,
            String tableName,
            int crsCode,
            String styleWorkspace,
            String styleName
    ) throws WorkSpaceNotFoundException, StyleServiceNotFoundException, ExistedException, ErrorException {
        GSLayerEncoder gsLayerEncoder = new GSLayerEncoder();
        if (!reader.existsStyleFromWorkspace(styleWorkspace, styleName)) {
            throw new StyleServiceNotFoundException(styleName);
        }
        gsLayerEncoder.setDefaultStyle(styleWorkspace, styleName);
        return createPostGISLayer(gsPostGISDatastoreEncoder, workspaceName, tableName, crsCode, gsLayerEncoder);
    }

    /**
     * 发布PostGIS 中存在的表，并指定style样式
     *
     * @param gsPostGISDatastoreEncoder PostGIS DataStore 配置对象
     * @param workspaceName             工作空间名称
     * @param tableName                 要发布的表名
     * @param crsCode                   坐标系代码
     * @param gsLayerEncoder            图层配置对象
     * @return 是否发布成功
     * @throws WorkSpaceNotFoundException 工作空间不存在
     * @throws ExistedException           数据源已存在、图层已存在
     * @throws ErrorException             数据源创建失败
     */
    private Boolean createPostGISLayer(
            GSPostGISDatastoreEncoder gsPostGISDatastoreEncoder,
            String workspaceName,
            String tableName,
            int crsCode,
            GSLayerEncoder gsLayerEncoder
    ) throws WorkSpaceNotFoundException, ExistedException, ErrorException {

        //如果工作空间不存在，则创建
        if (!this.reader.existsWorkspace(workspaceName)) {
            geoServerRESTPublisher.createWorkspace(workspaceName);
            log.info("工作空间成功创建：" + workspaceName);
        }

        //图层已存在，抛出异常
        if (this.reader.existsLayer(workspaceName, tableName)) {
            throw new ExistedException("图层：" + tableName);
        }

        //如果数据源不存在，则创建数据源
        if (!this.reader.existsDataStore(workspaceName, tableName)) {
            //    创建一个datastore
            boolean postGISDataStoreResult = geoServerRESTManager.getStoreManager().create(workspaceName, gsPostGISDatastoreEncoder);
        }
        //    获取 datastore 名称
        String storeName = gsPostGISDatastoreEncoder.getName();
        boolean publishDBLayerResult = false;
        GSFeatureTypeEncoder gsFeatureTypeEncoder = new GSFeatureTypeEncoder();
        gsFeatureTypeEncoder.setTitle(tableName);
        gsFeatureTypeEncoder.setNativeName(tableName);
        gsFeatureTypeEncoder.setName(tableName);
        gsFeatureTypeEncoder.setSRS("EPSG:" + crsCode);
        publishDBLayerResult = this.geoServerRESTPublisher.publishDBLayer(workspaceName, storeName, gsFeatureTypeEncoder, gsLayerEncoder);

        return publishDBLayerResult;
    }

    ///============== 发布shp图层 ===============================

    /**
     * 创建shp 图层，要求zip压缩文件名和其中的shp文件名保持一致，且shp文件名为数据源、图层名
     *
     * @param workspaceName 工作空间
     * @param shpZipFile    shp Zip 文件对象
     * @param crsCode       坐标系代码
     * @param charset       编码
     * @return shp图层是否创建成功
     * @throws FileNotFoundException      文件不存在错误
     * @throws FileNotFoundException      文件不存在错误
     * @throws ErrorException             shp源必须为 zip
     * @throws WorkSpaceNotFoundException 工作空间不存在
     * @throws ExistedException           图层名称已存在
     */
    public Boolean createShpLayerByZipFile(String workspaceName, File shpZipFile, int crsCode, String charset) throws FileNotFoundException, WorkSpaceNotFoundException, ErrorException, ExistedException {
        String crsName = "EPSG:" + crsCode;
        //    定义数据源名和图层名
        String storeName, layerName;
        storeName = layerName = FileUtil.getPrefix(shpZipFile);

        //如果工作空间不存在，则创建
        if (!this.reader.existsWorkspace(workspaceName)) {
            geoServerRESTPublisher.createWorkspace(workspaceName);
            log.info("工作空间成功创建：" + workspaceName);
        }

        if (reader.existsLayer(workspaceName, layerName)) {
            throw new ExistedException("图层名称：" + layerName);
        }
        // 指定编码
        NameValuePair[] nameValuePairs = {new NameValuePair("charset", charset)};
        return geoServerRESTPublisher.publishShp(workspaceName, storeName, layerName, shpZipFile, crsName);
    }

    /**
     * 通过shp文件创建shp图层，shp文件名即为图层名
     *
     * @param workspaceName 工作空间
     * @param dataStoreName 数据源名
     * @param shpFile       shp文件
     * @param crsCode       坐标系代码
     * @param styleName     样式名
     * @param charset       编码格式，支持中文为 gb2312
     * @return 是否发布成功
     * @throws ErrorException                文件需为shp文件
     * @throws FileNotFoundException         文件不存在
     * @throws ExistedException              图层已存在
     * @throws StyleServiceNotFoundException 样式不存在
     * @throws WorkSpaceNotFoundException    工作空间不存在不存在
     */
    public Boolean createShpLayerByShpFile(String workspaceName, String dataStoreName, File shpFile, int crsCode, String styleName, String charset) throws ErrorException, FileNotFoundException, ExistedException, StyleServiceNotFoundException, WorkSpaceNotFoundException {
        //判断样式是否存在
        if (!reader.getStyles().contains(styleName)) {
            throw new StyleServiceNotFoundException(styleName);
        }

        //如果工作空间不存在，则创建
        if (!this.reader.existsWorkspace(workspaceName)) {
            geoServerRESTPublisher.createWorkspace(workspaceName);
            log.info("工作空间成功创建：" + workspaceName);
        }

        String crsName = "EPSG:" + crsCode;
        String layerName = FileUtil.getPrefix(shpFile);
        //判断图层是否存在
        if (reader.existsLayer(workspaceName, layerName)) {
            throw new ExistedException("图层名称：%s" + layerName);
        }
        NameValuePair[] nameValuePairs = {new NameValuePair("charset", charset)};
        return geoServerRESTPublisher.publishShp(workspaceName
                , dataStoreName
                , nameValuePairs
                , layerName
                , GeoServerRESTPublisher.UploadMethod.EXTERNAL
                , shpFile.toURI()
                , crsName
                , styleName);
    }

    /**
     * 创建shp 图层，要求zip压缩文件名和其中的shp文件名保持一致，且shp文件名为数据源、图层名，并指定样式
     *
     * @param workspaceName 工作空间
     * @param shpZipFile    shp Zip 文件对象
     * @param crsCode       坐标系代码
     * @param styleName     样式名称
     * @param charset       编码
     * @return 是否shp 图层是否成功
     * @throws FileNotFoundException         文件不存在错误
     * @throws ErrorException                shp源必须为 zip
     * @throws WorkSpaceNotFoundException    工作空间不存在
     * @throws ExistedException              图层名称已存在
     * @throws StyleServiceNotFoundException style 样式服务不存在
     */
    public Boolean createShpLayerByZipFile(
            String workspaceName,
            File shpZipFile,
            int crsCode,
            String styleName,
            String charset
    ) throws FileNotFoundException, ErrorException, WorkSpaceNotFoundException, ExistedException, StyleServiceNotFoundException {
        String crsName = "EPSG:" + crsCode;
        //    定义数据源名和图层名
        String storeName, layerName;
        storeName = layerName = FileUtil.getPrefix(shpZipFile);

        //如果工作空间不存在，则创建
        if (!this.reader.existsWorkspace(workspaceName)) {
            geoServerRESTPublisher.createWorkspace(workspaceName);
            log.info("工作空间成功创建：" + workspaceName);
        }

        if (reader.existsLayer(workspaceName, layerName)) {
            throw new ExistedException("图层名称：%s" + layerName);
        }
        if (!reader.getStyles().contains(styleName)) {
            throw new StyleServiceNotFoundException(styleName);
        }
        NameValuePair[] nameValuePairs = {new NameValuePair("charset", charset)};
        return geoServerRESTPublisher.publishShp(workspaceName, storeName, layerName, shpZipFile, crsName, styleName);
    }

    /**
     * 创建shp 图层，要求zip压缩文件名和其中的shp文件名保持一致，且shp文件名为数据源、图层名，并指定在某工作空间下的样式服务
     *
     * @param workspaceName      工作空间
     * @param shpZipFile         shp Zip 文件对象
     * @param crsCode            坐标系代码
     * @param styleWorkspaceName 样式服务所在工作空间名称
     * @param styleName          样式名称
     * @return 创建shp 图层是否成功
     * @throws FileNotFoundException         文件不存在错误
     * @throws ErrorException                shp源文件必须为zip
     * @throws WorkSpaceNotFoundException    工作空间不存在
     * @throws ExistedException              图层名称已存在
     * @throws StyleServiceNotFoundException style样式服务不存在
     */
    public Boolean createShpLayerByZipFileStyle(
            String workspaceName,
            File shpZipFile,
            int crsCode,
            String styleWorkspaceName,
            String styleName,
            String charset
    ) throws FileNotFoundException, ErrorException, WorkSpaceNotFoundException, ExistedException, StyleServiceNotFoundException {
        String shpStyle = styleWorkspaceName + ":" + styleName;
        return createShpLayerByZipFile(workspaceName, shpZipFile, crsCode, shpStyle, charset);
    }

    ///============== 发布 Tiff ===============================

    /**
     * 发布Tiff 服务（wms）图层名为数据存储名
     *
     * @param workspaceName 工作空间名称
     * @param layerName     图层名称
     * @param tifFile       tif 文件对象
     * @return 是否发布成功
     * @throws FileNotFoundException      没有找到文件
     * @throws WorkSpaceNotFoundException 工作空间不存在
     * @throws ExistedException           图层已存在
     */
    public Boolean createGeoTIFFLayer(String workspaceName, String layerName, File tifFile) throws FileNotFoundException, WorkSpaceNotFoundException, ExistedException {
        //如果工作空间不存在，则创建
        if (!this.reader.existsWorkspace(workspaceName)) {
            geoServerRESTPublisher.createWorkspace(workspaceName);
            log.info("工作空间成功创建：" + workspaceName);
        }
        if (reader.existsLayer(workspaceName, layerName)) {
            throw new ExistedException("图层：" + layerName);
        }
        return geoServerRESTPublisher.publishGeoTIFF(workspaceName, layerName, tifFile);
    }

    /**
     * 发布Tiff 服务（wms）文件名默认为数据存储、图层名
     *
     * @param workspaceName 工作空间名称
     * @param tifFile       tif 文件对象
     * @return 是否发布成功
     * @throws FileNotFoundException      没有找到文件
     * @throws WorkSpaceNotFoundException 工作空间不存在
     * @throws ExistedException           图层已存在
     * @throws ErrorException             非tiff文件
     */
    public Boolean createGeoTIFFLayer(String workspaceName, File tifFile) throws FileNotFoundException, WorkSpaceNotFoundException, ExistedException, ErrorException {
        String layerName = FileUtil.getPrefix(tifFile);
        return createGeoTIFFLayer(workspaceName, layerName, tifFile);
    }

    /**
     * 发布Tiff 服务（wms）文件名默认为图层名,从本地读取文件，发布速度快
     *
     * @param workspaceName 工作空间名称
     * @param storeName     数据存储名
     * @param tifFile       tif 文件对象
     * @param crsCode       坐标系代码
     * @return 是否发布成功
     * @throws FileNotFoundException 没有找到文件
     */
    public Boolean createGeoTIFFLayer(String workspaceName, String storeName, File tifFile, int crsCode) throws FileNotFoundException {
        String name = FileUtil.getPrefix(tifFile);
        return publishGeoTiffLayer(workspaceName, storeName, name, tifFile, crsCode);
    }

    /**
     * 发布Tiff 服务（wms）从本地读取文件，发布速度快
     *
     * @param workspaceName 工作空间名称
     * @param storeName     数据存储名
     * @param layerName     图层名
     * @param tifFile       tif 文件对象
     * @param crsCode       坐标系代码
     * @return 是否发布成功
     * @throws FileNotFoundException 没有找到文件
     */
    public Boolean createGeoTIFFLayer(String workspaceName, String storeName, String layerName, File tifFile, int crsCode) throws FileNotFoundException {
        return publishGeoTiffLayer(workspaceName, storeName, layerName, tifFile, crsCode);
    }

    private Boolean publishGeoTiffLayer(String workspaceName, String storeName, String layerName, File tifFile, int crsCode) throws FileNotFoundException {
        String srs = String.format("EPSG:%s", crsCode);
        GSCoverageEncoder gsCoverageEncoder = new GSCoverageEncoder();
        gsCoverageEncoder.setName(layerName);
        gsCoverageEncoder.setNativeName(layerName);
        gsCoverageEncoder.setTitle(layerName);
        gsCoverageEncoder.setEnabled(true);
        gsCoverageEncoder.setSRS(srs);
        gsCoverageEncoder.setNativeFormat("GeoTIFF");
        gsCoverageEncoder.addSupportedFormats("GeoTIFF");
        gsCoverageEncoder.setNativeCRS(srs);
        gsCoverageEncoder.setRequestSRS(srs);
        gsCoverageEncoder.setResponseSRS(srs);
        gsCoverageEncoder.setProjectionPolicy(REPROJECT_TO_DECLARED);
        GSLayerEncoder gsLayerEncoder = new GSLayerEncoder();
        gsLayerEncoder.setEnabled(true);
        RESTCoverageStore restCoverageStore = geoServerRESTPublisher.publishExternalGeoTIFF(workspaceName, storeName, tifFile, gsCoverageEncoder, gsLayerEncoder);
        if (restCoverageStore != null) {
            log.info(restCoverageStore.getName());
            log.info(restCoverageStore.getWorkspaceName());
            log.info(restCoverageStore.getType());
            log.info(restCoverageStore.getURL());
            return true;
        } else return false;
    }


    ///============== 发布 Style样式服务 ===============================

    /**
     * 创建 Style 服务,文件名为上传的样式名
     * 不能将同一 SLD 文件创建多个style 服务，这将会导致删除异常
     *
     * @param sldFile sld文件对象
     * @return 返回是否创建成功
     * @throws ErrorException   文件类型错误
     * @throws ExistedException 样式服务已存在
     */
    public Boolean createStyle(File sldFile) throws ErrorException, ExistedException {
        //获取文件名，并将文件名作为样式名
        String styleName = FileUtil.getPrefix(sldFile);
        return this.createStyle(sldFile, styleName);
    }


    /**
     * 创建 Style 服务，并提供style 服务名称
     * 不能将同一 SLD 文件创建多个style 服务，这将会导致删除异常
     *
     * @param sldFile   sld 文件对象
     * @param styleName style 服务名称
     * @return 返回是否创建成功
     * @throws ExistedException 样式服务已存在
     */
    public Boolean createStyle(File sldFile, String styleName) throws ExistedException {
        //若样式服务已存在，则抛出已存在的异常
        if (reader.existsStyle(styleName)) {
            throw new ExistedException(styleName + "样式服务");
        }
        geoServerRESTPublisher.publishStyle(sldFile, styleName);
        // 发布之后需要更新一下，样式才能生效
        return geoServerRESTPublisher.updateStyle(sldFile, styleName);
    }

    /**
     * 删除工作空间中的样式
     *
     * @param workspace 工作空间名
     * @param styleName 样式名
     * @return 返回是否创建成功
     * @throws ErrorException   文件类型错误
     * @throws ExistedException 样式服务已存在
     */
    public Boolean deleteStyle(String workspace, String styleName) throws ErrorException, ExistedException {
        return geoServerRESTPublisher.removeStyleInWorkspace(workspace, styleName, true);
    }

    /**
     * 根据完整的 sld文档 字符串上传 style 样式服务
     *
     * @param sldBody   完整的sld文档字符串
     * @param styleName style 样式服务名
     * @return 是否上传成功
     * @throws ExistedException style样式服务已存在
     */
    public Boolean createStyleBySldBody(String sldBody, String styleName) throws ExistedException {
        if (reader.existsStyle(styleName)) {
            throw new ExistedException("样式服务：" + styleName);
        }
        geoServerRESTPublisher.publishStyle(sldBody, styleName);
        return geoServerRESTPublisher.updateStyle(sldBody, styleName);
    }


    /**
     * 创建 Style 服务到指定的工作空间下，文件名作为样式名
     * 不能将同一 SLD 文件创建多个style 服务，这将会导致删除异常
     *
     * @param workspaceName 工作空间名称
     * @param sldFile       sld文件对象
     * @return 返回是否创建成功
     * @throws WorkSpaceNotFoundException 工作空间不存在
     * @throws ExistedException           style服务已存在
     * @throws ErrorException             文件类型错误
     */
    public Boolean createStyleToWorkspace(String workspaceName, File sldFile) throws ErrorException, WorkSpaceNotFoundException, ExistedException {
        String styleName = FileUtil.getPrefix(sldFile);
        return this.createStyleToWorkspace(workspaceName, sldFile, styleName);
    }

    /**
     * 创建 Style 服务到指定的工作空间下，自定义 style 服务名称
     * 不能将同一 SLD 文件创建多个style 服务，这将会导致删除异常
     *
     * @param workspaceName 工作空间名称
     * @param sldFile       sld 文件对象
     * @param styleName     style 服务名称
     * @return 返回是否创建成功
     * @throws WorkSpaceNotFoundException 工作空间不存在
     * @throws ExistedException           工作空间中style样式服务已存在
     */
    public Boolean createStyleToWorkspace(String workspaceName, File sldFile, String styleName) throws WorkSpaceNotFoundException, ExistedException {
        //如果工作空间不存在则创建
        if (!reader.existsWorkspace(workspaceName)) {
            geoServerRESTPublisher.createWorkspace(workspaceName);
        }
        //如果工作空间中已存在样式文件
        if (reader.existsStyleFromWorkspace(workspaceName, styleName)) {
            throw new ExistedException("style 样式服务：" + styleName);
        }
        geoServerRESTPublisher.publishStyleInWorkspace(workspaceName, sldFile, styleName);

        return geoServerRESTPublisher.updateStyleInWorkspace(workspaceName, sldFile, styleName);
    }

    /**
     * 根据sld样式内容发布样式
     *
     * @param workspaceName 工作空间名
     * @param sldBody       sld 字符串
     * @param styleName     样式名
     * @return
     */
    public Boolean createStyleBySldBodyToWorkspace(String workspaceName, String sldBody, String styleName) throws WorkSpaceNotFoundException, ExistedException {
        //如果工作空间不存在则创建
        if (!reader.existsWorkspace(workspaceName)) {
            geoServerRESTPublisher.createWorkspace(workspaceName);
        }
        //如果工作空间中已存在样式文件
        if (reader.existsStyleFromWorkspace(workspaceName, styleName)) {
            throw new ExistedException("style 样式服务：" + styleName);
        }
        geoServerRESTPublisher.publishStyleInWorkspace(workspaceName, sldBody, styleName);
        return geoServerRESTPublisher.updateStyleInWorkspace(workspaceName, sldBody, styleName);
    }

    /**
     * 根据sld样式内容更新样式
     *
     * @param workspaceName 工作空间名
     * @param sldBody       sld 字符串
     * @param styleName     样式名
     * @return
     */
    public Boolean updateStyleBySldBodyToWorkspace(String workspaceName, String sldBody, String styleName){
        return geoServerRESTPublisher.updateStyleInWorkspace(workspaceName, sldBody, styleName);
    }

    /**
     * 根据sld样式内容发布样式
     *
     * @param workspaceName   工作空间名
     * @param rawStyleName    原来的样式名
     * @param modifyStyleName 修改的样式名
     * @return
     */
    public Boolean updateStyleNameInWorkspace(String workspaceName, String rawStyleName, String modifyStyleName) throws WorkSpaceNotFoundException, ExistedException {
        String url = String.format("%s/rest/workspaces/%s/styles/%s", geoServerRESTPublisher.getRestURL(), workspaceName, rawStyleName);
        JSONObject jsonObject = new JSONObject();
        JSONObject style = new JSONObject();
        style.set("name", modifyStyleName);
        jsonObject.set("style", style);
        return httpPut(url, jsonObject.toString());
    }

    /**
     * 给指定工作空间中的图层，修改默认样式，样式为指定工作空间中的样式
     *
     * @param workspaceName  工作空间名
     * @param layerName      原来的样式名
     * @param styleWorkspace 修改的样式名
     * @param styleName      修改的样式名
     * @return
     */
    public Boolean updateLayerDefaultStyle(String workspaceName, String layerName, String styleWorkspace, String styleName) throws WorkSpaceNotFoundException, ExistedException {
        GSLayerEncoder gsLayerEncoder = new GSLayerEncoder();
        gsLayerEncoder.setDefaultStyle(styleWorkspace, styleName);
        return geoServerRESTPublisher.configureLayer(workspaceName, layerName, gsLayerEncoder);
    }


    ///============== 图层切片服务 ===============================

    /**
     * 创建图层切片任务
     *
     * @param workspaceName: 工作空间名
     * @param gwcLayerName:  切片图层名
     * @param params:        切片参数
     * @return 是否开始任务
     * @throws ErrorException            参数不能为空
     * @throws GwcLayerNotFoundException 图层不存在
     * @Date: 2021/7/19 11:23
     */
    public Boolean createTilesTasks(String workspaceName, String gwcLayerName, TilesParamsJsonRoot params) throws ErrorException, GwcLayerNotFoundException {
        if (params == null) {
            throw new ErrorException("参数不能为空");
        }
        JSONObject jsonObject = JSONUtil.parseObj(params);
        String content = jsonObject.toString();
        if (StrUtil.isEmpty(content)) {
            throw new ErrorException("参数不能为空");
        }

        String url = String.format("%s/gwc/rest/seed/%s:%s.json",
                geoServerRESTPublisher.getRestURL(),
                workspaceName, gwcLayerName);

        String post = HTTPUtils.post(url, content, ContentType.JSON,
                geoServerRESTPublisher.getUsername(), geoServerRESTPublisher.getPassword());

        //查询任务执行状态,返回json格式的字符串
        // {"long-array-array":[[0,44739244,0,19,1],[0,44739244,0,20,1],[0,44739244,0,21,1]]}
        String taskStates = reader.GetSpecificRunningGwcTasks(workspaceName, gwcLayerName);
        JSONObject jsonTaskStates = JSONUtil.parseObj(taskStates);
        JSONArray taskStatesArray = (JSONArray) jsonTaskStates.get("long-array-array");
        return taskStatesArray.size() > 0;
    }

    /**
     * 终止切片任务
     *
     * @param workspaceName: 工作空间名
     * @param gwcLayerName:  切片图层名
     * @param killType:      终止类型，TaskKillType.RUNNING OR TaskKillType.PENDING
     * @return 是否终止成功
     * @throws GwcLayerNotFoundException 切片图层不存在
     * @throws ErrorException            获取图层的切片状态发生异常
     * @Date: 2021/7/19 14:47
     */
    public Boolean stopTilesTask(String workspaceName, String gwcLayerName, String killType) throws GwcLayerNotFoundException, ErrorException {
        if (!reader.existsGwcLayers(workspaceName, gwcLayerName)) {
            throw new GwcLayerNotFoundException(workspaceName + ":" + gwcLayerName);
        }

        String url = String.format("%s/gwc/rest/seed/%s:%s",
                geoServerRESTPublisher.getRestURL(), workspaceName, gwcLayerName);

        String result = HTTPUtils.post(url, "kill_all=running", ContentType.JSON, geoServerRESTPublisher.getUsername()
                , geoServerRESTPublisher.getPassword());
        log.info(result);
        //获取任务运行的状态
        String taskStates = reader.GetSpecificRunningGwcTasks(workspaceName, gwcLayerName);
        JSONObject jsonTaskStates = JSONUtil.parseObj(taskStates);
        JSONArray taskStatesArray = (JSONArray) jsonTaskStates.get("long-array-array");

        if (taskStatesArray == null) {
            throw new ErrorException(String.format("查询 %s:%s 任务的状态发生异常", workspaceName, gwcLayerName));
        }

        return taskStatesArray.size() == 0;
    }

    /**
     * 终止全部在运行的任务
     *
     * @return 是否终止任务
     * @throws ErrorException 获取全部任务状态出现异常
     * @Date: 2021/7/19 14:46
     */
    public Boolean stopAllTilesTasks() throws ErrorException {
        String url = String.format("%s/gwc/rest/seed",
                geoServerRESTPublisher.getRestURL());

        String content = String.format("kill_all=%s", TaskKillType.ALL);

        String result = HTTPUtils.post(url, content, ContentType.JSON, geoServerRESTPublisher.getUsername(), geoServerRESTPublisher.getPassword());
        log.info(result);

        String tasks = reader.GetRunningGwcTasksForAllLayers();
        JSONObject jsonTaskStates = JSONUtil.parseObj(tasks);
        JSONArray taskStatesArray = (JSONArray) jsonTaskStates.get("long-array-array");

        if (taskStatesArray == null) {
            throw new ErrorException("查询所有任务的状态发生异常");
        }
        return taskStatesArray.size() == 0;
    }

    ///**
    // * 终止全部在运行的任务
    // *
    // * @return 是否终止任务
    // * @throws ErrorException 获取全部任务状态出现异常
    // * @Date: 2021/7/19 14:46
    // */
    //public Boolean gridsets() throws Exception {
    //    String url = String.format("%s/gwc/rest/gridsets",
    //            geoServerRESTPublisher.getRestURL());
    //    String xml = HTTPUtils.get(url, geoServerRESTPublisher.getUsername(), geoServerRESTPublisher.getPassword());
    //    JSONObject jsonObject = JSONUtil.xmlToJson(xml);
    //    log.info(jsonObject.toString());
    //    return true;
    //}

    /**
     * 改变服务状态，启用或关闭服务
     *
     * @param workspace 工作空间名
     * @param dataStore 数据存储名
     * @param layerName 图层名
     * @param enabled   是否启用服务，true 启用，false 关闭
     * @return
     */
    public Boolean changeLayerServiceState(String workspace, String dataStore, String layerName, boolean enabled) {
        String url = String.format("%s/rest/workspaces/%s/datastores/%s/featuretypes/%s", geoServerRESTPublisher.getRestURL(), workspace, dataStore, layerName);
        JSONObject layer = new JSONObject();
        layer.set("enabled", enabled);
        JSONObject featureType = new JSONObject();
        featureType.set("featureType", layer);
        return httpPut(url, featureType.toString());
    }

    /**
     * httpPut 更新请求
     *
     * @param url     url 地址
     * @param content json数据
     * @return 更新结果
     */
    private boolean httpPut(String url, String content) {
        log.info("更新成功：%s", url);
        return "".equals(HTTPUtils.putJson(url, content, geoServerRESTPublisher.getUsername(), geoServerRESTPublisher.getPassword()));
    }

    /**
     * http 查询请求
     *
     * @param url url 地址
     * @return 查询结果
     */
    private String httpGet(String url) throws Exception {
        log.info("查询成功：%s", url);
        return HTTPUtils.getAsJSON(url, geoServerRESTPublisher.getUsername(), geoServerRESTPublisher.getPassword()).toString();
    }

    /**
     * http 新增请求
     *
     * @param url     url 地址
     * @param content json 数据
     * @return
     */
    private boolean httpPost(String url, String content) {
        log.info("新增成功：%s", url);
        return "".equals(HTTPUtils.postJson(url, content, geoServerRESTPublisher.getUsername(), geoServerRESTPublisher.getPassword()));
    }

    /**
     * http 删除请求
     *
     * @param url url 地址
     * @return
     */
    private boolean httpDelete(String url) {
        log.info("删除成功：%s", url);
        return HTTPUtils.delete(url, geoServerRESTPublisher.getUsername(), geoServerRESTPublisher.getPassword());
    }

    /**
     * 移除工作空间中图层
     *
     * @param workspace 工作空间名
     * @param layerName 图层名
     * @return
     */
    public Boolean removeLayer(String workspace, String layerName) {
        String url = String.format("%s/rest/layers/%s?recurse=true", geoServerRESTPublisher.getRestURL(), workspace + ":" + layerName);
        return httpDelete(url);
    }


    /**
     * 删除数据存储空间
     *
     * @param workspace     工作空间名
     * @param dataStoreName 数据存储名
     * @param recurse       是否递归删除
     * @return
     */
    public Boolean removeDataStore(String workspace, String dataStoreName, boolean recurse) throws WorkSpaceNotFoundException, ErrorException {
        return geoServerRESTPublisher.removeDatastore(workspace, dataStoreName, recurse);
    }
}
