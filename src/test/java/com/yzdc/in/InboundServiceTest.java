package com.yzdc.in;


import com.yzdc.in.channel.JedisUtils;
import com.yzdc.in.service.InboundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Desc:    测试上报kafka客户端
 * Author: Iron
 * CreateDate: 2016-11-07
 * CopyRight: Beijing Yunzong Co., Ltd.
 */
public class InboundServiceTest extends AbstractTestNGTest {


    @Autowired
    private InboundService inboundService;//需要依赖的服务

    /**
     * 上报至kafka
     */
    @Test
    public void testKafka() {
        String sync = "{\"msg\": \"累计上线活动数!\",\"topic\": \"yzmall\", \"inType\": \"kafka\",\"producerType\": \"async\", \"dataInfo\": [{\"report_date\": 1479258937, \"data_type\": \"1\", \"active_number\": 7}, {\"report_date\": 1479258937, \"data_type\": \"5\", \"active_number\": 224}]}";
        String async = "{\"msg\": \"累计上线活动数!\",\"topic\": \"yzmall\", \"inType\": \"kafka\",\"producerType\": \"sync\", \"dataInfo\": {\"report_date\": 1479258937, \"data_type\": \"1\", \"active_number\": 7}}";
        try {
            String msg = inboundService.handleMallInbound(sync);
            Assert.assertEquals(msg, "success");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            String msg = inboundService.handleMallInbound(async);
            Assert.assertEquals(msg, "success");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 上报至redis
     */
    @Test
    public void testRedis() {
        String inbound = "{\"msg\": \"上报redis!\", \"inType\": \"redis\", \"dataInfo\": {\"key\": \"realTimeTest\",\"value\":1479258937}}";
        try {
            String msg = inboundService.handleMallInbound(inbound);
            Assert.assertEquals(msg, "success");
            Assert.assertEquals("1479258937", JedisUtils.getString("realTimeTest"));
            System.out.print(JedisUtils.getString("realTimeTest"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    /**
     * 上报至mysql数据库
     */
    public void testMysql() {
        String inbound = "{\"msg\": \"累计上线活动数!\", \"inType\": \"mysql\", \"dataInfo\": [{\"report_date\": 1479258937, \"data_type\": \"1\", \"active_number\": 7}, {\"report_date\": 1479258937, \"data_type\": \"5\", \"active_number\": 224}]}";
        try {
            String msg = inboundService.handleMallInbound(inbound);
            Assert.assertEquals(msg, "success");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
