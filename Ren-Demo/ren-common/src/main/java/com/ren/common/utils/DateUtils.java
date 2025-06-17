package com.ren.common.utils;

import cn.hutool.core.date.DateException;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 时间工具类
 * 
 * @author ren
 */
public class DateUtils {
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", 
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     * 
     * @return Date() 当前日期
     */
    public static Date getNowDate()
    {
        return new Date();
    }

    /**
     * 获取当前字符串类型日期, 格式为yyyy-MM-dd
     * 
     * @return String
     */
    public static String getDate()
    {
        return dateTimeNow(YYYY_MM_DD);
    }

    /**
     * 获取当前自渡川类型时间, 格式为yyyy-MM-dd HH:mm:ss
     *
     * @return String
     */
    public static final String getTime()
    {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 获取当前字符串类型时间, 格式为yyyyMMddHHmmss
     *
     * @return String
     */
    public static final String dateTimeNow()
    {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    /**
     * 将当前时间按照所传的格式进行格式化
     *
     * @return String
     */
    public static final String dateTimeNow(final String format)
    {
        return parseDateToStr(format, new Date());
    }

    /**
     * 将传过来的时间按照yyyy-MM-dd格式进行格式化
     *
     * @return String
     */
    public static final String dateTime(final Date date)
    {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    /**
     * 将传过来的Date时间按照所传的格式进行格式化
     *
     * @return String
     */
    public static final String parseDateToStr(final String format, final Date date)
    {
        return new SimpleDateFormat(format).format(date);
    }

    /**
     * 将传过来的字符串时间按照所传的格式进行格式化
     *
     * @return String
     */
    public static final Date dateTime(final String format, final String ts)
    {
        try
        {
            return new SimpleDateFormat(format).parse(ts);
        }
        catch (ParseException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath()
    {
        Date now = new Date();
        return DateUtil.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime()
    {
        Date now = new Date();
        return DateUtil.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str)
    {
        if (str == null)
        {
            return null;
        }
        try
        {
            return DateUtil.parse(str.toString(), parsePatterns);
        }catch (DateException e) {
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate()
    {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算相差天数
     */
    public static int differentDaysByMillisecond(Date date1, Date date2)
    {
        return Math.abs((int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24)));
    }

    /**
     * 计算时间差
     *
     * @param endDate 最后时间
     * @param startTime 开始时间
     * @return 时间差（天/小时/分钟）
     */
    public static String timeDistance(Date endDate, Date startTime)
    {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - startTime.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * LocalDateTime转为Date类型
     */
    public static Date toDate(LocalDateTime temporalAccessor)
    {
        ZonedDateTime zdt = temporalAccessor.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * LocalDate转为Date类型
     */
    public static Date toDate(LocalDate temporalAccessor)
    {
        LocalDateTime localDateTime = LocalDateTime.of(temporalAccessor, LocalTime.of(0, 0, 0));
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 将时间戳转换为字符串（默认时区，默认格式）
     * @param timestamp 毫秒时间戳
     * @return 格式化的日期时间字符串（yyyy-MM-dd HH:mm:ss）
     */
    public static String timestampToStrDefault(long timestamp) {
        return timestampToStr(timestamp, ZoneId.systemDefault(), YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 带时区的转换方法
     * @param timestamp 毫秒时间戳
     * @param timeZone 时区ID（如 "Asia/Shanghai"）
     * @return 格式化的日期时间字符串
     */
    public static String timestampToStrTimeZone(long timestamp, String timeZone) {
        return timestampToStr(timestamp, ZoneId.of(timeZone), YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 带格式化选项的转换方法
     * @param timestamp 毫秒时间戳
     * @param pattern 格式模式（如 "yyyy/MM/dd HH:mm"）
     * @return 格式化的日期时间字符串
     */
    public static String timestampToStrPattern(long timestamp, String pattern) {
        return timestampToStr(timestamp, ZoneId.systemDefault(), pattern);
    }

    /**
     * 将时间戳转换为字符串
     * @param timestamp 毫秒时间戳
     * @param zoneId 时区ID对象
     * @param pattern 格式模式
     * @return 格式化的日期时间字符串
     */
    public static String timestampToStr(long timestamp, ZoneId zoneId, String pattern) {
        // 1. 将毫秒时间戳转换为Instant
        Instant instant = Instant.ofEpochMilli(timestamp);

        // 2. 将Instant转换为指定时区的LocalDateTime
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, zoneId);

        // 3. 创建格式化器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        // 4. 格式化为字符串
        return dateTime.format(formatter);
    }

    /**
     * 根据时间字符串得到时间戳（单位：毫秒）
     * @param dateStr
     * @param datecode
     * @return java.lang.Long
     * @author zhiwei
     * @date 2025/06/16 13:23
     */
    public static Long convertDateStringToLong(String dateStr, String datecode) {
        if(StrUtil.isBlank(datecode)) datecode = YYYY_MM_DD_HH_MM_SS;
        return (DateUtil.parse(dateStr, datecode).getTime());
    }

    /**
     * 得到给定日期（long型的时间戳） 的零点和24点的时戳
     * @param currentTimestamp
     * @return long[]
     * @author ren
     * @date 2025/06/16 13:57
     */
    @SuppressWarnings("static-access")
    public static long[] getOneDayTimestamp(Long currentTimestamp) {
        long[] timeArray = new long[2];
        Date date = new Date();
        if (currentTimestamp != null) date = new Date(currentTimestamp * 1000);
        timeArray[0] = DateUtil.beginOfDay(date).getTime()/1000;
        timeArray[1] = DateUtil.endOfDay(date).getTime()/1000;
        return timeArray;
    }
}