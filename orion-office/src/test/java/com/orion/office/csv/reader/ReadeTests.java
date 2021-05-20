package com.orion.office.csv.reader;

import com.orion.lang.Console;
import com.orion.lang.collect.MutableMap;
import com.orion.office.csv.core.CsvReader;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author Jiahang Li
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
                .defaultRaw("def_line");
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
        CsvMapReader<String, Object> reader = new CsvMapReader<String, Object>(map, Console::trace)
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

    @Test
    public void rawIteratorTests() {
        CsvRawReader reader = new CsvRawReader(array, Console::trace)
                .defaultRaw("def_line");
        reader.getOption()
                .setSkipEmptyRows(false)
                .setUseComments(false);
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
        reader.getOption()
                .setSkipEmptyRows(false)
                .setUseComments(false);
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
        reader.getOption()
                .setSkipEmptyRows(false)
                .setUseComments(false);
        CsvReaderIterator<ImportUser> iterator = reader.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
        iterator.close();
    }

}
