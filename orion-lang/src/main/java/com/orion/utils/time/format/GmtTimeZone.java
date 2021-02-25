package com.orion.utils.time.format;

import com.orion.constant.Const;
import com.orion.utils.Exceptions;

import java.util.Date;
import java.util.TimeZone;

/**
 * copy with apache
 */
class GmtTimeZone extends TimeZone {

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
        offset = negate ? -milliseconds : milliseconds;
        zoneId = twoDigits(
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
        throw Exceptions.unSupport();
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
        return "[GmtTimeZone id=\"" + zoneId + "\",offset=" + offset + ']';
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
