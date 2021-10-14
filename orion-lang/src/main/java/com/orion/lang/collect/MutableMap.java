package com.orion.lang.collect;

import com.orion.lang.mutable.MutableString;
import com.orion.lang.wrapper.Args;
import com.orion.lang.wrapper.Pair;
import com.orion.utils.Arrays1;
import com.orion.utils.Valid;
import com.orion.utils.convert.Converts;
import com.orion.utils.math.BigDecimals;
import com.orion.utils.math.BigIntegers;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;

/**
 * 可转换的Map
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/1/5 17:20
 */
@SuppressWarnings("ALL")
public interface MutableMap<K, V> extends Map<K, V> {

    default void puts(Args.Entry<K, V>... es) {
        int length = Arrays1.length(es);
        for (int i = 0; i < length; i++) {
            put(es[i].getKey(), es[i].getValue());
        }
    }

    default void puts(Map.Entry<K, V>... es) {
        int length = Arrays1.length(es);
        for (int i = 0; i < length; i++) {
            put(es[i].getKey(), es[i].getValue());
        }
    }

    default void puts(Pair<K, V>... es) {
        int length = Arrays1.length(es);
        for (int i = 0; i < length; i++) {
            put(es[i].getKey(), es[i].getValue());
        }
    }

    default List<V> gets(K... ks) {
        List<V> list = new ArrayList<>();
        int length = Arrays1.length(ks);
        for (int i = 0; i < length; i++) {
            list.add(get(ks[i]));
        }
        return list;
    }

    default Byte getByte(K k) {
        V v = get(k);
        if (v == null) {
            return null;
        }
        return Converts.toByte(v);
    }

    default Byte getByte(K k, Byte def) {
        V v = get(k);
        if (v == null) {
            return def;
        }
        return Converts.toByte(v);
    }

    default byte getByteValue(K k) {
        V v = get(k);
        if (v == null) {
            return 0;
        }
        return Converts.toByte(v);
    }

    default byte getByteValue(K k, byte def) {
        V v = get(k);
        if (v == null) {
            return def;
        }
        return Converts.toByte(v);
    }

    default Short getShort(K k) {
        V v = get(k);
        if (v == null) {
            return null;
        }
        return Converts.toShort(k);
    }

    default Short getShort(K k, Short def) {
        V v = get(k);
        if (v == null) {
            return def;
        }
        return Converts.toShort(k);
    }

    default short getShortValue(K k) {
        V v = get(k);
        if (v == null) {
            return 0;
        }
        return Converts.toShort(k);
    }

    default short getShortValue(K k, short def) {
        V v = get(k);
        if (v == null) {
            return def;
        }
        return Converts.toShort(k);
    }

    default Integer getInt(K k) {
        V v = get(k);
        if (v == null) {
            return null;
        }
        return Converts.toInt(k);
    }

    default Integer getInt(K k, Integer def) {
        V v = get(k);
        if (v == null) {
            return def;
        }
        return Converts.toInt(k);
    }

    default int getIntValue(K k) {
        V v = get(k);
        if (v == null) {
            return 0;
        }
        return Converts.toInt(k);
    }

    default int getIntValue(K k, int def) {
        V v = get(k);
        if (v == null) {
            return def;
        }
        return Converts.toInt(k);
    }

    default Long getLong(K k) {
        V v = get(k);
        if (v == null) {
            return null;
        }
        return Converts.toLong(v);
    }

    default Long getLong(K k, Long def) {
        V v = get(k);
        if (v == null) {
            return def;
        }
        return Converts.toLong(v);
    }

    default long getLongValue(K k) {
        V v = get(k);
        if (v == null) {
            return 0L;
        }
        return Converts.toLong(v);
    }

    default long getLongValue(K k, long def) {
        V v = get(k);
        if (v == null) {
            return def;
        }
        return Converts.toLong(v);
    }

    default Float getFloat(K k) {
        V v = get(k);
        if (v == null) {
            return null;
        }
        return Converts.toFloat(v);
    }

    default Float getFloat(K k, Float def) {
        V v = get(k);
        if (v == null) {
            return def;
        }
        return Converts.toFloat(v);
    }

    default float getFloatValue(K k) {
        V v = get(k);
        if (v == null) {
            return 0F;
        }
        return Converts.toFloat(v);
    }

    default float getFloatValue(K k, float def) {
        V v = get(k);
        if (v == null) {
            return def;
        }
        return Converts.toFloat(v);
    }

    default Double getDouble(K k) {
        V v = get(k);
        if (v == null) {
            return null;
        }
        return Converts.toDouble(v);
    }

    default Double getDouble(K k, Double def) {
        V v = get(k);
        if (v == null) {
            return def;
        }
        return Converts.toDouble(v);
    }

    default double getDoubleValue(K k) {
        V v = get(k);
        if (v == null) {
            return 0.0;
        }
        return Converts.toDouble(v);
    }

    default double getDoubleValue(K k, double def) {
        V v = get(k);
        if (v == null) {
            return def;
        }
        return Converts.toDouble(v);
    }

    default Boolean getBoolean(K k) {
        V v = get(k);
        if (v == null) {
            return null;
        }
        return Converts.toBoolean(v);
    }

