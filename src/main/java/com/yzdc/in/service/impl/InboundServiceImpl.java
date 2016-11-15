package com.yzdc.in.service.impl;

import com.yzdc.in.channel.JedisUtils;
import com.yzdc.in.channel.KafkaUtils;
import com.yzdc.in.dao.YzdcMallCampRealTimeDAO;
import com.yzdc.in.model.YzdcMallCampRealTime;
import com.yzdc.in.service.InboundService;
import com.yzdc.in.utils.DateUtils;
import com.yzdc.in.utils.JsonUtils;
import com.yzdc.in.utils.PropertyTools;
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
        String retMsg = "fail";
        logger.info("----> InboundService.handleMallInbound!");
        JSONObject jsonObject = JSONObject.fromObject(inbound);
        if (jsonObject != null && !jsonObject.isEmpty()) {
            String inType = jsonObject.get("inType").toString();
            if ("mysql".equalsIgnoreCase(inType)) {
                // 写入mysql数据库
                doCampRealTime(jsonObject.get("dataInfo").toString());
                retMsg = "success";
            } else if ("redis".equalsIgnoreCase(inType)) {
                // 写入redis
                JedisUtils.setString("", "");
                retMsg = "success";
            } else if ("kafka".equalsIgnoreCase(inType)) {
                // 写入kafka
                KafkaUtils.sendKafkaMsg(PropertyTools.getPropertyBy("kafka.mall.topic"), jsonObject.get("dataInfo").toString());
                retMsg = "success";
            }
        }
        return retMsg;
    }


    /**
     * 处理mall活动数据,先插入数据,再进行删除
     *
     * @param jsonStr
     * @return
     */
    private boolean doCampRealTime(String jsonStr) throws Exception {
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
}
