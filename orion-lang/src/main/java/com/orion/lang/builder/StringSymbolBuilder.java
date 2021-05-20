package com.orion.lang.builder;

import com.orion.able.Buildable;
import com.orion.utils.Strings;
import com.orion.utils.Valid;

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
public class StringSymbolBuilder implements Buildable<String> {

    private String prefix;

    private String suffix;

    private String symbol;

    private Predicate<String> filter;

    private Function<String, String> wrapper;

    private List<String> modifiers = new ArrayList<>();

    public StringSymbolBuilder() {
    }

    public StringSymbolBuilder(String symbol) {
        this.symbol = symbol;
    }

    public StringSymbolBuilder(String prefix, String suffix, String symbol) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.symbol = symbol;
    }

    public static StringSymbolBuilder of() {
        return new StringSymbolBuilder();
    }

    public static StringSymbolBuilder of(String symbol) {
        return new StringSymbolBuilder(symbol);
    }

    public static StringSymbolBuilder of(Supplier<String> symbolSupplier) {
        Valid.notNull(symbolSupplier, "symbol supplier is null");
        return new StringSymbolBuilder(symbolSupplier.get());
    }

    public static StringSymbolBuilder of(String prefix, String suffix, String symbol) {
        return new StringSymbolBuilder(prefix, suffix, symbol);
    }

    /**
     * 设置标识符
     *
     * @param symbol symbol
     * @return this
     */
    public StringSymbolBuilder symbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    /**
     * 设置标识符
     *
     * @param symbolSupplier symbol
     * @return this
     */
    public StringSymbolBuilder symbol(Supplier<String> symbolSupplier) {
        Valid.notNull(symbolSupplier, "symbol supplier is null");
        this.symbol = symbolSupplier.get();
        return this;
    }

    /**
     * 设置前缀
     *
     * @param prefix prefix
     * @return this
     */
    public StringSymbolBuilder prefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    /**
     * 设置前缀
     *
     * @param prefixSupplier prefix
     * @return this
     */
    public StringSymbolBuilder prefix(Supplier<String> prefixSupplier) {
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
    public StringSymbolBuilder suffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    /**
     * 设置后缀
     *
     * @param suffixSupplier suffix
     * @return this
     */
    public StringSymbolBuilder suffix(Supplier<String> suffixSupplier) {
        Valid.notNull(suffixSupplier, "suffix supplier is null");
        this.suffix = suffixSupplier.get();
        return this;
    }

    /**
     * 跳过null
     *
     * @return this
     */
    public StringSymbolBuilder skipNull() {
        addFilter(Objects::nonNull);
        return this;
    }

    /**
     * 跳过空串
     *
     * @return this
     */
    public StringSymbolBuilder skipEmpty() {
        addFilter(Strings::isNotEmpty);
        return this;
    }

    /**
     * 跳过空串
     *
     * @return this
     */
    public StringSymbolBuilder skipBlank() {
        addFilter(Strings::isNotBlank);
        return this;
    }

    /**
     * 过滤
     *
     * @param filter 过滤器
     * @return this
     */
    public StringSymbolBuilder filter(Predicate<String> filter) {
        addFilter(filter);
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
    public StringSymbolBuilder wrapper(Function<String, String> wrapper) {
        Valid.notNull(wrapper, "wrapper is null");
        this.wrapper = wrapper;
        return this;
    }

    public StringSymbolBuilder with(String s) {
        modifiers.add(s);
        return this;
    }

    public StringSymbolBuilder with(Supplier<String> supplier) {
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
            if (symbol != null) {
                builder.append(symbol);
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
