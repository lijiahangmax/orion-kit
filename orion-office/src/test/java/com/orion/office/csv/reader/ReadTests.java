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
package com.orion.office.csv.reader;

import com.orion.lang.define.Console;
import com.orion.lang.define.collect.MutableMap;
import com.orion.office.csv.core.CsvReader;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/2/7 15:29
 */
public class ReadTests {

    private static CsvReader array = new CsvReader("C:\\Users\\ljh15\\Desktop\\csv\\array.csv");
    private static CsvReader map = new CsvReader("C:\\Users\\ljh15\\Desktop\\csv\\map.csv");
    private static CsvReader bean = new CsvReader("C:\\Users\\ljh15\\Desktop\\csv\\bean.csv");
    private static CsvReader lambda = new CsvReader("C:\\Users\\ljh15\\Desktop\\csv\\lambda.csv");

    @Test
    public void rawTests() {
        CsvRawReader reader = new CsvRawReader(array, Console::trace)
                .defaultRaw("def_line");
        reader.skipEmptyRows(false);
        reader.getOption().setUseComments(false);
        reader.read(1)
                .read()
                .close();
    }

    @Test
    public void arrayTests() {
        CsvArrayReader reader = new CsvArrayReader(array, Console::trace)
                .capacity(4)
                .columns(0, 2, 1, 3, 4);
        reader.getOption().setUseComments(true);
        reader.read(1)
                .skip()
                .read()
                .close();
    }

    @Test
    public void mapTests() {
        CsvMapReader<String, Object> reader = new CsvMapReader<String, Object>(map, Console::trace)
                .linked()
                .nullPutKey(false)
                .mapping(0, "seq")
                .mapping(1, "id")
                .mapping(2, "date")
                .mapping(3, "name")
                .mapping(7, "empty1")
                .mapping(8, "empty2", "def");
        reader.skipEmptyRows(false)
                .skip(3)
                .read()
                .close();
    }

    @Test
    public void beanTests() {
        CsvBeanReader<ImportUser> reader = new CsvBeanReader<>(bean, ImportUser.class, Console::trace)
                .nullInvoke();
        reader.skip(2).read()
                .close();
    }

    @Test
    public void lambdaTests() {
        CsvLambdaReader<ImportUser> reader = new CsvLambdaReader<>(lambda, Console::trace, ImportUser::new);
        reader.nullInvoke();
        reader.mapping(0, Integer::valueOf, ImportUser::setId)
                .mapping(2, ImportUser::setName)
                .mapping(3, ImportUser::setDate)
                .mapping(4, ImportUser::setDesc)
                .mapping(5, ImportUser::setEmpty);
        reader.skip(1)
                .read()
                .close();
    }

    @Test
    public void rawIteratorTests() {
        CsvRawReader reader = new CsvRawReader(array, Console::trace)
                .defaultRaw("def_line");
        reader.skipEmptyRows(false);
        reader.getOption().setUseComments(false);
        CsvReaderIterator<String> iterator = reader.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
        iterator.close();
    }

    @Test
    public void arrayIteratorTests() {
        CsvArrayReader reader = new CsvArrayReader(array, Console::trace)
                .capacity(4)
                .columns(0, 2, 1, 3, 4);
        reader.getOption().setUseComments(true);
        CsvReaderIterator<String[]> iterator = reader.iterator();
        while (iterator.hasNext()) {
            System.out.println(Arrays.toString(iterator.next()));
        }
        iterator.close();
    }

    @Test
    public void mapIteratorTests() {
        CsvMapReader<String, Object> reader = new CsvMapReader<String, Object>(map, Console::trace)
                .linked()
                .nullPutKey(false)
                .mapping(0, "seq")
                .mapping(1, "id")
                .mapping(2, "date")
                .mapping(3, "name")
                .mapping(7, "empty1")
                .mapping(8, "empty2", "def");
        reader.skipEmptyRows(false).skip(3);
        CsvReaderIterator<MutableMap<String, Object>> iterator = reader.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
        iterator.close();
    }

    @Test
    public void beanIteratorTests() {
        CsvBeanReader<ImportUser> reader = new CsvBeanReader<>(bean, ImportUser.class, Console::trace)
                .nullInvoke();
        reader.skip(2);
        CsvReaderIterator<ImportUser> iterator = reader.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
        iterator.close();
    }

    @Test
    public void lambdaIteratorTests() {
        CsvLambdaReader<ImportUser> reader = new CsvLambdaReader<>(lambda, Console::trace, ImportUser::new);
        reader.nullInvoke();
        reader.mapping(0, Integer::valueOf, ImportUser::setId)
                .mapping(2, ImportUser::setName)
                .mapping(3, ImportUser::setDate)
                .mapping(4, ImportUser::setDesc)
                .mapping(5, ImportUser::setEmpty);
        CsvReaderIterator<ImportUser> iterator = reader.iterator();
        reader.skip(1);
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
        iterator.close();
    }

}
