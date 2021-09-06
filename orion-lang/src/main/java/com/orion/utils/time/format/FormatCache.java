package com.orion.utils.time.format;

import com.orion.lang.wrapper.Tuple;
import com.orion.utils.Exceptions;
import com.orion.utils.Valid;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * copy with apache
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/25 12:40
 */
abstract class FormatCache<F extends Format> {

    protected FormatCache() {
    }

    private final ConcurrentMap<Tuple, F> INSTANCE_CACHE = new ConcurrentHashMap<>(7);

    private static final ConcurrentMap<Tuple, String> DATE_TIME_PATTERN_CACHE = new ConcurrentHashMap<>(7);

    /**
     * 获取默认时区的实例
     *
     * @return instance
     */
    public F getInstance() {
        return getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, TimeZone.getDefault(), Locale.getDefault());
    }

    /**
     * 获取实例
     *
     * @param pattern  格式
     * @param timeZone 时区
     * @param locale   地区
     * @return instance
     */
    public F getInstance(String pattern, TimeZone timeZone, Locale locale) {
        Valid.notBlank(pattern, "pattern must not be empty");
        if (timeZone == null) {
            timeZone = TimeZone.getDefault();
        }
        if (locale == null) {
            locale = Locale.getDefault();
        }
        Tuple key = Tuple.of(pattern, timeZone, locale);
        F format = INSTANCE_CACHE.get(key);
        if (format == null) {
            format = createInstance(pattern, timeZone, locale);
            F previousValue = INSTANCE_CACHE.putIfAbsent(key, format);
            if (previousValue != null) {
                format = previousValue;
            }
        }
        return format;
    }

    /**
     * 创建实例
     *
     * @param pattern  格式
     * @param timeZone 时区
     * @param locale   地区
     * @return instance
     */
    protected abstract F createInstance(String pattern, TimeZone timeZone, Locale locale);

    /**
     * 获取实例
     *
     * @param dateStyle 日期格式
     * @param timeStyle 时间格式
     * @param timeZone  时区
     * @param locale    地区
     * @return instance
     */
    private F getDateTimeInstance(Integer dateStyle, Integer timeStyle, TimeZone timeZone, Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        String pattern = getPatternForStyle(dateStyle, timeStyle, locale);
        return getInstance(pattern, timeZone, locale);
    }

    /**
     * 获取实例
     *
     * @param dateStyle 日期格式
     * @param timeStyle 时间格式
     * @param timeZone  时区
     * @param locale    地区
     * @return instance
     */
    F getDateTimeInstance(int dateStyle, int timeStyle, TimeZone timeZone, Locale locale) {
        return getDateTimeInstance(Integer.valueOf(dateStyle), Integer.valueOf(timeStyle), timeZone, locale);
    }

    /**
     * 获取实例
     *
     * @param dateStyle 日期格式
     * @param timeZone  时区
     * @param locale    地区
     * @return instance
     */
    F getDateInstance(int dateStyle, TimeZone timeZone, Locale locale) {
        return getDateTimeInstance(dateStyle, null, timeZone, locale);
    }

    /**
     * 获取实例
     *
     * @param timeStyle 时间格式
     * @param timeZone  时区
     * @param locale    地区
     * @return instance
     */
    F getTimeInstance(int timeStyle, TimeZone timeZone, Locale locale) {
        return getDateTimeInstance(null, timeStyle, timeZone, locale);
    }

    /**
     * 获取日期时间格式
     *
     * @param dateStyle 日期格式
     * @param timeStyle 时间格式
     * @param locale    地区
     * @return 格式
     */
    static String getPatternForStyle(Integer dateStyle, Integer timeStyle, Locale locale) {
        Tuple key = Tuple.of(dateStyle, timeStyle, locale);
        String pattern = DATE_TIME_PATTERN_CACHE.get(key);
        if (pattern == null) {
            try {
                DateFormat formatter;
                if (dateStyle == null) {
                    formatter = DateFormat.getTimeInstance(timeStyle, locale);
                } else if (timeStyle == null) {
                    formatter = DateFormat.getDateInstance(dateStyle, locale);
                } else {
                    formatter = DateFormat.getDateTimeInstance(dateStyle, timeStyle, locale);
                }
                pattern = ((SimpleDateFormat) formatter).toPattern();
                String previous = DATE_TIME_PATTERN_CACHE.putIfAbsent(key, pattern);
                if (previous != null) {
                    pattern = previous;
                }
            } catch (ClassCastException ex) {
                throw Exceptions.argument("no date time pattern for locale: " + locale);
            }
        }
        return pattern;
    }

}
