package com.happy3friends.toiletmapbackend.utils;

import com.happy3friends.toiletmapbackend.constant.DateTimeConstant;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeUtil {
    public static ZonedDateTime getZoneDateTimeNow() {
        return ZonedDateTime.ofInstant(Instant.now(), ZoneId.of(DateTimeConstant.ZONE_ID));
    }

    public static Date convertZoneDateTimeToDate(ZonedDateTime zonedDateTime) {
        return Date.from(zonedDateTime.toInstant());
    }

    public static Date getDateNow() {
        return convertZoneDateTimeToDate(getZoneDateTimeNow());
    }

    public static Timestamp getTimestampNow() {
        return new Timestamp(convertZoneDateTimeToDate(getZoneDateTimeNow()).getTime());
    }

    public static Date convertDateSqlToDate(java.sql.Date dateSql) {
        return new java.util.Date(dateSql.getTime());
    }

    public static String convertLocalDateTimeWithPattern(LocalDateTime localDateTime, String pattern) {
        return DateTimeFormatter.ofPattern(pattern).format(localDateTime);
    }
}
