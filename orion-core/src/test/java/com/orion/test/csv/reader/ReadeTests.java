package com.orion.test.csv.reader;

import com.orion.csv.core.CsvReader;
import com.orion.csv.reader.CsvArrayReader;
import com.orion.csv.reader.CsvBeanReader;
import com.orion.csv.reader.CsvMapReader;
import com.orion.csv.reader.CsvRawReader;
import com.orion.lang.Console;
import org.junit.Test;

/**
 * @author ljh15
 * @version 1.0.0
 * @since 2021/2/7 15:29
 */
public class ReadeTests {

    private static CsvReader array = new CsvReader("C:\\Users\\ljh15\\Desktop\\csv\\array.csv");
    private static CsvReader map = new CsvReader("C:\\Users\\ljh15\\Desktop\\csv\\map.csv");
    private static CsvReader bean = new CsvReader("C:\\Users\\ljh15\\Desktop\\csv\\bean.csv");

    @Test
    public void rawTests() {
        CsvRawReader reader = new CsvRawReader(array, Console::trace)
                .defaultRaw("def");
        reader.getOption()
                .setSkipEmptyRows(false)
                .setUseComments(false);
        reader.read(1)
                .read()
                .close();
    }

    @Test
    public void arrayTests() {
        CsvArrayReader reader = new CsvArrayReader(array, Console::trace)
                .rowOfNullToEmpty()
                .capacity(4)
                .columns(0, 2, 1, 3, 4);
        reader.getOption()
                .setSkipEmptyRows(false)
                .setUseComments(false);
        reader.read(1)
                .skip()
                .read()
                .close();
    }

    @Test
    public void mapTests() {
        CsvMapReader<String> reader = new CsvMapReader<String>(map, Console::trace)
                .linked()
                .nullPutKey(false)
                .mapping("seq", 0)
                .mapping("id", 1)
                .mapping("name", 3)
                .mapping("date", 2)
                .mapping("empty1", 7)
                .mapping("empty2", 8)
                .defaultValue("empty2", "def");
        reader.getOption()
                .setSkipEmptyRows(false)
                .setUseComments(false);
        reader.skip()
                .read(1)
                .read()
                .close();
    }

    @Test
    public void beanTests() {
        CsvBeanReader<ImportUser> reader = new CsvBeanReader<>(bean, ImportUser.class, Console::trace)
                .nullInvoke();
        reader.read()
                .close();
    }

}
