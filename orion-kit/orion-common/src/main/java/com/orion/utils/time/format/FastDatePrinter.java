package com.orion.utils.time.format;

import com.orion.utils.Exceptions;

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

    private final String mPattern;

    private final TimeZone mTimeZone;

    private final Locale mLocale;

    private transient Rule[] mRules;

    private transient int mMaxLengthEstimate;

    protected FastDatePrinter(String pattern, TimeZone timeZone, Locale locale) {
        mPattern = pattern;
        mTimeZone = timeZone;
        mLocale = locale;
        init();
    }

    private void init() {
        List<Rule> rulesList = parsePattern();
        mRules = rulesList.toArray(new Rule[0]);
        int len = 0;
        for (int i = mRules.length; --i >= 0; ) {
            len += mRules[i].estimateLength();
        }
        mMaxLengthEstimate = len;
    }

    protected List<Rule> parsePattern() {
        DateFormatSymbols symbols = new DateFormatSymbols(mLocale);
        List<Rule> rules = new ArrayList<>();

        String[] ERAs = symbols.getEras();
        String[] months = symbols.getMonths();
        String[] shortMonths = symbols.getShortMonths();
        String[] weekdays = symbols.getWeekdays();
        String[] shortWeekdays = symbols.getShortWeekdays();
        String[] AmPmStrings = symbols.getAmPmStrings();

        int length = mPattern.length();
        int[] indexRef = new int[1];

        for (int i = 0; i < length; i++) {
            indexRef[0] = i;
            String token = parseToken(mPattern, indexRef);
            i = indexRef[0];

            int tokenLen = token.length();
            if (tokenLen == 0) {
                break;
            }

            Rule rule;
            char c = token.charAt(0);

            switch (c) {
                case 'G': // era designator (text)
                    rule = new TextField(Calendar.ERA, ERAs);
                    break;
                case 'y': // year (number)
                case 'Y': // week year
                    if (tokenLen == 2) {
                        rule = TwoDigitYearField.INSTANCE;
                    } else {
                        rule = selectNumberRule(Calendar.YEAR, Math.max(tokenLen, 4));
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
                        rule = UnpaddedMonthField.INSTANCE;
                    }
                    break;
                case 'd': // day in month (number)
                    rule = selectNumberRule(Calendar.DAY_OF_MONTH, tokenLen);
                    break;
                case 'h': // hour in am/pm (number, 1..12)
                    rule = new TwelveHourField(selectNumberRule(Calendar.HOUR, tokenLen));
                    break;
                case 'H': // hour in day (number, 0..23)
                    rule = selectNumberRule(Calendar.HOUR_OF_DAY, tokenLen);
                    break;
                case 'm': // minute in hour (number)
                    rule = selectNumberRule(Calendar.MINUTE, tokenLen);
                    break;
                case 's': // second in minute (number)
                    rule = selectNumberRule(Calendar.SECOND, tokenLen);
                    break;
                case 'S': // millisecond (number)
                    rule = selectNumberRule(Calendar.MILLISECOND, tokenLen);
                    break;
                case 'E': // day in week (text)
                    rule = new TextField(Calendar.DAY_OF_WEEK, tokenLen < 4 ? shortWeekdays : weekdays);
                    break;
                case 'u': // day in week (number)
                    rule = new DayInWeekField(selectNumberRule(Calendar.DAY_OF_WEEK, tokenLen));
                    break;
                case 'D': // day in year (number)
                    rule = selectNumberRule(Calendar.DAY_OF_YEAR, tokenLen);
                    break;
                case 'F': // day of week in month (number)
                    rule = selectNumberRule(Calendar.DAY_OF_WEEK_IN_MONTH, tokenLen);
                    break;
                case 'w': // week in year (number)
                    rule = selectNumberRule(Calendar.WEEK_OF_YEAR, tokenLen);
                    break;
                case 'W': // week in month (number)
                    rule = selectNumberRule(Calendar.WEEK_OF_MONTH, tokenLen);
                    break;
                case 'a': // am/pm marker (text)
                    rule = new TextField(Calendar.AM_PM, AmPmStrings);
                    break;
                case 'k': // hour in day (1..24)
                    rule = new TwentyFourHourField(selectNumberRule(Calendar.HOUR_OF_DAY, tokenLen));
                    break;
                case 'K': // hour in am/pm (0..11)
                    rule = selectNumberRule(Calendar.HOUR, tokenLen);
                    break;
                case 'X': // ISO 8601
                    rule = Iso8601_Rule.getRule(tokenLen);
                    break;
                case 'z': // time zone (text)
                    if (tokenLen >= 4) {
                        rule = new TimeZoneNameRule(mTimeZone, mLocale, TimeZone.LONG);
                    } else {
                        rule = new TimeZoneNameRule(mTimeZone, mLocale, TimeZone.SHORT);
                    }
                    break;
                case 'Z': // time zone (value)
                    if (tokenLen == 1) {
                        rule = TimeZoneNumberRule.INSTANCE_NO_COLON;
                    } else if (tokenLen == 2) {
                        rule = Iso8601_Rule.ISO8601_HOURS_COLON_MINUTES;
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
                    throw new IllegalArgumentException("Illegal pattern component: " + token);
            }

            rules.add(rule);
        }

        return rules;
    }

    protected String parseToken(String pattern, int[] indexRef) {
        StringBuilder buf = new StringBuilder();

        int i = indexRef[0];
        int length = pattern.length();

        char c = pattern.charAt(i);
        if (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z') {
            // Scan a run of the same character, which indicates a time
            // pattern.
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
            // This will identify token as text.
            buf.append('\'');

            boolean inLiteral = false;

            for (; i < length; i++) {
                c = pattern.charAt(i);

                if (c == '\'') {
                    if (i + 1 < length && pattern.charAt(i + 1) == '\'') {
                        // '' is treated as escaped '
                        i++;
                        buf.append(c);
                    } else {
                        inLiteral = !inLiteral;
                    }
                } else if (!inLiteral &&
                        (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z')) {
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

    protected NumberRule selectNumberRule(int field, int padding) {
        switch (padding) {
            case 1:
                return new UnpaddedNumberField(field);
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
            throw new IllegalArgumentException("Unknown class: " +
                    (obj == null ? "<null>" : obj.getClass().getName()));
        }
    }

    @Override
    public String format(long millis) {
        Calendar c = newCalendar();
        c.setTimeInMillis(millis);
        return applyRulesToString(c);
    }

    private String applyRulesToString(Calendar c) {
        return applyRules(c, new StringBuilder(mMaxLengthEstimate)).toString();
    }

    private Calendar newCalendar() {
        return Calendar.getInstance(mTimeZone, mLocale);
    }

    @Override
    public String format(Date date) {
        Calendar c = newCalendar();
        c.setTime(date);
        return applyRulesToString(c);
    }

    @Override
    public String format(Calendar calendar) {
        return format(calendar, new StringBuilder(mMaxLengthEstimate)).toString();
    }

    @Override
    public <B extends Appendable> B format(long millis, B buf) {
        Calendar c = newCalendar();
        c.setTimeInMillis(millis);
        return applyRules(c, buf);
    }

    @Override
    public <B extends Appendable> B format(Date date, B buf) {
        Calendar c = newCalendar();
        c.setTime(date);
        return applyRules(c, buf);
    }

    @Override
    public <B extends Appendable> B format(Calendar calendar, B buf) {
        // do not pass in calendar directly, this will cause TimeZone of FastDatePrinter to be ignored
        if (!calendar.getTimeZone().equals(mTimeZone)) {
            calendar = (Calendar) calendar.clone();
            calendar.setTimeZone(mTimeZone);
        }
        return applyRules(calendar, buf);
    }

    private <B extends Appendable> B applyRules(Calendar calendar, B buf) {
        try {
            for (Rule rule : mRules) {
                rule.appendTo(buf, calendar);
            }
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
        return buf;
    }

    @Override
    public String getPattern() {
        return mPattern;
    }

    @Override
    public TimeZone getTimeZone() {
        return mTimeZone;
    }

    @Override
    public Locale getLocale() {
        return mLocale;
    }

    public int getMaxLengthEstimate() {
        return mMaxLengthEstimate;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof FastDatePrinter)) {
            return false;
        }
        FastDatePrinter other = (FastDatePrinter) obj;
        return mPattern.equals(other.mPattern)
                && mTimeZone.equals(other.mTimeZone)
                && mLocale.equals(other.mLocale);
    }

    @Override
    public int hashCode() {
        return mPattern.hashCode() + 13 * (mTimeZone.hashCode() + 13 * mLocale.hashCode());
    }

    @Override
    public String toString() {
        return "FastDatePrinter[" + mPattern + "," + mLocale + "," + mTimeZone.getID() + "]";
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        init();
    }

    private static void appendDigits(Appendable buffer, int value) throws IOException {
        buffer.append((char) (value / 10 + '0'));
        buffer.append((char) (value % 10 + '0'));
    }

    private static final int MAX_DIGITS = 10;

    private static void appendFullDigits(Appendable buffer, int value, int minFieldWidth) throws IOException {
        // specialized paths for 1 to 4 digits -> avoid the memory allocation from the temporary work array
        // see LANG-1248
        if (value < 10000) {
            // less memory allocation path works for four digits or less

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
            // left zero pad
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
            // more memory allocation path works for any digits

            // build up decimal representation in reverse
            char[] work = new char[MAX_DIGITS];
            int digit = 0;
            while (value != 0) {
                work[digit++] = (char) (value % 10 + '0');
                value = value / 10;
            }

            // pad with zeros
            while (digit < minFieldWidth) {
                buffer.append('0');
                --minFieldWidth;
            }

            // reverse
            while (--digit >= 0) {
                buffer.append(work[digit]);
            }
        }
    }

    private interface Rule {
        /**
         * Returns the estimated length of the result.
         *
         * @return the estimated length
         */
        int estimateLength();

        /**
         * Appends the value of the specified calendar to the output buffer based on the rule implementation.
         *
         * @param buf      the output buffer
         * @param calendar calendar to be appended
         * @throws IOException if an I/O error occurs
         */
        void appendTo(Appendable buf, Calendar calendar) throws IOException;
    }

    private interface NumberRule extends Rule {
        /**
         * Appends the specified value to the output buffer based on the rule implementation.
         *
         * @param buffer the output buffer
         * @param value  the value to be appended
         * @throws IOException if an I/O error occurs
         */
        void appendTo(Appendable buffer, int value) throws IOException;
    }

    private static class CharacterLiteral implements Rule {
        private char mValue;

        /**
         * Constructs a new instance of {@code CharacterLiteral}
         * to hold the specified value.
         *
         * @param value the character literal
         */
        CharacterLiteral(char value) {
            mValue = value;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int estimateLength() {
            return 1;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
            buffer.append(mValue);
        }
    }

    private static class StringLiteral implements Rule {
        private String mValue;

        /**
         * Constructs a new instance of {@code StringLiteral}
         * to hold the specified value.
         *
         * @param value the string literal
         */
        StringLiteral(String value) {
            mValue = value;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int estimateLength() {
            return mValue.length();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
            buffer.append(mValue);
        }
    }

    private static class TextField implements Rule {
        private int mField;
        private String[] mValues;

        /**
         * Constructs an instance of {@code TextField}
         * with the specified field and values.
         *
         * @param field  the field
         * @param values the field values
         */
        TextField(int field, String[] values) {
            mField = field;
            mValues = values;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int estimateLength() {
            int max = 0;
            for (int i = mValues.length; --i >= 0; ) {
                int len = mValues[i].length();
                if (len > max) {
                    max = len;
                }
            }
            return max;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
            buffer.append(mValues[calendar.get(mField)]);
        }
    }

    private static class UnpaddedNumberField implements NumberRule {
        private int mField;

        /**
         * Constructs an instance of {@code UnpadedNumberField} with the specified field.
         *
         * @param field the field
         */
        UnpaddedNumberField(int field) {
            mField = field;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int estimateLength() {
            return 4;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
            appendTo(buffer, calendar.get(mField));
        }

        /**
         * {@inheritDoc}
         */
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

    private static class UnpaddedMonthField implements NumberRule {
        static UnpaddedMonthField INSTANCE = new UnpaddedMonthField();

        /**
         * Constructs an instance of {@code UnpaddedMonthField}.
         */
        UnpaddedMonthField() {
            super();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int estimateLength() {
            return 2;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
            appendTo(buffer, calendar.get(Calendar.MONTH) + 1);
        }

        /**
         * {@inheritDoc}
         */
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
        private int mField;
        private int mSize;

        /**
         * Constructs an instance of {@code PaddedNumberField}.
         *
         * @param field the field
         * @param size  size of the output field
         */
        PaddedNumberField(int field, int size) {
            if (size < 3) {
                // Should use UnpaddedNumberField or TwoDigitNumberField.
                throw new IllegalArgumentException();
            }
            mField = field;
            mSize = size;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int estimateLength() {
            return mSize;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
            appendTo(buffer, calendar.get(mField));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void appendTo(Appendable buffer, int value) throws IOException {
            appendFullDigits(buffer, value, mSize);
        }
    }

    private static class TwoDigitNumberField implements NumberRule {
        private int mField;

        /**
         * Constructs an instance of {@code TwoDigitNumberField} with the specified field.
         *
         * @param field the field
         */
        TwoDigitNumberField(int field) {
            mField = field;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int estimateLength() {
            return 2;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
            appendTo(buffer, calendar.get(mField));
        }

        /**
         * {@inheritDoc}
         */
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

        /**
         * Constructs an instance of {@code TwoDigitYearField}.
         */
        TwoDigitYearField() {
            super();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int estimateLength() {
            return 2;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
            appendTo(buffer, calendar.get(Calendar.YEAR) % 100);
        }

        /**
         * {@inheritDoc}
         */
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
            appendTo(buffer, calendar.get(Calendar.MONTH) + 1);
        }

        @Override
        public void appendTo(Appendable buffer, int value) throws IOException {
            appendDigits(buffer, value);
        }
    }

    private static class TwelveHourField implements NumberRule {
        private NumberRule mRule;

        TwelveHourField(NumberRule rule) {
            mRule = rule;
        }

        @Override
        public int estimateLength() {
            return mRule.estimateLength();
        }

        @Override
        public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
            int value = calendar.get(Calendar.HOUR);
            if (value == 0) {
                value = calendar.getLeastMaximum(Calendar.HOUR) + 1;
            }
            mRule.appendTo(buffer, value);
        }

        @Override
        public void appendTo(Appendable buffer, int value) throws IOException {
            mRule.appendTo(buffer, value);
        }
    }

    private static class TwentyFourHourField implements NumberRule {
        private NumberRule mRule;

        TwentyFourHourField(NumberRule rule) {
            mRule = rule;
        }

        @Override
        public int estimateLength() {
            return mRule.estimateLength();
        }

        @Override
        public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
            int value = calendar.get(Calendar.HOUR_OF_DAY);
            if (value == 0) {
                value = calendar.getMaximum(Calendar.HOUR_OF_DAY) + 1;
            }
            mRule.appendTo(buffer, value);
        }

        @Override
        public void appendTo(Appendable buffer, int value) throws IOException {
            mRule.appendTo(buffer, value);
        }
    }

    private static class DayInWeekField implements NumberRule {
        private NumberRule mRule;

        DayInWeekField(NumberRule rule) {
            mRule = rule;
        }

        @Override
        public int estimateLength() {
            return mRule.estimateLength();
        }

        @Override
        public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
            int value = calendar.get(Calendar.DAY_OF_WEEK);
            mRule.appendTo(buffer, value == Calendar.SUNDAY ? 7 : value - 1);
        }

        @Override
        public void appendTo(Appendable buffer, int value) throws IOException {
            mRule.appendTo(buffer, value);
        }
    }

    private static class WeekYear implements NumberRule {
        private NumberRule mRule;

        WeekYear(NumberRule rule) {
            mRule = rule;
        }

        @Override
        public int estimateLength() {
            return mRule.estimateLength();
        }

        @Override
        public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
            mRule.appendTo(buffer, calendar.getWeekYear());
        }

        @Override
        public void appendTo(Appendable buffer, int value) throws IOException {
            mRule.appendTo(buffer, value);
        }
    }

    private static final ConcurrentMap<TimeZoneDisplayKey, String> cTimeZoneDisplayCache = new ConcurrentHashMap<>(7);

    static String getTimeZoneDisplay(TimeZone tz, boolean daylight, int style, Locale locale) {
        TimeZoneDisplayKey key = new TimeZoneDisplayKey(tz, daylight, style, locale);
        String value = cTimeZoneDisplayCache.get(key);
        if (value == null) {
            value = tz.getDisplayName(daylight, style, locale);
            String prior = cTimeZoneDisplayCache.putIfAbsent(key, value);
            if (prior != null) {
                value = prior;
            }
        }
        return value;
    }

    private static class TimeZoneNameRule implements Rule {
        private Locale mLocale;
        private int mStyle;
        private String mStandard;
        private String mDaylight;

        TimeZoneNameRule(TimeZone timeZone, Locale locale, int style) {
            mLocale = locale;
            mStyle = style;

            mStandard = getTimeZoneDisplay(timeZone, false, style, locale);
            mDaylight = getTimeZoneDisplay(timeZone, true, style, locale);
        }

        @Override
        public int estimateLength() {
            return Math.max(mStandard.length(), mDaylight.length());
        }

        @Override
        public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
            TimeZone zone = calendar.getTimeZone();
            if (calendar.get(Calendar.DST_OFFSET) == 0) {
                buffer.append(getTimeZoneDisplay(zone, false, mStyle, mLocale));
            } else {
                buffer.append(getTimeZoneDisplay(zone, true, mStyle, mLocale));
            }
        }
    }

    private static class TimeZoneNumberRule implements Rule {
        static TimeZoneNumberRule INSTANCE_COLON = new TimeZoneNumberRule(true);
        static TimeZoneNumberRule INSTANCE_NO_COLON = new TimeZoneNumberRule(false);

        boolean mColon;

        /**
         * Constructs an instance of {@code TimeZoneNumberRule} with the specified properties.
         *
         * @param colon add colon between HH and MM in the output if {@code true}
         */
        TimeZoneNumberRule(boolean colon) {
            mColon = colon;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int estimateLength() {
            return 5;
        }

        /**
         * {@inheritDoc}
         */
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

            if (mColon) {
                buffer.append(':');
            }

            int minutes = offset / (60 * 1000) - 60 * hours;
            appendDigits(buffer, minutes);
        }
    }

    private static class Iso8601_Rule implements Rule {

        // Sign TwoDigitHours or Z
        static Iso8601_Rule ISO8601_HOURS = new Iso8601_Rule(3);
        // Sign TwoDigitHours Minutes or Z
        static Iso8601_Rule ISO8601_HOURS_MINUTES = new Iso8601_Rule(5);
        // Sign TwoDigitHours : Minutes or Z
        static Iso8601_Rule ISO8601_HOURS_COLON_MINUTES = new Iso8601_Rule(6);

        /**
         * Factory method for Iso8601_Rules.
         *
         * @param tokenLen a token indicating the length of the TimeZone String to be formatted.
         * @return a Iso8601_Rule that can format TimeZone String of length {@code tokenLen}. If no such
         * rule exists, an IllegalArgumentException will be thrown.
         */
        static Iso8601_Rule getRule(int tokenLen) {
            switch (tokenLen) {
                case 1:
                    return ISO8601_HOURS;
                case 2:
                    return ISO8601_HOURS_MINUTES;
                case 3:
                    return ISO8601_HOURS_COLON_MINUTES;
                default:
                    throw new IllegalArgumentException("invalid number of X");
            }
        }

        int length;

        /**
         * Constructs an instance of {@code Iso8601_Rule} with the specified properties.
         *
         * @param length The number of characters in output (unless Z is output)
         */
        Iso8601_Rule(int length) {
            this.length = length;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int estimateLength() {
            return length;
        }

        /**
         * {@inheritDoc}
         */
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
        private TimeZone mTimeZone;
        private int mStyle;
        private Locale mLocale;

        /**
         * Constructs an instance of {@code TimeZoneDisplayKey} with the specified properties.
         *
         * @param timeZone the time zone
         * @param daylight adjust the style for daylight saving time if {@code true}
         * @param style    the timezone style
         * @param locale   the timezone locale
         */
        TimeZoneDisplayKey(TimeZone timeZone,
                           boolean daylight, int style, Locale locale) {
            mTimeZone = timeZone;
            if (daylight) {
                mStyle = style | 0x80000000;
            } else {
                mStyle = style;
            }
            mLocale = locale;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            return (mStyle * 31 + mLocale.hashCode()) * 31 + mTimeZone.hashCode();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof TimeZoneDisplayKey) {
                TimeZoneDisplayKey other = (TimeZoneDisplayKey) obj;
                return
                        mTimeZone.equals(other.mTimeZone) &&
                                mStyle == other.mStyle &&
                                mLocale.equals(other.mLocale);
            }
            return false;
        }
    }
}