    default Boolean getBoolean(K k, Boolean def) {
        V v = get(k);
        if (v == null) {
            return def;
        }
        return Converts.toBoolean(v);
    }

    default boolean getBooleanValue(K k) {
        V v = get(k);
        if (v == null) {
            return false;
        }
        return Converts.toBoolean(v);
    }

    default boolean getBooleanValue(K k, boolean def) {
        V v = get(k);
        if (v == null) {
            return def;
        }
        return Converts.toBoolean(v);
    }

    default Character getChar(K k) {
        V v = get(k);
        if (v == null) {
            return null;
        }
        return Converts.toChar(v);
    }

    default Character getChar(K k, Character def) {
        V v = get(k);
        if (v == null) {
            return def;
        }
        return Converts.toChar(v);
    }

    default char getCharValue(K k) {
        V v = get(k);
        if (v == null) {
            return 0;
        }
        return Converts.toChar(v);
    }

    default char getCharValue(K k, char def) {
        V v = get(k);
        if (v == null) {
            return def;
        }
        return Converts.toChar(v);
    }

    default String getString(K k) {
        return Converts.toString(get(k));
    }

    default String getString(K k, String def) {
        V v = get(k);
        if (v == null) {
            return def;
        }
        return Converts.toString(v);
    }

    default MutableString getMutableString(K k) {
        return new MutableString(Converts.toString(get(k)));
    }

    default MutableString getMutableString(K k, String def) {
        V v = get(k);
        if (v == null) {
            return new MutableString(def);
        }
        return new MutableString(Converts.toString(v));
    }

    default MutableString getMutableString(K k, MutableString def) {
        V v = get(k);
        if (v == null) {
            return def;
        }
        return new MutableString(Converts.toString(v));
    }

    default Date getDate(K k) {
        return Converts.toDate(get(k));
    }

    default Date getDate(K k, Date def) {
        V v = get(k);
        if (v == null) {
            return def;
        }
        return Converts.toDate(v);
    }

    default LocalDateTime getLocalDateTime(K k) {
        return Converts.toLocalDateTime(get(k));
    }

    default LocalDateTime getLocalDateTime(K k, LocalDateTime def) {
        V v = get(k);
        if (v == null) {
            return def;
        }
        return Converts.toLocalDateTime(v);
    }

    default LocalDate getLocalDate(K k) {
        return Converts.toLocalDate(get(k));
    }

    default LocalDate getLocalDate(K k, LocalDate def) {
        V v = get(k);
        if (v == null) {
            return def;
        }
        return Converts.toLocalDate(v);
    }

    default BigDecimal getBigDecimal(K k) {
        return BigDecimals.toBigDecimal(get(k));
    }

    default BigDecimal getBigDecimal(K k, BigDecimal def) {
        V v = get(k);
        if (v == null) {
            return def;
        }
        return BigDecimals.toBigDecimal(v);
    }

    default BigInteger getBigInteger(K k) {
        return BigIntegers.toBigInteger(get(k));
    }

    default BigInteger getBigInteger(K k, BigInteger def) {
        V v = get(k);
        if (v == null) {
            return def;
        }
        return BigIntegers.toBigInteger(v);
    }

    default <NK, NV> Map<NK, NV> getMap(String key) {
        V v = get(key);
        if (v instanceof Map) {
            return (Map<NK, NV>) v;
        } else {
            return null;
        }
    }

    default <NK, NV> Map<NK, NV> getMap(String key, Map<NK, NV> def) {
        V v = get(key);
        if (v instanceof Map) {
            return (Map<NK, NV>) v;
        } else {
            return def;
        }
    }

    default <NE> List<NE> getList(String key) {
        V v = get(key);
        if (v instanceof List) {
            return (List<NE>) v;
        } else {
            return null;
        }
    }

    default <NE> List<NE> getList(String key, List<NE> def) {
        V v = get(key);
        if (v instanceof List) {
            return (List<NE>) v;
        } else {
            return def;
        }
    }

    default <NE> Set<NE> getSet(String key) {
        V v = get(key);
        if (v instanceof Set) {
            return (Set<NE>) v;
        } else {
            return null;
        }
    }

    default <NE> Set<NE> getSet(String key, Set<NE> def) {
        V v = get(key);
        if (v instanceof Set) {
            return (Set<NE>) v;
        } else {
            return def;
        }
    }

    default <E> E getObject(K k, Function<V, E> f) {
        Valid.notNull(f);
        V v = get(k);
        return v == null ? null : f.apply(v);
    }

    default <E> E getObject(K k, V def, Function<V, E> f) {
        Valid.notNull(f);
        V v = get(k);
        return v == null ? f.apply(def) : f.apply(v);
    }

    default <E> E getObject(K k) {
        V v = get(k);
        if (v == null) {
            return null;
        }
        return (E) v;
    }

    default <E> E getObject(K k, E def) {
        V v = get(k);
        if (v == null) {
            return def;
        }
        return (E) v;
    }

    default V get(K k, V def) {
        V v = get(k);
        if (v == null) {
            return def;
        }
        return v;
    }

}
