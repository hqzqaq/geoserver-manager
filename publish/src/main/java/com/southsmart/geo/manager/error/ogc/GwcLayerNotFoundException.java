package com.southsmart.geo.manager.error.ogc;

/**
 * gwc 图层不存在
 *
 * @author huquanzhi
 * @version 1.0.0
 * @class GwcLayerNotFoundException
 * @date 2021/7/16  16:09
 */

public class GwcLayerNotFoundException extends Exception{
    public GwcLayerNotFoundException(String gwcLayerName){
        super(String.format("gwc图层：%s不存在",gwcLayerName));
    }
}
