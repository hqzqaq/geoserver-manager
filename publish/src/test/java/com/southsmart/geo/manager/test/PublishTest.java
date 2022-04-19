package com.southsmart.geo.manager.test;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.southsmart.geo.manager.GeoServerManager;
import com.southsmart.geo.manager.constant.FormatConstant;
import com.southsmart.geo.manager.constant.TaskKillType;
import com.southsmart.geo.manager.constant.TileType;
import com.southsmart.geo.manager.entity.SeedRequest;
import com.southsmart.geo.manager.entity.Srs;
import com.southsmart.geo.manager.entity.TilesParamsJsonRoot;
import com.southsmart.geo.manager.error.ErrorException;
import com.southsmart.geo.manager.error.ExistedException;
import com.southsmart.geo.manager.error.ogc.GwcLayerNotFoundException;
import com.southsmart.geo.manager.error.ogc.StyleServiceNotFoundException;
import com.southsmart.geo.manager.error.ogc.WorkSpaceNotFoundException;
import com.southsmart.geo.manager.improve.ImprovePostGISDatastore;
import com.southsmart.geo.manager.reader.GeoServerReader;
import it.geosolutions.geoserver.rest.HTTPUtils;
import org.junit.Test;

import java.io.*;
import java.net.MalformedURLException;

/**
 * @author huquanzhi
 * @version 1.0.0
 * @class PublishUtils
 * @date 2021/7/6  9:50
 * @Desc 测试代码
 */

public class PublishTest {
    //final String url = "http://localhost:8080/geoserver";
    final String url = "http://172.16.11.229:8080/geoserver";
    final String geoServerUserName = "admin";
    final String geoServerPassword = "geoserver";

    //postgis连接配置
    String host = "172.16.11.229";
    int port = 5432;
    String user = "zzland";
    String password = "zzland";
    String database = "zhengzhou_land";
    //模式
    String schema = "public";
    final String tableName = "yixian";
    int crsCode = 4547;
    //工作空间
    final String workspace = "zhengzhou";
    //数据存储
    String dataStoreName = "zhengzhou";
    String styleName = "road2";
    GeoServerManager geoServerManager = new GeoServerManager(url, geoServerUserName, geoServerPassword);
    GeoServerReader geoServerReader = new GeoServerReader(url);

    public PublishTest() throws MalformedURLException {
    }

    @Test
    public void testCreatePostGISLayer() throws WorkSpaceNotFoundException, ExistedException, ErrorException {
        ImprovePostGISDatastore improvePostGISDatastore = new ImprovePostGISDatastore(host, port, user, password, database, schema, dataStoreName);
        //geoServerManager.createPostGISLayer(improvePostGISDatastore.builder(), workspace, tableName, crsCode);
         geoServerManager.createPostGISLayer(improvePostGISDatastore.builder(),workspace,tableName,crsCode,styleName);
    }

    @Test
    public void testCreateShpLayerByZipFile() throws FileNotFoundException, WorkSpaceNotFoundException, ExistedException, ErrorException {
        //geoServerManager.createShpLayerByZipFile(workspace, new File("src/main/resources/testdata/shpzipfile/states.zip"), crsCode,styleName);
        // geoServerManager.createShpLayerByZipFile(workspace,new File("src/main/resources/testdata/shpzipfile/states.zip"),crsCode,styleName);
    }

    @Test
    public void testCreateShpLayerByShpFile() throws FileNotFoundException, WorkSpaceNotFoundException, ExistedException, ErrorException, StyleServiceNotFoundException {
         int code = 4548;
        geoServerManager.createShpLayerByShpFile(workspace, dataStoreName, new File("E:\\desktop\\test-data\\shp\\yixian-ydbp11.shp"), code, styleName,"gb2312");
    }

