package com.orion.lang.collect;

import com.orion.utils.ext.StringExt;
import com.orion.utils.*;
import com.orion.utils.math.BigIntegers;
import com.orion.utils.math.Decimals;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;

/**
 * 可转换的HashSet
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/2/29 20:50
 */
@SuppressWarnings("unchecked")
public class ConvertHashSet<E> extends HashSet<E> {

    public ConvertHashSet() {
        super();
    }

    public ConvertHashSet(Collection<? extends E> c) {
        super(c);
    }

    public ConvertHashSet(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public ConvertHashSet(int initialCapacity) {
        super(initialCapacity);
    }

    public void adds(E... es) {
        int length = Arrays1.length(es);
        for (int i = 0; i < length; i++) {
            add(es[i]);
        }
    }

    public E get(int i) {
        int idx = 0;
        for (E e : this) {
            if (idx++ == i) {
                return e;
            }
        }
        return null;
    }

    public List<E> get(int... is) {
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

    public Byte getByte(int i) {
        E e = get(i);
        if (e == null) {
            return null;
        }
        return Converts.toByte(e);
    }

    public Byte getByte(int i, Byte def) {
        E e = get(i);
        if (e == null) {
            return def;
        }
        return Converts.toByte(e);
    }

    public byte getByteValue(int i) {
        E e = get(i);
        if (e == null) {
            return 0;
        }
        return Converts.toByte(e);
    }

    public byte getByteValue(int i, byte def) {
        E e = get(i);
        if (e == null) {
            return def;
        }
        return Converts.toByte(e);
    }

    public Short getShort(int i) {
        E e = get(i);
        if (e == null) {
            return null;
        }
        return Converts.toShort(e);
    }

    public Short getShort(int i, Short def) {
        E e = get(i);
        if (e == null) {
            return def;
        }
        return Converts.toShort(e);
    }

    public short getShortValue(int i) {
        E e = get(i);
        if (e == null) {
            return 0;
        }
        return Converts.toShort(e);
    }

    public short getShortValue(int i, short def) {
        E e = get(i);
        if (e == null) {
            return def;
        }
        return Converts.toShort(e);
    }

    public Integer getInt(int i) {
        E e = get(i);
        if (e == null) {
            return null;
        }
        return Converts.toInt(e);
    }

    public Integer getInt(int i, Integer def) {
        E e = get(i);
        if (e == null) {
            return def;
        }
        return Converts.toInt(e);
    }

    public int getIntValue(int i) {
        E e = get(i);
        if (e == null) {
            return 0;
        }
        return Converts.toInt(e);
    }

    public int getIntValue(int i, int def) {
        E e = get(i);
        if (e == null) {
            return def;
        }
        return Converts.toInt(e);
    }

    public Long getLong(int i) {
        E e = get(i);
        if (e == null) {
            return null;
        }
        return Converts.toLong(e);
    }

    public Long getLong(int i, Long def) {
        E e = get(i);
        if (e == null) {
            return def;
        }
        return Converts.toLong(e);
    }

    public long getLongValue(int i) {
        E e = get(i);
        if (e == null) {
            return 0L;
        }
        return Converts.toLong(e);
    }

    public long getLongValue(int i, long def) {
        E e = get(i);
        if (e == null) {
            return def;
        }
        return Converts.toLong(e);
    }

    public Float getFloat(int i) {
        E e = get(i);
        if (e == null) {
            return null;
        }
        return Converts.toFloat(e);
    }

    public Float getFloat(int i, Float def) {
        E e = get(i);
        if (e == null) {
            return def;
        }
        return Converts.toFloat(e);
    }

    public float getFloatValue(int i) {
        E e = get(i);
        if (e == null) {
            return 0F;
        }
        return Converts.toFloat(e);
    }

    public float getFloatValue(int i, float def) {
        E e = get(i);
        if (e == null) {
            return def;
        }
        return Converts.toFloat(e);
    }

    public Double getDouble(int i) {
        E e = get(i);
        if (e == null) {
            return null;
        }
        return Converts.toDouble(e);
    }

    public Double getDouble(int i, Double def) {
        E e = get(i);
        if (e == null) {
            return def;
        }
        return Converts.toDouble(e);
    }

    public double getDoubleValue(int i) {
        E e = get(i);
        if (e == null) {
            return 0.0;
        }
        return Converts.toDouble(e);
    }

    public double getDoubleValue(int i, double def) {
        E e = get(i);
        if (e == null) {
            return def;
        }
        return Converts.toDouble(e);
    }

    public Boolean getBoolean(int i) {
        E e = get(i);
        if (e == null) {
            return null;
        }
        return Converts.toBoolean(e);
    }

    public Boolean getBoolean(int i, Boolean def) {
        E e = get(i);
        if (e == null) {
            return def;
        }
        return Converts.toBoolean(e);
    }

    public boolean getBooleanValue(int i) {
        E e = get(i);
        if (e == null) {
            return false;
        }
        return Converts.toBoolean(e);
    }

    public boolean getBooleanValue(int i, boolean def) {
        E e = get(i);
        if (e == null) {
            return def;
        }
        return Converts.toBoolean(e);
    }

    public Character getChar(int i) {
        E e = get(i);
        if (e == null) {
            return null;
        }
        return Converts.toChar(e);
    }

    public Character getChar(int i, Character def) {
        E e = get(i);
        if (e == null) {
            return def;
        }
        return Converts.toChar(e);
    }

    public char getCharValue(int i) {
        E e = get(i);
        if (e == null) {
            return 0;
        }
        return Converts.toChar(e);
    }

    public char getCharValue(int i, char def) {
        E e = get(i);
        if (e == null) {
            return def;
        }
        return Converts.toChar(e);
    }

    public String getString(int i) {
        return Converts.toString(get(i));
    }

    public String getString(int i, String def) {
        E e = get(i);
        if (e == null) {
            return def;
        }
        return Converts.toString(e);
    }

    public StringExt getStringExt(int i) {
        return new StringExt(Converts.toString(get(i)));
    }

    public StringExt getStringExt(int i, StringExt def) {
        E e = get(i);
        if (e == null) {
            return def;
        }
        return new StringExt(Converts.toString(e));
    }

    public StringExt getStringExt(int i, String def) {
        E e = get(i);
        if (e == null) {
            return new StringExt(def);
        }
        return new StringExt(Converts.toString(e));
    }

    public Date getDate(int i) {
        return Converts.toDate(get(i));
    }

    public Date getDate(int i, Date def) {
        E e = get(i);
        if (e == null) {
            return def;
        }
        return Converts.toDate(e);
    }

    public BigDecimal getBigDecimal(int i) {
        return Decimals.toDecimal(get(i));
    }

    public BigDecimal getBigDecimal(int i, BigDecimal def) {
        E e = get(i);
        if (e == null) {
            return def;
        }
        return Decimals.toDecimal(e);
    }

    public BigInteger getBigInteger(int i) {
        return BigIntegers.toBigInteger(get(i));
    }

    public BigInteger getBigInteger(int i, BigInteger def) {
        E e = get(i);
        if (e == null) {
            return def;
        }
        return BigIntegers.toBigInteger(e);
    }

    public <NK, NV> Map<NK, NV> getMap(int i) {
        E e = get(i);
        if (e instanceof Map) {
            return (Map<NK, NV>) e;
        } else {
            return null;
        }
    }

    public <NK, NV> Map<NK, NV> getMap(int i, Map<NK, NV> def) {
        E e = get(i);
        if (e instanceof Map) {
            return (Map<NK, NV>) e;
        } else {
            return def;
        }
    }

    public <NE> List<NE> getList(int i) {
        E e = get(i);
        if (e instanceof List) {
            return (List<NE>) e;
        } else {
            return null;
        }
    }

    public <NE> List<NE> getList(int i, List<NE> def) {
        E e = get(i);
        if (e instanceof List) {
            return (List<NE>) e;
        } else {
            return def;
        }
    }

    public <NE> Set<NE> getSet(int i) {
        E e = get(i);
        if (e instanceof Set) {
            return (Set<NE>) e;
        } else {
            return null;
        }
    }

    public <NE> Set<NE> getSet(int i, Set<NE> def) {
        E e = get(i);
        if (e instanceof Set) {
            return (Set<NE>) e;
        } else {
            return def;
        }
    }

    public <V> V getObject(int i, Function<E, V> f) {
        Valid.notNull(f);
        E e = get(i);
        return e == null ? null : f.apply(e);
    }

    public <V> V getObject(int i, E def, Function<E, V> f) {
        Valid.notNull(f);
        E e = get(i);
        return e == null ? f.apply(def) : f.apply(e);
    }

    public <V> V getObject(int i) {
        E e = get(i);
        if (e == null) {
            return null;
        }
        return (V) e;
    }

    public <V> V getObject(int i, V def) {
        E e = get(i);
        if (e == null) {
            return def;
        }
        return (V) e;
    }

    public E get(int i, E def) {
        E e = get(i);
        if (e == null) {
            return def;
        }
        return e;
    }

}
