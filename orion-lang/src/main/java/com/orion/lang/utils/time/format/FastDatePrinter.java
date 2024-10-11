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
package com.orion.lang.utils.time.format;

import com.orion.lang.constant.Const;
import com.orion.lang.utils.Exceptions;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * copy with apache
 */
public class FastDatePrinter implements DatePrinter, Serializable {

    private static final long serialVersionUID = 1L;

    private static final int MAX_DIGITS = 10;

    private static final ConcurrentMap<TimeZoneDisplayKey, String> TIME_ZONE_DISPLAY_CACHE = new ConcurrentHashMap<>(Const.CAPACITY_8);

    private final String pattern;

    private final TimeZone timeZone;

    private final Locale locale;

    private transient Rule[] rules;

    /**
     * 预估最大长度
     */
    private transient int maxLengthEstimate;

    protected FastDatePrinter(String pattern, TimeZone timeZone, Locale locale) {
        this.pattern = pattern;
        this.timeZone = timeZone;
        this.locale = locale;
        this.init();
    }

    /**
     * 初始化规则
     */
    private void init() {
        List<Rule> rulesList = this.parsePattern();
        this.rules = rulesList.toArray(new Rule[0]);
        int len = 0;
        for (int i = rules.length; --i >= 0; ) {
            len += rules[i].estimateLength();
        }
        this.maxLengthEstimate = len;
    }

