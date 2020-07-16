package com.orion.lang.collect;

import com.orion.lang.MapEntry;
import com.orion.utils.ext.StringExt;
import com.orion.lang.wrapper.Args;
import com.orion.utils.*;
import com.orion.utils.math.BigIntegers;
import com.orion.utils.math.BigDecimals;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;

/**
 * 可转换的HashMap
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/2/27 1:03
 */
@SuppressWarnings("unchecked")
public class ConvertHashMap<K, V> extends HashMap<K, V> {

    public ConvertHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public ConvertHashMap(int initialCapacity) {
        super(initialCapacity);
    }

    public ConvertHashMap() {
        super();
    }

    public ConvertHashMap(Map<? extends K, ? extends V> m) {
        super(m);
    }

    public void puts(Args.Entry<K, V>... es) {
        int length = Arrays1.length(es);
        for (int i = 0; i < length; i++) {
            put(es[i].getKey(), es[i].getValue());
        }
    }

    public void puts(Entry<K, V>... es) {
        int length = Arrays1.length(es);
        for (int i = 0; i < length; i++) {
            put(es[i].getKey(), es[i].getValue());
        }
    }

    public void puts(MapEntry<K, V>... es) {
        int length = Arrays1.length(es);
        for (int i = 0; i < length; i++) {
            put(es[i].getKey(), es[i].getValue());
        }
    }

    public List<V> gets(K... ks) {
        List<V> list = new ArrayList<>();
        int length = Arrays1.length(ks);
        for (int i = 0; i < length; i++) {
            list.add(get(ks[i]));
        }
        return list;
    }

    public Byte getByte(K k) {
        V v = get(k);
        if (v == null) {
            return null;
        }
        return Converts.toByte(v);
    }

    public Byte getByte(K k, Byte def) {
        V v = get(k);
        if (v == null) {
            return def;
        }
        return Converts.toByte(v);
    }

    public byte getByteValue(K k) {
        V v = get(k);
        if (v == null) {
            return 0;
        }
        return Converts.toByte(v);
    }

    public byte getByteValue(K k, byte def) {
        V v = get(k);
        if (v == null) {
            return def;
        }
        return Converts.toByte(v);
    }

    public Short getShort(K k) {
        V v = get(k);
        if (v == null) {
            return null;
        }
        return Converts.toShort(k);
    }

    public Short getShort(K k, Short def) {
        V v = get(k);
        if (v == null) {
            return def;
        }
        return Converts.toShort(k);
    }

    public short getShortValue(K k) {
        V v = get(k);
        if (v == null) {
            return 0;
        }
        return Converts.toShort(k);
    }

    public short getShortValue(K k, short def) {
        V v = get(k);
        if (v == null) {
            return def;
        }
        return Converts.toShort(k);
    }

    public Integer getInt(K k) {
        V v = get(k);
        if (v == null) {
            return null;
        }
        return Converts.toInt(k);
    }

    public Integer getInt(K k, Integer def) {
        V v = get(k);
        if (v == null) {
            return def;
        }
        return Converts.toInt(k);
    }

    public int getIntValue(K k) {
        V v = get(k);
        if (v == null) {
            return 0;
        }
        return Converts.toInt(k);
    }

    public int getIntValue(K k, int def) {
        V v = get(k);
        if (v == null) {
            return def;
        }
        return Converts.toInt(k);
    }

    public Long getLong(K k) {
        V v = get(k);
        if (v == null) {
            return null;
        }
        return Converts.toLong(v);
    }

    public Long getLong(K k, Long def) {
        V v = get(k);
        if (v == null) {
            return def;
        }
        return Converts.toLong(v);
    }

    public long getLongValue(K k) {
        V v = get(k);
        if (v == null) {
            return 0L;
        }
        return Converts.toLong(v);
    }

    public long getLongValue(K k, long def) {
        V v = get(k);
        if (v == null) {
            return def;
        }
        return Converts.toLong(v);
    }

    public Float getFloat(K k) {
        V v = get(k);
        if (v == null) {
            return null;
        }
        return Converts.toFloat(v);
    }

    public Float getFloat(K k, Float def) {
        V v = get(k);
        if (v == null) {
            return def;
        }
        return Converts.toFloat(v);
    }

    public float getFloatValue(K k) {
        V v = get(k);
        if (v == null) {
            return 0F;
        }
        return Converts.toFloat(v);
    }

    public float getFloatValue(K k, float def) {
        V v = get(k);
        if (v == null) {
            return def;
        }
        return Converts.toFloat(v);
    }

    public Double getDouble(K k) {
        V v = get(k);
        if (v == null) {
            return null;
        }
        return Converts.toDouble(v);
    }

    public Double getDouble(K k, Double def) {
        V v = get(k);
        if (v == null) {
            return def;
        }
        return Converts.toDouble(v);
    }

    public double getDoubleValue(K k) {
        V v = get(k);
        if (v == null) {
            return 0.0;
        }
        return Converts.toDouble(v);
    }

    public double getDoubleValue(K k, double def) {
        V v = get(k);
        if (v == null) {
            return def;
        }
        return Converts.toDouble(v);
    }

