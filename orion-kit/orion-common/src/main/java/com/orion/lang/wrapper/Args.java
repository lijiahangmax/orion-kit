package com.orion.lang.wrapper;

import java.io.Serializable;

/**
 * 多参数工具
 *
 * @author Li
 * @version 1.0.0
 * @date 2019/7/26 14:06
 */
@SuppressWarnings("ALL")
public class Args implements Serializable {

    private static final long serialVersionUID = -5879929949885108697L;

    private Args() {
    }

    public static Zero toArgs(Object... os) {
        if (os == null) {
            return of(null);
        } else if (os.length == 0) {
            return of();
        } else if (os.length == 1) {
            return of(os[0]);
        } else if (os.length == 2) {
            return of(os[0], os[1]);
        } else if (os.length == 3) {
            return of(os[0], os[1], os[2]);
        } else if (os.length == 4) {
            return of(os[0], os[1], os[2], os[3]);
        } else if (os.length == 5) {
            return of(os[0], os[1], os[2], os[3], os[4]);
        } else {
            throw new IllegalArgumentException("ConvertError: maxSize: 5, thisSize: " + os.length);
        }
    }

    public static Object[] toArray(Zero args) {
        if (args == null) {
            return new Object[]{};
        } else if (args instanceof Five) {
            Five five = (Five) args;
            return new Object[]{five.getArg1(), five.getArg2(), five.getArg3(), five.getArg4(), five.arg5};
        } else if (args instanceof Four) {
            Four four = (Four) args;
            return new Object[]{four.getArg1(), four.getArg2(), four.getArg3(), four.arg4};
        } else if (args instanceof Three) {
            Three three = (Three) args;
            return new Object[]{three.getArg1(), three.getArg2(), three.arg3};
        } else if (args instanceof Two) {
            Two two = (Two) args;
            return new Object[]{two.getArg1(), two.arg2};
        } else if (args instanceof One) {
            One one = (One) args;
            return new Object[]{one.arg1};
        } else {
            return new Object[]{};
        }
    }

    public static <K, V> Entry<K, V> entry() {
        return new Entry<>();
    }

    public static <K, V> Entry<K, V> entry(K k, V v) {
        return new Entry<>(k, v);
    }

    public static Zero zero() {
        return new Zero();
    }

    public static <A1> One<A1> one() {
        return new One<>();
    }

    public static <A1, A2> Two<A1, A2> two() {
        return new Two<>();
    }

    public static <A1, A2, A3> Three<A1, A2, A3> three() {
        return new Three<>();
    }

    public static <A1, A2, A3, A4> Four<A1, A2, A3, A4> four() {
        return new Four<>();
    }

    public static <A1, A2, A3, A4, A5> Five<A1, A2, A3, A4, A5> five() {
        return new Five<>();
    }

    public static Zero of() {
        return new Zero();
    }

    public static <A1> One<A1> of(A1 arg1) {
        return new One<>(arg1);
    }

    public static <A1, A2> Two<A1, A2> of(A1 arg1, A2 arg2) {
        return new Two<>(arg1, arg2);
    }

    public static <A1, A2, A3> Three<A1, A2, A3> of(A1 arg1, A2 arg2, A3 arg3) {
        return new Three<>(arg1, arg2, arg3);
    }

    public static <A1, A2, A3, A4> Four<A1, A2, A3, A4> of(A1 arg1, A2 arg2, A3 arg3, A4 arg4) {
        return new Four<>(arg1, arg2, arg3, arg4);
    }

    public static <A1, A2, A3, A4, A5> Five<A1, A2, A3, A4, A5> of(A1 arg1, A2 arg2, A3 arg3, A4 arg4, A5 arg5) {
        return new Five<>(arg1, arg2, arg3, arg4, arg5);
    }

    public static class Entry<K, V> implements Serializable {

        private static final long serialVersionUID = -2036635494283534L;

        private K key;

        private V value;

        private Entry() {
        }

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public Entry<K, V> setKey(K key) {
            this.key = key;
            return this;
        }

        public V getValue() {
            return value;
        }

        public Entry<K, V> setValue(V value) {
            this.value = value;
            return this;
        }

