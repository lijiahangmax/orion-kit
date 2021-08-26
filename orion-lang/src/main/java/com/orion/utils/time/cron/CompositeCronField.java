package com.orion.utils.time.cron;

import com.orion.utils.Valid;

import java.time.temporal.Temporal;

/**
 * copy with spring
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/24 12:35
 */
final class CompositeCronField extends CronField {

    private final CronField[] fields;

    private final String value;

    private CompositeCronField(Type type, CronField[] fields, String value) {
        super(type);
        this.fields = fields;
        this.value = value;
    }

    /**
     * 将给定的字段组成一个 {@link CronField}
     */
    public static CronField compose(CronField[] fields, Type type, String value) {
        Valid.notEmpty(fields, "fields must not be empty");
        Valid.notBlank(value, "value must not be empty");
        if (fields.length == 1) {
            return fields[0];
        } else {
            return new CompositeCronField(type, fields, value);
        }
    }

    @Override
    public <T extends Temporal & Comparable<? super T>> T nextOrSame(T temporal) {
        T result = null;
        for (CronField field : this.fields) {
            T candidate = field.nextOrSame(temporal);
            if (result == null ||
                    candidate != null && candidate.compareTo(result) < 0) {
                result = candidate;
            }
        }
        return result;
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompositeCronField)) {
            return false;
        }
        CompositeCronField other = (CompositeCronField) o;
        return type() == other.type() &&
                this.value.equals(other.value);
    }

    @Override
    public String toString() {
        return type() + " '" + this.value + "'";
    }

}
