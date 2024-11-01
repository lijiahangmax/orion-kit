/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
 *
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package cn.orionsec.kit.lang.utils.time.format;

import cn.orionsec.kit.lang.utils.Exceptions;

import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.text.ParsePosition;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * copy with apache
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/26 13:08
 */
public class FastDateParser implements DateParser, Serializable {

    private static final long serialVersionUID = 3L;

    private static final Locale JAPANESE_IMPERIAL = new Locale("ja", "JP", "JP");

    private final String pattern;

    private final TimeZone timeZone;

    private final Locale locale;

    private final int century;

    private final int startYear;

    private transient List<StrategyAndWidth> patterns;

    private static final Comparator<String> LONGER_FIRST_LOWERCASE = Comparator.reverseOrder();

    @SuppressWarnings("unchecked")
    private static final ConcurrentMap<Locale, Strategy>[] CACHES = new ConcurrentMap[Calendar.FIELD_COUNT];

    protected FastDateParser(String pattern, TimeZone timeZone, Locale locale) {
        this(pattern, timeZone, locale, null);
    }

    protected FastDateParser(String pattern, TimeZone timeZone, Locale locale, Date centuryStart) {
        this.pattern = pattern;
        this.timeZone = timeZone;
        this.locale = locale;

        Calendar definingCalendar = Calendar.getInstance(timeZone, locale);

        int centuryStartYear;
        if (centuryStart != null) {
            definingCalendar.setTime(centuryStart);
            centuryStartYear = definingCalendar.get(Calendar.YEAR);
        } else if (locale.equals(JAPANESE_IMPERIAL)) {
            centuryStartYear = 0;
        } else {
            definingCalendar.setTime(new Date());
            centuryStartYear = definingCalendar.get(Calendar.YEAR) - 80;
        }
        this.century = centuryStartYear / 100 * 100;
        this.startYear = centuryStartYear - century;
        this.init(definingCalendar);
    }

    /**
     * 初始化解析格式
     *
     * @param definingCalendar calendar
     */
    private void init(Calendar definingCalendar) {
        this.patterns = new ArrayList<>();
        StrategyParser fm = new StrategyParser(definingCalendar);
        for (; ; ) {
            StrategyAndWidth field = fm.getNextStrategy();
            if (field == null) {
                break;
            }
            patterns.add(field);
        }
    }

    /**
     * 是否为格式化字符
     *
     * @param c c
     * @return ignore
     */
    private static boolean isFormatLetter(char c) {
        return c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z';
    }

    @Override
    public String getPattern() {
        return pattern;
    }

