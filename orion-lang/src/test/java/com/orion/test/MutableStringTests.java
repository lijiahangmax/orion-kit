package com.orion.test;

import com.orion.lang.mutable.MutableString;
import org.junit.Test;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/5/13 18:26
 */
public class MutableStringTests {

    @Test
    public void s() {
        MutableString e = new MutableString(null);
        System.out.println("----------byte----------");
        System.out.println(e.toByte());
        System.out.println(e.toByteValue());
        System.out.println(e.toByte((byte) 1));
        System.out.println(e.toByteValue((byte) 1));
        System.out.println("----------short----------");
        System.out.println(e.toShort());
        System.out.println(e.toShortValue());
        System.out.println(e.toShort((short) 1));
        System.out.println(e.toShortValue((short) 1));
        System.out.println("----------int----------");
        System.out.println(e.toInt());
        System.out.println(e.toIntValue());
        System.out.println(e.toInt(1));
        System.out.println(e.toIntValue(1));
        System.out.println("----------long----------");
        System.out.println(e.toLong());
        System.out.println(e.toLongValue());
        System.out.println(e.toLong(1L));
        System.out.println(e.toLongValue(1L));
        System.out.println("----------float----------");
        System.out.println(e.toFloat());
        System.out.println(e.toFloatValue());
        System.out.println(e.toFloat(1.1f));
        System.out.println(e.toFloatValue(1.1f));
        System.out.println("----------double----------");
        System.out.println(e.toDouble());
        System.out.println(e.toDoubleValue());
        System.out.println(e.toDouble(1.1));
        System.out.println(e.toDoubleValue(1.1));
        System.out.println("----------boolean----------");
        System.out.println(e.toBoolean());
        System.out.println(e.toBooleanValue());
        System.out.println(e.toBoolean(false));
        System.out.println(e.toBooleanValue(false));
        System.out.println("----------char----------");
        System.out.println(e.toChar());
        System.out.println(e.toCharValue());
        System.out.println(e.toChar('1'));
        System.out.println(e.toCharValue('1'));
        System.out.println("--------------------");
        System.out.println(e.get());
        System.out.println(e.get("1"));
        System.out.println(e.isEmpty());
        System.out.println(e.isNotEmpty());
        System.out.println(e.isBlank());
        System.out.println(e.isNotBlank());
        System.out.println(e.isNull());
        System.out.println(e.isNotNull());
        System.out.println(e.set("<123{}"));
        System.out.println(e.cleanXss());
        System.out.println(e.recodeXss());
        System.out.println(e.urlDecode());
        System.out.println(e.urlDecode());
        System.out.println(e.sign("SHA1"));
        System.out.println(e.sign("MD5"));
        System.out.println(e.concat("1", "2"));
        System.out.println(e.concatBefore("1", "2"));
        System.out.println(e.format(1));
    }

}
