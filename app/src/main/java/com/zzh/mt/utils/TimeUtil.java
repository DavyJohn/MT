package com.zzh.mt.utils;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by 腾翔信息 on 2017/3/13.
 */

public class TimeUtil {

    public static String getTime(){
        String nowTime = null;
        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日");
        Date curDate = new Date(System.currentTimeMillis());
        nowTime = sf.format(curDate);
        return nowTime;
    }
    public static String getData(){
        String nowTime = null;
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());
        nowTime = sf.format(curDate);
        return nowTime;
    }
}
