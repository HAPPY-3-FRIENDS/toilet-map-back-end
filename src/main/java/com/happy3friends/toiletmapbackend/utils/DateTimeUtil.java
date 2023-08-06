package com.happy3friends.toiletmapbackend.utils;

import com.happy3friends.toiletmapbackend.constant.DateTimeConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(DateTimeUtil.class);

    public static ZonedDateTime getZoneDateTimeNow() {
        return ZonedDateTime.ofInstant(Instant.now(), ZoneId.of(DateTimeConstant.ZONE_ID));
    }

    public static Date getDateNow() {
        return new Date(convertZoneDateTimeToDate(getZoneDateTimeNow()).getTime());
    }

    public static Date getDateNowWithInitialTime(int hour, int minus, int second, int milliSecond) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of(DateTimeConstant.ZONE_ID)));
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minus);
        cal.set(Calendar.SECOND, second);
        cal.set(Calendar.MILLISECOND, milliSecond);

        return cal.getTime();
    }

    public static Timestamp getTimestampNow() {
        return new Timestamp(convertZoneDateTimeToDate(getZoneDateTimeNow()).getTime());
    }

    public static Date convertZoneDateTimeToDate(ZonedDateTime zonedDateTime) {
        return Date.from(zonedDateTime.toInstant());
    }

    public static Timestamp convertDateToTimestamp(Date datetime) {
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

    public static Date convertStringToDate(String strDate, String pattern) {
        DateFormat df = new SimpleDateFormat(pattern);
        try {
            return df.parse(strDate);
        } catch (ParseException ex) {
            LOGGER.error("Parse DateTime Exception: " + ex.getMessage());
        }
        return null;
    }

    public static Timestamp convertStringToTimestamp(String strDate) {
        DateFormat df = new SimpleDateFormat(DateTimeConstant.yyyy_MM_dd__HH_mm_ssssss);
        try {
            return convertDateToTimestamp(df.parse(strDate));
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

    public static String convertSqlTimeToHHMMPattern(Time time) {
        String strTime = time.toString();
        return strTime.substring(0, 5);
    }

    public static Date getFirstDateOfCurrentMonth() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of(DateTimeConstant.ZONE_ID)));
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return new Date(cal.getTime().getTime());
    }

    public static Date getEndDateOfPreviousQuarter() {
        // get today's date
        LocalDate today = LocalDate.now();
        // get previous quarter
        LocalDate previousQuarter = today.minus(1, IsoFields.QUARTER_YEARS);
        // get last day in previous quarter
        long lastDayOfQuarter = IsoFields.DAY_OF_QUARTER.rangeRefinedBy(previousQuarter).getMaximum();
        // get the date corresponding to the last day of quarter
        LocalDate lastDayInPreviousQuarter = previousQuarter.with(IsoFields.DAY_OF_QUARTER, lastDayOfQuarter);

        return Date.from(lastDayInPreviousQuarter.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
