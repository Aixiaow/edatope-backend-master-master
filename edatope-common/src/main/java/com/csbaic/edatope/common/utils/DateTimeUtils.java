package com.csbaic.edatope.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public final class DateTimeUtils {

    /**
     * 时间格式
     */
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 时间格式中文
     */
    public static final String DATE_TIME_FORMAT_CN = "yyyy年MM月dd HH:mm:ss";

    /**
     * 日期格式
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 日期格式
     */
    public static final String DATE_FORMAT_CN = "yyyy年MM月dd";


    /**
     * 判断时间格式是否正确
     *
     * @param datetime
     * @param format
     * @return
     */
    public static boolean isFormatted(String datetime, String format) {

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            formatter.parse(datetime);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }

    }

    public static LocalDateTime stringToLocalDateTime(String date){
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(date, df);
    }

    public static Date stringToDate(String date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = null;
        try {
            date1 = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1;
    }

    /**
     * 相差天数
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDaysByMillisecond(Date date1, Date date2) {
        return (int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24));
    }

    /**
     * 相加天数
     * @param date
     * @param day
     * @return
     */
    public static Date getDateAddDay(Date date, int day) {
        Calendar ca = new GregorianCalendar();
        ca.setTime(date);
        ca.add(Calendar.DAY_OF_YEAR, day);
        return ca.getTime();
    }
}
