package com.orion.utils.time.cron;

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Arrays;

/**
 * copy with spring
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/24 11:58
 */
public class Cron {

    static final int MAX_ATTEMPTS = 366;

    private final CronField[] fields;

    private final String expression;

    protected Cron(
            CronField seconds,
            CronField minutes,
            CronField hours,
            CronField daysOfMonth,
            CronField months,
            CronField daysOfWeek,
            String expression) {
        // 颠倒顺序, 首先进行大的更改
        // 以确保我们以 0 纳秒结束, 添加了一个额外的字段
        this.fields = new CronField[]{daysOfWeek, months, daysOfMonth, hours, minutes, seconds, CronField.zeroNanos()};
        this.expression = expression;
    }

    /**
     * 计算下一个与此表达式匹配的 {@link Temporal}
     *
     * @param temporal 种子值
     * @param <T>      时间类型
     * @return 匹配此表达式的下一个时间 如果找不到这样的时间返回 null
     */
    public <T extends Temporal & Comparable<? super T>> T next(T temporal) {
        return this.nextOrSame(ChronoUnit.NANOS.addTo(temporal, 1));
    }

    private <T extends Temporal & Comparable<? super T>> T nextOrSame(T temporal) {
        for (int i = 0; i < MAX_ATTEMPTS; i++) {
            T result = this.nextOrSameInternal(temporal);
            if (result == null || result.equals(temporal)) {
                return result;
            }
            temporal = result;
        }
        return null;
    }

    private <T extends Temporal & Comparable<? super T>> T nextOrSameInternal(T temporal) {
        for (CronField field : this.fields) {
            temporal = field.nextOrSame(temporal);
            if (temporal == null) {
                return null;
            }
        }
        return temporal;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.fields);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof Cron) {
            Cron other = (Cron) o;
            return Arrays.equals(this.fields, other.fields);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return expression;
    }

}