        @Override
        public String toString() {
            return "Entry{" +
                    "key=" + key +
                    ", value=" + value +
                    '}';
        }

    }

    public static class Zero implements Serializable {

        private static final long serialVersionUID = -4911462478280805L;

        public int length;

        {
            if (getClass() == Zero.class) {
                length = 0;
            } else if (getClass() == One.class) {
                length = 1;
            } else if (getClass() == Two.class) {
                length = 2;
            } else if (getClass() == Three.class) {
                length = 3;
            } else if (getClass() == Four.class) {
                length = 4;
            } else if (getClass() == Five.class) {
                length = 5;
            }
        }

        private Zero() {
        }

    }

    public static class One<A1> extends Zero {

        private static final long serialVersionUID = -8485735532097524L;

        private A1 arg1;

        private One() {
        }

        private One(A1 arg1) {
            this.arg1 = arg1;
        }

        public A1 getArg1() {
            return arg1;
        }

        public One<A1> setArg1(A1 arg1) {
            this.arg1 = arg1;
            return this;
        }

        @Override
        public String toString() {
            return "One{" +
                    "arg1=" + arg1 +
                    '}';
        }
    }

    public static class Two<A1, A2> extends One<A1> {

        private static final long serialVersionUID = -4818468600715154L;

        private A2 arg2;

        private Two() {
        }

        private Two(A1 arg1, A2 arg2) {
            super(arg1);
            this.arg2 = arg2;
        }

        public A2 getArg2() {
            return arg2;
        }

        public Two<A1, A2> setArg2(A2 arg2) {
            this.arg2 = arg2;
            return this;
        }

        @Override
        public String toString() {
            return "Two{" +
                    "arg1=" + getArg1() +
                    ", arg2=" + arg2 +
                    '}';
        }
    }

    public static class Three<A1, A2, A3> extends Two<A1, A2> {

        private static final long serialVersionUID = -9254528388442463L;

        private A3 arg3;

        private Three() {
        }

        private Three(A1 arg1, A2 arg2, A3 arg3) {
            super(arg1, arg2);
            this.arg3 = arg3;
        }

        public A3 getArg3() {
            return arg3;
        }

        public Three<A1, A2, A3> setArg3(A3 arg3) {
            this.arg3 = arg3;
            return this;
        }

        @Override
        public String toString() {
            return "Three{" +
                    "arg1=" + getArg1() +
                    ", arg2=" + getArg2() +
                    ", arg3=" + arg3 +
                    '}';
        }
    }

    public static class Four<A1, A2, A3, A4> extends Three<A1, A2, A3> {

        private static final long serialVersionUID = -6218493405659150L;

        private A4 arg4;

        private Four() {
        }

        private Four(A1 arg1, A2 arg2, A3 arg3, A4 arg4) {
            super(arg1, arg2, arg3);
            this.arg4 = arg4;
        }

        public A4 getArg4() {
            return arg4;
        }

        public Four<A1, A2, A3, A4> setArg4(A4 arg4) {
            this.arg4 = arg4;
            return this;
        }

        @Override
        public String toString() {
            return "Four{" +
                    "arg1=" + getArg1() +
                    ", arg2=" + getArg2() +
                    ", arg3=" + getArg3() +
                    ", arg4=" + arg4 +
                    '}';
        }
    }

    public static class Five<A1, A2, A3, A4, A5> extends Four<A1, A2, A3, A4> {

        private static final long serialVersionUID = -1861791979844559L;

        private A5 arg5;

        private Five() {
        }

        private Five(A1 arg1, A2 arg2, A3 arg3, A4 arg4, A5 arg5) {
            super(arg1, arg2, arg3, arg4);
            this.arg5 = arg5;
        }

        public A5 getArg5() {
            return arg5;
        }

        public Five<A1, A2, A3, A4, A5> setArg5(A5 arg5) {
            this.arg5 = arg5;
            return this;
        }

        @Override
        public String toString() {
            return "Five{" +
                    "arg1=" + getArg1() +
                    ", arg2=" + getArg2() +
                    ", arg3=" + getArg3() +
                    ", arg4=" + getArg4() +
                    ", arg5=" + arg5 +
                    '}';
        }
    }

}