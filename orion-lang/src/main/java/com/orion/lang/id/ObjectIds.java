/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
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
package com.orion.lang.id;

import com.orion.lang.utils.Strings;
import com.orion.lang.utils.Systems;

/**
 * MongoDB id 生成工具
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/15 18:09
 */
public class ObjectIds {

    private static final int MACHINE_CODE;

    private static final ObjectIdWorker ID_WORKER;

    private ObjectIds() {
    }

    static {
        MACHINE_CODE = Systems.getMachineCode();
        ID_WORKER = new ObjectIdWorker(MACHINE_CODE);
    }

    /**
     * 判断 objectId 是否有效
     *
     * @param s 字符串
     * @return ignore
     */
    public static boolean isValid(String s) {
        if (s == null) {
            return false;
        }
        s = s.replaceAll("-", Strings.EMPTY);
        int len = s.length();
        if (len != 24) {
            return false;
        }

        char c;
        for (int i = 0; i < len; i++) {
            c = s.charAt(i);
            if (c >= '0' && c <= '9') {
                continue;
            }
            if (c >= 'a' && c <= 'f') {
                continue;
            }
            if (c >= 'A' && c <= 'F') {
                continue;
            }
            return false;
        }
        return true;
    }

    /**
     * 获取一个 objectId
     *
     * @return objectId
     */
    public static byte[] nextBytes() {
        return ID_WORKER.nextBytes();
    }

    /**
     * 获取一个 objectId (无下划线)
     *
     * @return objectId
     */
    public static String nextId() {
        return ID_WORKER.nextId();
    }

    /**
     * 获取一个 objectId
     *
     * @param symbol 是否包含分隔符
     * @return objectId
     */
    public static String nextId(boolean symbol) {
        return ID_WORKER.nextId(symbol);
    }

    /**
     * 获取机器唯一信息
     *
     * @return 机器码
     */
    public static int getMachineCode() {
        return MACHINE_CODE;
    }

}
