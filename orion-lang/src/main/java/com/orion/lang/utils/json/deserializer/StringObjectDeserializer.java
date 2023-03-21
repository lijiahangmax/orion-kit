package com.orion.lang.utils.json.deserializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import java.lang.reflect.Type;

/**
 * string json 反序列化
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/1/10 16:36
 */
public class StringObjectDeserializer implements ObjectDeserializer {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        Object parse = parser.parse();
        if (parse == null) {
            return null;
        }
        if (parse instanceof String) {
            return (T) JSON.parseObject((String) parse);
        } else {
            return (T) parse;
        }
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }

}
