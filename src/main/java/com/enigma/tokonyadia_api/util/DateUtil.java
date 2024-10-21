package com.enigma.tokonyadia_api.util;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    public static final String PATTERN_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm:ss Z";

    public static String localDateTimeToString(LocalDateTime localDateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(PATTERN_YYYY_MM_DD_HH_MM);
        return dateTimeFormatter.format(localDateTime);
    }

    public static String zonedDateTimeToString(ZonedDateTime zonedDateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(PATTERN_YYYY_MM_DD_HH_MM);
        return dateTimeFormatter.format(zonedDateTime);
    }
}