    public Boolean getBoolean(K k) {
        V v = get(k);
        if (v == null) {
            return null;
        }
        return Converts.toBoolean(v);
    }

    public Boolean getBoolean(K k, Boolean def) {
        V v = get(k);
        if (v == null) {
            return def;
        }
        return Converts.toBoolean(v);
    }

    public boolean getBooleanValue(K k) {
        V v = get(k);
        if (v == null) {
            return false;
        }
        return Converts.toBoolean(v);
    }

    public boolean getBooleanValue(K k, boolean def) {
        V v = get(k);
        if (v == null) {
            return def;
        }
        return Converts.toBoolean(v);
    }

    public Character getChar(K k) {
        V v = get(k);
        if (v == null) {
            return null;
        }
        return Converts.toChar(v);
    }

    public Character getChar(K k, Character def) {
        V v = get(k);
        if (v == null) {
            return def;
        }
        return Converts.toChar(v);
    }

    public char getCharValue(K k) {
        V v = get(k);
        if (v == null) {
            return 0;
        }
        return Converts.toChar(v);
    }

    public char getCharValue(K k, char def) {
        V v = get(k);
        if (v == null) {
            return def;
        }
        return Converts.toChar(v);
    }

    public String getString(K k) {
        return Converts.toString(get(k));
    }

    public String getString(K k, String def) {
        V v = get(k);
        if (v == null) {
            return def;
        }
        return Converts.toString(v);
    }

    public StringExt getStringExt(K k) {
        return new StringExt(Converts.toString(get(k)));
    }

    public StringExt getStringExt(K k, String def) {
        V v = get(k);
        if (v == null) {
            return new StringExt(def);
        }
        return new StringExt(Converts.toString(v));
    }

    public StringExt getStringExt(K k, StringExt def) {
        V v = get(k);
        if (v == null) {
            return def;
        }
        return new StringExt(Converts.toString(v));
    }

    public Date getDate(K k) {
        return Converts.toDate(get(k));
    }

    public Date getDate(K k, Date def) {
        V v = get(k);
        if (v == null) {
            return def;
        }
        return Converts.toDate(v);
    }

    public LocalDateTime getLocalDateTime(K k) {
        return Converts.toLocalDateTime(get(k));
    }

    public LocalDateTime getLocalDateTime(K k, LocalDateTime def) {
        V v = get(k);
        if (v == null) {
            return def;
        }
        return Converts.toLocalDateTime(v);
    }

    public LocalDate getLocalDate(K k) {
        return Converts.toLocalDate(get(k));
    }

    public LocalDate getLocalDate(K k, LocalDate def) {
        V v = get(k);
        if (v == null) {
            return def;
        }
        return Converts.toLocalDate(v);
    }

    public BigDecimal getBigDecimal(K k) {
        return BigDecimals.toBigDecimal(get(k));
    }

    public BigDecimal getBigDecimal(K k, BigDecimal def) {
        V v = get(k);
        if (v == null) {
            return def;
        }
        return BigDecimals.toBigDecimal(v);
    }

    public BigInteger getBigInteger(K k) {
        return BigIntegers.toBigInteger(get(k));
    }

    public BigInteger getBigInteger(K k, BigInteger def) {
        V v = get(k);
        if (v == null) {
            return def;
        }
        return BigIntegers.toBigInteger(v);
    }

    public <NK, NV> Map<NK, NV> getMap(String key) {
        V v = get(key);
        if (v instanceof Map) {
            return (Map<NK, NV>) v;
        } else {
            return null;
        }
    }

    public <NK, NV> Map<NK, NV> getMap(String key, Map<NK, NV> def) {
        V v = get(key);
        if (v instanceof Map) {
            return (Map<NK, NV>) v;
        } else {
            return def;
        }
    }

    public <NE> List<NE> getList(String key) {
        V v = get(key);
        if (v instanceof List) {
            return (List<NE>) v;
        } else {
            return null;
        }
    }

    public <NE> List<NE> getList(String key, List<NE> def) {
        V v = get(key);
        if (v instanceof List) {
            return (List<NE>) v;
        } else {
            return def;
        }
    }

    public <NE> Set<NE> getSet(String key) {
        V v = get(key);
        if (v instanceof Set) {
            return (Set<NE>) v;
        } else {
            return null;
        }
    }

    public <NE> Set<NE> getSet(String key, Set<NE> def) {
        V v = get(key);
        if (v instanceof Set) {
            return (Set<NE>) v;
        } else {
            return def;
        }
    }

    public <E> E getObject(K k, Function<V, E> f) {
        Valid.notNull(f);
        V v = get(k);
        return v == null ? null : f.apply(v);
    }

    public <E> E getObject(K k, V def, Function<V, E> f) {
        Valid.notNull(f);
        V v = get(k);
        return v == null ? f.apply(def) : f.apply(v);
    }

    public <E> E getObject(K k) {
        V v = get(k);
        if (v == null) {
            return null;
        }
        return (E) v;
    }

    public <E> E getObject(K k, E def) {
        V v = get(k);
        if (v == null) {
            return def;
        }
        return (E) v;
    }

    public V get(K k, V def) {
        V v = get(k);
        if (v == null) {
            return def;
        }
        return v;

    }

}
