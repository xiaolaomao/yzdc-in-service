package com.yzdc.in.utils;


import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

/**
 * Desc:    kafka分区函数
 * Author: Iron
 * CreateDate: 2016-11-07
 * CopyRight: Beijing Yunzong Co., Ltd.
 */
public class KafkaPartitioner implements Partitioner {
    public KafkaPartitioner(VerifiableProperties props) {
    }

    /**
     * kafka分区
     *
     * @param key
     * @param a_numPartitions
     * @return
     */
    public int partition(Object key, int a_numPartitions) {
        int partition = 0;
        String stringKey = (String) key;
        int offset = stringKey.lastIndexOf('.');
        if (offset > 0) {
            partition = Integer.parseInt(stringKey.substring(offset + 1)) % a_numPartitions;
        }
        return partition;
    }
}