    /**
     * 解析格式
     *
     * @return 规则
     */
    protected List<Rule> parsePattern() {
        DateFormatSymbols symbols = new DateFormatSymbols(locale);
        List<Rule> rules = new ArrayList<>();

        String[] eras = symbols.getEras();
        String[] months = symbols.getMonths();
        String[] shortMonths = symbols.getShortMonths();
        String[] weekdays = symbols.getWeekdays();
        String[] shortWeekdays = symbols.getShortWeekdays();
        String[] amPmStrings = symbols.getAmPmStrings();

        int length = pattern.length();
        int[] indexRef = new int[1];

        for (int i = 0; i < length; i++) {
            indexRef[0] = i;
            // 解析
            String token = this.parseToken(pattern, indexRef);
            i = indexRef[0];
            int tokenLen = token.length();
            if (tokenLen == 0) {
                break;
            }
            Rule rule;
            char c = token.charAt(0);
            switch (c) {
                case 'G': // era designator (text)
                    rule = new TextField(Calendar.ERA, eras);
                    break;
                case 'y': // year (number)
                case 'Y': // week year
                    if (tokenLen == 2) {
                        rule = TwoDigitYearField.INSTANCE;
                    } else {
                        rule = this.selectNumberRule(Calendar.YEAR, Math.max(tokenLen, 4));
                    }
                    if (c == 'Y') {
                        rule = new WeekYear((NumberRule) rule);
                    }
                    break;
                case 'M': // month in year (text and number)
                    if (tokenLen >= 4) {
                        rule = new TextField(Calendar.MONTH, months);
                    } else if (tokenLen == 3) {
                        rule = new TextField(Calendar.MONTH, shortMonths);
                    } else if (tokenLen == 2) {
                        rule = TwoDigitMonthField.INSTANCE;
                    } else {
                        rule = UnPaddedMonthField.INSTANCE;
                    }
                    break;
                case 'd': // day in month (number)
                    rule = this.selectNumberRule(Calendar.DAY_OF_MONTH, tokenLen);
                    break;
                case 'h': // hour in am/pm (number, 1..12)
                    rule = new TwelveHourField(selectNumberRule(Calendar.HOUR, tokenLen));
                    break;
                case 'H': // hour in day (number, 0..23)
                    rule = this.selectNumberRule(Calendar.HOUR_OF_DAY, tokenLen);
                    break;
                case 'm': // minute in hour (number)
                    rule = this.selectNumberRule(Calendar.MINUTE, tokenLen);
                    break;
                case 's': // second in minute (number)
                    rule = this.selectNumberRule(Calendar.SECOND, tokenLen);
                    break;
                case 'S': // millisecond (number)
                    rule = this.selectNumberRule(Calendar.MILLISECOND, tokenLen);
                    break;
                case 'E': // day in week (text)
                    rule = new TextField(Calendar.DAY_OF_WEEK, tokenLen < 4 ? shortWeekdays : weekdays);
                    break;
                case 'u': // day in week (number)
                    rule = new DayInWeekField(this.selectNumberRule(Calendar.DAY_OF_WEEK, tokenLen));
                    break;
                case 'D': // day in year (number)
                    rule = this.selectNumberRule(Calendar.DAY_OF_YEAR, tokenLen);
                    break;
                case 'F': // day of week in month (number)
                    rule = this.selectNumberRule(Calendar.DAY_OF_WEEK_IN_MONTH, tokenLen);
                    break;
                case 'w': // week in year (number)
                    rule = this.selectNumberRule(Calendar.WEEK_OF_YEAR, tokenLen);
                    break;
                case 'W': // week in month (number)
                    rule = this.selectNumberRule(Calendar.WEEK_OF_MONTH, tokenLen);
                    break;
                case 'a': // am/pm marker (text)
                    rule = new TextField(Calendar.AM_PM, amPmStrings);
                    break;
                case 'k': // hour in day (1..24)
                    rule = new TwentyFourHourField(selectNumberRule(Calendar.HOUR_OF_DAY, tokenLen));
                    break;
                case 'K': // hour in am/pm (0..11)
                    rule = this.selectNumberRule(Calendar.HOUR, tokenLen);
                    break;
                case 'X': // ISO 8601
                    rule = Iso8601Rule.getRule(tokenLen);
                    break;
                case 'z': // time zone (text)
                    if (tokenLen >= 4) {
                        rule = new TimeZoneNameRule(timeZone, locale, TimeZone.LONG);
                    } else {
                        rule = new TimeZoneNameRule(timeZone, locale, TimeZone.SHORT);
                    }
                    break;
                case 'Z': // time zone (value)
                    if (tokenLen == 1) {
                        rule = TimeZoneNumberRule.INSTANCE_NO_COLON;
                    } else if (tokenLen == 2) {
                        rule = Iso8601Rule.ISO8601_HOURS_COLON_MINUTES;
                    } else {
                        rule = TimeZoneNumberRule.INSTANCE_COLON;
                    }
                    break;
                case '\'': // literal text
                    String sub = token.substring(1);
                    if (sub.length() == 1) {
                        rule = new CharacterLiteral(sub.charAt(0));
                    } else {
                        rule = new StringLiteral(sub);
                    }
                    break;
                default:
                    throw Exceptions.argument("illegal pattern component: " + token);
            }
            rules.add(rule);
        }
        return rules;
    }

