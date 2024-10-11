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
package com.orion.lang.define.iterator;

import com.orion.lang.utils.Valid;
import com.orion.lang.utils.reflect.Classes;

import java.io.Serializable;
import java.util.Iterator;

/**
 * 父类迭代器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/1/7 18:52
 */
public class ClassIterator<T> implements Iterator<Class<? super T>>, Iterable<Class<? super T>>, Serializable {

    private final Class<T> clazz;

    private Class<? super T> current;

    /**
     * 是否包含 Object.class
     */
    private final boolean includeObject;

    public ClassIterator(Class<T> clazz) {
        this(clazz, false);
    }

    public ClassIterator(Class<T> clazz, boolean includeObject) {
        Valid.notNull(clazz, "class is null");
        this.current = this.clazz = clazz;
        this.includeObject = includeObject;
    }

    @Override
    public boolean hasNext() {
        if (clazz.getName().contains(Classes.CGLIB_CLASS_SEPARATOR)) {
            return false;
        }
        this.current = current.getSuperclass();
        if (current == null) {
            return false;
        }
        return includeObject || !Object.class.equals(current);
    }

    /**
     * @return 父类
     */
    @Override
    public Class<? super T> next() {
        return current;
    }

    @Override
    public Iterator<Class<? super T>> iterator() {
        return this;
    }

}
