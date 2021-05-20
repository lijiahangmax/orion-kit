package com.orion.lang.collect;

import com.orion.lang.mutable.MutableString;
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
 * 可转换的Set
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/1/5 17:29
 */
@SuppressWarnings("ALL")
public interface MutableSet<E> extends Set<E> {

    default void adds(E... es) {
        int length = Arrays1.length(es);
        for (int i = 0; i < length; i++) {
            add(es[i]);
        }
    }

    default E get(int i) {
        int idx = 0;
        for (E e : this) {
            if (idx++ == i) {
                return e;
            }
        }
        return null;
    }

    default List<E> get(int... is) {
        int length = Arrays1.length(is);
        if (length == 0) {
            return new ArrayList<>();
        }
        List<E> r = new ArrayList<>();
        Arrays.sort(is);
        int idx = 0, isidx = 0;
        for (E e : this) {
            if (idx++ == is[isidx]) {
                isidx++;
                r.add(e);
            }
        }
        return r;
    }

    default Byte getByte(int i) {
        E e = get(i);
        if (e == null) {
            return null;
        }
        return Converts.toByte(e);
    }

    default Byte getByte(int i, Byte def) {
        E e = get(i);
        if (e == null) {
            return def;
        }
        return Converts.toByte(e);
    }

    default byte getByteValue(int i) {
        E e = get(i);
        if (e == null) {
            return 0;
        }
        return Converts.toByte(e);
    }

    default byte getByteValue(int i, byte def) {
        E e = get(i);
        if (e == null) {
            return def;
        }
        return Converts.toByte(e);
    }

    default Short getShort(int i) {
        E e = get(i);
        if (e == null) {
            return null;
        }
        return Converts.toShort(e);
    }

    default Short getShort(int i, Short def) {
        E e = get(i);
        if (e == null) {
            return def;
        }
        return Converts.toShort(e);
    }

    default short getShortValue(int i) {
        E e = get(i);
        if (e == null) {
            return 0;
        }
        return Converts.toShort(e);
    }

    default short getShortValue(int i, short def) {
        E e = get(i);
        if (e == null) {
            return def;
        }
        return Converts.toShort(e);
    }

    default Integer getInt(int i) {
        E e = get(i);
        if (e == null) {
            return null;
        }
        return Converts.toInt(e);
    }

    default Integer getInt(int i, Integer def) {
        E e = get(i);
        if (e == null) {
            return def;
        }
        return Converts.toInt(e);
    }

    default int getIntValue(int i) {
        E e = get(i);
        if (e == null) {
            return 0;
        }
        return Converts.toInt(e);
    }

    default int getIntValue(int i, int def) {
        E e = get(i);
        if (e == null) {
            return def;
        }
        return Converts.toInt(e);
    }

    default Long getLong(int i) {
        E e = get(i);
        if (e == null) {
            return null;
        }
        return Converts.toLong(e);
    }

    default Long getLong(int i, Long def) {
        E e = get(i);
        if (e == null) {
            return def;
        }
        return Converts.toLong(e);
    }

    default long getLongValue(int i) {
        E e = get(i);
        if (e == null) {
            return 0L;
        }
        return Converts.toLong(e);
    }

    default long getLongValue(int i, long def) {
        E e = get(i);
        if (e == null) {
            return def;
        }
        return Converts.toLong(e);
    }

    default Float getFloat(int i) {
        E e = get(i);
        if (e == null) {
            return null;
        }
        return Converts.toFloat(e);
    }

    default Float getFloat(int i, Float def) {
        E e = get(i);
        if (e == null) {
            return def;
        }
        return Converts.toFloat(e);
    }

    default float getFloatValue(int i) {
        E e = get(i);
        if (e == null) {
            return 0F;
        }
        return Converts.toFloat(e);
    }

    default float getFloatValue(int i, float def) {
        E e = get(i);
        if (e == null) {
            return def;
        }
        return Converts.toFloat(e);
    }

    default Double getDouble(int i) {
        E e = get(i);
        if (e == null) {
            return null;
        }
        return Converts.toDouble(e);
    }

    default Double getDouble(int i, Double def) {
        E e = get(i);
        if (e == null) {
            return def;
        }
        return Converts.toDouble(e);
    }

    default double getDoubleValue(int i) {
        E e = get(i);
        if (e == null) {
            return 0.0;
        }
        return Converts.toDouble(e);
    }

    default double getDoubleValue(int i, double def) {
        E e = get(i);
        if (e == null) {
            return def;
        }
        return Converts.toDouble(e);
    }

    default Boolean getBoolean(int i) {
        E e = get(i);
        if (e == null) {
            return null;
        }
        return Converts.toBoolean(e);
    }

    default Boolean getBoolean(int i, Boolean def) {
        E e = get(i);
        if (e == null) {
            return def;
        }
        return Converts.toBoolean(e);
    }

