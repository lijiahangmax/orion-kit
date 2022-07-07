package com.orion.lang.function;

import java.util.function.*;

/**
 * 提供者工具
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/1/23 10:50
 */
@SuppressWarnings("unchecked")
public class Suppliers {

    private Suppliers() {
    }

    /**
     * null 提供者
     */
    public static final Supplier<?> NULL_SUPPLER = () -> null;

    public static final ByteSupplier BYTE_SUPPLIER = () -> (byte) 0;

    public static final ShortSupplier SHORT_SUPPLIER = () -> (short) 0;

    public static final IntSupplier INT_SUPPLIER = () -> 0;

    public static final LongSupplier LONG_SUPPLIER = () -> 0L;

    public static final FloatSupplier FLOAT_SUPPLIER = () -> 0F;

    public static final DoubleSupplier DOUBLE_SUPPLIER = () -> 0D;

    public static final BooleanSupplier BOOLEAN_SUPPLIER = () -> false;

    public static final CharSupplier CHAR_SUPPLIER = () -> (char) 0;

    // -------------------- getter --------------------

    public static <T> Supplier<T> nullSupplier() {
        return (Supplier<T>) NULL_SUPPLER;
    }

}
