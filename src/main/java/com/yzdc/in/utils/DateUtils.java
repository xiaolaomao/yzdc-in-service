package com.yzdc.in.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Desc:    日期工具类
 * Author: Iron
 * CreateDate: 2016-10-14
 * CopyRight: Beijing Yunzong Co., Ltd.
 */
public class DateUtils {


    /**
     * 格式化时间格式
     * YYYY-MM-DD => YYYYMMDD
     * YYYYMMDD => YYYY-MM-DD
     *
     * @param dateStr
     * @return
     */
    public static String formatDate(String dateStr) {
        String ret = "-1";
        if (dateStr.length() == 8) {
            ret = dateStr.substring(0, 4) + "-" + dateStr.substring(4, 6) + "-" + dateStr.substring(6, 8);
        } else if (dateStr.length() == 10) {
            ret = dateStr.substring(0, 4) + dateStr.substring(5, 7) + dateStr.substring(8, 10);
        }
        return ret;
    }

    /**
     * 时间戳格式化
     *
     * @param timeStamp
     * @return
     */
    public static String formatDate(int timeStamp, String format) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        long lt = new Long(timeStamp);
        Date date = new Date(lt * 1000);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * 获取服务器当前时间
     *
     * @return
     */
    public static String getCurrentTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(new Date());
    }

    /**
     * 根据时间间隔返回日期
     *
     * @param date       日期
     * @param dateFormat 时间格式
     * @param interval   时间间隔
     * @return
     */
    public static String getIntervalDate(String date, String dateFormat, Integer interval) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date dt = sdf.parse(date);
        Calendar retDate = Calendar.getInstance();
        retDate.setTime(dt);
        retDate.add(Calendar.DAY_OF_YEAR, interval);
        Date dt1 = retDate.getTime();
        String retDateStr = sdf.format(dt1);
        return retDateStr;
    }

    /**
     * 获取两个日期间的差值（天）
     *
     * @param beginDate
     * @param endDate
     * @return
     * @throws Exception
     */
    public static Integer getDaySub(String beginDate, String endDate) throws Exception {
        if (beginDate == "-1") return -1;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long tmpInterval = ((sdf.parse(endDate).getTime() - sdf.parse(beginDate).getTime()) / (24 * 60 * 60 * 1000));
        Integer interval = Integer.valueOf(String.valueOf(tmpInterval));
        if (interval > 0) {
            return interval;
        } else {
            return -interval;
        }
    }
}
