package com.yzdc.in.channel;

import com.yzdc.in.utils.PropertyTools;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.Properties;

/**
 * Desc:    kafka工具类
 * Author: Iron
 * CreateDate: 2016-11-07
 * CopyRight: Beijing Yunzong Co., Ltd.
 */
public class KafkaUtils {

    private static String brokerList = PropertyTools.getPropertyBy("kafka.broker.list");
    private static String requiredAcks = PropertyTools.getPropertyBy("kafka.required.acks");

    /**
     * 向kafka中发送消息
     *
     * @param msg
     */
    public static void sendKafkaMsg(String topic, String msg) {
        Properties props = new Properties();
        //设置配置文件
        props.put("metadata.broker.list", brokerList);
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("partitioner.class", "com.yzdc.in.utils.KafkaPartitioner");
        props.put("request.required.acks", requiredAcks);
        ProducerConfig config = new ProducerConfig(props);
        //创建生产者
        Producer<String, String> producer = new Producer<String, String>(config);
        //拼装数据
        KeyedMessage<String, String> data = new KeyedMessage<String, String>(topic, msg);
        //发送数据
        producer.send(data);
        producer.close();
    }
}
