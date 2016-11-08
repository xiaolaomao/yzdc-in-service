package com.yzdc.in;

import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

/**
 * Desc:    测试上报kafka客户端
 * Author: Iron
 * CreateDate: 2016-11-07
 * CopyRight: Beijing Yunzong Co., Ltd.
 */
public class InboundControllerTest {
    //上报接口
    private static String url = "http://10.200.12.76:8091/yzdc-in-service/yzdc/inbound/mall";

    /**
     * post发送客户端
     *
     * @param urlString
     * @param params
     * @return
     */
    private static String httpPost(String urlString, String params) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(urlString);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/json");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(params);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        } finally {
            //使用finally块来关闭输出流、输入流
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 测试发送数据
     *
     * @param args
     */
    public static final void main(String[] args) {
        long timeNowMs = System.currentTimeMillis();
        //发送数据
        for (int i = 1; i <= 100; i++) {
            try {
                String msg = "{"
                        + "\"col1\":\"" + i + "\",\"col2\":\"yzdc\",\"col3\":\"yzdc\","
                        + "\"col4\":\"yzdc\",\"col5\":\"yzdc\","
                        + "\"col6\":\"yzdc\",\"col7\":\"yzdc\",\"col8\":\"yzdc\","
                        + "\"col9\":\"yzdc\",\"col10\":\"yzdc\","
                        + "\"col11\":\"yzdc\",\"col12\":\"yzdc\","
                        + "\"col13\":\"yzdc\","
                        + "\"createdAtMs\": " + (timeNowMs + 1) + "}";
                JSONObject json = JSONObject.fromObject(msg);
                httpPost(url, json.toString());
                System.err.println("正在发送数据：" + i + "::" + json.toString());
                //写入日志文件
                Thread.sleep(200);
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    }
}
