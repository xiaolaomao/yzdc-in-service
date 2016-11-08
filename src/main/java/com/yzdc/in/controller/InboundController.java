package com.yzdc.in.controller;

import com.yzdc.in.service.InboundService;
import net.sf.json.JSONObject;
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
        logger.info("----> come into InboundController.sayHello -->");
        PrintWriter pw = getJsonPrintWriter(response);
        response.setStatus(HttpStatus.OK.value());
        pw.println("hello guy from " + request.getRemoteHost());
        pw.flush();
        closePrintWriter(pw);
    }

    /**
     * 商业中心日志上报接口
     *
     * @param jsonStr
     * @param response
     * @author xingshen.zhao
     */
    @RequestMapping(value = "/mall", method = RequestMethod.POST)
    public void handleMallInbound(@RequestBody String jsonStr,
                                  HttpServletResponse response) {

        logger.info("--> come into InboundController.handleMallInbound for mall-->");
        //调用kafka消息队列
        JSONObject json = JSONObject.fromObject(jsonStr);
        String errorMsg = inboundService.handleMallInbound(json.toString());
        PrintWriter pw = getJsonPrintWriter(response);
        if (errorMsg == null || errorMsg.equals("")) {
            response.setStatus(HttpStatus.OK.value());
            pw.println("ok");
        } else {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            pw.println(errorMsg);
        }
        pw.flush();
        closePrintWriter(pw);
    }
}
