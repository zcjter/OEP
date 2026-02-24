package com.zijin.oep.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * 工具类
 */
public class UtilTools {
    /**
     * 将时间字符串转为Timestamp对象
     * @param timeString
     * @return
     */

    public static Timestamp timeString2TimeStamp(String timeString){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            // 将时间戳字符串解析为 Timestamp 对象
            Timestamp reviewTime = new Timestamp(dateFormat.parse(timeString).getTime());
            // 获取中国时区
            TimeZone timeZone = TimeZone.getTimeZone("Asia/Shanghai");
            // 将时间戳调整为中国时区的时间
            reviewTime.setTime(reviewTime.getTime() + timeZone.getRawOffset());
            return reviewTime;
        } catch (ParseException e) {
            // 处理解析异常
            e.printStackTrace();
        }
        return null;
    }
    public static Timestamp getCurrentTime(){
        // 设置中国时区
        TimeZone chinaTimeZone = TimeZone.getTimeZone("Asia/Shanghai");

        // 获取当前时间的毫秒数
        long currentTimeMillis = System.currentTimeMillis();

        // 将当前时间转换为中国时区
        long chinaTimeMillis = currentTimeMillis + chinaTimeZone.getRawOffset();
        Timestamp chinaTime = new Timestamp(chinaTimeMillis);

        return chinaTime;
    }
}