    default boolean getBooleanValue(int i) {
        E e = get(i);
        if (e == null) {
            return false;
        }
        return Converts.toBoolean(e);
    }

    default boolean getBooleanValue(int i, boolean def) {
        E e = get(i);
        if (e == null) {
            return def;
        }
        return Converts.toBoolean(e);
    }

    default Character getChar(int i) {
        E e = get(i);
        if (e == null) {
            return null;
        }
        return Converts.toChar(e);
    }

    default Character getChar(int i, Character def) {
        E e = get(i);
        if (e == null) {
            return def;
        }
        return Converts.toChar(e);
    }

    default char getCharValue(int i) {
        E e = get(i);
        if (e == null) {
            return 0;
        }
        return Converts.toChar(e);
    }

    default char getCharValue(int i, char def) {
        E e = get(i);
        if (e == null) {
            return def;
        }
        return Converts.toChar(e);
    }

    default String getString(int i) {
        return Converts.toString(get(i));
    }

    default String getString(int i, String def) {
        E e = get(i);
        if (e == null) {
            return def;
        }
        return Converts.toString(e);
    }

    default MutableString getStringExt(int i) {
        return new MutableString(Converts.toString(get(i)));
    }

    default MutableString getStringExt(int i, MutableString def) {
        E e = get(i);
        if (e == null) {
            return def;
        }
        return new MutableString(Converts.toString(e));
    }

    default MutableString getStringExt(int i, String def) {
        E e = get(i);
        if (e == null) {
            return new MutableString(def);
        }
        return new MutableString(Converts.toString(e));
    }

    default Date getDate(int i) {
        return Converts.toDate(get(i));
    }

    default Date getDate(int i, Date def) {
        E e = get(i);
        if (e == null) {
            return def;
        }
        return Converts.toDate(e);
    }

    default LocalDateTime getLocalDateTime(int i) {
        return Converts.toLocalDateTime(get(i));
    }

    default LocalDateTime getLocalDateTime(int i, LocalDateTime def) {
        E e = get(i);
        if (e == null) {
            return def;
        }
        return Converts.toLocalDateTime(e);
    }

    default LocalDate getLocalDate(int i) {
        return Converts.toLocalDate(get(i));
    }

    default LocalDate getLocalDateTime(int i, LocalDate def) {
        E e = get(i);
        if (e == null) {
            return def;
        }
        return Converts.toLocalDate(e);
    }

    default BigDecimal getBigDecimal(int i) {
        return BigDecimals.toBigDecimal(get(i));
    }

    default BigDecimal getBigDecimal(int i, BigDecimal def) {
        E e = get(i);
        if (e == null) {
            return def;
        }
        return BigDecimals.toBigDecimal(e);
    }

    default BigInteger getBigInteger(int i) {
        return BigIntegers.toBigInteger(get(i));
    }

    default BigInteger getBigInteger(int i, BigInteger def) {
        E e = get(i);
        if (e == null) {
            return def;
        }
        return BigIntegers.toBigInteger(e);
    }

    default <NK, NV> Map<NK, NV> getMap(int i) {
        E e = get(i);
        if (e instanceof Map) {
            return (Map<NK, NV>) e;
        } else {
            return null;
        }
    }

    default <NK, NV> Map<NK, NV> getMap(int i, Map<NK, NV> def) {
        E e = get(i);
        if (e instanceof Map) {
            return (Map<NK, NV>) e;
        } else {
            return def;
        }
    }

    default <NE> List<NE> getList(int i) {
        E e = get(i);
        if (e instanceof List) {
            return (List<NE>) e;
        } else {
            return null;
        }
    }

    default <NE> List<NE> getList(int i, List<NE> def) {
        E e = get(i);
        if (e instanceof List) {
            return (List<NE>) e;
        } else {
            return def;
        }
    }

    default <NE> Set<NE> getSet(int i) {
        E e = get(i);
        if (e instanceof Set) {
            return (Set<NE>) e;
        } else {
            return null;
        }
    }

    default <NE> Set<NE> getSet(int i, Set<NE> def) {
        E e = get(i);
        if (e instanceof Set) {
            return (Set<NE>) e;
        } else {
            return def;
        }
    }

    default <V> V getObject(int i, Function<E, V> f) {
        Valid.notNull(f);
        E e = get(i);
        return e == null ? null : f.apply(e);
    }

    default <V> V getObject(int i, E def, Function<E, V> f) {
        Valid.notNull(f);
        E e = get(i);
        return e == null ? f.apply(def) : f.apply(e);
    }

    default <V> V getObject(int i) {
        E e = get(i);
        if (e == null) {
            return null;
        }
        return (V) e;
    }

    default <V> V getObject(int i, V def) {
        E e = get(i);
        if (e == null) {
            return def;
        }
        return (V) e;
    }

    default E get(int i, E def) {
        E e = get(i);
        if (e == null) {
            return def;
        }
        return e;
    }

}