    @Override
    public TimeZone getTimeZone() {
        return timeZone;
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof FastDateParser)) {
            return false;
        }
        FastDateParser other = (FastDateParser) obj;
        return pattern.equals(other.pattern)
                && timeZone.equals(other.timeZone)
                && locale.equals(other.locale);
    }

    @Override
    public int hashCode() {
        return pattern.hashCode() + 13 * (timeZone.hashCode() + 13 * locale.hashCode());
    }

    @Override
    public String toString() {
        return pattern + ", " + locale + ", " + timeZone.getID();
    }

    @Override
    public Date parse(String source) {
        ParsePosition pp = new ParsePosition(0);
        Date date = parse(source, pp);
        if (date == null) {
            // 支持的日期范围
            if (locale.equals(JAPANESE_IMPERIAL)) {
                throw Exceptions.parseDate("(the " + locale + " locale does not support dates before 1868 AD)\n" +
                        "unParse date: \"" + source + " " + pp.getErrorIndex());
            }
            throw Exceptions.parseDate("unparsed date: " + source + " " + pp.getErrorIndex());
        }
        return date;
    }

    @Override
    public Date parse(String source, ParsePosition pos) {
        Calendar cal = Calendar.getInstance(timeZone, locale);
        cal.clear();
        return this.parse(source, pos, cal) ? cal.getTime() : null;
    }

    @Override
    public boolean parse(String source, ParsePosition pos, Calendar calendar) {
        ListIterator<StrategyAndWidth> lt = patterns.listIterator();
        while (lt.hasNext()) {
            StrategyAndWidth strategyAndWidth = lt.next();
            int maxWidth = strategyAndWidth.getMaxWidth(lt);
            if (!strategyAndWidth.strategy.parse(this, calendar, source, pos, maxWidth)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 转义字符拼接
     *
     * @param sb    sb
     * @param value value
     * @return sb
     */
    private static StringBuilder simpleQuote(StringBuilder sb, String value) {
        for (int i = 0; i < value.length(); ++i) {
            char c = value.charAt(i);
            switch (c) {
                case '\\':
                case '^':
                case '$':
                case '.':
                case '|':
                case '?':
                case '*':
                case '+':
                case '(':
                case ')':
                case '[':
                case '{':
                    sb.append('\\');
                default:
                    sb.append(c);
            }
        }
        if (sb.charAt(sb.length() - 1) == '.') {
            sb.append('?');
        }
        return sb;
    }

    /**
     * 获取字段值
     *
     * @param cal    日历
     * @param locale locale
     * @param field  field
     * @param regex  正则
     * @return name, field
     */
    private static Map<String, Integer> appendDisplayNames(Calendar cal, Locale locale, int field, StringBuilder regex) {
        Map<String, Integer> values = new HashMap<>();
        Map<String, Integer> displayNames = cal.getDisplayNames(field, Calendar.ALL_STYLES, locale);
        TreeSet<String> sorted = new TreeSet<>(LONGER_FIRST_LOWERCASE);
        for (Map.Entry<String, Integer> displayName : displayNames.entrySet()) {
            String key = displayName.getKey().toLowerCase(locale);
            if (sorted.add(key)) {
                values.put(key, displayName.getValue());
            }
        }
        for (String symbol : sorted) {
            simpleQuote(regex, symbol).append('|');
        }
        return values;
    }

    /**
     * 将日期调整到合适的世纪
     *
     * @param twoDigitYear year
     * @return [centuryStart  centuryStart+100)
     */
    private int adjustYear(int twoDigitYear) {
        int trial = century + twoDigitYear;
        return twoDigitYear >= startYear ? trial : trial + 100;
    }

    private Strategy getStrategy(char f, int width, Calendar definingCalendar) {
        switch (f) {
            default:
                throw Exceptions.argument("format '" + f + "' not supported");
            case 'D':
                return DAY_OF_YEAR_STRATEGY;
            case 'E':
                return this.getLocaleSpecificStrategy(Calendar.DAY_OF_WEEK, definingCalendar);
            case 'F':
                return DAY_OF_WEEK_IN_MONTH_STRATEGY;
            case 'G':
                return this.getLocaleSpecificStrategy(Calendar.ERA, definingCalendar);
            case 'H':  // Hour in day (0-23)
                return HOUR_OF_DAY_STRATEGY;
            case 'K':  // Hour in am/pm (0-11)
                return HOUR_STRATEGY;
            case 'M':
                return width >= 3 ? this.getLocaleSpecificStrategy(Calendar.MONTH, definingCalendar) : NUMBER_MONTH_STRATEGY;
            case 'S':
                return MILLISECOND_STRATEGY;
            case 'W':
                return WEEK_OF_MONTH_STRATEGY;
            case 'a':
                return this.getLocaleSpecificStrategy(Calendar.AM_PM, definingCalendar);
            case 'd':
                return DAY_OF_MONTH_STRATEGY;
            case 'h':  // Hour in am/pm (1-12), i.e. midday/midnight is 12, not 0
                return HOUR12_STRATEGY;
            case 'k':  // Hour in day (1-24), i.e. midnight is 24, not 0
                return HOUR24_OF_DAY_STRATEGY;
            case 'm':
                return MINUTE_STRATEGY;
            case 's':
                return SECOND_STRATEGY;
            case 'u':
                return DAY_OF_WEEK_STRATEGY;
            case 'w':
                return WEEK_OF_YEAR_STRATEGY;
            case 'y':
            case 'Y':
                return width > 2 ? LITERAL_YEAR_STRATEGY : ABBREVIATED_YEAR_STRATEGY;
            case 'X':
                return ISO8601TimeZoneStrategy.getStrategy(width);
            case 'Z':
                if (width == 2) {
                    return ISO8601TimeZoneStrategy.ISO_8601_3_STRATEGY;
                }
            case 'z':
                return this.getLocaleSpecificStrategy(Calendar.ZONE_OFFSET, definingCalendar);
        }
    }

    /**
     * 获取文本解析器
     *
     * @param field            field
     * @param definingCalendar definingCalendar
     * @return strategy
     */
    private Strategy getLocaleSpecificStrategy(int field, Calendar definingCalendar) {
        ConcurrentMap<Locale, Strategy> cache = getCache(field);
        Strategy strategy = cache.get(locale);
        if (strategy == null) {
            strategy = field == Calendar.ZONE_OFFSET
                    ? new TimeZoneStrategy(locale)
                    : new CaseInsensitiveTextStrategy(field, definingCalendar, locale);
            Strategy inCache = cache.putIfAbsent(locale, strategy);
            if (inCache != null) {
                return inCache;
            }
        }
        return strategy;
    }

    /**
     * 从缓存中获取解析器
     *
     * @param field field
     * @return strategy
     */
    private static ConcurrentMap<Locale, Strategy> getCache(int field) {
        synchronized (CACHES) {
            if (CACHES[field] == null) {
                CACHES[field] = new ConcurrentHashMap<>(3);
            }
            return CACHES[field];
        }
    }

    // -------------------- class --------------------

    private static final Strategy LITERAL_YEAR_STRATEGY = new NumberStrategy(Calendar.YEAR);
    private static final Strategy WEEK_OF_YEAR_STRATEGY = new NumberStrategy(Calendar.WEEK_OF_YEAR);
    private static final Strategy WEEK_OF_MONTH_STRATEGY = new NumberStrategy(Calendar.WEEK_OF_MONTH);
    private static final Strategy DAY_OF_YEAR_STRATEGY = new NumberStrategy(Calendar.DAY_OF_YEAR);
    private static final Strategy DAY_OF_MONTH_STRATEGY = new NumberStrategy(Calendar.DAY_OF_MONTH);
    private static final Strategy DAY_OF_WEEK_IN_MONTH_STRATEGY = new NumberStrategy(Calendar.DAY_OF_WEEK_IN_MONTH);
    private static final Strategy HOUR_OF_DAY_STRATEGY = new NumberStrategy(Calendar.HOUR_OF_DAY);
    private static final Strategy HOUR_STRATEGY = new NumberStrategy(Calendar.HOUR);
    private static final Strategy MINUTE_STRATEGY = new NumberStrategy(Calendar.MINUTE);
    private static final Strategy SECOND_STRATEGY = new NumberStrategy(Calendar.SECOND);
    private static final Strategy MILLISECOND_STRATEGY = new NumberStrategy(Calendar.MILLISECOND);

    private static final Strategy NUMBER_MONTH_STRATEGY = new NumberStrategy(Calendar.MONTH) {
        @Override
        int modify(FastDateParser parser, int iValue) {
            return iValue - 1;
        }
    };
    private static final Strategy DAY_OF_WEEK_STRATEGY = new NumberStrategy(Calendar.DAY_OF_WEEK) {
        @Override
        int modify(FastDateParser parser, int iValue) {
            return iValue == 7 ? Calendar.SUNDAY : iValue + 1;
        }
    };
    private static final Strategy HOUR24_OF_DAY_STRATEGY = new NumberStrategy(Calendar.HOUR_OF_DAY) {
        @Override
        int modify(FastDateParser parser, int iValue) {
            return iValue == 24 ? 0 : iValue;
        }
    };
    private static final Strategy HOUR12_STRATEGY = new NumberStrategy(Calendar.HOUR) {
        @Override
        int modify(FastDateParser parser, int iValue) {
            return iValue == 12 ? 0 : iValue;
        }
    };
    private static final Strategy ABBREVIATED_YEAR_STRATEGY = new NumberStrategy(Calendar.YEAR) {
        @Override
        int modify(FastDateParser parser, int iValue) {
            return iValue < 100 ? parser.adjustYear(iValue) : iValue;
        }
    };

    private static class StrategyAndWidth {

        Strategy strategy;
        int width;

        StrategyAndWidth(Strategy strategy, int width) {
            this.strategy = strategy;
            this.width = width;
        }

        int getMaxWidth(ListIterator<StrategyAndWidth> lt) {
            if (!strategy.isNumber() || !lt.hasNext()) {
                return 0;
            }
            Strategy nextStrategy = lt.next().strategy;
            lt.previous();
            return nextStrategy.isNumber() ? width : 0;
        }

    }

    private class StrategyParser {

        private Calendar definingCalendar;
        private int currentIdx;

        StrategyParser(Calendar definingCalendar) {
            this.definingCalendar = definingCalendar;
        }

        StrategyAndWidth getNextStrategy() {
            if (currentIdx >= pattern.length()) {
                return null;
            }
            char c = pattern.charAt(currentIdx);
            if (isFormatLetter(c)) {
                return letterPattern(c);
            }
            return literal();
        }

        private StrategyAndWidth letterPattern(char c) {
            int begin = currentIdx;
            while (++currentIdx < pattern.length()) {
                if (pattern.charAt(currentIdx) != c) {
                    break;
                }
            }
            int width = currentIdx - begin;
            return new StrategyAndWidth(getStrategy(c, width, definingCalendar), width);
        }

        private StrategyAndWidth literal() {
            boolean activeQuote = false;
            StringBuilder sb = new StringBuilder();
            while (currentIdx < pattern.length()) {
                char c = pattern.charAt(currentIdx);
                if (!activeQuote && isFormatLetter(c)) {
                    break;
                } else if (c == '\'' && (++currentIdx == pattern.length() || pattern.charAt(currentIdx) != '\'')) {
                    activeQuote = !activeQuote;
                    continue;
                }
                ++currentIdx;
                sb.append(c);
            }
            if (activeQuote) {
                throw Exceptions.argument("unterminated quote");
            }
            String formatField = sb.toString();
            return new StrategyAndWidth(new CopyQuotedStrategy(formatField), formatField.length());
        }

    }

    private abstract static class Strategy {

        boolean isNumber() {
            return false;
        }

        abstract boolean parse(FastDateParser parser, Calendar calendar, String source, ParsePosition pos, int maxWidth);

    }

    private abstract static class PatternStrategy extends Strategy {

        private Pattern pattern;

        void createPattern(StringBuilder regex) {
            createPattern(regex.toString());
        }

        void createPattern(String regex) {
            this.pattern = Pattern.compile(regex);
        }

        @Override
        boolean isNumber() {
            return false;
        }

        @Override
        boolean parse(FastDateParser parser, Calendar calendar, String source, ParsePosition pos, int maxWidth) {
            Matcher matcher = pattern.matcher(source.substring(pos.getIndex()));
            if (!matcher.lookingAt()) {
                pos.setErrorIndex(pos.getIndex());
                return false;
            }
            pos.setIndex(pos.getIndex() + matcher.end(1));
            setCalendar(parser, calendar, matcher.group(1));
            return true;
        }

        abstract void setCalendar(FastDateParser parser, Calendar cal, String value);
    }

    private static class CopyQuotedStrategy extends Strategy {

        private String formatField;

        CopyQuotedStrategy(String formatField) {
            this.formatField = formatField;
        }

        @Override
        boolean isNumber() {
            return false;
        }

        @Override
        boolean parse(FastDateParser parser, Calendar calendar, String source, ParsePosition pos, int maxWidth) {
            for (int idx = 0; idx < formatField.length(); ++idx) {
                int sIdx = idx + pos.getIndex();
                if (sIdx == source.length()) {
                    pos.setErrorIndex(sIdx);
                    return false;
                }
                if (formatField.charAt(idx) != source.charAt(sIdx)) {
                    pos.setErrorIndex(sIdx);
                    return false;
                }
            }
            pos.setIndex(formatField.length() + pos.getIndex());
            return true;
        }

    }

    private static class CaseInsensitiveTextStrategy extends PatternStrategy {
        private int field;
        Locale locale;
        private Map<String, Integer> lKeyValues;

        CaseInsensitiveTextStrategy(int field, Calendar definingCalendar, Locale locale) {
            this.field = field;
            this.locale = locale;

            StringBuilder regex = new StringBuilder();
            regex.append("((?iu)");
            lKeyValues = appendDisplayNames(definingCalendar, locale, field, regex);
            regex.setLength(regex.length() - 1);
            regex.append(")");
            this.createPattern(regex);
        }

        @Override
        void setCalendar(FastDateParser parser, Calendar cal, String value) {
            String lowerCase = value.toLowerCase(locale);
            Integer iVal = lKeyValues.get(lowerCase);
            if (iVal == null) {
                iVal = lKeyValues.get(lowerCase + '.');
            }
            cal.set(field, iVal);
        }
    }

    private static class NumberStrategy extends Strategy {

        private int field;

        NumberStrategy(int field) {
            this.field = field;
        }

        @Override
        boolean isNumber() {
            return true;
        }

        @Override
        boolean parse(FastDateParser parser, Calendar calendar, String source, ParsePosition pos, int maxWidth) {
            int idx = pos.getIndex();
            int last = source.length();
            if (maxWidth == 0) {
                // if no maxWidth, strip leading white space
                for (; idx < last; ++idx) {
                    char c = source.charAt(idx);
                    if (!Character.isWhitespace(c)) {
                        break;
                    }
                }
                pos.setIndex(idx);
            } else {
                int end = idx + maxWidth;
                if (last > end) {
                    last = end;
                }
            }
            for (; idx < last; ++idx) {
                char c = source.charAt(idx);
                if (!Character.isDigit(c)) {
                    break;
                }
            }
            if (pos.getIndex() == idx) {
                pos.setErrorIndex(idx);
                return false;
            }
            int value = Integer.parseInt(source.substring(pos.getIndex(), idx));
            pos.setIndex(idx);
            calendar.set(field, modify(parser, value));
            return true;
        }

        int modify(FastDateParser parser, int iValue) {
            return iValue;
        }

    }

    private static class TimeZoneStrategy extends PatternStrategy {

        private static final String RFC_822_TIME_ZONE = "[+-]\\d{4}";
        private static final String GMT_OPTION = "GMT[+-]\\d{1,2}:\\d{2}";
        private static final int ID = 0;

        private final Locale locale;
        private final Map<String, TzInfo> tzNames = new HashMap<>();

        TimeZoneStrategy(Locale locale) {
            this.locale = locale;
            StringBuilder sb = new StringBuilder();
            sb.append("((?iu)" + RFC_822_TIME_ZONE + "|" + GMT_OPTION);
            Set<String> sorted = new TreeSet<>(LONGER_FIRST_LOWERCASE);
            String[][] zones = DateFormatSymbols.getInstance(locale).getZoneStrings();
            for (String[] zoneNames : zones) {
                String tzId = zoneNames[ID];
                if ("GMT".equalsIgnoreCase(tzId)) {
                    continue;
                }
                TimeZone tz = TimeZone.getTimeZone(tzId);
                TzInfo standard = new TzInfo(tz, false);
                TzInfo tzInfo = standard;
                for (int i = 1; i < zoneNames.length; ++i) {
                    switch (i) {
                        case 3:
                            tzInfo = new TzInfo(tz, true);
                            break;
                        case 5:
                            tzInfo = standard;
                            break;
                        default:
                            break;
                    }
                    if (zoneNames[i] != null) {
                        String key = zoneNames[i].toLowerCase(locale);
                        if (sorted.add(key)) {
                            tzNames.put(key, tzInfo);
                        }
                    }
                }
            }
            for (String zoneName : sorted) {
                simpleQuote(sb.append('|'), zoneName);
            }
            sb.append(")");
            createPattern(sb);
        }

        @Override
        void setCalendar(FastDateParser parser, Calendar cal, String timeZone) {
            TimeZone tz = FastTimeZone.getGmtTimeZone(timeZone);
            if (tz != null) {
                cal.setTimeZone(tz);
            } else {
                String lowerCase = timeZone.toLowerCase(locale);
                TzInfo tzInfo = tzNames.get(lowerCase);
                if (tzInfo == null) {
                    // match missing the optional trailing period
                    tzInfo = tzNames.get(lowerCase + '.');
                }
                cal.set(Calendar.DST_OFFSET, tzInfo.dstOffset);
                cal.set(Calendar.ZONE_OFFSET, tzInfo.zone.getRawOffset());
            }
        }

        private static class TzInfo {
            TimeZone zone;
            int dstOffset;

            TzInfo(TimeZone tz, boolean useDst) {
                this.zone = tz;
                this.dstOffset = useDst ? tz.getDSTSavings() : 0;
            }
        }

    }

    private static class ISO8601TimeZoneStrategy extends PatternStrategy {
        // Z, +hh, -hh, +hhmm, -hhmm, +hh:mm or -hh:mm

        ISO8601TimeZoneStrategy(String pattern) {
            this.createPattern(pattern);
        }

        @Override
        void setCalendar(FastDateParser parser, Calendar cal, String value) {
            cal.setTimeZone(FastTimeZone.getGmtTimeZone(value));
        }

        private static final Strategy ISO_8601_1_STRATEGY = new ISO8601TimeZoneStrategy("(Z|(?:[+-]\\d{2}))");
        private static final Strategy ISO_8601_2_STRATEGY = new ISO8601TimeZoneStrategy("(Z|(?:[+-]\\d{2}\\d{2}))");
        private static final Strategy ISO_8601_3_STRATEGY = new ISO8601TimeZoneStrategy("(Z|(?:[+-]\\d{2}(?::)\\d{2}))");

        static Strategy getStrategy(int tokenLen) {
            switch (tokenLen) {
                case 1:
                    return ISO_8601_1_STRATEGY;
                case 2:
                    return ISO_8601_2_STRATEGY;
                case 3:
                    return ISO_8601_3_STRATEGY;
                default:
                    throw Exceptions.argument("invalid number of X");
            }
        }
    }

}
