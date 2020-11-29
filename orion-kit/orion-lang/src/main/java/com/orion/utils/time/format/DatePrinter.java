package com.orion.utils.time.format;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * copy with apache
 */
public interface DatePrinter {

    String format(long millis);

    String format(Date date);

    String format(Calendar calendar);

    <B extends Appendable> B format(long millis, B buf);

    <B extends Appendable> B format(Date date, B buf);

    <B extends Appendable> B format(Calendar calendar, B buf);

    String getPattern();

    TimeZone getTimeZone();

    Locale getLocale();

}
