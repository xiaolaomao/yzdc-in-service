package com.yzdc.in.controller;

import com.yzdc.in.utils.Constants;
import com.yzdc.in.utils.MD5Signature;
import com.yzdc.in.utils.PropertyTools;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Desc:     上报接口抽象类
 * Author: Iron
 * CreateDate: 2016-10-12
 * CopyRight: Beijing Yunzong Co., Ltd.
 */

public class AbstractBaseController {
    private final Logger logger = LogManager.getLogger(AbstractBaseController.class);

    private static int timeOut = Integer.valueOf(PropertyTools.getPropertyBy("auth.interval"));//有效时间间隔
    private static String secret = PropertyTools.getPropertyBy("auth.salt");//加密值


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
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
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
    protected boolean validInbound(String inbound, HttpServletRequest request, HttpServletResponse response) {
        if (inbound == null || inbound.equals("")) {
            return false;
        }
        String inKey = request.getHeader("inKey");
        String sign = request.getHeader("sign");
        String timeStamp = request.getHeader("timeStamp");
        boolean b = validVerfyHeader(inKey, inbound, timeStamp, sign, response);
        return b;
    }

    /**
     * 校验上报时的AppVerify头
     *
     * @param inbound
     * @param timeStamp
     * @param sign
     * @return
     */
    private boolean validVerfyHeader(String inKey, String inbound, String timeStamp, String sign, HttpServletResponse response) {
        try {
            // 时间有效期验证
            Long times = Long.parseLong(timeStamp);
            Long currentTimes = System.currentTimeMillis();
            Long interval = Math.abs(currentTimes - times);
            if (interval >= timeOut) {
                logger.error("------> 连接认证失败,当前时间戳已经过期!");
                this.returnError(response, "连接认证失败,当前时间戳已经过期!");
                return false;
            }
            // 拼装加密值
            Map<String, String> secretMap = new HashMap<String, String>();
            secretMap.put("data", inbound);
            secretMap.put("inKey", inKey);
            String dataSign = MD5Signature.signTopRequest(secretMap, secret, Constants.SIGN_METHOD_MD5);
            if (!dataSign.equals(sign)) {
                logger.error("------> 连接认证失败,当前时间戳已经过期!");
                this.returnError(response, "连接认证失败,校验值不合法!");
                return false;
            }
        } catch (IOException ex) {
            logger.error("------> Auth Fail:" + ex);
            this.returnError(response, "连接认证失败," + ex);
            return false;
        }
        return true;
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
