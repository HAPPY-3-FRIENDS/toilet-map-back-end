package com.happy3friends.toiletmapbackend.utils;

import com.happy3friends.toiletmapbackend.constant.DateTimeConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(DateTimeUtil.class);

    public static ZonedDateTime getZoneDateTimeNow() {
        return ZonedDateTime.ofInstant(Instant.now(), ZoneId.of(DateTimeConstant.ZONE_ID));
    }

    public static Date getDateNow() {
        return convertZoneDateTimeToDate(getZoneDateTimeNow());
    }

    public static Timestamp getTimestampNow() {
        return new Timestamp(convertZoneDateTimeToDate(getZoneDateTimeNow()).getTime());
    }

    public static Date convertZoneDateTimeToDate(ZonedDateTime zonedDateTime) {
        return Date.from(zonedDateTime.toInstant());
    }

    public static Timestamp convertDateToTimestime(Date datetime) {
        return new Timestamp(datetime.getTime());
    }

    public static Date convertDateSqlToDate(java.sql.Date dateSql) {
        return new java.util.Date(dateSql.getTime());
    }

    public static String convertLocalDateTimeWithPattern(LocalDateTime localDateTime, String pattern) {
        return DateTimeFormatter.ofPattern(pattern).format(localDateTime);
    }

    public static Date convertStringToDate(String strDate) {
        DateFormat df = new SimpleDateFormat(DateTimeConstant.dd_MM_yyyy__HH_mm_ss);
        try {
            return df.parse(strDate);
        } catch (ParseException ex) {
            LOGGER.error("Parse DateTime Exception: " + ex.getMessage());
        }
        return null;
    }

    public static Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

    public static boolean checkDateBetweenMinMax(Date min, Date max, Date date) {
        return date.after(addDays(min, -1)) && date.before(addDays(max, 1));
    }
}
