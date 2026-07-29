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
package cn.orionsec.kit.office.csv.merge;

import cn.orionsec.kit.office.csv.core.CsvReader;
import cn.orionsec.kit.office.csv.reader.CsvArrayReader;
import cn.orionsec.kit.office.csv.writer.CsvArrayWriter;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/2/23 14:59
 */
public class MergeTests {

    private static final File SP1 = new File("C:\\Users\\Administrator\\Desktop\\split1\\sp1.csv");
    private static final File SP2 = new File("C:\\Users\\Administrator\\Desktop\\split1\\sp2.csv");
    private static final File SP3 = new File("C:\\Users\\Administrator\\Desktop\\split1\\sp3.csv");
    private static final File SP4 = new File("C:\\Users\\Administrator\\Desktop\\split1\\sp4.csv");
    private static final File SP5 = new File("C:\\Users\\Administrator\\Desktop\\split1\\sp5.csv");

    @Before
    public void setUp() {
        // 文件不存在则跳过测试
        Assume.assumeTrue(SP1.exists() && SP2.exists() && SP3.exists() && SP4.exists() && SP5.exists());
    }

    @Test
    public void testMerge() {
        CsvArrayWriter writer = new CsvArrayWriter("C:\\Users\\Administrator\\Desktop\\3.csv");
        new CsvMerge(writer)
                .skipRows()
                .header("m1", "m2", "m3", "m4")
                .merge(new CsvArrayReader(new CsvReader(SP1)))
                .merge(new CsvArrayReader(new CsvReader(SP2)))
                .merge(new CsvArrayReader(new CsvReader(SP3)))
                .merge(new CsvArrayReader(new CsvReader(SP4)))
                .merge(new CsvArrayReader(new CsvReader(SP5)))
                .close();

    }

}
