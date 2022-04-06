package com.southsmart.geo.manager.utils;

import java.io.*;

/**
 * @author huquanzhi
 * @version 1.0.0
 * @class ComTools
 * @date 2021/7/16  15:17
 */

public class ComTools {
    /**
     * 读取文件内容
     *
     * @param file File对象
     * @return 返回文件内容
     * @throws IOException IO 错误
     */
    public static String readFile(File file) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(file));
        String str;
        StringBuilder stringBuffer = new StringBuilder();

        while ((str = in.readLine()) != null) {
            stringBuffer.append(str);
        }

        return stringBuffer.toString();
    }

    /**
     * 提供 curl get 提交方法
     *
     * @Date: 2021/7/16 15:19
     * @param cmds:  curl 命令行 String 数组
     * @return 执行curl命令返回值 JSON
     * @throws IOException 获取输入输出流不存在
     */
    public static String curlGet(String[] cmds) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(cmds);

        Process start = processBuilder.start();
        InputStream inputStream = start.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        StringBuilder stringBuilder = new StringBuilder();

        bufferedReader.lines().forEach(stringBuilder::append);

        return stringBuilder.toString();
    }

    /**
     * 提供curl post 请求方法
     * @param cmds curl 命令行 String 数组
     * @return 执行curl命令返回值 JSON
     * @throws IOException 获取输入输出流不存在
     */
    public static String curlPost(String[] cmds) throws IOException {
        return curlGet(cmds);
    }
}
