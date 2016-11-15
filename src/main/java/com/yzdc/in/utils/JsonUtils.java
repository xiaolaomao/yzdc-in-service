package com.yzdc.in.utils;

import com.alibaba.dubbo.common.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
     * @return
     */
    public static String responseRetMsg(int status, String msg) {
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

    /**
     * 将json字符串转换为java对象列表数据
     *
     * @param jsonString
     * @param clazz
     * @return
     */
    public static List getDTOList(String jsonString, Class clazz) {
        JSONArray array = JSONArray.fromObject(jsonString);
        List<Object> obj = new ArrayList<Object>(array.size());
        for (int i = 0; i < array.size(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            obj.add(JSONObject.toBean(jsonObject, clazz));
        }
        return obj;
    }
}
