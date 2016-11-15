package com.yzdc.in.service;

/**
 * Desc:     上报服务
 * Author: Iron
 * CreateDate: 2016-10-12
 * CopyRight: Beijing Yunzong Co., Ltd.
 */
public interface InboundService {

    /**
     * mall端数据上报接口
     *
     * @param inbound
     * @return add by xignshen.zhao 2016-10-12
     */
    String handleMallInbound(String inbound) throws Exception;

}
