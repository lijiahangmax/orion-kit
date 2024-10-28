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
package cn.orionsec.kit.test.encrypt;

import cn.orionsec.kit.lang.utils.crypto.Signatures;
import cn.orionsec.kit.lang.utils.io.Files1;
import org.junit.Test;

import java.io.File;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/9/29 9:58
 */
public class SignTests {

    @Test
    public void e() {
        String s = "123";
        System.out.println("Signatures.md5(s) = " + Signatures.md5(s));
        System.out.println("Signatures.md5(s, \"salt\") = " + Signatures.md5(s, "salt"));
        System.out.println("Signatures.md5(s, \"salt\", 3) = " + Signatures.md5(s, "salt", 3));
        System.out.println("Signatures.sha1(s) = " + Signatures.sha1(s));
        System.out.println("Signatures.sha224(s) = " + Signatures.sha224(s));
        System.out.println("Signatures.sha256(s) = " + Signatures.sha256(s));
        System.out.println("Signatures.sha384(s) = " + Signatures.sha384(s));
        System.out.println("Signatures.sha512(s) = " + Signatures.sha512(s));
        System.out.println();
        File f = new File("C:\\Users\\ljh15\\Desktop\\bug.txt");
        System.out.println("Files1.md5(f) = " + Files1.md5(f));
        System.out.println("Files1.sha1(f) = " + Files1.sha1(f));
        System.out.println("Files1.sha224(f) = " + Files1.sha224(f));
        System.out.println("Files1.sha256(f) = " + Files1.sha256(f));
        System.out.println("Files1.sha384(f) = " + Files1.sha384(f));
        System.out.println("Files1.sha512(f) = " + Files1.sha512(f));
        System.out.println();
        System.out.println("Signatures.hmacMd5(\"1\", \"23\") = " + Signatures.hmacMd5("1", "23"));
        System.out.println("Signatures.hmacSha1(\"1\", \"23\") = " + Signatures.hmacSha1("1", "23"));
        System.out.println("Signatures.hmacSha224(\"1\", \"23\") = " + Signatures.hmacSha224("1", "23"));
        System.out.println("Signatures.hmacSha256(\"1\", \"23\") = " + Signatures.hmacSha256("1", "23"));
        System.out.println("Signatures.hmacSha384(\"1\", \"23\") = " + Signatures.hmacSha384("1", "23"));
        System.out.println("Signatures.hmacSha512(\"1\", \"23\") = " + Signatures.hmacSha512("1", "23"));
    }

}
