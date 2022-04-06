package com.southsmart.geo.manager.entity;

/**
 * 构造执行切片任务的 java 对象
 *
 * @author huquanzhi
 * @version 1.0.0
 * @class TileTaskParams
 * @date 2021/7/19  10:41
 */

public class SeedRequest {
    /**
     * 图层名称
     */
    private String name;
    /**
     * 坐标系编码
     */
    private Srs srs;
    /**
     * 切图级别，最小为0
     */
    private int zoomStart;
    /**
     * 切图级别，最大为30
     */
    private int zoomStop;
    /**
     * 切片格式，FormatConstant.IMAGE_PNG OR FormatConstant.IMAGE_JPEG
     */
    private String format;
    /**
     * 切面任务执行类型,TileType
     * Reseed - regenerate all tiles
     * seed - generate missing tiles
     * Truncate - remove tiles
     */
    private String type;
    /**
     * 任务执行的线程数量 最大为 128
     */
    private int threadCount;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setSrs(Srs srs) {
        this.srs = srs;
    }

    public Srs getSrs() {
        return srs;
    }

    public void setZoomStart(int zoomStart) {
        this.zoomStart = zoomStart;
    }

    public int getZoomStart() {
        return zoomStart;
    }

    public void setZoomStop(int zoomStop) {
        this.zoomStop = zoomStop;
    }

    public int getZoomStop() {
        return zoomStop;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public SeedRequest() {
    }

    public SeedRequest(String name, Srs srs, int zoomStart, int zoomStop, String format, String type, int threadCount) {
        this.name = name;
        this.srs = srs;
        this.zoomStart = zoomStart;
        this.zoomStop = zoomStop;
        this.format = format;
        this.type = type;
        this.threadCount = threadCount;
    }
}
