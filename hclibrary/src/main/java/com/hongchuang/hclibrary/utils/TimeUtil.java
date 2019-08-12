package com.hongchuang.hclibrary.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/***
 * 功能描述:时间处理类
 * 作者:qiujialiu
 * 时间:2017/5/31
 ***/

public class TimeUtil {
    public static String format(long time, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(new Date(time));
    }

    public static String formatToFileName(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS");
        return format.format(new Date(time));
    }
    public static String formatName(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        return format.format(new Date(time));
    }

    public static String formatDate(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        return format.format(new Date());
    }

    //毫秒
    public static String formatToHour(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH");
        return format.format(new Date(time));
    }

    //秒
    public static String formatToYear(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        return format.format(new Date(time * 1000));
    }

    //秒
    public static String formatToMonth(long time) {
        SimpleDateFormat format = new SimpleDateFormat("MM");
        return format.format(new Date(time * 1000));
    }

    public static long parseTime(String time, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            return format.parse(time).getTime() / 1000;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    //毫秒
    public static long parseMillsTime(String time, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            return format.parse(time).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long parsedate(String time, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            return format.parse(time).getDate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getTickName() {

        SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS");

        return format.format(new Date());

    }


    public static boolean isToday(String time) {
        boolean result = false;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            long create = sdf.parse(time).getTime();

            Calendar now = Calendar.getInstance();

            long ms = 1000 * (now.get(Calendar.HOUR_OF_DAY) * 3600 + now.get(Calendar.MINUTE) * 60 + now.get(Calendar.SECOND));//毫秒数

            long ms_now = now.getTimeInMillis();

            if (ms_now - create < ms)
                result = true;
        } catch (Exception e) {
            result = false;
        }

        return result;
    }


    /**
     * 得到两个日期相差的天数
     */
    public static int getBetweenDay(long date1, long date2) {

        Calendar d1 = Calendar.getInstance();
        Calendar d2 = Calendar.getInstance();
        if (date1 > date2) {
            d1.setTimeInMillis(date2);
            d2.setTimeInMillis(date1);
        } else {
            d1.setTimeInMillis(date1);
            d2.setTimeInMillis(date2);
        }
        LogUtils.d("TimeUtil", d2.get(Calendar.DAY_OF_YEAR) + "----" + d1.get(Calendar.DAY_OF_YEAR));
        int day1 = d1.get(Calendar.DAY_OF_YEAR);
        int day2 = d2.get(Calendar.DAY_OF_YEAR);
        int y1 = d1.get(Calendar.YEAR);
        int y2 = d2.get(Calendar.YEAR);
        if (y1 == y2) {
            return day2 - day1;
        } else {
            return (int) Math.ceil((Math.abs(date2 - date1) / (1000 * 60 * 60 * 24)));
        }
    }

    /**
     * 由长整形的时间戳得到年月日
     *
     * @param time 　长整时间戳
     * @return　年月日的list集合
     */
    private static List<String> judgeDate(Long time) {
        List<String> strings = new ArrayList<>();
        String hourString;
        String minuteString;
        String yearString;
        String monthString;
        String dayString;
        String re_StrTime;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        re_StrTime = sdf.format(new Date(time));
        hourString = re_StrTime.substring(11, 13);
        minuteString = re_StrTime.substring(14, 16);
        yearString = re_StrTime.substring(0, 4);
        monthString = re_StrTime.substring(5, 7);
        dayString = re_StrTime.substring(8, 10);
        if ((Integer.parseInt(hourString) + 8) > 24) {
            int day = Integer.parseInt(dayString) + 1;
            dayString = Integer.toString(day);
        }
        strings.add(yearString);
        strings.add(monthString);
        strings.add(dayString);
        strings.add(hourString);
        strings.add(minuteString);
        return strings;
    }

    public static String formatTime(String end_date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date2;
        Calendar calendar = null;
        try {
            date2 = format.parse(end_date);
            calendar = Calendar.getInstance();
            calendar.setTime(date2);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (calendar != null) {

            return calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日";
        } else {
            return null;
        }
    }

    /**
     * 调此方法输入所要转换的时间输入例如（"2014-06-14-16-09-00"）返回时间戳
     *
     * @param time
     * @return
     */
    public static String dataOne(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM",
                Locale.CHINA);
        Date date;
        String times = null;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            String stf = String.valueOf(l);
            times = stf.substring(0, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return times;
    }

    public static boolean isLeapYear(int year) {
        return ((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0);
    }

}
