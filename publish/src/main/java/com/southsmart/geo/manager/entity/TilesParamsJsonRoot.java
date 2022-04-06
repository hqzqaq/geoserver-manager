package com.southsmart.geo.manager.entity;

/**
 * 构造执行切片任务的 java 对象，json 字符串的根节点
 *
 * @author huquanzhi
 * @version 1.0.0
 * @class JsonRootBean
 * @date 2021/7/19  11:01
 */

public class TilesParamsJsonRoot {
    private SeedRequest seedRequest;
    public void setSeedRequest(SeedRequest seedRequest) {
        this.seedRequest = seedRequest;
    }
    public SeedRequest getSeedRequest() {
        return seedRequest;
    }

    public TilesParamsJsonRoot() {
    }

    public TilesParamsJsonRoot(SeedRequest seedRequest) {
        this.seedRequest = seedRequest;
    }
}
