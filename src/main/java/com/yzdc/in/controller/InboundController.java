package com.yzdc.in.controller;

import com.yzdc.in.service.InboundService;
import com.yzdc.in.utils.DateUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Desc:     数据上服务
 * Author: Iron
 * CreateDate: 2016-10-12
 * CopyRight: Beijing Yunzong Co., Ltd.
 */
@Controller
@RequestMapping("/inbound")
public class InboundController extends AbstractBaseController {
    private final Logger logger = LogManager.getLogger(InboundController.class);
    @Autowired
    private InboundService inboundService;

    /**
     * 测试链接
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public void sayHello(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("----> come into InboundController.sayHello -->");
        PrintWriter pw = getJsonPrintWriter(response);
        response.setStatus(HttpStatus.OK.value());
        pw.println("hello guy from " + request.getRemoteHost());
        pw.flush();
        closePrintWriter(pw);
    }

    /**
     * 累计上线活动数
     *
     * @param jsonStr
     * @param request
     * @param response
     * @author xingshen.zhao
     */
    @RequestMapping(value = "/onlineCampaignRealTime", method = RequestMethod.POST, consumes = "application/json; charset=utf-8")
    public void onlineCampaignRealTime(@RequestBody String jsonStr,
                                       HttpServletRequest request,
                                       HttpServletResponse response) {
        logger.debug("--> come into InboundController.handleMallInbound for mall-->");
        PrintWriter pw = getJsonPrintWriter(response);
        // 校验合法性
        if (validInbound(jsonStr, request, response)) {
            try {
                logger.info("------> 时间" + DateUtils.getCurrentTime() + ":开始上报累计上线活动数据....");
                //调用kafka消息队列
                String retMsg = inboundService.handleMallInbound(jsonStr.toString());
                if (retMsg.equals("success")) {
                    response.setStatus(HttpStatus.OK.value());
                    pw.print("----->上报成功!");
                }
                logger.info("------> 时间" + DateUtils.getCurrentTime() + ":上报累计上线活动数据成功!");
            } catch (Exception ex) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                pw.print("----->上报失败,原因:" + ex.getMessage());
            }
            pw.flush();
            closePrintWriter(pw);
        }
    }
}
