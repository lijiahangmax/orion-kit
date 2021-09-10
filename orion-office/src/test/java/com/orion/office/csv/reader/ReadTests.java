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
                .mapping("seq", 0)
                .mapping("id", 1)
                .mapping("name", 3)
                .mapping("date", 2)
                .mapping("empty1", 7)
                .mapping("empty2", 8)
                .defaultValue("empty2", "def");
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
                .mapping("seq", 0)
                .mapping("id", 1)
                .mapping("name", 3)
                .mapping("date", 2)
                .mapping("empty1", 7)
                .mapping("empty2", 8)
                .defaultValue("empty2", "def");
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
