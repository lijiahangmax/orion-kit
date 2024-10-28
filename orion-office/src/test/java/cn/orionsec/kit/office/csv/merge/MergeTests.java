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
package cn.orionsec.kit.office.csv.merge;

import cn.orionsec.kit.office.csv.core.CsvReader;
import cn.orionsec.kit.office.csv.reader.CsvArrayReader;
import cn.orionsec.kit.office.csv.writer.CsvArrayWriter;
import org.junit.Test;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/2/23 14:59
 */
public class MergeTests {

    @Test
    public void testMerge() {
        CsvArrayWriter writer = new CsvArrayWriter("C:\\Users\\ljh15\\Desktop\\3.csv");
        new CsvMerge(writer)
                .skipRows()
                .header("m1", "m2", "m3", "m4")
                .merge(new CsvArrayReader(new CsvReader("C:\\Users\\ljh15\\Desktop\\split1\\sp1.csv")))
                .merge(new CsvArrayReader(new CsvReader("C:\\Users\\ljh15\\Desktop\\split1\\sp2.csv")))
                .merge(new CsvArrayReader(new CsvReader("C:\\Users\\ljh15\\Desktop\\split1\\sp3.csv")))
                .merge(new CsvArrayReader(new CsvReader("C:\\Users\\ljh15\\Desktop\\split1\\sp4.csv")))
                .merge(new CsvArrayReader(new CsvReader("C:\\Users\\ljh15\\Desktop\\split1\\sp5.csv")))
                .close();

    }

}
