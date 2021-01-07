package com.orion.utils.time.format;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * copy with apache
 */
public interface DateParser {

    Date parse(String source) throws ParseException;

    Date parse(String source, ParsePosition pos);

    boolean parse(String source, ParsePosition pos, Calendar calendar);

    String getPattern();

    TimeZone getTimeZone();

    Locale getLocale();

    Object parseObject(String source) throws ParseException;

    Object parseObject(String source, ParsePosition pos);

}
