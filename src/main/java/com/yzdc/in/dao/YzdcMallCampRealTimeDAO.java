package com.yzdc.in.dao;

import com.yzdc.in.model.YzdcMallCampRealTime;

import java.util.List;

/**
 * Desc:    插入活动表
 * Author: Iron
 * CreateDate: 2016-11-14
 * CopyRight: Beijing Yunzong Co., Ltd.
 */
public interface YzdcMallCampRealTimeDAO {

    /**
     * 插入数据
     *
     * @param yzdcMallCampRealTime
     * @return
     */
    int insertCampRealTime(YzdcMallCampRealTime yzdcMallCampRealTime);

    /**
     * 批量插入数据
     *
     * @param yzdcMallCampRealTimesList
     * @return
     */
    int batchInsertCampRealTime(List<YzdcMallCampRealTime> yzdcMallCampRealTimesList);

    /**
     * 更新数据
     *
     * @param yzdcMallCampRealTime
     * @return
     */
    int updateCampRealTime(YzdcMallCampRealTime yzdcMallCampRealTime);

    /**
     * 删除数据
     *
     * @param report_date
     * @return
     */
    int deleteCampRealTimeByBizDate(String report_date);
}