    /**
     * 解析格式
     *
     * @param pattern  pattern
     * @param indexRef index
     * @return 格式标记
     */
    protected String parseToken(String pattern, int[] indexRef) {
        StringBuilder buf = new StringBuilder();

        int i = indexRef[0];
        int length = pattern.length();

        char c = pattern.charAt(i);
        if (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z') {
            // 扫描同一字符的运行 时间模式
            buf.append(c);
            while (i + 1 < length) {
                char peek = pattern.charAt(i + 1);
                if (peek == c) {
                    buf.append(c);
                    i++;
                } else {
                    break;
                }
            }
        } else {
            // 文本类型
            buf.append('\'');
            boolean inLiteral = false;
            for (; i < length; i++) {
                c = pattern.charAt(i);
                if (c == '\'') {
                    if (i + 1 < length && pattern.charAt(i + 1) == '\'') {
                        // '' 被视为转义的 '
                        i++;
                        buf.append(c);
                    } else {
                        inLiteral = !inLiteral;
                    }
                } else if (!inLiteral && (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z')) {
                    i--;
                    break;
                } else {
                    buf.append(c);
                }
            }
        }
        indexRef[0] = i;
        return buf.toString();
    }

    /**
     * 选择合适的规则
     *
     * @param field   field
     * @param padding padding
     * @return rule
     */
    protected NumberRule selectNumberRule(int field, int padding) {
        switch (padding) {
            case 1:
                return new UnPaddedNumberField(field);
            case 2:
                return new TwoDigitNumberField(field);
            default:
                return new PaddedNumberField(field, padding);
        }
    }

    String format(Object obj) {
        if (obj instanceof Date) {
            return format((Date) obj);
        } else if (obj instanceof Calendar) {
            return format((Calendar) obj);
        } else if (obj instanceof Long) {
            return format(((Long) obj).longValue());
        } else {
            throw Exceptions.argument("unknown class: " + (obj == null ? "<null>" : obj.getClass().getName()));
        }
    }

    private Calendar newCalendar() {
        return Calendar.getInstance(timeZone, locale);
    }

    /**
     * 格式化日历
     *
     * @param calendar calendar
     * @param buffer   buffer
     * @param <B>      appender
     * @return appender
     */
    private <B extends Appendable> B format(Calendar calendar, B buffer) {
        if (!calendar.getTimeZone().equals(timeZone)) {
            calendar = (Calendar) calendar.clone();
            calendar.setTimeZone(timeZone);
        }
        return applyRules(calendar, buffer);
    }

    /**
     * 执行规则
     *
     * @param calendar calendar
     * @param buffer   buffer
     * @param <B>      appender
     * @return appender
     */
    private <B extends Appendable> B applyRules(Calendar calendar, B buffer) {
        try {
            for (Rule rule : rules) {
                rule.appendTo(buffer, calendar);
            }
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
        return buffer;
    }

    /**
     * 执行规则
     *
     * @param c calendar
     * @return ruleString
     */
    private String applyRulesToString(Calendar c) {
        return this.applyRules(c, new StringBuilder(maxLengthEstimate)).toString();
    }

    @Override
    public String format(long millis) {
        Calendar c = newCalendar();
        c.setTimeInMillis(millis);
        return this.applyRulesToString(c);
    }

    @Override
    public String format(Date date) {
        Calendar c = newCalendar();
        c.setTime(date);
        return this.applyRulesToString(c);
    }

    @Override
    public String format(Calendar calendar) {
        return this.format(calendar, new StringBuilder(maxLengthEstimate)).toString();
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

    public int getMaxLengthEstimate() {
        return maxLengthEstimate;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof FastDatePrinter)) {
            return false;
        }
        FastDatePrinter other = (FastDatePrinter) obj;
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

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.init();
    }

    private static void appendDigits(Appendable buffer, int value) throws IOException {
        buffer.append((char) (value / 10 + '0'));
        buffer.append((char) (value % 10 + '0'));
    }

    private static void appendFullDigits(Appendable buffer, int value, int minFieldWidth) throws IOException {
        if (value < 10000) {
            int nDigits = 4;
            if (value < 1000) {
                --nDigits;
                if (value < 100) {
                    --nDigits;
                    if (value < 10) {
                        --nDigits;
                    }
                }
            }
            // 左侧0填充
            for (int i = minFieldWidth - nDigits; i > 0; --i) {
                buffer.append('0');
            }
            switch (nDigits) {
                case 4:
                    buffer.append((char) (value / 1000 + '0'));
                    value %= 1000;
                case 3:
                    if (value >= 100) {
                        buffer.append((char) (value / 100 + '0'));
                        value %= 100;
                    } else {
                        buffer.append('0');
                    }
                case 2:
                    if (value >= 10) {
                        buffer.append((char) (value / 10 + '0'));
                        value %= 10;
                    } else {
                        buffer.append('0');
                    }
                case 1:
                    buffer.append((char) (value + '0'));
            }
        } else {
            // 十进制表示
            char[] work = new char[MAX_DIGITS];
            int digit = 0;
            while (value != 0) {
                work[digit++] = (char) (value % 10 + '0');
                value = value / 10;
            }
            // 0填充
            while (digit < minFieldWidth) {
                buffer.append('0');
                --minFieldWidth;
            }
            // 倒序
            while (--digit >= 0) {
                buffer.append(work[digit]);
            }
        }
    }

    static String getTimeZoneDisplay(TimeZone tz, boolean daylight, int style, Locale locale) {
        TimeZoneDisplayKey key = new TimeZoneDisplayKey(tz, daylight, style, locale);
        String value = TIME_ZONE_DISPLAY_CACHE.get(key);
        if (value == null) {
            value = tz.getDisplayName(daylight, style, locale);
            String prior = TIME_ZONE_DISPLAY_CACHE.putIfAbsent(key, value);
            if (prior != null) {
                value = prior;
            }
        }
        return value;
    }

    // -------------------- rule --------------------

    private interface Rule {

        /**
         * 返回结果的估计长度
         *
         * @return 长度
         */
        int estimateLength();

        /**
         * 根据规则将值拼接到日历
         *
         * @param buffer   输出缓冲区
         * @param calendar calendar
         * @throws IOException IOException
         */
        void appendTo(Appendable buffer, Calendar calendar) throws IOException;

    }

    private interface NumberRule extends Rule {

        /**
         * 根据规则将值拼接到缓冲区
         *
         * @param buffer 输出缓冲区
         * @param value  value
         * @throws IOException IOException
         */
        void appendTo(Appendable buffer, int value) throws IOException;

    }

    private static class CharacterLiteral implements Rule {

        private char mValue;

        CharacterLiteral(char value) {
            this.mValue = value;
        }

        @Override
        public int estimateLength() {
            return 1;
        }

        @Override
        public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
            buffer.append(mValue);
        }

    }

