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

import java.util.Date;
import java.util.TimeZone;

/**
 * copy with apache
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/25 12:36
 */
final class GmtTimeZone extends TimeZone {

    private static final int MILLISECONDS_PER_MINUTE = Const.MS_S_60;

    private static final int MINUTES_PER_HOUR = 60;

    private static final int HOURS_PER_DAY = 24;

    private static final long serialVersionUID = 1L;

    private final int offset;

    private final String zoneId;

    GmtTimeZone(boolean negate, int hours, int minutes) {
        if (hours >= HOURS_PER_DAY) {
            throw Exceptions.argument(hours + " hours out of range");
        }
        if (minutes >= MINUTES_PER_HOUR) {
            throw Exceptions.argument(minutes + " minutes out of range");
        }
        int milliseconds = (minutes + (hours * MINUTES_PER_HOUR)) * MILLISECONDS_PER_MINUTE;
        this.offset = negate ? -milliseconds : milliseconds;
        this.zoneId = twoDigits(
                twoDigits(new StringBuilder(9).append("GMT").append(negate ? '-' : '+'), hours)
                        .append(':'), minutes).toString();

    }

    private static StringBuilder twoDigits(StringBuilder sb, int n) {
        return sb.append((char) ('0' + (n / 10))).append((char) ('0' + (n % 10)));
    }

    @Override
    public int getOffset(int era, int year, int month, int day, int dayOfWeek, int milliseconds) {
        return offset;
    }

    @Override
    public void setRawOffset(int offsetMillis) {
        throw Exceptions.unsupported();
    }

    @Override
    public int getRawOffset() {
        return offset;
    }

    @Override
    public String getID() {
        return zoneId;
    }

    @Override
    public boolean useDaylightTime() {
        return false;
    }

    @Override
    public boolean inDaylightTime(Date date) {
        return false;
    }

    @Override
    public String toString() {
        return "id=\"" + zoneId + "\", offset=" + offset;
    }

    @Override
    public int hashCode() {
        return offset;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof GmtTimeZone)) {
            return false;
        }
        return zoneId.equals(((GmtTimeZone) other).zoneId);
    }

}
