package com.orion.utils.time.format;

import com.orion.utils.Valid;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * copy with apache
 */
abstract class FormatCache<F extends Format> {

    protected FormatCache() {
    }

    private final ConcurrentMap<MultipartKey, F> C_INSTANCE_CACHE = new ConcurrentHashMap<>(7);

    private static final ConcurrentMap<MultipartKey, String> C_DATE_TIME_INSTANCE_CACHE = new ConcurrentHashMap<>(7);

    public F getInstance() {
        return getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, TimeZone.getDefault(), Locale.getDefault());
    }

    public F getInstance(String pattern, TimeZone timeZone, Locale locale) {
        Valid.notBlank(pattern, "pattern must not be blank");
        if (timeZone == null) {
            timeZone = TimeZone.getDefault();
        }
        if (locale == null) {
            locale = Locale.getDefault();
        }
        MultipartKey key = new MultipartKey(pattern, timeZone, locale);
        F format = C_INSTANCE_CACHE.get(key);
        if (format == null) {
            format = createInstance(pattern, timeZone, locale);
            F previousValue = C_INSTANCE_CACHE.putIfAbsent(key, format);
            if (previousValue != null) {
                format = previousValue;
            }
        }
        return format;
    }

    protected abstract F createInstance(String pattern, TimeZone timeZone, Locale locale);

    private F getDateTimeInstance(Integer dateStyle, Integer timeStyle, TimeZone timeZone, Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        String pattern = getPatternForStyle(dateStyle, timeStyle, locale);
        return getInstance(pattern, timeZone, locale);
    }

    F getDateTimeInstance(int dateStyle, int timeStyle, TimeZone timeZone, Locale locale) {
        return getDateTimeInstance(Integer.valueOf(dateStyle), Integer.valueOf(timeStyle), timeZone, locale);
    }

    F getDateInstance(int dateStyle, TimeZone timeZone, Locale locale) {
        return getDateTimeInstance(dateStyle, null, timeZone, locale);
    }

    F getTimeInstance(int timeStyle, TimeZone timeZone, Locale locale) {
        return getDateTimeInstance(null, timeStyle, timeZone, locale);
    }

    static String getPatternForStyle(Integer dateStyle, Integer timeStyle, Locale locale) {
        MultipartKey key = new MultipartKey(dateStyle, timeStyle, locale);

        String pattern = C_DATE_TIME_INSTANCE_CACHE.get(key);
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
                String previous = C_DATE_TIME_INSTANCE_CACHE.putIfAbsent(key, pattern);
                if (previous != null) {
                    pattern = previous;
                }
            } catch (ClassCastException ex) {
                throw new IllegalArgumentException("No date time pattern for locale: " + locale);
            }
        }
        return pattern;
    }

    private static class MultipartKey {
        private Object[] keys;
        private int hashCode;

        MultipartKey(Object... keys) {
            this.keys = keys;
        }

        @Override
        public boolean equals(Object obj) {
            return Arrays.equals(keys, ((MultipartKey) obj).keys);
        }

        @Override
        public int hashCode() {
            if (hashCode == 0) {
                int rc = 0;
                for (Object key : keys) {
                    if (key != null) {
                        rc = rc * 7 + key.hashCode();
                    }
                }
                hashCode = rc;
            }
            return hashCode;
        }
    }

}
