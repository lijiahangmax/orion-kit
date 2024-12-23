/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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
package cn.orionsec.kit.lang.utils.time.cron;

import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Valid;
import cn.orionsec.kit.lang.utils.time.Dates;

import java.io.Serializable;
import java.util.*;

/**
 * copy with quartz
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/26 18:01
 */
public class Cron implements Serializable {

    private static final long serialVersionUID = -1403493223L;

    protected static final int SECOND = 0;
    protected static final int MINUTE = 1;
    protected static final int HOUR = 2;
    protected static final int DAY_OF_MONTH = 3;
    protected static final int MONTH = 4;
    protected static final int DAY_OF_WEEK = 5;
    protected static final int YEAR = 6;
    protected static final int ALL_SPEC_INT = 99; // aka '*'
    protected static final int NO_SPEC_INT = 98;  // aka '?'
    protected static final Integer ALL_SPEC = ALL_SPEC_INT;
    protected static final Integer NO_SPEC = NO_SPEC_INT;

    protected static final Map<String, Integer> MONTH_MAP = new HashMap<>(Const.CAPACITY_16);
    protected static final Map<String, Integer> WEEK_DAY_MAP = new HashMap<>(Const.CAPACITY_16);

    static {
        MONTH_MAP.put("JAN", 0);
        MONTH_MAP.put("FEB", 1);
        MONTH_MAP.put("MAR", 2);
        MONTH_MAP.put("APR", 3);
        MONTH_MAP.put("MAY", 4);
        MONTH_MAP.put("JUN", 5);
        MONTH_MAP.put("JUL", 6);
        MONTH_MAP.put("AUG", 7);
        MONTH_MAP.put("SEP", 8);
        MONTH_MAP.put("OCT", 9);
        MONTH_MAP.put("NOV", 10);
        MONTH_MAP.put("DEC", 11);

        WEEK_DAY_MAP.put("SUN", 1);
        WEEK_DAY_MAP.put("MON", 2);
        WEEK_DAY_MAP.put("TUE", 3);
        WEEK_DAY_MAP.put("WED", 4);
        WEEK_DAY_MAP.put("THU", 5);
        WEEK_DAY_MAP.put("FRI", 6);
        WEEK_DAY_MAP.put("SAT", 7);
    }

    private final String cronExpression;
    private TimeZone timeZone;

    protected transient TreeSet<Integer> seconds;
    protected transient TreeSet<Integer> minutes;
    protected transient TreeSet<Integer> hours;
    protected transient TreeSet<Integer> daysOfMonth;
    protected transient TreeSet<Integer> months;
    protected transient TreeSet<Integer> daysOfWeek;
    protected transient TreeSet<Integer> years;

    protected transient boolean lastDayOfWeek;
    protected transient int nthDayOfWeek;
    protected transient boolean lastDayOfMonth;
    protected transient boolean nearestWeekday;
    protected transient int lastDayOffset;
    protected transient boolean expressionParsed;

    public static final int MAX_YEAR = Calendar.getInstance().get(Calendar.YEAR) + 100;

    public Cron(String cronExpression) {
        Valid.notBlank(cronExpression, "cron expression not be empty");
        this.cronExpression = cronExpression.trim().toUpperCase(Locale.US);
        this.buildExpression(this.cronExpression);
    }

    public static Cron of(String cronExpression) {
        return new Cron(cronExpression);
    }

    /**
     * 表示给定的日期是否满足 cron 表达式 毫秒被忽略
     *
     * @param date date
     * @return 日期是否满足 cron 表达式
     */
    public boolean isSatisfiedBy(Date date) {
        Calendar testDateCal = Calendar.getInstance(this.getTimeZone());
        testDateCal.setTime(date);
        testDateCal.set(Calendar.MILLISECOND, 0);
        Date originalDate = testDateCal.getTime();
        testDateCal.add(Calendar.SECOND, -1);
        Date timeAfter = this.getTimeAfter(testDateCal.getTime());
        return ((timeAfter != null) && (timeAfter.equals(originalDate)));
    }

    /**
     * 返回满足 cron 表达式的给定时间的下一个时间
     * 如果未来不会执行 则返回null(准确)
     *
     * @param date 开始时间
     * @return 下一个时间
     */
    public Date getNextValidTimeAfter(Date date) {
        return this.getTimeAfter(date);
    }

    /**
     * 返回满足 cron 表达式的给定时间的下一个时间
     * 如果未来不会执行 则返回下一秒 不可能为null(不准确)
     *
     * @param date 开始时间
     * @return 下一个时间
     */
    public Date getNextInvalidTimeAfter(Date date) {
        long difference = 1000;
        // 移回最近的秒
        Calendar adjustCal = Calendar.getInstance(this.getTimeZone());
        adjustCal.setTime(date);
        adjustCal.set(Calendar.MILLISECOND, 0);
        Date lastDate = adjustCal.getTime();

        Date newDate;
        // 继续获取下一个包含时间, 直到它相隔超过一秒为止
        while (difference == 1000) {
            newDate = this.getTimeAfter(lastDate);
            if (newDate == null) {
                break;
            }
            difference = newDate.getTime() - lastDate.getTime();
            if (difference == 1000) {
                lastDate = newDate;
            }
        }
        return new Date(lastDate.getTime() + 1000);
    }

