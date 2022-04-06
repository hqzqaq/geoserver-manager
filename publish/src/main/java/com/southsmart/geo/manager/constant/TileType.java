package com.southsmart.geo.manager.constant;

/**
 * 切面执行的类型
 *
 * @author huquanzhi
 * @version 1.0.0
 * @class TileType
 * @date 2021/7/16  16:38
 */

public class TileType {
    /**
     * Reseed - regenerate all tiles
     */
    public static final String RESEED = "reseed";
    /**
     * Seed - generate missing tiles
     */
    public static final String SEED = "seed";
    /**
     * Truncate - remove tiles
     */
    public static final String TRUNCATE = "truncate";
}
