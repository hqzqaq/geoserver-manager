package com.southsmart.geo.manager.error.ogc;



/**
 * 样式服务不存在的错误
 *
 * @author huquanzhi
 * @version 1.0.0
 * @class StyleServiceNotFoundException
 * @date 2021/7/7  11:39
 */

public class StyleServiceNotFoundException extends Exception {
    public StyleServiceNotFoundException(String styleName){
        super(String.format("样式服务：%s不存在",styleName));
    }
}
