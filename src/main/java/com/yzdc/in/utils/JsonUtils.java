package com.yzdc.in.utils;

import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.dubbo.common.json.JSONObject;

import java.io.IOException;

/**
 * Desc: Json解析工具类
 * Author: Iron
 * CreateDate: 2016-10-10
 * CopyRight: Beijing Yunzong Co., Ltd.
 */
public class JsonUtils {
    static String jsonArrayData;
    static JSONObject result;

    /**
     * 返回response字符串
     *
     * @param status
     * @param msg
     * @param <T>
     * @return
     */
    public static <T> String responseRetMsg(int status, String msg) {
        result = new JSONObject();
        try {
            result.put("msg", msg);//提示信息
            result.put("status", status);// 200 成功 500 失败
            jsonArrayData = JSON.json(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonArrayData;
    }
}
