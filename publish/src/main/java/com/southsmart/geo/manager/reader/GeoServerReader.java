package com.southsmart.geo.manager.reader;


import com.southsmart.geo.manager.error.ogc.WorkSpaceNotFoundException;
import com.southsmart.geo.manager.error.ogc.GwcLayerNotFoundException;
import it.geosolutions.geoserver.rest.GeoServerRESTManager;
import it.geosolutions.geoserver.rest.GeoServerRESTReader;
import it.geosolutions.geoserver.rest.HTTPUtils;
import it.geosolutions.geoserver.rest.decoder.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


/**
 * 处理GeoServer 是否存在和列表查询
 *
 * @author huquanzhi
 * @version 1.0.0
 * @class GeoServerReader
 * @date 2021/7/7  11:50
 */

public class GeoServerReader {
    private String restUrl;
    private String userName;
    private String password;
    private final GeoServerRESTReader geoServerRESTReader;

    public GeoServerReader(String restUrl) throws MalformedURLException {
        this(restUrl, "admin", "geoserver");
    }

    public GeoServerReader(String restUrl, String userName, String password) throws MalformedURLException {
        this.restUrl = restUrl;
        this.userName = userName;
        this.password = password;
        GeoServerRESTManager geoServerRESTManager = new GeoServerRESTManager(new URL(restUrl), userName, password);
        geoServerRESTReader = geoServerRESTManager.getReader();
    }

    ///============ 工作空间部分 ===============================

    /**
     * 判断工作空间是否存在
     *
     * @param workspaceName 工作空间名称
     * @return 判断工作空间是否存在
     */
    public Boolean existsWorkspace(String workspaceName) {
        return geoServerRESTReader.existsWorkspace(workspaceName);
    }

    /**
     * 获取工作空间列表
     *
     * @return 工作空间列表
     */
    public ArrayList<String> getWorkspaces() {
        RESTWorkspaceList workspaces = geoServerRESTReader.getWorkspaces();
        ArrayList<String> workspacesList = new ArrayList<>();
        workspaces.forEach(restShortWorkspace -> {
            String name = restShortWorkspace.getName();
            workspacesList.add(name);
        });
        return workspacesList;
    }

    ///============ 矢量数据源部分 ===============================

    /**
     * 判断某个工作空间下的矢量数据源是否存在
     *
     * @param workspaceName 工作空间名称
     * @param datastoreName 数据源名称
     * @return 数据源是否存在
     * @throws WorkSpaceNotFoundException 工作空间不存在
     */
    public Boolean existsDataStore(String workspaceName, String datastoreName) throws WorkSpaceNotFoundException {
        if (!existsWorkspace(workspaceName)) {
            throw new WorkSpaceNotFoundException(workspaceName);
        }
        return geoServerRESTReader.existsDatastore(workspaceName, datastoreName);
    }

    /**
     * 获取数据源列表
     *
     * @param workspaceName 工作空间名称
     * @return 数据集名称列表
     * @throws WorkSpaceNotFoundException 工作空间不存在
     */
    public ArrayList<String> getDataStores(String workspaceName) throws WorkSpaceNotFoundException {
        if (!existsWorkspace(workspaceName)) {
            throw new WorkSpaceNotFoundException(workspaceName);
        }
        RESTDataStoreList dataStores = geoServerRESTReader.getDatastores(workspaceName);
        return new ArrayList<>(dataStores.getNames());
    }

    ///============ 栅格数据源部分 ===============================

    /**
     * 判断栅格数据源是否存在
     *
     * @param workspaceName     工作空间名称
     * @param coverageStoreName 栅格数据源名称
     * @return 栅格数据源是否存在
     * @throws WorkSpaceNotFoundException 工作空间不存在
     */
    public Boolean existsCoverageStore(String workspaceName, String coverageStoreName) throws WorkSpaceNotFoundException {
        if (!existsWorkspace(workspaceName)) {
            throw new WorkSpaceNotFoundException(workspaceName);
        }
        return geoServerRESTReader.existsCoveragestore(workspaceName, coverageStoreName);
    }

    /**
     * 获取栅格数据源列表
     *
     * @param workspaceName 工作空间名称
     * @return 栅格数据源列表数组
     * @throws WorkSpaceNotFoundException 工作空间不存在
     */
    public ArrayList<String> getCoverageStores(String workspaceName) throws WorkSpaceNotFoundException {
        if (!existsWorkspace(workspaceName)) {
            throw new WorkSpaceNotFoundException(workspaceName);
        }
        RESTCoverageStoreList coverageStores = geoServerRESTReader.getCoverageStores(workspaceName);
        return new ArrayList<>(coverageStores.getNames());
    }

    ///============ 图层部分 ===============================

    /**
     * 判断某个工作空间下的图层是否存在
     *
     * @param workspaceName 工作空间名称
     * @param layerName     图层名称
     * @return 图层是否存在
     * @throws WorkSpaceNotFoundException 工作空间不存在
     */
    public Boolean existsLayer(String workspaceName, String layerName) throws WorkSpaceNotFoundException {
        if (!existsWorkspace(workspaceName)) {
            throw new WorkSpaceNotFoundException(workspaceName);
        }
        return geoServerRESTReader.existsLayer(workspaceName, layerName);
    }

    /**
     * 获取所有图层名称列表
     *
     * @return 图层名称列表
     */
    public ArrayList<String> getLayersList() {
        RESTLayerList layers = geoServerRESTReader.getLayers();
        return new ArrayList<>(layers.getNames());
    }


    ///============ 图层组部分 ===============================