    private static class StringLiteral implements Rule {

        private String value;

        StringLiteral(String value) {
            this.value = value;
        }

        @Override
        public int estimateLength() {
            return value.length();
        }

        @Override
        public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
            buffer.append(value);
        }

    }

    private static class TextField implements Rule {

        private int field;

        private String[] values;

        TextField(int field, String[] values) {
            this.field = field;
            this.values = values;
        }

        @Override
        public int estimateLength() {
            int max = 0;
            for (int i = values.length; --i >= 0; ) {
                int len = values[i].length();
                if (len > max) {
                    max = len;
                }
            }
            return max;
        }

        @Override
        public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
            buffer.append(values[calendar.get(field)]);
        }

    }

    private static class UnPaddedNumberField implements NumberRule {

        private int field;

        UnPaddedNumberField(int field) {
            this.field = field;
        }

        @Override
        public int estimateLength() {
            return 4;
        }

        @Override
        public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
            this.appendTo(buffer, calendar.get(field));
        }

        @Override
        public void appendTo(Appendable buffer, int value) throws IOException {
            if (value < 10) {
                buffer.append((char) (value + '0'));
            } else if (value < 100) {
                appendDigits(buffer, value);
            } else {
                appendFullDigits(buffer, value, 1);
            }
        }

    }

    private static class UnPaddedMonthField implements NumberRule {

        static UnPaddedMonthField INSTANCE = new UnPaddedMonthField();

        UnPaddedMonthField() {
            super();
        }

        @Override
        public int estimateLength() {
            return 2;
        }

        @Override
        public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
            this.appendTo(buffer, calendar.get(Calendar.MONTH) + 1);
        }

        @Override
        public void appendTo(Appendable buffer, int value) throws IOException {
            if (value < 10) {
                buffer.append((char) (value + '0'));
            } else {
                appendDigits(buffer, value);
            }
        }

    }

    private static class PaddedNumberField implements NumberRule {

        private int field;

        private int size;

        PaddedNumberField(int field, int size) {
            if (size < 3) {
                throw Exceptions.argument();
            }
            this.field = field;
            this.size = size;
        }

        @Override
        public int estimateLength() {
            return size;
        }

        @Override
        public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
            this.appendTo(buffer, calendar.get(field));
        }

        @Override
        public void appendTo(Appendable buffer, int value) throws IOException {
            appendFullDigits(buffer, value, size);
        }

    }

    private static class TwoDigitNumberField implements NumberRule {

        private int field;

        TwoDigitNumberField(int field) {
            this.field = field;
        }

        @Override
        public int estimateLength() {
            return 2;
        }

        @Override
        public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
            this.appendTo(buffer, calendar.get(field));
        }

        @Override
        public void appendTo(Appendable buffer, int value) throws IOException {
            if (value < 100) {
                appendDigits(buffer, value);
            } else {
                appendFullDigits(buffer, value, 2);
            }
        }

    }

    private static class TwoDigitYearField implements NumberRule {

        static TwoDigitYearField INSTANCE = new TwoDigitYearField();

        TwoDigitYearField() {
            super();
        }

        @Override
        public int estimateLength() {
            return 2;
        }

        @Override
        public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
            this.appendTo(buffer, calendar.get(Calendar.YEAR) % 100);
        }

        @Override
        public void appendTo(Appendable buffer, int value) throws IOException {
            appendDigits(buffer, value);
        }

    }

    private static class TwoDigitMonthField implements NumberRule {

        static TwoDigitMonthField INSTANCE = new TwoDigitMonthField();

        TwoDigitMonthField() {
            super();
        }

        @Override
        public int estimateLength() {
            return 2;
        }

        @Override
        public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
            this.appendTo(buffer, calendar.get(Calendar.MONTH) + 1);
        }

        @Override
        public void appendTo(Appendable buffer, int value) throws IOException {
            appendDigits(buffer, value);
        }

    }

    private static class TwelveHourField implements NumberRule {

        private NumberRule rule;

        TwelveHourField(NumberRule rule) {
            this.rule = rule;
        }

        @Override
        public int estimateLength() {
            return rule.estimateLength();
        }

        @Override
        public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
            int value = calendar.get(Calendar.HOUR);
            if (value == 0) {
                value = calendar.getLeastMaximum(Calendar.HOUR) + 1;
            }
            rule.appendTo(buffer, value);
        }

        @Override
        public void appendTo(Appendable buffer, int value) throws IOException {
            rule.appendTo(buffer, value);
        }

    }

    private static class TwentyFourHourField implements NumberRule {

        private NumberRule rule;

        TwentyFourHourField(NumberRule rule) {
            this.rule = rule;
        }

        @Override
        public int estimateLength() {
            return rule.estimateLength();
        }

        @Override
        public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
            int value = calendar.get(Calendar.HOUR_OF_DAY);
            if (value == 0) {
                value = calendar.getMaximum(Calendar.HOUR_OF_DAY) + 1;
            }
            rule.appendTo(buffer, value);
        }

        @Override
        public void appendTo(Appendable buffer, int value) throws IOException {
            rule.appendTo(buffer, value);
        }

    }

    private static class DayInWeekField implements NumberRule {

        private NumberRule rule;

        DayInWeekField(NumberRule rule) {
            this.rule = rule;
        }

        @Override
        public int estimateLength() {
            return rule.estimateLength();
        }

        @Override
        public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
            int value = calendar.get(Calendar.DAY_OF_WEEK);
            rule.appendTo(buffer, value == Calendar.SUNDAY ? 7 : value - 1);
        }

        @Override
        public void appendTo(Appendable buffer, int value) throws IOException {
            rule.appendTo(buffer, value);
        }

    }

    private static class WeekYear implements NumberRule {

        private NumberRule rule;

        WeekYear(NumberRule rule) {
            this.rule = rule;
        }

        @Override
        public int estimateLength() {
            return rule.estimateLength();
        }

        @Override
        public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
            rule.appendTo(buffer, calendar.getWeekYear());
        }

        @Override
        public void appendTo(Appendable buffer, int value) throws IOException {
            rule.appendTo(buffer, value);
        }

    }

    private static class TimeZoneNameRule implements Rule {

        private Locale locale;

        private int style;

        private String standard;

        private String daylight;

        TimeZoneNameRule(TimeZone timeZone, Locale locale, int style) {
            this.locale = locale;
            this.style = style;
            this.standard = getTimeZoneDisplay(timeZone, false, style, locale);
            this.daylight = getTimeZoneDisplay(timeZone, true, style, locale);
        }

        @Override
        public int estimateLength() {
            return Math.max(standard.length(), daylight.length());
        }

        @Override
        public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
            TimeZone zone = calendar.getTimeZone();
            if (calendar.get(Calendar.DST_OFFSET) == 0) {
                buffer.append(getTimeZoneDisplay(zone, false, style, locale));
            } else {
                buffer.append(getTimeZoneDisplay(zone, true, style, locale));
            }
        }
    }

    private static class TimeZoneNumberRule implements Rule {

        static TimeZoneNumberRule INSTANCE_COLON = new TimeZoneNumberRule(true);

        static TimeZoneNumberRule INSTANCE_NO_COLON = new TimeZoneNumberRule(false);

        boolean colon;

        TimeZoneNumberRule(boolean colon) {
            this.colon = colon;
        }

        @Override
        public int estimateLength() {
            return 5;
        }

        @Override
        public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
            int offset = calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET);
            if (offset < 0) {
                buffer.append('-');
                offset = -offset;
            } else {
                buffer.append('+');
            }
            int hours = offset / (60 * 60 * 1000);
            appendDigits(buffer, hours);
            if (colon) {
                buffer.append(':');
            }
            int minutes = offset / (60 * 1000) - 60 * hours;
            appendDigits(buffer, minutes);
        }
    }

    private static class Iso8601Rule implements Rule {

        /**
         * Sign TwoDigitHours or Z
         */
        static Iso8601Rule ISO8601_HOURS = new Iso8601Rule(3);

        /**
         * Sign TwoDigitHours Minutes or Z
         */
        static Iso8601Rule ISO8601_HOURS_MINUTES = new Iso8601Rule(5);

        /**
         * Sign TwoDigitHours : Minutes or Z
         */
        static Iso8601Rule ISO8601_HOURS_COLON_MINUTES = new Iso8601Rule(6);

        int length;

        Iso8601Rule(int length) {
            this.length = length;
        }

        static Iso8601Rule getRule(int tokenLen) {
            switch (tokenLen) {
                case 1:
                    return ISO8601_HOURS;
                case 2:
                    return ISO8601_HOURS_MINUTES;
                case 3:
                    return ISO8601_HOURS_COLON_MINUTES;
                default:
                    throw Exceptions.argument("invalid number of X");
            }
        }

        @Override
        public int estimateLength() {
            return length;
        }

        @Override
        public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
            int offset = calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET);
            if (offset == 0) {
                buffer.append("Z");
                return;
            }
            if (offset < 0) {
                buffer.append('-');
                offset = -offset;
            } else {
                buffer.append('+');
            }
            int hours = offset / (60 * 60 * 1000);
            appendDigits(buffer, hours);
            if (length < 5) {
                return;
            }
            if (length == 6) {
                buffer.append(':');
            }
            int minutes = offset / (60 * 1000) - 60 * hours;
            appendDigits(buffer, minutes);
        }
    }

    private static class TimeZoneDisplayKey {

        private final TimeZone timeZone;

        private final int style;

        private final Locale locale;

        TimeZoneDisplayKey(TimeZone timeZone, boolean daylight, int style, Locale locale) {
            this.timeZone = timeZone;
            if (daylight) {
                this.style = style | 0x80000000;
            } else {
                this.style = style;
            }
            this.locale = locale;
        }

        @Override
        public int hashCode() {
            return (style * 31 + locale.hashCode()) * 31 + timeZone.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof TimeZoneDisplayKey) {
                TimeZoneDisplayKey other = (TimeZoneDisplayKey) obj;
                return timeZone.equals(other.timeZone)
                        && style == other.style
                        && locale.equals(other.locale);
            }
            return false;
        }
    }

}
