package com.yzdc.in.service.impl;

import com.yzdc.in.channel.JedisUtils;
import com.yzdc.in.channel.KafkaUtils;
import com.yzdc.in.dao.YzdcMallCampRealTimeDAO;
import com.yzdc.in.model.YzdcMallCampRealTime;
import com.yzdc.in.service.InboundService;
import com.yzdc.in.utils.DateUtils;
import com.yzdc.in.utils.JsonUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * Desc:    上报服务实现类
 * Author: Iron
 * CreateDate: 2016-11-14
 * CopyRight: Beijing Yunzong Co., Ltd.
 */
@Service
public class InboundServiceImpl implements InboundService, Serializable {

    private final Logger logger = LogManager.getLogger(InboundServiceImpl.class);

    @Autowired
    private YzdcMallCampRealTimeDAO yzdcMallCampRealTimeDAO;

    /**
     * mall端数据上报接口
     *
     * @param inbound
     * @return add by xignshen.zhao 2016-10-12
     */
    @Transactional(rollbackFor = Exception.class)
    public String handleMallInbound(String inbound) throws Exception {
        boolean retFlag = false;
        logger.debug("----> InboundService.handleMallInbound!");
        JSONObject jsonObject = JSONObject.fromObject(inbound);
        if (jsonObject != null && !jsonObject.isEmpty()) {
            String inType = jsonObject.get("inType").toString();
            if ("mysql".equalsIgnoreCase(inType)) {
                // 写入mysql数据库
                retFlag = handleMysql(jsonObject.get("dataInfo").toString());
            } else if ("redis".equalsIgnoreCase(inType)) {
                // 写入redis
                retFlag = handleRedis(jsonObject.get("dataInfo").toString());
            } else if ("kafka".equalsIgnoreCase(inType)) {
                // 写入kafka
                String topic = jsonObject.get("topic").toString();
                String producerType = jsonObject.get("producerType").toString();
                retFlag = handleKafka(topic, jsonObject.get("dataInfo").toString(), producerType);
            }
        }
        if (retFlag) {
            return "success";
        } else {
            return "fail";
        }
    }


    /**
     * 处理mall活动数据,先插入数据,再进行删除
     *
     * @param jsonStr
     * @return
     */
    private boolean handleMysql(String jsonStr) throws Exception {
        Boolean retFlag = false;
        List<YzdcMallCampRealTime> insList = JsonUtils.getDTOList(jsonStr, YzdcMallCampRealTime.class);
        if (insList != null && insList.size() > 0) {
            String biz_date = DateUtils.formatDate(insList.get(0).getReport_date(), "yyyy-MM-dd");
            //删除数据
            yzdcMallCampRealTimeDAO.deleteCampRealTimeByBizDate(biz_date);
            //插入数据
            int ret = yzdcMallCampRealTimeDAO.batchInsertCampRealTime(insList);
            if (ret > 0) {
                JedisUtils.setString("mallCampRealTime", JedisUtils.EXRP_HOUR, String.valueOf(System.currentTimeMillis()));
                retFlag = true;
            }
        }
        return retFlag;
    }


    /**
     * 上报数据至kafka
     *
     * @param topic
     * @param jsonStr
     * @param producerType
     * @return
     */
    private boolean handleKafka(String topic, String jsonStr, String producerType) {
        Boolean retFlag = true;
        try {
            KafkaUtils.sendKafkaMsg(topic, jsonStr, producerType);
        } catch (Exception ex) {
            logger.error("----->上报数据至kafka失败!原因:" + ex.getMessage());
            retFlag = false;
        }
        return retFlag;
    }


    /**
     * 上报至redis
     *
     * @param jsonStr
     * @return
     */
    private boolean handleRedis(String jsonStr) {
        Boolean retFlag = true;
        JSONObject jsonObject = JSONObject.fromObject(jsonStr);
        String key = jsonObject.get("key").toString();
        String value = jsonObject.get("value").toString();
        try {
            //TODO 目前只支持写入key,value值,后续可根据需要处理更负载的逻辑
            JedisUtils.setString(key, value);
        } catch (Exception ex) {
            logger.error("----->上报数据至redis失败!原因:" + ex.getMessage());
            retFlag = false;
        }
        return retFlag;
    }
}