    @Test
    public void testCreateGeoTIFFLayer() throws FileNotFoundException, WorkSpaceNotFoundException, ExistedException, ErrorException {
        String store = "geotiff";
        //geoServerManager.createGeoTIFFLayer(workspace, new File("src/main/resources/testdata/tiff/word.tiff"));
        // geoServerManager.createGeoTIFFLayer(workspace,"word",new File("E:\\desktop\\test-data\\tif\\ddyx_CGCS2000_1201.tif"));

        //geoServerManager.createGeoTIFFLayer("zhengzhou",store,new File("\\\\172.16.11.40\\zhengzhou\\ddyx.tif"),4547);
        //geoServerManager.createGeoTIFFLayer("zhengzhou",store,"yysy",new File("E:\\desktop\\ddyx-test-0409.tif"),4547);
        geoServerManager.createGeoTIFFLayer("zhengzhou",store,"yysy",new File("E:\\desktop\\ddyx-test-0409.tif"),4547);
    }

    @Test
    public void testCreateStyle() throws ExistedException, WorkSpaceNotFoundException, ErrorException {
        // geoServerManager.createStyleToWorkspace("test",new File("src/main/resources/testdata/stylefile/restteststyle.sld"),"mytest");
        //geoServerManager.createStyleToWorkspace("zhengzhou", new File("E:\\desktop\\road2.sld"));
        //geoServerManager.createStyle(new File("E:\\desktop\\road2.sld"));
        geoServerManager.createStyleToWorkspace("zhengzhou",new File("E:\\desktop\\road2.sld"),"road2");
    }

    @Test
    public void testUpdateStyleNameInWorkspace() throws ExistedException, WorkSpaceNotFoundException, ErrorException {
        // geoServerManager.createStyleToWorkspace("test",new File("src/main/resources/testdata/stylefile/restteststyle.sld"),"mytest");
        //geoServerManager.createStyleToWorkspace("zhengzhou", new File("E:\\desktop\\road2.sld"));
        geoServerManager.updateStyleNameInWorkspace("zhengzhou","road","1461662104309202944");
    }

    @Test
    public void testDeleteStyle() throws ExistedException, WorkSpaceNotFoundException, ErrorException {
        geoServerManager.deleteStyle("zhengzhou", "road");
    }

    @Test
    public void testCreateStyleBySldBody() throws IOException, ExistedException {
        BufferedReader in = new BufferedReader(new FileReader(new File("src/main/resources/testdata/stylefile/restteststyle.sld")));
        String str;
        StringBuilder stringBuffer = new StringBuilder();
        while ((str = in.readLine()) != null) {
            stringBuffer.append(str);
        }
        System.out.println(stringBuffer.toString());
        geoServerManager.createStyleBySldBody(stringBuffer.toString(), "mytest");
    }

    @Test
    public void testCreateStyleBySldBodyToWorkspace() throws IOException, ExistedException, WorkSpaceNotFoundException {
        BufferedReader in = new BufferedReader(new FileReader(new File("src/main/resources/testdata/stylefile/restteststyle.sld")));
        String str;
        StringBuilder stringBuffer = new StringBuilder();
        while ((str = in.readLine()) != null) {
            stringBuffer.append(str);
        }
        System.out.println(stringBuffer.toString());
        geoServerManager.createStyleBySldBodyToWorkspace("test", stringBuffer.toString(), "mytest");
    }

    @Test
    public void testExistsGwcLayers() throws IOException {
        System.out.println(geoServerReader.existsGwcLayers("test1", "word"));
    }

    @Test
    public void testupdateLayerDefaultStyle() throws IOException, ExistedException, WorkSpaceNotFoundException {
        geoServerManager.updateLayerDefaultStyle("zhengzhou","yixian-ydbp11","zhengzhou","road2");
    }

    @Test
    public void testGetSpecificRunningGwcTasks() throws IOException, GwcLayerNotFoundException {
        System.out.println(geoServerReader.GetSpecificRunningGwcTasks("test1", "word"));
    }

