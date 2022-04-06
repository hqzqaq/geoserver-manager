package com.southsmart.geo.manager.entity;

/**
 * 构造执行切片任务的 java 对象，坐标系代码
 *
 * @author huquanzhi
 * @version 1.0.0
 * @class Srs
 * @date 2021/7/19  11:00
 */

public class Srs {
    private int number;
    public void setNumber(int number) {
        this.number = number;
    }
    public int getNumber() {
        return number;
    }

    public Srs(int number) {
        this.number = number;
    }

    public Srs() {
    }
}
