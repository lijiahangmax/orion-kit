package com.orion.lang.define.wrapper;

import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.convert.Converts;
import com.orion.lang.utils.json.Jsons;

import java.util.Date;
import java.util.Map;

/**
 * Wrapper 适配器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/3/19 11:02
 */
public class WrapperAdapter {

    /**
     * 转换为 wrapper 的 key
     */
    private static final String[] DEFAULT_CODE = {"code"};

    private static final String[] DEFAULT_MSG = {"msg", "message"};

    private static final String[] DEFAULT_DATA = {"data", "res", "result"};

    private WrapperAdapter() {
    }

    /**
     * 转Wrapper
     */
    public static HttpWrapper<Object> toHttp(String json) {
        return (HttpWrapper<Object>) toWrapper(json, Object.class, true);
    }

    public static HttpWrapper<Object> toHttp(Map<?, ?> map) {
        return (HttpWrapper<Object>) toWrapper(map, Object.class, true);
    }

    public static <T> HttpWrapper<T> toHttp(String json, Class<T> dataClass) {
        return (HttpWrapper<T>) toWrapper(json, dataClass, true);
    }

    public static <T> HttpWrapper<T> toHttp(Map<?, ?> map, Class<T> dataClass) {
        return (HttpWrapper<T>) toWrapper(map, dataClass, true);
    }

    public static RpcWrapper<Object> toRpc(String json) {
        return (RpcWrapper<Object>) toWrapper(json, Object.class, false);
    }

    public static RpcWrapper<Object> toRpc(Map<?, ?> map) {
        return (RpcWrapper<Object>) toWrapper(map, Object.class, false);
    }

    public static <T> RpcWrapper<T> toRpc(String json, Class<T> dataClass) {
        return (RpcWrapper<T>) toWrapper(json, dataClass, false);
    }

    public static <T> RpcWrapper<T> toRpc(Map<?, ?> map, Class<T> dataClass) {
        return (RpcWrapper<T>) toWrapper(map, dataClass, false);
    }

    private static <T> Wrapper<T> toWrapper(String json, Class<T> dataClass, boolean http) {
        if (Strings.isBlank(json)) {
            if (http) {
                return HttpWrapper.get();
            } else {
                return RpcWrapper.get();
            }
        }
        return toWrapper(Jsons.toMap(json), dataClass, http);
    }

    private static <T> Wrapper<T> toWrapper(Map<?, ?> map, Class<T> dataClass, boolean http) {
        if (map == null) {
            if (http) {
                return HttpWrapper.get();
            } else {
                return RpcWrapper.get();
            }
        }
        // 解析code
        String[] codeArr = new String[DEFAULT_CODE.length];
        for (int i = 0; i < DEFAULT_CODE.length; i++) {
            Object code = map.get(DEFAULT_CODE[i]);
            if (code != null) {
                codeArr[i] = code.toString();
                break;
            }
        }
        int code = codeAdapter(codeArr);

        // 解析msg
        String[] msgArr = new String[DEFAULT_MSG.length];
        for (int i = 0; i < DEFAULT_MSG.length; i++) {
            Object msg = map.get(DEFAULT_MSG[i]);
            if (msg != null) {
                msgArr[i] = msg.toString();
                break;
            }
        }
        String msg = msgAdapter(msgArr);

        // 解析data
        Object[] dataArr = new Object[DEFAULT_DATA.length];
        for (int i = 0; i < DEFAULT_DATA.length; i++) {
            Object data = map.get(DEFAULT_DATA[i]);
            if (data != null) {
                dataArr[i] = data;
                break;
            }
        }
        T data = dataAdapter(dataClass, dataArr);
        if (http) {
            return HttpWrapper.of(code, msg, data);
        } else {
            return RpcWrapper.of(code, msg, data);
        }
    }

    /**
     * 适配器
     */
    private static int codeAdapter(String... codes) {
        for (String code : codes) {
            if (Strings.isNumber(code)) {
                return Integer.parseInt(code);
            }
        }
        return 0;
    }

    private static String msgAdapter(String... msgs) {
        for (String msg : msgs) {
            if (Strings.isNoneBlank(msg)) {
                return msg;
            }
        }
        return Strings.EMPTY;
    }

    @SuppressWarnings("unchecked")
    private static <T> T dataAdapter(Class<T> dataClass, Object... ds) {
        for (Object data : ds) {
            if (data != null) {
                try {
                    if (dataClass == Byte.class) {
                        return (T) ((Byte) Converts.toByte(data));
                    } else if (dataClass == Short.class) {
                        return (T) ((Short) Converts.toShort(data));
                    } else if (dataClass == Integer.class) {
                        return (T) ((Integer) Converts.toInt(data));
                    } else if (dataClass == Long.class) {
                        return (T) ((Long) Converts.toLong(data));
                    } else if (dataClass == Float.class) {
                        return (T) ((Float) Converts.toFloat(data));
                    } else if (dataClass == Double.class) {
                        return (T) ((Double) Converts.toDouble(data));
                    } else if (dataClass == Boolean.class) {
                        return (T) ((Boolean) Converts.toBoolean(data));
                    } else if (dataClass == Character.class) {
                        return (T) ((Character) Converts.toChar(data));
                    } else if (dataClass == Date.class) {
                        return (T) Converts.toDate(data);
                    } else if (dataClass == String.class) {
                        return (T) data.toString();
                    } else {
                        return Jsons.parse(Jsons.toJson(data), dataClass);
                    }
                } catch (Exception e) {
                    Exceptions.printStacks(e);
                }
            }
        }
        return null;
    }

}
