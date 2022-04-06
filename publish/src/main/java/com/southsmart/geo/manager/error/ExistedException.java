package com.southsmart.geo.manager.error;

/**
 * 已存在的错误
 *
 * @author huquanzhi
 * @version 1.0.0
 * @class ExistedException
 * @date 2021/7/7  11:34
 */

public class ExistedException extends Exception{
    public ExistedException(String message){
        super(message + "已存在");
    }
}