    public TimeZone getTimeZone() {
        if (timeZone == null) {
            this.timeZone = TimeZone.getDefault();
        }
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    /**
     * 获取表达式摘要
     *
     * @return 摘要
     */
    public String getExpressionSummary() {
        return new StringBuilder()
                .append("seconds: ")
                .append(this.getExpressionSetSummary(seconds))
                .append("\n")
                .append("minutes: ")
                .append(this.getExpressionSetSummary(minutes))
                .append("\n")
                .append("hours: ")
                .append(this.getExpressionSetSummary(hours))
                .append("\n")
                .append("daysOfMonth: ")
                .append(this.getExpressionSetSummary(daysOfMonth))
                .append("\n")
                .append("months: ")
                .append(this.getExpressionSetSummary(months))
                .append("\n")
                .append("daysOfWeek: ")
                .append(this.getExpressionSetSummary(daysOfWeek))
                .append("\n")
                .append("lastDayOfWeek: ")
                .append(lastDayOfWeek)
                .append("\n")
                .append("nearestWeekday: ")
                .append(nearestWeekday)
                .append("\n")
                .append("nthDayOfWeek: ")
                .append(nthDayOfWeek)
                .append("\n")
                .append("lastDayOfMonth: ")
                .append(lastDayOfMonth)
                .append("\n")
                .append("years: ")
                .append(this.getExpressionSetSummary(years))
                .append("\n")
                .toString();
    }

    @Override
    public String toString() {
        return cronExpression;
    }

    // -------------------- parse --------------------

    /**
     * 构建表达式
     *
     * @param expression expression
     */
    protected void buildExpression(String expression) {
        this.expressionParsed = true;

        try {
            if (seconds == null) {
                this.seconds = new TreeSet<>();
            }
            if (minutes == null) {
                this.minutes = new TreeSet<>();
            }
            if (hours == null) {
                this.hours = new TreeSet<>();
            }
            if (daysOfMonth == null) {
                this.daysOfMonth = new TreeSet<>();
            }
            if (months == null) {
                this.months = new TreeSet<>();
            }
            if (daysOfWeek == null) {
                this.daysOfWeek = new TreeSet<>();
            }
            if (years == null) {
                this.years = new TreeSet<>();
            }
            int exprOn = SECOND;

            StringTokenizer exprTokens = new StringTokenizer(expression, " \t", false);
            while (exprTokens.hasMoreTokens() && exprOn <= YEAR) {
                String expr = exprTokens.nextToken().trim();
                // throw an exception if L is used with other days of the month
                if (exprOn == DAY_OF_MONTH && expr.indexOf('L') != -1 && expr.length() > 1 && expr.contains(",")) {
                    throw Exceptions.parseCron("support for specifying 'L' and 'LW' with other days of the month is not implemented");
                }
                // throw an exception if L is used with other days of the week
                if (exprOn == DAY_OF_WEEK && expr.indexOf('L') != -1 && expr.length() > 1 && expr.contains(",")) {
                    throw Exceptions.parseCron("support for specifying 'L' with other days of the week is not implemented");
                }
                if (exprOn == DAY_OF_WEEK && expr.indexOf('#') != -1 && expr.indexOf('#', expr.indexOf('#') + 1) != -1) {
                    throw Exceptions.parseCron("support for specifying multiple \"nth\" days is not implemented.");
                }

                StringTokenizer vTok = new StringTokenizer(expr, Const.COMMA);
                while (vTok.hasMoreTokens()) {
                    String v = vTok.nextToken();
                    this.storeExpressionVal(0, v, exprOn);
                }
                exprOn++;
            }

            if (exprOn <= DAY_OF_WEEK) {
                throw Exceptions.parseCron("unexpected end of expression.", expression.length());
            }

            if (exprOn <= YEAR) {
                this.storeExpressionVal(0, "*", YEAR);
            }

            TreeSet<Integer> dow = this.getSet(DAY_OF_WEEK);
            TreeSet<Integer> dom = this.getSet(DAY_OF_MONTH);

            boolean dayOfMSpec = !dom.contains(NO_SPEC);
            boolean dayOfWSpec = !dow.contains(NO_SPEC);

            if (!dayOfMSpec || dayOfWSpec) {
                if (!dayOfWSpec || dayOfMSpec) {
                    throw Exceptions.parseCron("support for specifying both a day of week AND a day-of-month parameter is not implemented.", 0);
                }
            }
        } catch (Exception e) {
            throw Exceptions.parseCron("illegal cron expression format (" + e.toString() + ")", 0);
        }
    }

    protected int storeExpressionVal(int pos, String s, int type) {
        int incr = 0;
        int i = this.skipWhiteSpace(pos, s);
        if (i >= s.length()) {
            return i;
        }
        char c = s.charAt(i);
        if ((c >= 'A') && (c <= 'Z') && (!s.equals("L")) && (!s.equals("LW")) && (!s.matches("^L-[0-9]*[W]?"))) {
            String sub = s.substring(i, i + 3);
            int sval = -1;
            int eval = -1;
            if (type == MONTH) {
                sval = this.getMonthNumber(sub) + 1;
                if (sval <= 0) {
                    throw Exceptions.parseCron("invalid month value: '" + sub + "'", i);
                }
                if (s.length() > i + 3) {
                    c = s.charAt(i + 3);
                    if (c == '-') {
                        i += 4;
                        sub = s.substring(i, i + 3);
                        eval = this.getMonthNumber(sub) + 1;
                        if (eval <= 0) {
                            throw Exceptions.parseCron("invalid month value: '" + sub + "'", i);
                        }
                    }
                }
            } else if (type == DAY_OF_WEEK) {
                sval = this.getDayOfWeekNumber(sub);
                if (sval < 0) {
                    throw Exceptions.parseCron("invalid day of week value: '" + sub + "'", i);
                }
                if (s.length() > i + 3) {
                    c = s.charAt(i + 3);
                    if (c == '-') {
                        i += 4;
                        sub = s.substring(i, i + 3);
                        eval = this.getDayOfWeekNumber(sub);
                        if (eval < 0) {
                            throw Exceptions.parseCron("invalid day of week value: '" + sub + "'", i);
                        }
                    } else if (c == '#') {
                        try {
                            i += 4;
                            this.nthDayOfWeek = Integer.parseInt(s.substring(i));
                            if (nthDayOfWeek < 1 || nthDayOfWeek > 5) {
                                throw Exceptions.runtime();
                            }
                        } catch (Exception e) {
                            throw Exceptions.parseCron("A numeric value between 1 and 5 must follow the '#' option", i);
                        }
                    } else if (c == 'L') {
                        this.lastDayOfWeek = true;
                        i++;
                    }
                }
            } else {
                throw Exceptions.parseCron("illegal characters for this position: '" + sub + "'", i);
            }
            if (eval != -1) {
                incr = 1;
            }
            this.addToSet(sval, eval, incr, type);
            return (i + 3);
        }
        if (c == '?') {
            i++;
            if ((i + 1) < s.length() && (s.charAt(i) != ' ' && s.charAt(i + 1) != '\t')) {
                throw Exceptions.parseCron("Illegal character after '?': " + s.charAt(i), i);
            }
            if (type != DAY_OF_WEEK && type != DAY_OF_MONTH) {
                throw Exceptions.parseCron("'?' can only be specified for Day-of-Month or day of week.", i);
            }
            if (type == DAY_OF_WEEK && !lastDayOfMonth) {
                int val = daysOfMonth.last();
                if (val == NO_SPEC_INT) {
                    throw Exceptions.parseCron("'?' can only be specified for Day-of-Month -OR- day of week.", i);
                }
            }
            this.addToSet(NO_SPEC_INT, -1, 0, type);
            return i;
        }

        if (c == '*' || c == '/') {
            if (c == '*' && (i + 1) >= s.length()) {
                this.addToSet(ALL_SPEC_INT, -1, incr, type);
                return i + 1;
            } else if (c == '/' && ((i + 1) >= s.length() || s.charAt(i + 1) == ' ' || s.charAt(i + 1) == '\t')) {
                throw Exceptions.parseCron("'/' must be followed by an integer.", i);
            } else if (c == '*') {
                i++;
            }
            c = s.charAt(i);
            if (c == '/') {
                i++;
                if (i >= s.length()) {
                    throw Exceptions.parseCron("unexpected end of string.", i);
                }
                incr = this.getNumericValue(s, i);
                i++;
                if (incr > 10) {
                    i++;
                }
                this.checkIncrementRange(incr, type, i);
            } else {
                incr = 1;
            }
            this.addToSet(ALL_SPEC_INT, -1, incr, type);
            return i;
        } else if (c == 'L') {
            i++;
            if (type == DAY_OF_MONTH) {
                this.lastDayOfMonth = true;
            }
            if (type == DAY_OF_WEEK) {
                this.addToSet(7, 7, 0, type);
            }
            if (type == DAY_OF_MONTH && s.length() > i) {
                c = s.charAt(i);
                if (c == '-') {
                    ValueSet vs = this.getValue(0, s, i + 1);
                    this.lastDayOffset = vs.value;
                    if (lastDayOffset > 30) throw Exceptions.parseCron("Offset from last day must be <= 30", i + 1);
                    i = vs.pos;
                }
                if (s.length() > i) {
                    c = s.charAt(i);
                    if (c == 'W') {
                        this.nearestWeekday = true;
                        i++;
                    }
                }
            }
            return i;
        } else if (c >= '0' && c <= '9') {
            int val = Integer.parseInt(String.valueOf(c));
            i++;
            if (i >= s.length()) {
                this.addToSet(val, -1, -1, type);
            } else {
                c = s.charAt(i);
                if (c >= '0' && c <= '9') {
                    ValueSet vs = getValue(val, s, i);
                    val = vs.value;
                    i = vs.pos;
                }
                i = this.checkNext(i, s, val, type);
                return i;
            }
        } else {
            throw Exceptions.parseCron("unexpected character: " + c, i);
        }
        return i;
    }

    /**
     * 检查表达式区间
     *
     * @param incr   value
     * @param type   type
     * @param idxPos pos
     */
    private void checkIncrementRange(int incr, int type, int idxPos) {
        if (incr > 59 && (type == SECOND || type == MINUTE)) {
            throw Exceptions.parseCron("increment > 60 : " + incr, idxPos);
        } else if (incr > 23 && (type == HOUR)) {
            throw Exceptions.parseCron("increment > 24 : " + incr, idxPos);
        } else if (incr > 31 && (type == DAY_OF_MONTH)) {
            throw Exceptions.parseCron("increment > 31 : " + incr, idxPos);
        } else if (incr > 7 && (type == DAY_OF_WEEK)) {
            throw Exceptions.parseCron("increment > 7 : " + incr, idxPos);
        } else if (incr > 12 && (type == MONTH)) {
            throw Exceptions.parseCron("increment > 12 : " + incr, idxPos);
        }
    }

    protected int checkNext(int pos, String s, int val, int type) {
        int end = -1;
        int i = pos;
        if (i >= s.length()) {
            this.addToSet(val, end, -1, type);
            return i;
        }
        char c = s.charAt(pos);
        if (c == 'L') {
            if (type == DAY_OF_WEEK) {
                if (val < 1 || val > 7) {
                    throw Exceptions.parseCron("day of week values must be between 1 and 7", -1);
                }
                this.lastDayOfWeek = true;
            } else {
                throw Exceptions.parseCron("'L' option is not valid here. (pos=" + i + ")", i);
            }
            TreeSet<Integer> set = this.getSet(type);
            set.add(val);
            i++;
            return i;
        }

        if (c == 'W') {
            if (type == DAY_OF_MONTH) {
                this.nearestWeekday = true;
            } else {
                throw Exceptions.parseCron("'W' option is not valid here. (pos=" + i + ")", i);
            }
            if (val > 31)
                throw Exceptions.parseCron("'W' option does not make sense with values larger than 31 (max number of days in a month)", i);
            TreeSet<Integer> set = this.getSet(type);
            set.add(val);
            i++;
            return i;
        }

        if (c == '#') {
            if (type != DAY_OF_WEEK) {
                throw Exceptions.parseCron("'#' option is not valid here. (pos=" + i + ")", i);
            }
            i++;
            try {
                this.nthDayOfWeek = Integer.parseInt(s.substring(i));
                if (nthDayOfWeek < 1 || nthDayOfWeek > 5) {
                    throw Exceptions.runtime();
                }
            } catch (Exception e) {
                throw Exceptions.parseCron("a numeric value between 1 and 5 must follow the '#' option", i);
            }
            TreeSet<Integer> set = getSet(type);
            set.add(val);
            i++;
            return i;
        }

        if (c == '-') {
            i++;
            c = s.charAt(i);
            int v = Integer.parseInt(String.valueOf(c));
            end = v;
            i++;
            if (i >= s.length()) {
                this.addToSet(val, end, 1, type);
                return i;
            }
            c = s.charAt(i);
            if (c >= '0' && c <= '9') {
                ValueSet vs = this.getValue(v, s, i);
                end = vs.value;
                i = vs.pos;
            }
            if (i < s.length() && ((c = s.charAt(i)) == '/')) {
                i++;
                c = s.charAt(i);
                int v2 = Integer.parseInt(String.valueOf(c));
                i++;
                if (i >= s.length()) {
                    this.addToSet(val, end, v2, type);
                    return i;
                }
                c = s.charAt(i);
                if (c >= '0' && c <= '9') {
                    ValueSet vs = this.getValue(v2, s, i);
                    int v3 = vs.value;
                    this.addToSet(val, end, v3, type);
                    i = vs.pos;
                    return i;
                } else {
                    this.addToSet(val, end, v2, type);
                    return i;
                }
            } else {
                this.addToSet(val, end, 1, type);
                return i;
            }
        }

        if (c == '/') {
            if ((i + 1) >= s.length() || s.charAt(i + 1) == ' ' || s.charAt(i + 1) == '\t') {
                throw Exceptions.parseCron("'/' must be followed by an integer.", i);
            }
            i++;
            c = s.charAt(i);
            int v2 = Integer.parseInt(String.valueOf(c));
            i++;
            if (i >= s.length()) {
                this.checkIncrementRange(v2, type, i);
                this.addToSet(val, end, v2, type);
                return i;
            }
            c = s.charAt(i);
            if (c >= '0' && c <= '9') {
                ValueSet vs = getValue(v2, s, i);
                int v3 = vs.value;
                this.checkIncrementRange(v3, type, i);
                this.addToSet(val, end, v3, type);
                i = vs.pos;
                return i;
            } else {
                throw Exceptions.parseCron("unexpected character '" + c + "' after '/'", i);
            }
        }
        this.addToSet(val, end, 0, type);
        i++;
        return i;
    }

    protected String getExpressionSetSummary(Set<Integer> set) {
        if (set.contains(NO_SPEC)) {
            return "?";
        }
        if (set.contains(ALL_SPEC)) {
            return "*";
        }
        StringBuilder buf = new StringBuilder();
        Iterator<Integer> itr = set.iterator();
        boolean first = true;
        while (itr.hasNext()) {
            Integer iVal = itr.next();
            String val = iVal.toString();
            if (!first) {
                buf.append(Const.COMMA);
            }
            buf.append(val);
            first = false;
        }
        return buf.toString();
    }

    protected String getExpressionSetSummary(List<Integer> list) {
        if (list.contains(NO_SPEC)) {
            return "?";
        }
        if (list.contains(ALL_SPEC)) {
            return "*";
        }
        StringBuilder buf = new StringBuilder();
        Iterator<Integer> itr = list.iterator();
        boolean first = true;
        while (itr.hasNext()) {
            Integer iVal = itr.next();
            String val = iVal.toString();
            if (!first) {
                buf.append(Const.COMMA);
            }
            buf.append(val);
            first = false;
        }
        return buf.toString();
    }

    protected int skipWhiteSpace(int i, String s) {
        for (; i < s.length() && (s.charAt(i) == ' ' || s.charAt(i) == '\t'); i++) {
        }
        return i;
    }

    protected int findNextWhiteSpace(int i, String s) {
        for (; i < s.length() && (s.charAt(i) != ' ' || s.charAt(i) != '\t'); i++) {
        }
        return i;
    }

    protected void addToSet(int val, int end, int incr, int type) {
        TreeSet<Integer> set = this.getSet(type);

        if (type == SECOND || type == MINUTE) {
            if ((val < 0 || val > 59 || end > 59) && (val != ALL_SPEC_INT)) {
                throw Exceptions.parseCron("minute and second values must be between 0 and 59", -1);
            }
        } else if (type == HOUR) {
            if ((val < 0 || val > 23 || end > 23) && (val != ALL_SPEC_INT)) {
                throw Exceptions.parseCron("hour values must be between 0 and 23", -1);
            }
        } else if (type == DAY_OF_MONTH) {
            if ((val < 1 || val > 31 || end > 31) && (val != ALL_SPEC_INT)
                    && (val != NO_SPEC_INT)) {
                throw Exceptions.parseCron("day of month values must be between 1 and 31", -1);
            }
        } else if (type == MONTH) {
            if ((val < 1 || val > 12 || end > 12) && (val != ALL_SPEC_INT)) {
                throw Exceptions.parseCron("month values must be between 1 and 12", -1);
            }
        } else if (type == DAY_OF_WEEK) {
            if ((val == 0 || val > 7 || end > 7) && (val != ALL_SPEC_INT) && (val != NO_SPEC_INT)) {
                throw Exceptions.parseCron("day of week values must be between 1 and 7", -1);
            }
        }

        if ((incr == 0 || incr == -1) && val != ALL_SPEC_INT) {
            if (val != -1) {
                set.add(val);
            } else {
                set.add(NO_SPEC);
            }
            return;
        }

        int startAt = val;
        int stopAt = end;

        if (val == ALL_SPEC_INT && incr <= 0) {
            incr = 1;
            set.add(ALL_SPEC); // put in a marker, but also fill values
        }

        if (type == SECOND || type == MINUTE) {
            if (stopAt == -1) {
                stopAt = 59;
            }
            if (startAt == -1 || startAt == ALL_SPEC_INT) {
                startAt = 0;
            }
        } else if (type == HOUR) {
            if (stopAt == -1) {
                stopAt = 23;
            }
            if (startAt == -1 || startAt == ALL_SPEC_INT) {
                startAt = 0;
            }
        } else if (type == DAY_OF_MONTH) {
            if (stopAt == -1) {
                stopAt = 31;
            }
            if (startAt == -1 || startAt == ALL_SPEC_INT) {
                startAt = 1;
            }
        } else if (type == MONTH) {
            if (stopAt == -1) {
                stopAt = 12;
            }
            if (startAt == -1 || startAt == ALL_SPEC_INT) {
                startAt = 1;
            }
        } else if (type == DAY_OF_WEEK) {
            if (stopAt == -1) {
                stopAt = 7;
            }
            if (startAt == -1 || startAt == ALL_SPEC_INT) {
                startAt = 1;
            }
        } else if (type == YEAR) {
            if (stopAt == -1) {
                stopAt = MAX_YEAR;
            }
            if (startAt == -1 || startAt == ALL_SPEC_INT) {
                startAt = 1970;
            }
        }

        // if the end of the range is before the start, then we need to overflow into
        // the next day, month etc. This is done by adding the maximum amount for that
        // type, and using modulus max to determine the value being added.
        int max = -1;
        if (stopAt < startAt) {
            switch (type) {
                case SECOND:
                    max = 60;
                    break;
                case MINUTE:
                    max = 60;
                    break;
                case HOUR:
                    max = 24;
                    break;
                case MONTH:
                    max = 12;
                    break;
                case DAY_OF_WEEK:
                    max = 7;
                    break;
                case DAY_OF_MONTH:
                    max = 31;
                    break;
                case YEAR:
                    throw Exceptions.argument("start year must be less than stop year");
                default:
                    throw Exceptions.argument("unexpected type encountered");
            }
            stopAt += max;
        }

        for (int i = startAt; i <= stopAt; i += incr) {
            if (max == -1) {
                // ie: there's no max to overflow over
                set.add(i);
            } else {
                // take the modulus to get the real value
                int i2 = i % max;
                // 1-indexed ranges should not include 0, and should include their max
                if (i2 == 0 && (type == MONTH || type == DAY_OF_WEEK || type == DAY_OF_MONTH)) {
                    i2 = max;
                }
                set.add(i2);
            }
        }
    }

    TreeSet<Integer> getSet(int type) {
        switch (type) {
            case SECOND:
                return seconds;
            case MINUTE:
                return minutes;
            case HOUR:
                return hours;
            case DAY_OF_MONTH:
                return daysOfMonth;
            case MONTH:
                return months;
            case DAY_OF_WEEK:
                return daysOfWeek;
            case YEAR:
                return years;
            default:
                return null;
        }
    }

    protected ValueSet getValue(int v, String s, int i) {
        char c = s.charAt(i);
        StringBuilder s1 = new StringBuilder(String.valueOf(v));
        while (c >= '0' && c <= '9') {
            s1.append(c);
            i++;
            if (i >= s.length()) {
                break;
            }
            c = s.charAt(i);
        }
        ValueSet val = new ValueSet();
        val.pos = (i < s.length()) ? i : i + 1;
        val.value = Integer.parseInt(s1.toString());
        return val;
    }

    protected int getNumericValue(String s, int i) {
        int endOfVal = findNextWhiteSpace(i, s);
        String val = s.substring(i, endOfVal);
        return Integer.parseInt(val);
    }

    protected int getMonthNumber(String s) {
        Integer integer = MONTH_MAP.get(s);
        if (integer == null) {
            return -1;
        }
        return integer;
    }

    protected int getDayOfWeekNumber(String s) {
        Integer integer = WEEK_DAY_MAP.get(s);
        if (integer == null) {
            return -1;
        }
        return integer;
    }

    // -------------------- compute --------------------

    public Date getTimeAfter(Date afterTime) {
        // Computation is based on Gregorian year only.
        Calendar cl = new GregorianCalendar(this.getTimeZone());

        // move ahead one second, since we're computing the time *after* the
        // given time
        afterTime = new Date(afterTime.getTime() + 1000);
        // CronTrigger does not deal with milliseconds
        cl.setTime(afterTime);
        cl.set(Calendar.MILLISECOND, 0);

        boolean gotOne = false;
        // loop until we've computed the next time, or we've past the endTime
        while (!gotOne) {
            //if (endTime != null && cl.getTime().after(endTime)) return null;
            if (cl.get(Calendar.YEAR) > 2999) { // prevent endless loop...
                return null;
            }
            SortedSet<Integer> st;
            int t = 0;

            int sec = cl.get(Calendar.SECOND);
            int min = cl.get(Calendar.MINUTE);

            // get second
            st = seconds.tailSet(sec);
            if (st.size() != 0) {
                sec = st.first();
            } else {
                sec = seconds.first();
                min++;
                cl.set(Calendar.MINUTE, min);
            }
            cl.set(Calendar.SECOND, sec);

            min = cl.get(Calendar.MINUTE);
            int hr = cl.get(Calendar.HOUR_OF_DAY);
            t = -1;

            // get minute
            st = minutes.tailSet(min);
            if (st.size() != 0) {
                t = min;
                min = st.first();
            } else {
                min = minutes.first();
                hr++;
            }
            if (min != t) {
                cl.set(Calendar.SECOND, 0);
                cl.set(Calendar.MINUTE, min);
                setCalendarHour(cl, hr);
                continue;
            }
            cl.set(Calendar.MINUTE, min);

            hr = cl.get(Calendar.HOUR_OF_DAY);
            int day = cl.get(Calendar.DAY_OF_MONTH);
            t = -1;

            // get hour
            st = hours.tailSet(hr);
            if (st.size() != 0) {
                t = hr;
                hr = st.first();
            } else {
                hr = hours.first();
                day++;
            }
            if (hr != t) {
                cl.set(Calendar.SECOND, 0);
                cl.set(Calendar.MINUTE, 0);
                cl.set(Calendar.DAY_OF_MONTH, day);
                setCalendarHour(cl, hr);
                continue;
            }
            cl.set(Calendar.HOUR_OF_DAY, hr);

            day = cl.get(Calendar.DAY_OF_MONTH);
            int mon = cl.get(Calendar.MONTH) + 1;
            // '+ 1' because calendar is 0-based for this field, and we are
            // 1-based
            t = -1;
            int tmon = mon;

            // get day
            boolean dayOfMSpec = !daysOfMonth.contains(NO_SPEC);
            boolean dayOfWSpec = !daysOfWeek.contains(NO_SPEC);
            if (dayOfMSpec && !dayOfWSpec) { // get day by day of month rule
                st = daysOfMonth.tailSet(day);
                if (lastDayOfMonth) {
                    if (!nearestWeekday) {
                        t = day;
                        day = getLastDayOfMonth(mon, cl.get(Calendar.YEAR));
                        day -= lastDayOffset;
                        if (t > day) {
                            mon++;
                            if (mon > 12) {
                                mon = 1;
                                tmon = 3333; // ensure test of mon != tmon further below fails
                                cl.add(Calendar.YEAR, 1);
                            }
                            day = 1;
                        }
                    } else {
                        t = day;
                        day = getLastDayOfMonth(mon, cl.get(Calendar.YEAR));
                        day -= lastDayOffset;
                        Calendar tcal = Calendar.getInstance(getTimeZone());
                        tcal.set(Calendar.SECOND, 0);
                        tcal.set(Calendar.MINUTE, 0);
                        tcal.set(Calendar.HOUR_OF_DAY, 0);
                        tcal.set(Calendar.DAY_OF_MONTH, day);
                        tcal.set(Calendar.MONTH, mon - 1);
                        tcal.set(Calendar.YEAR, cl.get(Calendar.YEAR));

                        int ldom = getLastDayOfMonth(mon, cl.get(Calendar.YEAR));
                        int dow = tcal.get(Calendar.DAY_OF_WEEK);
                        if (dow == Calendar.SATURDAY && day == 1) {
                            day += 2;
                        } else if (dow == Calendar.SATURDAY) {
                            day -= 1;
                        } else if (dow == Calendar.SUNDAY && day == ldom) {
                            day -= 2;
                        } else if (dow == Calendar.SUNDAY) {
                            day += 1;
                        }

                        tcal.set(Calendar.SECOND, sec);
                        tcal.set(Calendar.MINUTE, min);
                        tcal.set(Calendar.HOUR_OF_DAY, hr);
                        tcal.set(Calendar.DAY_OF_MONTH, day);
                        tcal.set(Calendar.MONTH, mon - 1);
                        Date nTime = tcal.getTime();
                        if (nTime.before(afterTime)) {
                            day = 1;
                            mon++;
                        }
                    }
                } else if (nearestWeekday) {
                    t = day;
                    day = daysOfMonth.first();

                    Calendar tcal = Calendar.getInstance(getTimeZone());
                    tcal.set(Calendar.SECOND, 0);
                    tcal.set(Calendar.MINUTE, 0);
                    tcal.set(Calendar.HOUR_OF_DAY, 0);
                    tcal.set(Calendar.DAY_OF_MONTH, day);
                    tcal.set(Calendar.MONTH, mon - 1);
                    tcal.set(Calendar.YEAR, cl.get(Calendar.YEAR));

                    int ldom = getLastDayOfMonth(mon, cl.get(Calendar.YEAR));
                    int dow = tcal.get(Calendar.DAY_OF_WEEK);

                    if (dow == Calendar.SATURDAY && day == 1) {
                        day += 2;
                    } else if (dow == Calendar.SATURDAY) {
                        day -= 1;
                    } else if (dow == Calendar.SUNDAY && day == ldom) {
                        day -= 2;
                    } else if (dow == Calendar.SUNDAY) {
                        day += 1;
                    }

                    tcal.set(Calendar.SECOND, sec);
                    tcal.set(Calendar.MINUTE, min);
                    tcal.set(Calendar.HOUR_OF_DAY, hr);
                    tcal.set(Calendar.DAY_OF_MONTH, day);
                    tcal.set(Calendar.MONTH, mon - 1);
                    Date nTime = tcal.getTime();
                    if (nTime.before(afterTime)) {
                        day = daysOfMonth.first();
                        mon++;
                    }
                } else if (st.size() != 0) {
                    t = day;
                    day = st.first();
                    // make sure we don't over-run a short month, such as february
                    int lastDay = getLastDayOfMonth(mon, cl.get(Calendar.YEAR));
                    if (day > lastDay) {
                        day = daysOfMonth.first();
                        mon++;
                    }
                } else {
                    day = daysOfMonth.first();
                    mon++;
                }

                if (day != t || mon != tmon) {
                    cl.set(Calendar.SECOND, 0);
                    cl.set(Calendar.MINUTE, 0);
                    cl.set(Calendar.HOUR_OF_DAY, 0);
                    cl.set(Calendar.DAY_OF_MONTH, day);
                    cl.set(Calendar.MONTH, mon - 1);
                    // '- 1' because calendar is 0-based for this field, and we
                    // are 1-based
                    continue;
                }
            } else if (dayOfWSpec && !dayOfMSpec) { // get day by day of week rule
                if (lastDayOfWeek) { // are we looking for the last XXX day of
                    // the month?
                    int dow = daysOfWeek.first(); // desired
                    // d-o-w
                    int cDow = cl.get(Calendar.DAY_OF_WEEK); // current d-o-w
                    int daysToAdd = 0;
                    if (cDow < dow) {
                        daysToAdd = dow - cDow;
                    }
                    if (cDow > dow) {
                        daysToAdd = dow + (7 - cDow);
                    }

                    int lDay = getLastDayOfMonth(mon, cl.get(Calendar.YEAR));

                    if (day + daysToAdd > lDay) { // did we already miss the
                        // last one?
                        cl.set(Calendar.SECOND, 0);
                        cl.set(Calendar.MINUTE, 0);
                        cl.set(Calendar.HOUR_OF_DAY, 0);
                        cl.set(Calendar.DAY_OF_MONTH, 1);
                        cl.set(Calendar.MONTH, mon);
                        // no '- 1' here because we are promoting the month
                        continue;
                    }

                    // find date of last occurrence of this day in this month...
                    while ((day + daysToAdd + 7) <= lDay) {
                        daysToAdd += 7;
                    }

                    day += daysToAdd;
                    if (daysToAdd > 0) {
                        cl.set(Calendar.SECOND, 0);
                        cl.set(Calendar.MINUTE, 0);
                        cl.set(Calendar.HOUR_OF_DAY, 0);
                        cl.set(Calendar.DAY_OF_MONTH, day);
                        cl.set(Calendar.MONTH, mon - 1);
                        // '- 1' here because we are not promoting the month
                        continue;
                    }

                } else if (nthDayOfWeek != 0) {
                    // are we looking for the Nth XXX day in the month?
                    int dow = daysOfWeek.first(); // desired
                    // d-o-w
                    int cDow = cl.get(Calendar.DAY_OF_WEEK); // current d-o-w
                    int daysToAdd = 0;
                    if (cDow < dow) {
                        daysToAdd = dow - cDow;
                    } else if (cDow > dow) {
                        daysToAdd = dow + (7 - cDow);
                    }

                    boolean dayShifted = false;
                    if (daysToAdd > 0) {
                        dayShifted = true;
                    }

                    day += daysToAdd;
                    int weekOfMonth = day / 7;
                    if (day % 7 > 0) {
                        weekOfMonth++;
                    }

                    daysToAdd = (nthDayOfWeek - weekOfMonth) * 7;
                    day += daysToAdd;
                    if (daysToAdd < 0
                            || day > getLastDayOfMonth(mon, cl
                            .get(Calendar.YEAR))) {
                        cl.set(Calendar.SECOND, 0);
                        cl.set(Calendar.MINUTE, 0);
                        cl.set(Calendar.HOUR_OF_DAY, 0);
                        cl.set(Calendar.DAY_OF_MONTH, 1);
                        cl.set(Calendar.MONTH, mon);
                        // no '- 1' here because we are promoting the month
                        continue;
                    } else if (daysToAdd > 0 || dayShifted) {
                        cl.set(Calendar.SECOND, 0);
                        cl.set(Calendar.MINUTE, 0);
                        cl.set(Calendar.HOUR_OF_DAY, 0);
                        cl.set(Calendar.DAY_OF_MONTH, day);
                        cl.set(Calendar.MONTH, mon - 1);
                        // '- 1' here because we are NOT promoting the month
                        continue;
                    }
                } else {
                    int cDow = cl.get(Calendar.DAY_OF_WEEK); // current d-o-w
                    int dow = daysOfWeek.first(); // desired
                    // d-o-w
                    st = daysOfWeek.tailSet(cDow);
                    if (st.size() > 0) {
                        dow = st.first();
                    }
                    int daysToAdd = 0;
                    if (cDow < dow) {
                        daysToAdd = dow - cDow;
                    }
                    if (cDow > dow) {
                        daysToAdd = dow + (7 - cDow);
                    }
                    int lDay = getLastDayOfMonth(mon, cl.get(Calendar.YEAR));
                    if (day + daysToAdd > lDay) { // will we pass the end of
                        // the month?
                        cl.set(Calendar.SECOND, 0);
                        cl.set(Calendar.MINUTE, 0);
                        cl.set(Calendar.HOUR_OF_DAY, 0);
                        cl.set(Calendar.DAY_OF_MONTH, 1);
                        cl.set(Calendar.MONTH, mon);
                        // no '- 1' here because we are promoting the month
                        continue;
                    } else if (daysToAdd > 0) { // are we swithing days?
                        cl.set(Calendar.SECOND, 0);
                        cl.set(Calendar.MINUTE, 0);
                        cl.set(Calendar.HOUR_OF_DAY, 0);
                        cl.set(Calendar.DAY_OF_MONTH, day + daysToAdd);
                        cl.set(Calendar.MONTH, mon - 1);
                        // '- 1' because calendar is 0-based for this field,
                        // and we are 1-based
                        continue;
                    }
                }
            } else {
                // dayOfWSpec && !dayOfMSpec
                throw Exceptions.unsupported("support for specifying both a day of week AND a day-of-month parameter is not implemented.");
            }
            cl.set(Calendar.DAY_OF_MONTH, day);

            mon = cl.get(Calendar.MONTH) + 1;
            // '+ 1' because calendar is 0-based for this field, and we are
            // 1-based
            int year = cl.get(Calendar.YEAR);
            t = -1;

            // test for expressions that never generate a valid fire date,
            // but keep looping...
            if (year > MAX_YEAR) {
                return null;
            }

            // get month
            st = months.tailSet(mon);
            if (st.size() != 0) {
                t = mon;
                mon = st.first();
            } else {
                mon = months.first();
                year++;
            }
            if (mon != t) {
                cl.set(Calendar.SECOND, 0);
                cl.set(Calendar.MINUTE, 0);
                cl.set(Calendar.HOUR_OF_DAY, 0);
                cl.set(Calendar.DAY_OF_MONTH, 1);
                cl.set(Calendar.MONTH, mon - 1);
                // '- 1' because calendar is 0-based for this field, and we are
                // 1-based
                cl.set(Calendar.YEAR, year);
                continue;
            }
            cl.set(Calendar.MONTH, mon - 1);
            // '- 1' because calendar is 0-based for this field, and we are
            // 1-based

            year = cl.get(Calendar.YEAR);
            t = -1;

            // get year...................................................
            st = years.tailSet(year);
            if (st.size() != 0) {
                t = year;
                year = st.first();
            } else {
                return null; // ran out of years...
            }

            if (year != t) {
                cl.set(Calendar.SECOND, 0);
                cl.set(Calendar.MINUTE, 0);
                cl.set(Calendar.HOUR_OF_DAY, 0);
                cl.set(Calendar.DAY_OF_MONTH, 1);
                cl.set(Calendar.MONTH, 0);
                // '- 1' because calendar is 0-based for this field, and we are
                // 1-based
                cl.set(Calendar.YEAR, year);
                continue;
            }
            cl.set(Calendar.YEAR, year);
            gotOne = true;
        } // while( !done )
        return cl.getTime();
    }

    /**
     * 设置日历hour
     *
     * @param cal  calendar
     * @param hour hour
     */
    protected void setCalendarHour(Calendar cal, int hour) {
        cal.set(Calendar.HOUR_OF_DAY, hour);
        if (cal.get(Calendar.HOUR_OF_DAY) != hour && hour != 24) {
            cal.set(Calendar.HOUR_OF_DAY, hour + 1);
        }
    }

    protected int getLastDayOfMonth(int monthNum, int year) {
        return Dates.getMonthLastDay(year, monthNum);
    }

    static class ValueSet {

        public int value;

        public int pos;

    }

}

