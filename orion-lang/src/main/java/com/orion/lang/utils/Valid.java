package com.orion.lang.utils;

import com.orion.lang.able.IHttpResponse;
import com.orion.lang.constant.Const;
import com.orion.lang.exception.argument.InvalidArgumentException;

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

    private static final String VALID_NOT_EQUAL = "the validated objects not equal";
    private static final String VALID_IS_EQUAL = "the validated objects is equal";
    private static final String VALID_NOT_COMPARE = "the validated objects not compare";
    private static final String VALID_IS_COMPARE = "the validated objects is compare";
    private static final String VALID_NOT_LT = "the validated objects not less than";
    private static final String VALID_NOT_LT_EQ = "the validated objects not less than or equal";
    private static final String VALID_NOT_GT = "the validated objects not greater than";
    private static final String VALID_NOT_GT_EQ = "the validated objects not greater than or equal";
    private static final String VALID_IS_FALSE = "the validated expression is false";
    private static final String VALID_IS_TRUE = "the validated expression is true";
    private static final String VALID_IS_NULL = "the validated object is null";
    private static final String VALID_NOT_ARRAY = "the validated object not in array";
    private static final String VALID_NOT_COLLECTION = "the validated object not in collection";
    private static final String VALID_NOT_IN_ARRAY = "the validated object in array";
    private static final String VALID_NOT_IN_COLLECTION = "the validated object in collection";
    private static final String VALID_ARRAY_IS_EMPTY = "the validated array is empty";
    private static final String VALID_COLLECTION_IS_EMPTY = "the validated collection is empty";
    private static final String VALID_MAP_IS_EMPTY = "the validated map is empty";
    private static final String VALID_STRING_IS_EMPTY = "the validated string is empty";
    private static final String VALID_STRING_IS_BLANK = "the validated string is blank";
    private static final String VALID_STRING_NOT_NUMBER = "the validated string not numbers";
    private static final String VALID_STRING_NOT_INTEGER = "the validated string not integer";
    private static final String VALID_STRING_NOT_DOUBLE = "the validated string sequence not double";
    private static final String VALID_ARRAY_INDEX = "the validated array index is invalid: {}";
    private static final String VALID_COLLECTION_INDEX = "the validated collection index is invalid: {}";
    private static final String VALID_ARRAY_CONTENTS_NULL = "the validated array contains null element";
    private static final String VALID_COLLECTION_CONTENTS_NULL = "the validated collection contains null element";
    private static final String VALID_STRING_NOT_MATCH = "the string {} not match the pattern {}";
    private static final String VALID_STRING_MATCH = "the string {} match the pattern {}";
    private static final String VALID_VALUE_IS_ZERO = "the validated value is zero";
    private static final String VALID_VALUE_IS_NAN = "the validated value is not a number";
    private static final String VALID_VALUE_NOT_IN_RANGE = "the value {} is not in the specified inclusive range of {} to {}";
    private static final String VALID_VALUE_IN_RANGE = "the value {} in the specified inclusive range of {} to {}";
    private static final String VALID_STRING_LENGTH = "the validated value length is not equal {}";
    private static final String VALID_STRING_LENGTH_IN = "the validated value length is not in {} of {}";
    private static final String VALID_LENGTH_NOT_GT = "the validated value length is not greater than {}";
    private static final String VALID_LENGTH_NOT_GT_EQ = "the validated value length is not greater than or equal {}";
    private static final String VALID_LENGTH_NOT_LT = "the validated value length is not less than {}";
    private static final String VALID_LENGTH_NOT_LT_EQ = "the validated value length is not less than or equal {}";
    private static final String VALID_NOT_INSTANCE = "expected type: {}, actual: {}";
    private static final String HTTP_REQ_NOT_OK = "http request not success. code: {}, url: {}";

    public Valid() {
    }

    public static void eq(Object o1, Object o2) {
        eq(o1, o2, VALID_NOT_EQUAL);
    }

    public static void eq(Object o1, Object o2, String message, Object... values) {
        notNull(o1);
        if (!Objects1.eq(o1, o2)) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static void neq(Object o1, Object o2) {
        neq(o1, o2, VALID_IS_EQUAL);
    }

    public static void neq(Object o1, Object o2, String message, Object... values) {
        notNull(o1);
        if (Objects1.eq(o1, o2)) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static <T extends Comparable<T>> void compare(T t1, T t2) {
        compare(t1, t2, VALID_NOT_COMPARE);
    }

    public static <T extends Comparable<T>> void compare(T t1, T t2, String message, Object... values) {
        notNull(t1);
        notNull(t2);
        if (t1.compareTo(t2) != 0) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static <T extends Comparable<T>> void notCompare(T t1, T t2) {
        notCompare(t1, t2, VALID_IS_COMPARE);
    }

    public static <T extends Comparable<T>> void notCompare(T t1, T t2, String message, Object... values) {
        notNull(t1);
        notNull(t2);
        if (t1.compareTo(t2) == 0) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static <T> void compare(T t1, T t2, Comparator<T> comparator) {
        compare(t1, t2, comparator, VALID_NOT_COMPARE);
    }

    public static <T> void compare(T t1, T t2, Comparator<T> comparator, String message, Object... values) {
        Valid.notNull(comparator);
        if (comparator.compare(t1, t2) != 0) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static <T> void notCompare(T t1, T t2, Comparator<T> comparator) {
        notCompare(t1, t2, comparator, VALID_IS_COMPARE);
    }

    public static <T> void notCompare(T t1, T t2, Comparator<T> comparator, String message, Object... values) {
        Valid.notNull(comparator);
        if (comparator.compare(t1, t2) == 0) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
    }

    public static <T extends Comparable<T>> T lt(T value, T refer) {
        return lt(value, refer, VALID_NOT_LT);
    }

    public static <T extends Comparable<T>> T lt(T value, T refer, String message, Object... values) {
        notNull(value);
        notNull(refer);
        if (value.compareTo(refer) >= 0) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return value;
    }

    public static <T extends Comparable<T>> T lte(T value, T refer) {
        return lte(value, refer, VALID_NOT_LT_EQ);
    }

    public static <T extends Comparable<T>> T lte(T value, T refer, String message, Object... values) {
        notNull(value);
        notNull(refer);
        if (value.compareTo(refer) > 0) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return value;
    }

    public static <T extends Comparable<T>> T gt(T value, T refer) {
        return gt(value, refer, VALID_NOT_GT);
    }

    public static <T extends Comparable<T>> T gt(T value, T refer, String message, Object... values) {
        notNull(value);
        notNull(refer);
        if (value.compareTo(refer) <= 0) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return value;
    }

    public static <T extends Comparable<T>> T gte(T value, T refer) {
        return gte(value, refer, VALID_NOT_GT_EQ);
    }

    public static <T extends Comparable<T>> T gte(T value, T refer, String message, Object... values) {
        notNull(value);
        notNull(refer);
        if (value.compareTo(refer) < 0) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return value;
    }

    public static boolean isTrue(BooleanSupplier s) {
        return isTrue(s, VALID_IS_FALSE);
    }

    public static boolean isTrue(BooleanSupplier s, String message, Object... values) {
        notNull(s);
        if (!s.getAsBoolean()) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return true;
    }

    public static boolean isTrue(boolean expression) {
        return isTrue(expression, VALID_IS_FALSE);
    }

    public static boolean isTrue(boolean expression, String message, Object... values) {
        if (!expression) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return true;
    }

    public static boolean isFalse(BooleanSupplier s) {
        return isFalse(s, VALID_IS_TRUE);
    }

    public static boolean isFalse(BooleanSupplier s, String message, Object... values) {
        notNull(s);
        if (s.getAsBoolean()) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return false;
    }

    public static boolean isFalse(boolean expression) {
        return isFalse(expression, VALID_IS_TRUE);
    }

    public static boolean isFalse(boolean expression, String message, Object... values) {
        if (expression) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return false;
    }

    public static <T> T notNull(T object) {
        return notNull(object, VALID_IS_NULL);
    }

    public static <T> T notNull(T object, String message, Object... values) {
        if (object == null) {
            throw Exceptions.nullArgument(Strings.format(message, values));
        }
        return object;
    }

    @SafeVarargs
    public static <T> T in(T t, T... arr) {
        return in(t, arr, VALID_NOT_ARRAY);
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
        return in(t, c, VALID_NOT_COLLECTION);
    }

    public static <T> T in(T t, Collection<? extends T> c, String message, Object... values) {
        notNull(t);
        notEmpty(c);
        if (!c.contains(t)) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return t;
    }

    @SafeVarargs
    public static <T> T notIn(T t, T... arr) {
        return notIn(t, arr, VALID_NOT_IN_ARRAY);
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
        return notIn(t, c, VALID_NOT_IN_COLLECTION);
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
        return notEmpty(array, VALID_ARRAY_IS_EMPTY);
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
        return notEmpty(collection, VALID_COLLECTION_IS_EMPTY);
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
        return notEmpty(map, VALID_MAP_IS_EMPTY);
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
        return notEmpty(s, VALID_STRING_IS_EMPTY);
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

    public static String notBlank(String s) {
        return notBlank(s, VALID_STRING_IS_BLANK);
    }

    public static String notBlank(String s, String message, Object... values) {
        if (Strings.isBlank(s)) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return s;
    }

    public static String isNumber(String s) {
        return isNumber(s, VALID_STRING_NOT_NUMBER);
    }

    public static String isNumber(String s, String message, Object... values) {
        if (!Strings.isNumber(s)) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return s;
    }

    public static String isInteger(String s) {
        return isInteger(s, VALID_STRING_NOT_INTEGER);
    }

    public static String isInteger(String s, String message, Object... values) {
        if (!Strings.isInteger(s)) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return s;
    }

    public static String isDouble(String s) {
        return isNumber(s, VALID_STRING_NOT_DOUBLE);
    }

    public static String isDouble(String s, String message, Object... values) {
        if (!Strings.isDouble(s)) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return s;
    }

    public static <T> T validIndex(T[] array, int index) {
        return validIndex(array, index, VALID_ARRAY_INDEX, index);
    }

    public static <T> T validIndex(T[] array, int index, String message, Object... values) {
        notNull(array);
        if (index < 0 || index >= array.length) {
            throw Exceptions.indexArgument(Strings.format(message, values));
        }
        return array[index];
    }

    public static <T extends Collection<?>> void validIndex(T collection, int index) {
        validIndex(collection, index, VALID_COLLECTION_INDEX, index);
    }

    public static <T extends Collection<?>> void validIndex(T collection, int index, String message, Object... values) {
        notNull(collection);
        if (index < 0 || index >= collection.size()) {
            throw Exceptions.indexArgument(Strings.format(message, values));
        }
    }

    public static <T> T[] noNullElements(T[] array) {
        return noNullElements(array, VALID_ARRAY_CONTENTS_NULL);
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
        return noNullElements(iterable, VALID_COLLECTION_CONTENTS_NULL);
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
        return notMatches(input, pattern, VALID_STRING_NOT_MATCH, input, pattern);
    }

    public static String isMatches(String input, String pattern, String message, Object... values) {
        if (!Pattern.matches(pattern, input)) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return input;
    }

    public static String notMatches(String input, String pattern) {
        return notMatches(input, pattern, VALID_STRING_MATCH, input, pattern);
    }

    public static String notMatches(String input, String pattern, String message, Object... values) {
        if (Pattern.matches(pattern, input)) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return input;
    }

    public static int notZero(int value) {
        return notZero(value, VALID_VALUE_IS_ZERO);
    }

    public static int notZero(int value, String message, Object... values) {
        if (value == 0) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return value;
    }

    public static long notZero(long value) {
        return notZero(value, VALID_VALUE_IS_ZERO);
    }

    public static long notZero(long value, String message, Object... values) {
        if (value == 0) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return value;
    }

    public static double notZero(double value) {
        return notZero(value, VALID_VALUE_IS_ZERO);
    }

    public static double notZero(double value, String message, Object... values) {
        if (value == 0) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return value;
    }

    public static BigDecimal notZero(BigDecimal value) {
        return notZero(value, VALID_VALUE_IS_ZERO);
    }

    public static BigDecimal notZero(BigDecimal value, String message, Object... values) {
        notNull(value);
        notCompare(BigDecimal.ZERO, value, message, values);
        return value;
    }

    public static BigInteger notZero(BigInteger value) {
        return notZero(value, VALID_VALUE_IS_ZERO);
    }

    public static BigInteger notZero(BigInteger value, String message, Object... values) {
        notNull(value);
        notCompare(BigInteger.ZERO, value, message, values);
        return value;
    }

    public static double notNaN(float value) {
        return notNaN(value, VALID_VALUE_IS_NAN);
    }

    public static double notNaN(float value, String message, Object... values) {
        if (Float.isNaN(value)) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return value;
    }

    public static double notNaN(double value) {
        return notNaN(value, VALID_VALUE_IS_NAN);
    }

    public static double notNaN(double value, String message, Object... values) {
        if (Double.isNaN(value)) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return value;
    }

    public static <T extends Comparable<T>> T inRange(T value, T start, T end) {
        return inRange(value, start, end, VALID_VALUE_NOT_IN_RANGE, value, start, end);
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
        return notInRange(start, end, value, VALID_VALUE_IN_RANGE, value, start, end);
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

    public static String validLength(String s, int length) {
        return validLength(s, length, VALID_STRING_LENGTH, length);
    }

    public static String validLength(String s, int length, String message, Object... values) {
        notNull(s);
        if (s.length() != length) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return s;
    }

    public static String validLengthIn(String s, int start, int end) {
        return validLengthIn(s, start, end, VALID_STRING_LENGTH_IN, start, end);
    }

    public static String validLengthIn(String s, int start, int end, String message, Object... values) {
        notNull(s);
        if (!Compares.inRange(s.length(), start, end)) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return s;
    }

    public static String validLengthGt(String s, int length) {
        return validLengthGt(s, length, VALID_LENGTH_NOT_GT, length);
    }

    public static String validLengthGt(String s, int length, String message, Object... values) {
        notNull(s);
        if (s.length() <= length) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return s;
    }

    public static String validLengthGte(String s, int length) {
        return validLengthGte(s, length, VALID_LENGTH_NOT_GT_EQ, length);
    }

    public static String validLengthGte(String s, int length, String message, Object... values) {
        notNull(s);
        if (s.length() < length) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return s;
    }

    public static String validLengthLt(String s, int length) {
        return validLengthLt(s, length, VALID_LENGTH_NOT_LT, length);
    }

    public static String validLengthLt(String s, int length, String message, Object... values) {
        notNull(s);
        if (s.length() >= length) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return s;
    }

    public static String validLengthLte(String s, int length) {
        return validLengthLte(s, length, VALID_LENGTH_NOT_LT_EQ, length);
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
            throw Exceptions.invalidArgument(Strings.format(VALID_NOT_INSTANCE, type.getName(), obj == null ? Const.NULL : obj.getClass().getName()));
        }
        return type.cast(obj);
    }

    public static <T> T isInstanceOf(Object obj, Class<T> type, String message, Object... values) {
        if (!type.isInstance(obj)) {
            throw Exceptions.invalidArgument(Strings.format(message, values));
        }
        return type.cast(obj);
    }

    public static <T extends IHttpResponse> T validHttpOk(T resp) {
        notNull(resp);
        int code = resp.getCode();
        if (code < 200 || code >= 300) {
            throw Exceptions.httpRequest(Strings.format(HTTP_REQ_NOT_OK, code, resp.getUrl()));
        }
        return resp;
    }

}
