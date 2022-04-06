package com.southsmart.geo.manager.error.ogc;

/**
 * 图层组不存在的错误
 *
 * @author huquanzhi
 * @version 1.0.0
 * @class LayerGroupNotFoundException
 * @date 2021/7/7  11:37
 */

public class LayerGroupNotFoundException extends Exception{
    public LayerGroupNotFoundException(String layerGroupName){
        super(String.format("图层组：%s不存在",layerGroupName));
    }
}
