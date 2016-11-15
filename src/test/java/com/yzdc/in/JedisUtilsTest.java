package com.yzdc.in;

import com.yzdc.in.channel.JedisUtils;

/**
 * Desc:
 * Author: Iron
 * CreateDate: 2016-11-15
 * CopyRight: Beijing Yunzong Co., Ltd.
 */
public class JedisUtilsTest {


    /**
     * 测试类
     *
     * @param args
     */
    public static void main(String[] args) {
        //JedisUtils.setString("mallCampRealTime", "2222");
        System.out.println(JedisUtils.getString("mallCampRealTime"));
    }
}
