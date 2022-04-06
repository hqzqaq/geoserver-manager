package com.southsmart.geo.manager.error;

/**
 * 一般性错误
 *
 * @author huquanzhi
 * @version 1.0.0
 * @class ErrorException
 * @date 2021/7/7  11:35
 */

public class ErrorException extends Exception{
    public ErrorException(String message){
        super(message);
    }
}
