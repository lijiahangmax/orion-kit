/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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
package cn.orionsec.kit.lang.define.builder;

import cn.orionsec.kit.lang.able.Buildable;
import cn.orionsec.kit.lang.utils.Objects1;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.Valid;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * 字符串构建器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/2/10 10:20
 */
public class StringJoiner implements Buildable<String> {

    private String prefix;

    private String suffix;

    private String delimiter;

    private Predicate<String> filter;

    private Function<String, String> wrapper;

    private final List<String> modifiers;

    public StringJoiner() {
        this.modifiers = new ArrayList<>();
    }

    public StringJoiner(String delimiter) {
        this(delimiter, null, null);
    }

    public StringJoiner(String delimiter, String prefix, String suffix) {
        this.delimiter = delimiter;
        this.prefix = prefix;
        this.suffix = suffix;
        this.modifiers = new ArrayList<>();
    }

    public static StringJoiner of() {
        return new StringJoiner();
    }

    public static StringJoiner of(String delimiter) {
        return new StringJoiner(delimiter);
    }

    public static StringJoiner of(Supplier<String> delimiterSupplier) {
        Valid.notNull(delimiterSupplier, "delimiter supplier is null");
        return new StringJoiner(delimiterSupplier.get());
    }

    public static StringJoiner of(String delimiter, String prefix, String suffix) {
        return new StringJoiner(delimiter, prefix, suffix);
    }

    /**
     * 设置标识符
     *
     * @param delimiter delimiter
     * @return this
     */
    public StringJoiner delimiter(String delimiter) {
        this.delimiter = delimiter;
        return this;
    }

    /**
     * 设置标识符
     *
     * @param delimiterSupplier delimiter
     * @return this
     */
    public StringJoiner delimiter(Supplier<String> delimiterSupplier) {
        Valid.notNull(delimiterSupplier, "delimiter supplier is null");
        this.delimiter = delimiterSupplier.get();
        return this;
    }

    /**
     * 设置前缀
     *
     * @param prefix prefix
     * @return this
     */
    public StringJoiner prefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    /**
     * 设置前缀
     *
     * @param prefixSupplier prefix
     * @return this
     */
    public StringJoiner prefix(Supplier<String> prefixSupplier) {
        Valid.notNull(prefixSupplier, "prefix supplier is null");
        this.prefix = prefixSupplier.get();
        return this;
    }

    /**
     * 设置后缀
     *
     * @param suffix suffix
     * @return this
     */
    public StringJoiner suffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    /**
     * 设置后缀
     *
     * @param suffixSupplier suffix
     * @return this
     */
    public StringJoiner suffix(Supplier<String> suffixSupplier) {
        Valid.notNull(suffixSupplier, "suffix supplier is null");
        this.suffix = suffixSupplier.get();
        return this;
    }

    /**
     * 跳过null
     *
     * @return this
     */
    public StringJoiner skipNull() {
        this.addFilter(Objects::nonNull);
        return this;
    }

    /**
     * 跳过空串
     *
     * @return this
     */
    public StringJoiner skipEmpty() {
        this.addFilter(Strings::isNotEmpty);
        return this;
    }

    /**
     * 跳过空串
     *
     * @return this
     */
    public StringJoiner skipBlank() {
        this.addFilter(Strings::isNotBlank);
        return this;
    }

    /**
     * 过滤
     *
     * @param filter 过滤器
     * @return this
     */
    public StringJoiner filter(Predicate<String> filter) {
        this.addFilter(filter);
        return this;
    }

    /**
     * 添加过滤器
     *
     * @param filter 过滤器
     */
    private void addFilter(Predicate<String> filter) {
        Valid.notNull(filter, "filter is null");
        if (this.filter == null) {
            this.filter = filter;
        }
        this.filter = this.filter.and(filter);
    }

    /**
     * 包装器
     *
     * @param wrapper wrapper
     * @return this
     */
    public StringJoiner wrapper(Function<String, String> wrapper) {
        Valid.notNull(wrapper, "wrapper is null");
        this.wrapper = wrapper;
        return this;
    }

    public StringJoiner with(String s) {
        modifiers.add(s);
        return this;
    }

    public StringJoiner with(Object o) {
        modifiers.add(Objects1.toString(o));
        return this;
    }

    public StringJoiner with(Supplier<String> supplier) {
        Valid.notNull(supplier, "supplier is null");
        modifiers.add(supplier.get());
        return this;
    }

    @Override
    public String build() {
        StringBuilder builder = new StringBuilder();
        if (prefix != null) {
            builder.append(prefix);
        }
        boolean add = false;
        for (String modifier : modifiers) {
            if (filter != null) {
                if (!filter.test(modifier)) {
                    continue;
                }
            }
            if (wrapper != null) {
                modifier = wrapper.apply(modifier);
            }
            builder.append(modifier);
            if (delimiter != null) {
                builder.append(delimiter);
                add = true;
            }
        }
        if (add) {
            builder.deleteCharAt(builder.length() - 1);
        }
        if (suffix != null) {
            builder.append(suffix);
        }
        return builder.toString();
    }

}
