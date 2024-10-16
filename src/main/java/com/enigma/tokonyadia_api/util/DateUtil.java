package com.enigma.tokonyadia_api.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    public static final String PATTERN_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm:ss";

    public static String localDateTimeToString(LocalDateTime localDateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(PATTERN_YYYY_MM_DD_HH_MM);
        return dateTimeFormatter.format(localDateTime);
    }
}
