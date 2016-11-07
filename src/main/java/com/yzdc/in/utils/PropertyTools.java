package com.yzdc.in.utils;


import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * Desc:    配置文件读取工具类
 * Author: Iron
 * CreateDate: 2016-11-07
 * CopyRight: Beijing Yunzong Co., Ltd.
 */
public class PropertyTools {
    private static Config conf = ConfigFactory.load();

    public static String getPropertyBy(String key) {
        return conf.getString(key).trim();
    }
}
