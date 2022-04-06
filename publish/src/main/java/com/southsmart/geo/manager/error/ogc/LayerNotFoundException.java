package com.southsmart.geo.manager.error.ogc;

/**
 * 图层不存在的错误
 *
 * @author huquanzhi
 * @version 1.0.0
 * @class LayerNotFoundException
 * @date 2021/7/7  11:38
 */

public class LayerNotFoundException extends Exception{
    public LayerNotFoundException(String coverageStoreName){
        super(String.format("图层：%s不存在",coverageStoreName));
    }
}