    /**
     * 判断图层组是否存在
     *
     * @param workspaceName  工作空间名称
     * @param layerGroupName 图层组名称
     * @return 图层组是否存在
     * @throws WorkSpaceNotFoundException 工作空间不存在
     */
    public Boolean existsLayerGroup(String workspaceName, String layerGroupName) throws WorkSpaceNotFoundException {
        if (!existsWorkspace(workspaceName)) {
            throw new WorkSpaceNotFoundException(workspaceName);
        }
        return geoServerRESTReader.existsLayerGroup(workspaceName, layerGroupName);
    }

    /**
     * 获取所有图层组名称列表
     *
     * @param workspaceName 工作空间名称
     * @return 图层组名称列表
     */
    public ArrayList<String> getLayerGroups(String workspaceName) {
        RESTLayerGroupList layerGroups = geoServerRESTReader.getLayerGroups(workspaceName);
        return new ArrayList<>(layerGroups.getNames());
    }

    ///============ style样式服务部分 ===============================

    /**
     * 判断某工作空间下是否包含 style 服务
     *
     * @param workspaceName 工作空间名称
     * @param styleName     style 服务名称
     * @return 是否包含style服务
     * @throws WorkSpaceNotFoundException 工作空间不存在
     */
    public Boolean existsStyleFromWorkspace(String workspaceName, String styleName) throws WorkSpaceNotFoundException {
        if (!existsWorkspace(workspaceName)) {
            throw new WorkSpaceNotFoundException(workspaceName);
        }
        return geoServerRESTReader.existsStyle(workspaceName, styleName);
    }

    /**
     * 获取所有样式服务
     *
     * @return 样式服务名称列表
     */
    public ArrayList<String> getStyles() {
        RESTStyleList styleList = geoServerRESTReader.getStyles();
        return new ArrayList<>(styleList.getNames());
    }

    /**
     * 获取某工作空间下所有样式服务
     *
     * @param workspaceName 工作空间名称
     * @return 样式服务名称列表
     * @throws WorkSpaceNotFoundException 工作空间不存在
     */
    public ArrayList<String> getStyles(String workspaceName) throws WorkSpaceNotFoundException {
        if (!existsWorkspace(workspaceName)) {
            throw new WorkSpaceNotFoundException(workspaceName);
        }
        RESTStyleList styleList = geoServerRESTReader.getStyles(workspaceName);
        return new ArrayList<>(styleList.getNames());
    }

    /**
     * 样式服务是否存在
     *
     * @Date: 2021/7/19 11:42
     * @param styleName:  样式服务名
     * @return 样式服务是否存在
     */
    public Boolean existsStyle(String styleName) {
        return geoServerRESTReader.existsStyle(styleName);
    }

    ///============ 切片服务部分 ===============================

    /**
     * 判断图层是否存在与 gwcLayers 中
     *
     * @param workspaceName: 工作空间名
     * @param gwcLayersName: 图层名
     * @return 是否存在
     * @Date: 2021/7/19 9:44
     */
    public Boolean existsGwcLayers(String workspaceName, String gwcLayersName) {
        //请求路径
        String url = String.format("%s/gwc/rest/layers", this.restUrl);
        String result = HTTPUtils.get(url, this.userName, this.password);
        return result.contains(workspaceName + ":" + gwcLayersName);
    }

    /**
     * 获取所有图层切片任务的运行状态
     *
     * @return 任务运行状态 :[已处理的瓦片数量、要处理的瓦片数量,剩余瓦片数量,任务的id,任务运行状态]
     * -1 = ABORTED
     *  0 = PENDING
     *  1 = RUNNING
     *  2 = DONE
     * @Date: 2021/7/19 9:39
     */
    public String GetRunningGwcTasksForAllLayers() {
        //请求路径
        String url = String.format("%s/gwc/rest/seed.json", this.restUrl);
        return HTTPUtils.get(url, this.userName, this.password);
    }

    /**
     * 获取指定图层名的切片任务执行状态
     *
     * @param workspaceName: 工作空间名
     * @param gwcLayerName:  切片图层名
     * @return 任务执行状态 json 字符串数组，
     * 任务运行状态 :[要处理的瓦片数量,剩余瓦片数量,任务的id,{任务线程的序号},任务运行状态]
     *  -1 = ABORTED
     *  0 = PENDING
     *  1 = RUNNING
     *  2 = DONE
     * @throws GwcLayerNotFoundException 图层不存在
     * @Date: 2021/7/19 11:39
     */
    public String GetSpecificRunningGwcTasks(String workspaceName, String gwcLayerName) throws GwcLayerNotFoundException {
        if (!existsGwcLayers(workspaceName, gwcLayerName)) {
            throw new GwcLayerNotFoundException(gwcLayerName);
        }
        //请求路径
        String url = String.format("%s/gwc/rest/seed/%s:%s.json", this.restUrl, workspaceName, gwcLayerName);
        return HTTPUtils.get(url, this.userName, this.password);
    }

    /**
     * 获取 gwc 图层的边界
     *
     * @Date: 2021/7/19 11:52
     * @param workspaceName: 工作空间名
     * @param gwcLayerName: gwc 图层名
     * @param srcCode: 坐标系代码
     * @return 边界坐标数组
     * @throws GwcLayerNotFoundException 切片图层不存在
     */
    public String getGwcLayersBounds(String workspaceName, String gwcLayerName, Integer srcCode) throws GwcLayerNotFoundException {
        if (!existsGwcLayers(workspaceName, gwcLayerName)) {
            throw new GwcLayerNotFoundException(workspaceName + ":" + gwcLayerName);
        }
        //请求路径
        String url = String.format("%s/gwc/rest/bounds/%s:%s/EPSG:%s/java",
                this.restUrl, workspaceName, gwcLayerName, srcCode);
        return HTTPUtils.get(url, this.userName, this.password);
    }
}