    @Test
    public void testCreateTilesTasks() throws IOException, GwcLayerNotFoundException, ErrorException {
        // geoServerManager.startTilesTasks("test1:word","4326",1,12
        // , FormatConstant.IMAGE_PNG, TileType.RESEED,3);
        SeedRequest seedRequest = new SeedRequest();
        seedRequest.setName("test1:word");
        seedRequest.setZoomStart(1);
        seedRequest.setZoomStop(12);
        seedRequest.setSrs(new Srs(4326));
        seedRequest.setFormat(FormatConstant.IMAGE_PNG);
        seedRequest.setType(TileType.RESEED);
        seedRequest.setThreadCount(1);
        TilesParamsJsonRoot tilesParamsJsonRoot = new TilesParamsJsonRoot();
        tilesParamsJsonRoot.setSeedRequest(seedRequest);
        Boolean aBoolean = geoServerManager.createTilesTasks("test1", "word", tilesParamsJsonRoot);
        if (aBoolean) {
            System.out.println("任务执行中。。。。。。");
        } else {
            System.out.println("任务开始失败。。。。");
        }
    }

    @Test
    public void testStopTilesTasks() throws IOException, GwcLayerNotFoundException {
        System.out.println(geoServerReader.getGwcLayersBounds("test1", "word", 4326));
    }

    @Test
    public void testObjectToJson() {
        SeedRequest seedRequest = new SeedRequest();
        seedRequest.setName("test1:word");
        seedRequest.setZoomStart(1);
        seedRequest.setZoomStop(12);
        seedRequest.setSrs(new Srs(4326));
        seedRequest.setFormat(FormatConstant.IMAGE_PNG);
        seedRequest.setType(TileType.RESEED);
        seedRequest.setThreadCount(1);
        TilesParamsJsonRoot tilesParamsJsonRoot = new TilesParamsJsonRoot();
        tilesParamsJsonRoot.setSeedRequest(seedRequest);
        JSONObject jsonObject = JSONUtil.parseObj(tilesParamsJsonRoot);
        System.out.println(jsonObject.toString());
    }

    @Test
    public void testStopTilesTask() throws GwcLayerNotFoundException, ErrorException, IOException, InterruptedException {
        testCreateTilesTasks();
        Thread.sleep(5000);
        geoServerManager.stopTilesTask("test1","word", TaskKillType.RUNNING);
    }

    @Test
    public void testStopAllTilesTask() throws GwcLayerNotFoundException, ErrorException, IOException, InterruptedException {
        testCreateTilesTasks();
        Thread.sleep(5000);
        Boolean aBoolean = geoServerManager.stopAllTilesTasks();
        if (aBoolean) {
            System.out.println("任务全部终止");
        } else {
            System.out.println("任务未结束");
        }
    }

    @Test
    public void test() throws MalformedURLException {
        final String url1 = "http://localhost:8080/geoserver";
        String url2 = String.format("%s/gwc/rest/gridsets.json",url1);
        System.out.println(HTTPUtils.get(url2,geoServerUserName,geoServerPassword));
    }

    @Test
    public void stopLayerServiceTest() throws Exception {
        String layerName = "yixian-ydbp11";
        Boolean aBoolean = geoServerManager.changeLayerServiceState(workspace,dataStoreName,layerName,false);
        System.out.println(aBoolean);
    }

    @Test
    public void removeLayerTest() {
        String layerName = "yixian-ydbp11";
        Boolean aBoolean = geoServerManager.removeLayer("zhengzhou","word");
        System.out.println(aBoolean);
    }

    @Test
    public void removeStoreTest() throws WorkSpaceNotFoundException, ErrorException {
        String layerName = "yixian-ydbp11";
        String storeName = "shp";
        Boolean aBoolean = geoServerManager.removeDataStore(workspace,storeName, true);
        System.out.println(aBoolean);
    }

    @Test
    public void layerExistTest() throws Exception {
        Boolean aBoolean = geoServerManager.layerExist("zhengzhou", "LandSupply");
        System.out.println(aBoolean);
    }

}
