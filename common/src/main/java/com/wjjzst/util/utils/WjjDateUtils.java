package com.wjjzst.util.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: Wjj
 * @Date: 2021/6/3 11:26 上午
 * @desc:
 */
public class WjjDateUtils {
    public static final String YEAR_MONTH_NUMBER = "yyyyMM";

    public static String date2Str(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }
}
