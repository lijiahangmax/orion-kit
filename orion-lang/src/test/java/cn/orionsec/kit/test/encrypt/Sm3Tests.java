/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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
package cn.orionsec.kit.test.encrypt;

import cn.orionsec.kit.lang.utils.Assert;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.crypto.SM3;
import org.junit.Test;

import java.util.Arrays;

/**
 * SM3 测试
 * <p>
 * 标准测试向量参见 GB/T 32905-2016
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/9/2 15:30
 */
public class Sm3Tests {

    /**
     * SM3("abc") 标准向量
     */
    private static final String ABC_DIGEST = "66c7f0f462eeedd9d1f2d46bdc10e4e24167c4875cf2f7a2297da02b8f4ba8e0";

    /**
     * SM3("abcd...ijkl 64字符") 标准向量
     */
    private static final String ABCD_64_DIGEST = "debe9ff92275b8a138604889c18e5a4d6fdb70e5387e5765293dcba39c0c5732";

    @Test
    public void standardVector() {
        // 标准向量 1: SM3("abc")
        String abc = SM3.digestHex("abc");
        Assert.isTrue(ABC_DIGEST.equals(abc), "sm3 abc vector error " + abc);
        // 标准向量 2: SM3("abcd" * 16) 64 字符
        String s = "abcd";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            sb.append(s);
        }
        String digest = SM3.digestHex(sb.toString());
        Assert.isTrue(ABCD_64_DIGEST.equals(digest), "sm3 64 chars vector error " + digest);
    }

    @Test
    public void digestLength() {
        // 摘要固定 256bit (32 字节 / 64 位 hex)
        for (int i = 0; i < 100; i++) {
            String val = Strings.randomChars(i);
            byte[] bytes = SM3.digest(val);
            Assert.isTrue(bytes.length == 32, "digest length error");
            String hex = SM3.digestHex(val);
            Assert.isTrue(hex.length() == 64, "digest hex length error");
            Assert.isTrue(hex.matches("[0-9a-f]+"), "digest hex format error");
        }
    }

    @Test
    public void deterministicAndConsistent() {
        // 相同输入摘要相同
        for (int i = 0; i < 100; i++) {
            String val = Strings.randomChars(32);
            String d1 = SM3.digestHex(val);
            String d2 = SM3.digestHex(val);
            Assert.isTrue(d1.equals(d2), "digest not deterministic");
            // byte[] 与 String 入参一致
            Assert.isTrue(Arrays.equals(SM3.digest(val), SM3.digest(Strings.bytes(val))), "digest input type error");
            Assert.isTrue(SM3.digestHex(val).equals(SM3.digestHex(Strings.bytes(val))), "digestHex input type error");
        }
    }

    @Test
    public void differentInputDifferentDigest() {
        // 不同输入摘要不同
        String d1 = SM3.digestHex("a");
        String d2 = SM3.digestHex("b");
        Assert.isTrue(!d1.equals(d2), "digest collision error");
        // 相似输入摘要差异很大 (雪崩效应)
        String s1 = SM3.digestHex("orion-kit");
        String s2 = SM3.digestHex("orion-kis");
        Assert.isTrue(!s1.equals(s2), "avalanche error");
    }

    @Test
    public void chineseAndEmpty() {
        // 中文
        String chinese = SM3.digestHex("你好世界");
        Assert.isTrue(chinese.length() == 64, "chinese digest length error");
        Assert.isTrue(chinese.equals(SM3.digestHex("你好世界")), "chinese digest not deterministic");
        // 空字符串
        String empty = SM3.digestHex("");
        Assert.isTrue(empty.length() == 64, "empty digest length error");
        byte[] emptyBytes = SM3.digest(new byte[0]);
        Assert.isTrue(emptyBytes.length == 32, "empty digest bytes length error");
        // byte[] 空与字符串空一致
        Assert.isTrue(empty.equals(SM3.digestHex(new byte[0])), "empty digest not consistent");
    }

}
