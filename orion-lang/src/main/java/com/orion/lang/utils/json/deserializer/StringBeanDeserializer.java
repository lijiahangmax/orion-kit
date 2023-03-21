package com.orion.lang.utils.json.deserializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import java.lang.reflect.Type;

/**
 * string bean 反序列化
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/1/10 16:36
 */
public class StringBeanDeserializer implements ObjectDeserializer {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        String val = parser.getInput();
        if (val == null) {
            return null;
        }
        return (T) JSON.parseObject(val, type);
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }

}
