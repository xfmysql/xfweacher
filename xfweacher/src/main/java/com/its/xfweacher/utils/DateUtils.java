package com.its.xfweacher.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xf on 2016/3/11.
 */
public class DateUtils {

    public static Date String2Date(String dateStr,String format){
        DateFormat sdf = new SimpleDateFormat(format);
        try {
            Date date = sdf.parse(dateStr);
            return date;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String Timestamp2String(long times,String format){
        Timestamp ts = new Timestamp(times);
        String tsStr = "";
        DateFormat sdf = new SimpleDateFormat(format);
        try {
            //方法一
            tsStr = sdf.format(ts);
            System.out.println(tsStr);
            //方法二
            //tsStr = ts.toString();
            //System.out.println(tsStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tsStr;
    }

    /**
     * 日期转换成字符串
     * @param date
     * @param format
     * @return
     */
    public static String Timestamp2String(Date date,String format){

        String tsStr = "";
        DateFormat sdf = new SimpleDateFormat(format);
        try {
            //方法一
            tsStr = sdf.format(date);
            System.out.println(tsStr);
            //方法二
            //tsStr = ts.toString();
            //System.out.println(tsStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tsStr;
    }
}
