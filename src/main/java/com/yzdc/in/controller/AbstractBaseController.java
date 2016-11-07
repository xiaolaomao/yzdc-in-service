package com.yzdc.in.controller;

import com.yzdc.in.utils.Md5Encrypt;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * Desc:     上报接口抽象类
 * Author: Iron
 * CreateDate: 2016-10-12
 * CopyRight: Beijing Yunzong Co., Ltd.
 */

public class AbstractBaseController {
    private final Logger logger = LogManager.getLogger(AbstractBaseController.class);

    //手机上报时间戳同系统当前时间的允许的时间差
    private static long ValidTimeDuarationMsecons = 5 * 60 * 1000;

    /**
     * 通用返回错误信息方法
     *
     * @param response
     * @param errInfo
     */
    protected void returnError(HttpServletResponse response, String errInfo) {
        response.setContentType("text/plain;charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        response.setStatus(HttpStatus.BAD_REQUEST.value());

        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.println(errInfo);
            pw.flush();
        } catch (IOException e) {
            logger.error("response.getWriter() error: " + e.getMessage());
        } finally {
            pw.close();
        }
    }

    /**
     * 校验提交的上报信息
     *
     * @param inbound
     * @param request
     * @return
     */
    protected boolean validInbound(String inbound, HttpServletRequest request) {
        if (inbound == null || inbound.equals("")) {
            return false;
        }
        //TODO 解析json信息校验上报数据的合法性
        return false;
    }

    /**
     * 校验上报时的AppVerify头
     *
     * @param appId
     * @param appKey
     * @param varifyApp
     * @return
     */
    private boolean validVerfyHeader(
            String appId, String appKey, String varifyApp
    ) {
        if (varifyApp != null && !varifyApp.equals("")) {
            String[] validateArray = varifyApp.split(";");
            if (validateArray.length < 2) {
                logger.error("validateArray length < 2");
                return false;
            }
            String md5Code = validateArray[0].replaceFirst("md5=", "");
            String ts = validateArray[1].replaceFirst("ts=", "");
            //验证时间戳
            if (!validateTimestamp(ts)) {
                logger.error("timestamp invalide");
                return false;
            }
            String localMd5 = Md5Encrypt.md5(appId + ":" + appKey + ":" + ts);
            if (!localMd5.equals(md5Code)) {
                logger.info("request md5 is :" + md5Code + " and localMd5 is:" + localMd5 + " from MD5(" + appId + ":" + appKey + ":" + ts + ")");
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    /**
     * 校验时间戳
     *
     * @param ts
     * @return
     */
    private boolean validateTimestamp(String ts) {
        if (ts == null || ts.equals("")) {
            return false;
        }
        if (ts.length() < 13) {
            ts = ts + "000";
        }
        Date d = new Date();
        Date dbefor = new Date(d.getTime() - ValidTimeDuarationMsecons);// 前十分中时间
        Date dafter = new Date(d.getTime() + ValidTimeDuarationMsecons);// 后十分钟时间
        logger.debug("dbefore timestmp:" + dbefor.getTime()
                + " and dafter timestamp:" + dafter.getTime());
        Long requestTimestmp = Long.parseLong(ts);
        if (requestTimestmp > dbefor.getTime()
                && requestTimestmp < dafter.getTime()) {
            return true;
        } else {
            logger.debug("requestTimestmp =" + requestTimestmp);
            return false;
        }
    }

    /**
     * 写信息
     *
     * @param response
     * @return
     */
    protected PrintWriter getJsonPrintWriter(HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter pw = null;
        if (response != null) {
            try {
                pw = response.getWriter();
            } catch (IOException e) {
                logger.error(" get printWrite error !!!");
            }
        }
        return pw;
    }

    /**
     * 关闭打印流
     *
     * @param pw
     */
    protected void closePrintWriter(PrintWriter pw) {
        if (pw != null) {
            pw.flush();
            pw.close();
        }
    }
}
