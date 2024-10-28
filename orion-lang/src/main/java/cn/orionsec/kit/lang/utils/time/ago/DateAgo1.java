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
package cn.orionsec.kit.lang.utils.time.ago;

import cn.orionsec.kit.lang.utils.time.DateStream;

import java.util.Date;

import static cn.orionsec.kit.lang.utils.time.Dates.getMonthLastDay;
import static cn.orionsec.kit.lang.utils.time.Dates.stream;

/**
 * 获取时间前后 通过对比字段实现 不是实际差异时间
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/29 11:34
 */
public class DateAgo1 extends Ago {

    /**
     * 是否使用精确时间 如: 23小时前/昨天
     */
    private boolean strict;

    public DateAgo1(Date target) {
        super(target);
    }

    public DateAgo1(Date source, Date target) {
        super(source, target);
    }

    public DateAgo1(Date target, DateAgoHint hint) {
        super(target, hint);
    }

    public DateAgo1(Date source, Date target, DateAgoHint hint) {
        super(source, target, hint);
    }

    public static DateAgo1 of(Date target) {
        return new DateAgo1(target);
    }

    public static DateAgo1 of(Date source, Date target) {
        return new DateAgo1(source, target);
    }

    /**
     * 是否使用精确时间
     *
     * @param strict true: 23小时前 false: 昨天
     * @return this
     */
    public DateAgo1 strict(boolean strict) {
        this.strict = strict;
        return this;
    }

    @Override
    public String ago() {
        if (hint == null) {
            this.hint = new DateAgoHint();
        }
        DateStream ss = stream(source);
        // 目标时间
        int sYear = ss.getYear(),
                sMonth = ss.getMonth(),
                sDay = ss.getDay(),
                sHour = ss.getHour(),
                sMinute = ss.getMinute(),
                sSecond = ss.getSecond();
        // 比较时间
        DateStream ts = stream(target);
        int tYear = ts.getYear(),
                tMonth = ts.getMonth(),
                tDay = ts.getDay(),
                tHour = ts.getHour(),
                tMinute = ts.getMinute(),
                tSecond = ts.getSecond();
        if (target.before(source)) {
            // 判断年
            if (sYear > tYear) {
                // 很久以前
                if ((sYear - tYear) > 10) {
                    return hint.longAgo;
                } else if (sYear - tYear == 1) {
                    if (strict) {
                        if (sMonth > tMonth) {
                            return 1 + hint.yearAgo;
                        } else if (sMonth == tMonth) {
                            if (sDay > tDay) {
                                return 1 + hint.yearAgo;
                            } else {
                                return 11 + hint.monthAgo;
                            }
                        } else {
                            return (12 - tMonth + sMonth) + hint.monthAgo;
                        }
                    } else {
                        return hint.lastYear;
                    }
                } else {
                    return (sYear - tYear) + hint.yearAgo;
                }
            }
            // 判断月
            if (sMonth > tMonth) {
                if (sMonth - tMonth == 1 && strict) {
                    if (sDay >= tDay) {
                        return 1 + hint.monthAgo;
                    } else {
                        return (getMonthLastDay(sYear, sMonth) - (tDay - sDay)) + hint.dayAgo;
                    }
                } else {
                    return (sMonth - tMonth) + hint.monthAgo;
                }
            }
            // 判断日
            if (sDay > tDay) {
                if (strict) {
                    if (sDay - tDay == 1) {
                        if (sHour >= tHour) {
                            return hint.yesterday;
                        } else {
                            return (24 - tHour + sHour) + hint.hourAgo;
                        }
                    } else if (sDay - tDay == 2) {
                        return hint.beforeYesterday;
                    } else {
                        return (sDay - tDay) + hint.dayAgo;
                    }
                } else {
                    if (sDay - tDay == 1) {
                        return hint.yesterday;
                    } else if (sDay - tDay == 2) {
                        return hint.beforeYesterday;
                    } else {
                        return (sDay - tDay) + hint.dayAgo;
                    }
                }
            }
            // 判断时
            if (sHour > tHour) {
                if (sHour - tHour == 1 && strict) {
                    if (sMinute >= tMinute) {
                        return 1 + hint.hourAgo;
                    } else {
                        return (60 - tMinute + sMinute) + hint.minuteAgo;
                    }
                } else {
                    return (sHour - tHour) + hint.hourAgo;
                }
            }
            // 判断分
            if (sMinute > tMinute) {
                return (sMinute - tMinute) + hint.minuteAgo;
            }
            // 判断秒
            if (strict) {
                return (sSecond - tSecond) + hint.secondAgo;
            } else {
                return hint.justNow;
            }
        } else if (target.after(source)) {
            // 判断年
            if (sYear < tYear) {
                if ((tYear - sYear) > 10) {
                    return hint.longFuture;
                } else if (tYear - sYear == 1) {
                    if (strict) {
                        if (sMonth > tMonth) {
                            return (12 + tMonth - sMonth) + hint.monthFuture;
                        } else if (sMonth == tMonth) {
                            if (tDay > sDay) {
                                return 1 + hint.yearFuture;
                            } else {
                                return 11 + hint.monthFuture;
                            }
                        } else {
                            return 1 + hint.yearFuture;
                        }
                    } else {
                        return hint.nextYear;
                    }
                } else {
                    return (tYear - sYear) + hint.yearFuture;
                }
            }
            // 判断月
            if (sMonth < tMonth) {
                if (tMonth - sMonth == 1 && strict) {
                    if (sDay >= tDay) {
                        return (getMonthLastDay(sYear, sMonth) + (tDay - sDay)) + hint.dayFuture;
                    } else {
                        return 1 + hint.monthFuture;
                    }
                } else {
                    return (tMonth - sMonth) + hint.monthFuture;
                }
            }
            // 判断日
            if (sDay < tDay) {
                if (tDay - sDay == 1 && strict) {
                    if (tHour < sHour) {
                        return (24 - sHour + tHour) + hint.hourFuture;
                    } else {
                        return hint.tomorrow;
                    }
                } else if (tDay - sDay == 1) {
                    return hint.tomorrow;
                } else if (tDay - sDay == 2) {
                    return hint.afterTomorrow;
                } else {
                    return (tDay - sDay) + hint.dayFuture;
                }
            }
            // 判断时
            if (sHour < tHour) {
                if (tHour - sHour == 1 && strict) {
                    if (tMinute < sMinute) {
                        return (60 - sMinute + tMinute) + hint.minuteFuture;
                    } else {
                        return 1 + hint.hourFuture;
                    }
                } else {
                    return (tHour - sHour) + hint.hourFuture;
                }
            }
            // 判断分
            if (sMinute < tMinute) {
                return (tMinute - sMinute) + hint.minuteFuture;
            }
            // 判断秒
            if (strict) {
                return (tSecond - sSecond) + hint.secondFuture;
            } else {
                return hint.moment;
            }
        } else {
            // 现在
            return strict ? (0 + hint.secondAgo) : hint.now;
        }
    }

}
