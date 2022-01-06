package com.wjjzst.common.utils.file;


import java.io.InputStream;

/**
 * @Author: Wjj
 * @Date: 2021/7/9 10:34 上午
 * @desc:
 */
public class SourceFileUtil {
    public static InputStream getFileFromResources(String resourceFilePath) {
        return ClassLoader.getSystemResourceAsStream(resourceFilePath);
    }
}
