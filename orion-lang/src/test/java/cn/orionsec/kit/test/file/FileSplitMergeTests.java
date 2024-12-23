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
package cn.orionsec.kit.test.file;

import cn.orionsec.kit.lang.utils.io.Files1;
import cn.orionsec.kit.lang.utils.io.split.FileMerge;
import cn.orionsec.kit.lang.utils.io.split.FileSplit;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/4/22 15:44
 */
public class FileSplitMergeTests {

    @Test
    public void s1() {
        // split
        String file = "F:\\test\\jdk1.7.zip";
        System.out.println(Files1.md5(file));
        String[] c = new FileSplit(file).call();
        System.out.println(Arrays.toString(c));
    }

    @Test
    public void m1() {
        // merge
        String file = "F:\\test\\jdk1.7.zip";
        String newFile = new FileMerge(file + ".block").call();
        System.out.println(newFile);
        System.out.println(Files1.md5(newFile));
    }

}
