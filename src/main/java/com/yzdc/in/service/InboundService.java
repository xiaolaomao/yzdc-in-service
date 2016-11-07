package com.yzdc.in.service;

import com.yzdc.in.utils.KafkaUtils;
import com.yzdc.in.utils.PropertyTools;
import org.springframework.stereotype.Service;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Desc:     上报Kafka接口
 * Author: Iron
 * CreateDate: 2016-10-12
 * CopyRight: Beijing Yunzong Co., Ltd.
 */
@Service
public class InboundService {
    private final Logger logger = LogManager.getLogger(InboundService.class);

    /**
     * mall端日志上报接口
     *
     * @param inbound
     * @return add by xignshen.zhao 2016-10-12
     */
    public String handleMallInbound(String inbound) {
        logger.info("----> InboundService.handleMallInbound!");
        try {
            KafkaUtils.sendKafkaMsg(PropertyTools.getPropertyBy("kafka.mall.topic"), inbound);
        } catch (Exception e) {
            logger.error("----> InboundService.handleMallInbound,Exception:", e);
            return "----> InboundService.handleMallInbound,send to kafka error!";
        }
        return null;
    }
}
