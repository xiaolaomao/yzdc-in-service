package com.yzdc.in.channel;

import com.yzdc.in.utils.PropertyTools;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import net.sf.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
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
    public static void sendKafkaMsg(String topic, String msg, String producerType) throws Exception {
        //转换为小写
        producerType = producerType.toLowerCase();
        Properties props = new Properties();
        //设置配置文件
        props.put("metadata.broker.list", brokerList);
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("partitioner.class", "com.yzdc.in.channel.KafkaPartitioner");
        props.put("request.required.acks", requiredAcks);
        props.put("producer.type", producerType);
        ProducerConfig config = new ProducerConfig(props);
        //创建生产者
        Producer<String, String> producer = new Producer<String, String>(config);
        if (producerType.equals("async")) {
            List<KeyedMessage<String, String>> messages = new ArrayList<KeyedMessage<String, String>>(100);//100条信息发送一次
            //转换为json数组
            JSONArray jsonArray = JSONArray.fromObject(msg);
            for (int i = 0; i < jsonArray.size(); i++) {
                KeyedMessage<String, String> message =
                        new KeyedMessage<String, String>(topic, jsonArray.get(i).toString());
                messages.add(message);
                if (i % 100 == 0) {
                    producer.send(messages);
                    messages.clear();
                }
            }
            producer.send(messages);
        } else {
            //拼装数据
            KeyedMessage<String, String> data = new KeyedMessage<String, String>(topic, msg);
            //发送数据
            producer.send(data);
        }
        producer.close();
    }
}
