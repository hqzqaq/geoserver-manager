package com.southsmart.geo.manager.error.ogc;

/**
 * 工作空间不存在的错误
 *
 * @author huquanzhi
 * @version 1.0.0
 * @class WorkSpaceNotFoundException
 * @date 2021/7/7  11:42
 */

public class WorkSpaceNotFoundException extends Exception{
    public WorkSpaceNotFoundException(String workspaceName){
        super(String.format("工作空间：%s不存在",workspaceName));
    }
}
