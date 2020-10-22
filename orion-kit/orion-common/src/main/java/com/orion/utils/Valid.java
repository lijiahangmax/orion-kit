package com.orion.utils;

import com.orion.exception.argument.InvalidArgumentException;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.regex.Pattern;

/**
 * 合法性验证
 * <p>
 * 可以被拓展
 * 所抛出的异常必须为 {@link InvalidArgumentException} 或其子类
 *
 * @author Li
 * @version 1.0.0
 * @since 2019/11/18 16:25
 */
@SuppressWarnings("ALL")
public abstract class Valid {

    public Valid() {
    }

    public static void eq(Object o1, Object o2) {
        eq(o1, o2, "the validated objects not equal");
    }

    public static void eq(Object o1, Object o2, String message, Object... values) {
        notNull(o1);
        if (!Objects1.eq(o1, o2)) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static void neq(Object o1, Object o2) {
        neq(o1, o2, "The validated objects is equal");
    }

    public static void neq(Object o1, Object o2, String message, Object... values) {
        notNull(o1);
        if (Objects1.eq(o1, o2)) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static void lt(int i1, int i2) {
        lt(i1, i2, "The validated numbers not less than");
    }

    public static void lt(int i1, int i2, String message, Object... values) {
        if (i1 >= i2) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static void lt(long i1, long i2) {
        lt(i1, i2, "The validated numbers not less than");
    }

    public static void lt(long i1, long i2, String message, Object... values) {
        if (i1 >= i2) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static void lt(double i1, double i2) {
        lt(i1, i2, "The validated numbers not less than");
    }

    public static void lt(double i1, double i2, String message, Object... values) {
        if (i1 >= i2) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static void lt(BigDecimal i1, BigDecimal i2) {
        lt(i1, i2, "The validated numbers not less than");
    }

    public static void lt(BigDecimal i1, BigDecimal i2, String message, Object... values) {
        notNull(i1);
        notNull(i2);
        if (i1.compareTo(i2) >= 0) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static void lt(BigInteger i1, BigInteger i2) {
        lt(i1, i2, "The validated numbers not less than");
    }

    public static void lt(BigInteger i1, BigInteger i2, String message, Object... values) {
        notNull(i1);
        notNull(i2);
        if (i1.compareTo(i2) >= 0) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static void lte(int i1, int i2) {
        lte(i1, i2, "The validated numbers not less than or equal");
    }

    public static void lte(int i1, int i2, String message, Object... values) {
        if (i1 > i2) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static void lte(long i1, long i2) {
        lte(i1, i2, "The validated numbers not less than or equal");
    }

    public static void lte(long i1, long i2, String message, Object... values) {
        if (i1 > i2) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static void lte(double i1, double i2) {
        lte(i1, i2, "The validated numbers not less than or equal");
    }

    public static void lte(double i1, double i2, String message, Object... values) {
        if (i1 > i2) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static void lte(BigDecimal i1, BigDecimal i2) {
        lte(i1, i2, "The validated numbers not less than or equal");
    }

    public static void lte(BigDecimal i1, BigDecimal i2, String message, Object... values) {
        notNull(i1);
        notNull(i2);
        if (i1.compareTo(i2) > 0) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static void lte(BigInteger i1, BigInteger i2) {
        lte(i1, i2, "The validated numbers not less than or equal");
    }

    public static void lte(BigInteger i1, BigInteger i2, String message, Object... values) {
        notNull(i1);
        notNull(i2);
        if (i1.compareTo(i2) > 0) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static void gt(int i1, int i2) {
        gt(i1, i2, "The validated numbers not greater than");
    }

    public static void gt(int i1, int i2, String message, Object... values) {
        if (i1 <= i2) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static void gt(long i1, long i2) {
        gt(i1, i2, "The validated numbers not greater than");
    }

    public static void gt(long i1, long i2, String message, Object... values) {
        if (i1 <= i2) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static void gt(double i1, double i2) {
        gt(i1, i2, "The validated numbers not greater than");
    }

    public static void gt(double i1, double i2, String message, Object... values) {
        if (i1 <= i2) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static void gt(BigDecimal i1, BigDecimal i2) {
        gt(i1, i2, "The validated numbers not greater than");
    }

    public static void gt(BigDecimal i1, BigDecimal i2, String message, Object... values) {
        notNull(i1);
        notNull(i2);
        if (i1.compareTo(i2) <= 0) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static void gt(BigInteger i1, BigInteger i2) {
        gt(i1, i2, "The validated numbers not greater than");
    }

    public static void gt(BigInteger i1, BigInteger i2, String message, Object... values) {
        notNull(i1);
        notNull(i2);
        if (i1.compareTo(i2) <= 0) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static void gte(int i1, int i2) {
        gte(i1, i2, "The validated numbers not greater than or equal");
    }

    public static void gte(int i1, int i2, String message, Object... values) {
        if (i1 < i2) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static void gte(long i1, long i2) {
        gte(i1, i2, "The validated numbers not greater than or equal");
    }

    public static void gte(long i1, long i2, String message, Object... values) {
        if (i1 < i2) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static void gte(double i1, double i2) {
        gte(i1, i2, "The validated numbers not greater than or equal");
    }

    public static void gte(double i1, double i2, String message, Object... values) {
        if (i1 < i2) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static void gte(BigDecimal i1, BigDecimal i2) {
        gte(i1, i2, "The validated numbers not greater than or equal");
    }

    public static void gte(BigDecimal i1, BigDecimal i2, String message, Object... values) {
        notNull(i1);
        notNull(i2);
        if (i1.compareTo(i2) < 0) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static void gte(BigInteger i1, BigInteger i2) {
        gte(i1, i2, "The validated numbers not greater than or equal");
    }

    public static void gte(BigInteger i1, BigInteger i2, String message, Object... values) {
        notNull(i1);
        notNull(i2);
        if (i1.compareTo(i2) < 0) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static void isTrue(BooleanSupplier s) {
        if (!s.getAsBoolean()) {
            throw Exceptions.invalidArgument("The validated expression is false");
        }
    }

    public static void isTrue(BooleanSupplier s, String message, Object... values) {
        if (!s.getAsBoolean()) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static void isTrue(boolean expression) {
        if (!expression) {
            throw Exceptions.invalidArgument("The validated expression is false");
        }
    }

    public static void isTrue(boolean expression, String message, Object... values) {
        if (!expression) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static void isFalse(boolean expression) {
        if (expression) {
            throw Exceptions.invalidArgument("The validated expression is true");
        }
    }

    public static void isFalse(boolean expression, String message, Object... values) {
        if (expression) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static void isFalse(BooleanSupplier s) {
        if (s.getAsBoolean()) {
            throw Exceptions.invalidArgument("The validated expression is true");
        }
    }

    public static void isFalse(BooleanSupplier s, String message, Object... values) {
        if (s.getAsBoolean()) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static <T> void notNull(T object) {
        notNull(object, "The validated object is null");
    }

    public static <T> void notNull(T object, String message, Object... values) {
        if (object == null) {
            throw Exceptions.nullArgument(Strings.format(message, values));
        }
    }

    public static <T> void notEmpty(T[] array) {
        notEmpty(array, "The validated array is empty");
    }

    public static <T> void notEmpty(T[] array, String message, Object... values) {
        if (array == null) {
            throw Exceptions.nullArgument(Strings.format(message, values));
        }
        if (array.length == 0) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static <T extends Collection<?>> void notEmpty(T collection) {
        notEmpty(collection, "The validated collection is empty");
    }

    public static <T extends Collection<?>> void notEmpty(T collection, String message, Object... values) {
        if (collection == null) {
            throw Exceptions.nullArgument(Strings.format(message, values));
        }
        if (collection.isEmpty()) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static <T extends Map<?, ?>> void notEmpty(T map) {
        notEmpty(map, "The validated map is empty");
    }

    public static <T extends Map<?, ?>> void notEmpty(T map, String message, Object... values) {
        if (map == null) {
            throw Exceptions.nullArgument(Strings.format(message, values));
        }
        if (map.isEmpty()) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static void notEmpty(String chars) {
        notEmpty(chars, "The validated character sequence is empty");
    }

    public static void notEmpty(String s, String message, Object... values) {
        if (s == null) {
            throw Exceptions.nullArgument(Strings.format(message, values));
        }
        if (s.length() == 0) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static void notNumber(String s) {
        notNumber(s, "The validated character sequence not numbers");
    }

    public static void notNumber(String s, String message, Object... values) {
        if (Strings.isNotNumber(s)) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static void notBlank(String s) {
        notBlank(s, "The validated character sequence is blank");
    }

    public static void notBlank(String s, String message, Object... values) {
        if (Strings.isBlank(s)) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static <T> void validIndex(T[] array, int index) {
        validIndex(array, index, "The validated array index is invalid: {}", index);
    }

    public static <T> void validIndex(T[] array, int index, String message, Object... values) {
        notNull(array);
        if (index < 0 || index >= array.length) {
            throw Exceptions.indexArgument(Strings.format(message, values));
        }
    }

    public static <T extends Collection<?>> void validIndex(T collection, int index) {
        validIndex(collection, index, "The validated collection index is invalid: {}", index);
    }

    public static <T extends Collection<?>> void validIndex(T collection, int index, String message, Object... values) {
        notNull(collection);
        if (index < 0 || index >= collection.size()) {
            throw Exceptions.indexArgument(Strings.format(message, values));
        }
    }

    public static <T> void noNullElements(T[] array) {
        noNullElements(array, "The validated array contains null element at index: {}");
    }

    public static <T> void noNullElements(T[] array, String message) {
        notNull(array);
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) {
                throw Exceptions.invalidArgument(Strings.format(message, i));
            }
        }
    }

    public static <T extends Iterable<?>> void noNullElements(T iterable) {
        noNullElements(iterable, "The validated collection contains null element at index: {}");
    }

    public static <T extends Iterable<?>> void noNullElements(T iterable, String message) {
        notNull(iterable);
        int i = 0;
        for (Iterator<?> it = iterable.iterator(); it.hasNext(); i++) {
            if (it.next() == null) {
                throw Exceptions.invalidArgument(Strings.format(message, i));
            }
        }
    }

    public static void notMatches(CharSequence input, String pattern) {
        notMatches(input, pattern, "The string {} does not match the pattern {}");
    }

    public static void notMatches(CharSequence input, String pattern, String message, Object... values) {
        if (!Pattern.matches(pattern, input)) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static void notZero(int value) {
        notZero(value, "The validated value is zero");
    }

    public static void notZero(int value, String message, Object... values) {
        if (0 == value) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static void notZero(long value) {
        notZero(value, "The validated value is zero");
    }

    public static void notZero(long value, String message, Object... values) {
        if (0 == value) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static void notZero(double value) {
        notZero(value, "The validated value is zero");
    }

    public static void notZero(double value, String message, Object... values) {
        if (0 == value) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static void notZero(BigDecimal value) {
        notZero(value, "The validated value is zero");
    }

    public static void notZero(BigDecimal value, String message, Object... values) {
        if (value == null) {
            throw Exceptions.nullArgument();
        }
        if (BigDecimal.ZERO.equals(value)) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static void notZero(BigInteger value) {
        notZero(value, "The validated value is zero");
    }

    public static void notZero(BigInteger value, String message, Object... values) {
        if (value == null) {
            throw Exceptions.nullArgument();
        }
        if (BigInteger.ZERO.equals(value)) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static void notNaN(double value) {
        notNaN(value, "The validated value is not a number");
    }

    public static void notNaN(double value, String message, Object... values) {
        if (Double.isNaN(value)) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static void notFound(File value) {
        notFound(value, "The validated file is not found");
    }

    public static void notFound(File value, String message, Object... values) {
        if (value == null || !value.exists()) {
            throw Exceptions.notFound(Strings.format(message, values));
        }
    }

    public static void notFile(File value) {
        notFile(value, "The validated file is not file");
    }

    public static void notFile(File value, String message, Object... values) {
        if (value == null || !value.exists() || !value.isFile()) {
            throw Exceptions.notFound(Strings.format(message, values));
        }
    }

    public static void notDirectory(File value) {
        notDirectory(value, "The validated file is not directory");
    }

    public static void notDirectory(File value, String message, Object... values) {
        if (value == null || !value.exists() || !value.isDirectory()) {
            throw Exceptions.notFound(Strings.format(message, values));
        }
    }

    public static <T> void notInclude(T start, T end, Comparable<T> value) {
        if (value.compareTo(start) < 0 || value.compareTo(end) > 0) {
            throw Exceptions.invalidArgument(Strings.format("The value {} is not in the specified inclusive range of {} to {}", value, start, end));
        }
    }

    public static <T> void notInclude(T start, T end, Comparable<T> value, String message, Object... values) {
        if (value.compareTo(start) < 0 || value.compareTo(end) > 0) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static void notInclude(long start, long end, long value) {
        if (value < start || value > end) {
            throw Exceptions.invalidArgument(Strings.format("The value {} is not in the specified inclusive range of {} to {}", value, start, end));
        }
    }

    public static void notInclude(long start, long end, long value, String message) {
        if (value < start || value > end) {
            throw Exceptions.invalidArgument(message);
        }
    }

    public static void notInclude(double start, double end, double value) {
        if (value < start || value > end) {
            throw Exceptions.invalidArgument(Strings.format("The value {} is not in the specified inclusive range of {} to {}", value, start, end));
        }
    }

    public static void notInclude(double start, double end, double value, String message) {
        if (value < start || value > end) {
            throw Exceptions.invalidArgument(message);
        }
    }

    public static <T> void notCompare(T start, T end, Comparable<T> value) {
        if (value.compareTo(start) <= 0 || value.compareTo(end) >= 0) {
            throw Exceptions.invalidArgument(Strings.format("The value {} is not in the specified exclusive range of {} to {}", value, start, end));
        }
    }

    public static <T> void notCompare(T start, T end, Comparable<T> value, String message, Object... values) {
        if (value.compareTo(start) <= 0 || value.compareTo(end) >= 0) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static void notExclude(long start, long end, long value) {
        if (value <= start || value >= end) {
            throw Exceptions.invalidArgument(Strings.format("The value {} is not in the specified exclusive range of {} to {}", value, start, end));
        }
    }

    public static void notExclude(long start, long end, long value, String message) {
        if (value <= start || value >= end) {
            throw Exceptions.invalidArgument(message);
        }
    }

    public static void notExclude(double start, double end, double value) {
        if (value <= start || value >= end) {
            throw Exceptions.invalidArgument(Strings.format("The value {} is not in the specified exclusive range of {} to {}", value, start, end));
        }
    }

    public static void notExclude(double start, double end, double value, String message) {
        if (value <= start || value >= end) {
            throw Exceptions.invalidArgument(message);
        }
    }

    public static void isInstanceOf(Object obj, Class<?> type) {
        if (!type.isInstance(obj)) {
            throw Exceptions.invalidArgument(Strings.format("Expected type: {}, actual: {}", type.getName(), obj == null ? "null" : obj.getClass().getName()));
        }
    }

    public static void isInstanceOf(Object obj, Class<?> type, String message, Object... values) {
        if (!type.isInstance(obj)) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

}
