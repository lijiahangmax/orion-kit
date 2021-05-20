package com.orion.utils;

/**
 * boolean工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/8/13 12:41
 */
public class Booleans {

    private Booleans() {
    }

    /**
     * 获取相反值
     *
     * @param bool bool
     * @return negate
     */
    public static Boolean negate(Boolean bool) {
        if (bool == null) {
            return null;
        }
        return bool ? Boolean.FALSE : Boolean.TRUE;
    }

    /**
     * 是否为true
     *
     * @param bool bool
     * @return ignore
     */
    public static boolean isTrue(Boolean bool) {
        return Boolean.TRUE.equals(bool);
    }

    /**
     * 是否不为true
     *
     * @param bool bool
     * @return ignore
     */
    public static boolean isNotTrue(Boolean bool) {
        return !Boolean.TRUE.equals(bool);
    }

    /**
     * 是否为false
     *
     * @param bool bool
     * @return ignore
     */
    public static boolean isFalse(Boolean bool) {
        return Boolean.FALSE.equals(bool);
    }

    /**
     * 是否不为false
     *
     * @param bool bool
     * @return ignore
     */
    public static boolean isNotFalse(Boolean bool) {
        return !Boolean.FALSE.equals(bool);
    }

    /**
     * Boolean -> boolean
     *
     * @param bool Boolean
     * @return boolean
     */
    public static boolean toBoolean(Boolean bool) {
        return bool != null && bool;
    }

    /**
     * int -> boolean
     *
     * @param value 0 | !0
     * @return 0false
     */
    public static boolean toBoolean(int value) {
        return value != 0;
    }

    public static String toString(boolean bool, String trueString, String falseString) {
        return bool ? trueString : falseString;
    }

    /**
     * 并且
     *
     * @param array boolean[]
     * @return 全为true则为true
     */
    public static boolean and(boolean... array) {
        if (array == null) {
            return false;
        }
        if (array.length == 0) {
            return false;
        }
        for (boolean element : array) {
            if (!element) {
                return false;
            }
        }
        return true;
    }

    /**
     * 并且
     *
     * @param array Boolean[]
     * @return 全为true则为true
     */
    public static Boolean and(Boolean... array) {
        if (array == null) {
            return false;
        }
        if (array.length == 0) {
            return false;
        }
        boolean[] primitive = Arrays1.drap(array);
        return and(primitive) ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * 或者
     *
     * @param array boolean[]
     * @return 一个为true则为true
     */
    public static boolean or(boolean... array) {
        if (array == null) {
            return false;
        }
        if (array.length == 0) {
            return false;
        }
        for (boolean element : array) {
            if (element) {
                return true;
            }
        }
        return false;
    }

    /**
     * 或者
     *
     * @param array Boolean[]
     * @return 一个为true则为true
     */
    public static Boolean or(Boolean... array) {
        if (array == null) {
            return false;
        }
        if (array.length == 0) {
            return false;
        }
        boolean[] primitive = Arrays1.drap(array);
        return or(primitive) ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * 比较
     *
     * @param x b1
     * @param y b2
     * @return compare
     */
    public static int compare(boolean x, boolean y) {
        if (x == y) {
            return 0;
        }
        if (x) {
            return 1;
        } else {
            return -1;
        }
    }

}
