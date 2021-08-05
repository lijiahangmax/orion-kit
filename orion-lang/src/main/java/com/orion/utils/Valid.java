package com.orion.utils;

import com.orion.exception.argument.InvalidArgumentException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Comparator;
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
@SuppressWarnings("unused")
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
        neq(o1, o2, "the validated objects is equal");
    }

    public static void neq(Object o1, Object o2, String message, Object... values) {
        notNull(o1);
        if (Objects1.eq(o1, o2)) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static <T extends Comparable<T>> void compare(T t1, T t2) {
        compare(t1, t2, "the validated numbers not compare equal");
    }

    public static <T extends Comparable<T>> void compare(T t1, T t2, String message, Object... values) {
        notNull(t1);
        notNull(t2);
        if (t1.compareTo(t2) != 0) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static <T extends Comparable<T>> void notCompare(T t1, T t2) {
        notCompare(t1, t2, "the validated numbers is compare equal");
    }

    public static <T extends Comparable<T>> void notCompare(T t1, T t2, String message, Object... values) {
        notNull(t1);
        notNull(t2);
        if (t1.compareTo(t2) == 0) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static <T> void compare(T t1, T t2, Comparator<T> comparator) {
        compare(t1, t2, comparator, "the validated numbers not compare {} {}", t1, t2);
    }

    public static <T> void compare(T t1, T t2, Comparator<T> comparator, String message, Object... values) {
        if (comparator.compare(t1, t2) != 0) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static <T> void notCompare(T t1, T t2, Comparator<T> comparator) {
        notCompare(t1, t2, comparator, "the validated numbers is compare {} {}", t1, t2);
    }

    public static <T> void notCompare(T t1, T t2, Comparator<T> comparator, String message, Object... values) {
        if (comparator.compare(t1, t2) == 0) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static <T extends Comparable<T>> T lt(T t1, T t2) {
        return lt(t1, t2, "the validated numbers not less than");
    }

    public static <T extends Comparable<T>> T lt(T t1, T t2, String message, Object... values) {
        notNull(t1);
        notNull(t2);
        if (t1.compareTo(t2) >= 0) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return t1;
    }

    public static <T extends Comparable<T>> T lte(T t1, T t2) {
        return lte(t1, t2, "the validated numbers not less than or equal");
    }

    public static <T extends Comparable<T>> T lte(T t1, T t2, String message, Object... values) {
        notNull(t1);
        notNull(t2);
        if (t1.compareTo(t2) > 0) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return t1;
    }

    public static <T extends Comparable<T>> T gt(T t1, T t2) {
        return gt(t1, t2, "the validated numbers not greater than");
    }

    public static <T extends Comparable<T>> T gt(T t1, T t2, String message, Object... values) {
        notNull(t1);
        notNull(t2);
        if (t1.compareTo(t2) <= 0) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return t1;
    }

    public static <T extends Comparable<T>> T gte(T t1, T t2) {
        return gte(t1, t2, "the validated numbers not greater than or equal");
    }

    public static <T extends Comparable<T>> T gte(T t1, T t2, String message, Object... values) {
        notNull(t1);
        notNull(t2);
        if (t1.compareTo(t2) < 0) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return t1;
    }

    public static boolean isTrue(BooleanSupplier s) {
        return isTrue(s, "the validated expression is false");
    }

    public static boolean isTrue(BooleanSupplier s, String message, Object... values) {
        notNull(s);
        if (!s.getAsBoolean()) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return true;
    }

    public static boolean isTrue(boolean expression) {
        return isTrue(expression, "the validated expression is false");
    }

    public static boolean isTrue(boolean expression, String message, Object... values) {
        if (!expression) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return true;
    }

    public static boolean isFalse(BooleanSupplier s) {
        return isFalse(s, "the validated expression is true");
    }

    public static boolean isFalse(BooleanSupplier s, String message, Object... values) {
        notNull(s);
        if (s.getAsBoolean()) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return false;
    }

    public static boolean isFalse(boolean expression) {
        return isFalse(expression, "the validated expression is true");
    }

    public static boolean isFalse(boolean expression, String message, Object... values) {
        if (expression) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return false;
    }

    public static <T> T notNull(T object) {
        return notNull(object, "the validated object is null");
    }

    public static <T> T notNull(T object, String message, Object... values) {
        if (object == null) {
            throw Exceptions.nullArgument(Strings.format(message, values));
        }
        return object;
    }

    public static <T> T in(T t, T... arr) {
        return in(t, arr, "the validated object not in array");
    }

    public static <T> T in(T t, T[] arr, String message, Object... values) {
        notNull(t);
        notEmpty(arr);
        if (!Arrays1.contains(arr, t)) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return t;
    }

    public static <T> T in(T t, Collection<? extends T> c) {
        return in(t, c, "the validated object not in collection");
    }

    public static <T> T in(T t, Collection<? extends T> c, String message, Object... values) {
        notNull(t);
        notEmpty(c);
        if (!c.contains(t)) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return t;
    }

    public static <T> T notIn(T t, T... arr) {
        return notIn(t, arr, "the validated object in array");
    }

    public static <T> T notIn(T t, T[] arr, String message, Object... values) {
        notNull(t);
        notEmpty(arr);
        if (Arrays1.contains(arr, t)) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return t;
    }

    public static <T> T notIn(T t, Collection<? extends T> c) {
        return notIn(t, c, "the validated object in collection");
    }

    public static <T> T notIn(T t, Collection<? extends T> c, String message, Object... values) {
        notNull(t);
        notEmpty(c);
        if (c.contains(t)) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return t;
    }

    public static <T> T[] notEmpty(T[] array) {
        return notEmpty(array, "the validated array is empty");
    }

    public static <T> T[] notEmpty(T[] array, String message, Object... values) {
        if (array == null) {
            throw Exceptions.nullArgument(Strings.format(message, values));
        }
        if (array.length == 0) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return array;
    }

    public static <T extends Collection<?>> T notEmpty(T collection) {
        return notEmpty(collection, "the validated collection is empty");
    }

    public static <T extends Collection<?>> T notEmpty(T collection, String message, Object... values) {
        if (collection == null) {
            throw Exceptions.nullArgument(Strings.format(message, values));
        }
        if (collection.isEmpty()) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return collection;
    }

    public static <T extends Map<?, ?>> T notEmpty(T map) {
        return notEmpty(map, "the validated map is empty");
    }

    public static <T extends Map<?, ?>> T notEmpty(T map, String message, Object... values) {
        if (map == null) {
            throw Exceptions.nullArgument(Strings.format(message, values));
        }
        if (map.isEmpty()) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return map;
    }

    public static String notEmpty(String s) {
        return notEmpty(s, "the validated character sequence is empty");
    }

    public static String notEmpty(String s, String message, Object... values) {
        if (s == null) {
            throw Exceptions.nullArgument(Strings.format(message, values));
        }
        if (s.length() == 0) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return s;
    }

    public static String isNumber(String s) {
        return isNumber(s, "the validated character sequence not numbers");
    }

    public static String isNumber(String s, String message, Object... values) {
        if (!Strings.isNumber(s)) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return s;
    }

    public static String isInteger(String s) {
        return isInteger(s, "the validated character sequence not integer");
    }

    public static String isInteger(String s, String message, Object... values) {
        if (!Strings.isInteger(s)) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return s;
    }

    public static String isDouble(String s) {
        return isNumber(s, "the validated character sequence not double");
    }

    public static String isDouble(String s, String message, Object... values) {
        if (!Strings.isDouble(s)) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return s;
    }

    public static String notBlank(String s) {
        return notBlank(s, "the validated character sequence is blank");
    }

    public static String notBlank(String s, String message, Object... values) {
        if (Strings.isBlank(s)) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return s;
    }

    public static <T> T validIndex(T[] array, int index) {
        return validIndex(array, index, "the validated array index is invalid: {}", index);
    }

    public static <T> T validIndex(T[] array, int index, String message, Object... values) {
        notNull(array);
        if (index < 0 || index >= array.length) {
            throw Exceptions.indexArgument(Strings.format(message, values));
        }
        return array[index];
    }

    public static <T extends Collection<?>> void validIndex(T collection, int index) {
        validIndex(collection, index, "the validated collection index is invalid: {}", index);
    }

    public static <T extends Collection<?>> void validIndex(T collection, int index, String message, Object... values) {
        notNull(collection);
        if (index < 0 || index >= collection.size()) {
            throw Exceptions.indexArgument(Strings.format(message, values));
        }
    }

    public static <T> T[] noNullElements(T[] array) {
        return noNullElements(array, "the validated array contains null element");
    }

    public static <T> T[] noNullElements(T[] array, String message, Object... values) {
        notNull(array);
        for (T t : array) {
            if (t == null) {
                throw Exceptions.invalidArgument(Strings.format(message, values));
            }
        }
        return array;
    }

    public static <T extends Iterable<?>> T noNullElements(T iterable) {
        return noNullElements(iterable, "the validated collection contains null element");
    }

    public static <T extends Iterable<?>> T noNullElements(T iterable, String message, Object... values) {
        notNull(iterable);
        int i = 0;
        for (Iterator<?> it = iterable.iterator(); it.hasNext(); i++) {
            if (it.next() == null) {
                throw Exceptions.invalidArgument(Strings.format(message, values));
            }
        }
        return iterable;
    }

    public static String isMatches(String input, String pattern) {
        return notMatches(input, pattern, "the string {} does not match the pattern {}", input, pattern);
    }

    public static String isMatches(String input, String pattern, String message, Object... values) {
        if (!Pattern.matches(pattern, input)) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return input;
    }

    public static String notMatches(String input, String pattern) {
        return notMatches(input, pattern, "the string {} match the pattern {}", input, pattern);
    }

    public static String notMatches(String input, String pattern, String message, Object... values) {
        if (Pattern.matches(pattern, input)) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return input;
    }

    public static int notZero(int value) {
        return notZero(value, "the validated value is zero");
    }

    public static int notZero(int value, String message, Object... values) {
        if (0 == value) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return value;
    }

    public static long notZero(long value) {
        return notZero(value, "the validated value is zero");
    }

    public static long notZero(long value, String message, Object... values) {
        if (0 == value) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return value;
    }

    public static double notZero(double value) {
        return notZero(value, "the validated value is zero");
    }

    public static double notZero(double value, String message, Object... values) {
        if (0 == value) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return value;
    }

    public static BigDecimal notZero(BigDecimal value) {
        return notZero(value, "the validated value is zero");
    }

    public static BigDecimal notZero(BigDecimal value, String message, Object... values) {
        notNull(value);
        notCompare(BigDecimal.ZERO, value, message, values);
        return value;
    }

    public static BigInteger notZero(BigInteger value) {
        return notZero(value, "the validated value is zero");
    }

    public static BigInteger notZero(BigInteger value, String message, Object... values) {
        notNull(value);
        notCompare(BigInteger.ZERO, value, message, values);
        return value;
    }

    public static double notNaN(float value) {
        return notNaN(value, "the validated value is not a number");
    }

    public static double notNaN(float value, String message, Object... values) {
        if (Float.isNaN(value)) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return value;
    }

    public static double notNaN(double value) {
        return notNaN(value, "the validated value is not a number");
    }

    public static double notNaN(double value, String message, Object... values) {
        if (Double.isNaN(value)) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return value;
    }

    public static <T extends Comparable<T>> T inRange(T value, T start, T end) {
        return inRange(value, start, end, "the value {} is not in the specified inclusive range of {} to {}", value, start, end);
    }

    public static <T extends Comparable<T>> T inRange(T value, T start, T end, String message, Object... values) {
        notNull(value);
        notNull(start);
        notNull(end);
        if (!Compares.inRange(value, start, end)) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return value;
    }

    public static <T extends Comparable<T>> T notInRange(T value, T start, T end) {
        return notInRange(start, end, value, "the value {} in the specified inclusive range of {} to {}", value, start, end);
    }

    public static <T extends Comparable<T>> T notInRange(T value, T start, T end, String message, Object... values) {
        notNull(value);
        notNull(start);
        notNull(end);
        if (Compares.inRange(value, start, end)) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return value;
    }

    public static String validLengthIn(String s, int start, int end) {
        return validLengthIn(s, start, end, "the validated value length is not in {} of {}", start, end);
    }

    public static String validLengthIn(String s, int start, int end, String message, Object... values) {
        notNull(s);
        if (!Compares.inRange(s.length(), start, end)) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return s;
    }

    public static String validLengthGt(String s, int length) {
        return validLengthGt(s, length, "the validated value length is not greater than {}", length);
    }

    public static String validLengthGt(String s, int length, String message, Object... values) {
        notNull(s);
        if (s.length() <= length) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return s;
    }

    public static String validLengthGte(String s, int length) {
        return validLengthGte(s, length, "the validated value length is not greater than or equal{}", length);
    }

    public static String validLengthGte(String s, int length, String message, Object... values) {
        notNull(s);
        if (s.length() < length) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return s;
    }

    public static String validLengthLt(String s, int length) {
        return validLengthLt(s, length, "the validated value length is not less than {}", length);
    }

    public static String validLengthLt(String s, int length, String message, Object... values) {
        notNull(s);
        if (s.length() >= length) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return s;
    }

    public static String validLengthLte(String s, int length) {
        return validLengthLte(s, length, "the validated value length is not less than or equal{}", length);
    }

    public static String validLengthLte(String s, int length, String message, Object... values) {
        notNull(s);
        if (s.length() > length) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return s;
    }

    public static <T> T isInstanceOf(Object obj, Class<T> type) {
        if (!type.isInstance(obj)) {
            throw Exceptions.invalidArgument(Strings.format("expected type: {}, actual: {}", type.getName(), obj == null ? "null" : obj.getClass().getName()));
        }
        return type.cast(obj);
    }

    public static <T> T isInstanceOf(Object obj, Class<T> type, String message, Object... values) {
        if (!type.isInstance(obj)) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return type.cast(obj);
    }

}
