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

    private Suppliers() {
    }

    // -------------------- getter --------------------

    public static <T> Supplier<T> nullSupplier() {
        return (Supplier<T>) NULL_SUPPLER;
    }

}
