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
package cn.orionsec.kit.lang.utils.time.ago;

import cn.orionsec.kit.lang.utils.time.Dates;

import java.util.Calendar;
import java.util.Date;

/**
 * 获取时间前后 通过对比相差时间戳实现 是实际差异时间
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/27 18:36
 */
public class DateAgo extends Ago {

    /**
     * 是否使用模糊时间 如: 昨天/23小时前
     */
    private boolean vague = true;

    /**
     * 是否使用周 如: 1周前/8天前
     */
    private boolean useWeek;

    public DateAgo(Date target) {
        super(target);
    }

    public DateAgo(Date source, Date target) {
        super(source, target);
    }

    public DateAgo(Date target, DateAgoHint hint) {
        super(target, hint);
    }

    public DateAgo(Date source, Date target, DateAgoHint hint) {
        super(source, target, hint);
    }

    public static DateAgo of(Date target) {
        return new DateAgo(target);
    }

    public static DateAgo of(Date source, Date target) {
        return new DateAgo(source, target);
    }

    /**
     * 设置使用模糊时间
     *
     * @param vague true: 昨天 false: 23小时前
     * @return this
     */
    public DateAgo vague(boolean vague) {
        this.vague = vague;
        return this;
    }

    /**
     * 设置是否使用周
     *
     * @param useWeek true: 1周前 false: 8天前
     * @return this
     */
    public DateAgo useWeek(boolean useWeek) {
        this.useWeek = useWeek;
        return this;
    }

    @Override
    public String ago() {
        if (hint == null) {
            this.hint = new DateAgoHint();
        }
        Calendar c1 = Dates.calendar(source);
        Calendar c2 = Dates.calendar(target);
        long s = source.getTime(), t = target.getTime();
        int sYear = c1.get(Calendar.YEAR),
                tYear = c2.get(Calendar.YEAR),
                sDay = c1.get(Calendar.DAY_OF_MONTH),
                tDay = c2.get(Calendar.DAY_OF_MONTH);
        if (s - t > 0) {
            long before = s - t;
            // 很久之前
            if (sYear - tYear > 10 && vague) {
                return hint.longAgo;
            }
            // 去年
            if (sYear - tYear == 1 && vague) {
                return hint.lastYear;
            }
            // 大于一年
            if (before > (Dates.DAY_STAMP * 365)) {
                return (before / (Dates.DAY_STAMP * 365)) + hint.yearAgo;
            }
            // 大于1月
            if (before > (Dates.MONTH_STAMP)) {
                return (before / (Dates.MONTH_STAMP)) != 12
                        ? (before / (Dates.MONTH_STAMP)) + hint.monthAgo
                        : 1 + hint.yearAgo;
            }
            // 大于一周
            if (before > Dates.WEEK_STAMP && useWeek) {
                return (before / Dates.WEEK_STAMP) + hint.weekAgo;
            }
            // 昨天
            if (sDay - tDay == 1 && vague) {
                return hint.yesterday;
            }
            // 前天
            if (sDay - tDay == 2 && vague) {
                return hint.beforeYesterday;
            }
            // 大于一天
            if (before > Dates.DAY_STAMP) {
                return (before / Dates.DAY_STAMP) + hint.dayAgo;
            }
            // 大于一小时
            if (before > Dates.HOUR_STAMP) {
                return (before / Dates.HOUR_STAMP) + hint.hourAgo;
            }
            // 大于一分钟
            if (before > Dates.MINUTE_STAMP) {
                return (before / Dates.MINUTE_STAMP) + hint.minuteAgo;
            }
            // 刚刚
            return vague ? hint.justNow : ((before / Dates.SECOND_STAMP) + hint.secondAgo);
        } else if (t - s > 0) {
            long after = t - s;
            // 很久之后
            if (tYear - sYear > 10 && vague) {
                return hint.longFuture;
            }
            // 明年
            if (tYear - sYear == 1 && vague) {
                return hint.nextYear;
            }
            // 大于一年
            if (after > (Dates.DAY_STAMP * 365)) {
                return (after / (Dates.DAY_STAMP * 365)) + hint.yearFuture;
            }
            // 大一1月
            if (after > (Dates.MONTH_STAMP)) {
                return (after / (Dates.MONTH_STAMP)) != 12
                        ? (after / (Dates.MONTH_STAMP)) + hint.monthFuture
                        : 1 + hint.yearFuture;
            }
            // 大于一周
            if (after > Dates.WEEK_STAMP && useWeek) {
                return (after / Dates.WEEK_STAMP) + hint.weekAgo;
            }
            // 明天
            if (tDay - sDay == 1 && vague) {
                return hint.tomorrow;
            }
            // 后天
            if (tDay - sDay == 2 && vague) {
                return hint.afterTomorrow;
            }
            // 大于一天
            if (after > Dates.DAY_STAMP) {
                return (after / Dates.DAY_STAMP) + hint.dayFuture;
            }
            // 大于一小时
            if (after > Dates.HOUR_STAMP) {
                return (after / Dates.HOUR_STAMP) + hint.hourFuture;
            }
            // 大于一分钟
            if (after > Dates.MINUTE_STAMP) {
                return (after / Dates.MINUTE_STAMP) + hint.minuteFuture;
            }
            // 片刻之后
            return vague ? hint.moment : ((after / Dates.SECOND_STAMP) + hint.secondFuture);
        } else {
            // 现在
            return vague ? hint.now : (0 + hint.secondAgo);
        }
    }

}
