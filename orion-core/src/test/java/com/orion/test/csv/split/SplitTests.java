package com.orion.test.csv.split;

import com.orion.csv.core.CsvReader;
import com.orion.csv.reader.CsvArrayReader;
import com.orion.csv.split.CsvColumnSplit;
import com.orion.csv.split.CsvRowSplit;
import org.junit.Test;

/**
 * @author ljh15
 * @version 1.0.0
 * @since 2021/2/23 14:59
 */
public class SplitTests {

    @Test
    public void rowSplitTests() {
        CsvArrayReader reader = new CsvArrayReader(new CsvReader("C:\\Users\\ljh15\\Desktop\\1.csv"));
        reader.getOption().setSkipEmptyRows(false);
        CsvRowSplit split = new CsvRowSplit(reader, 5).skip(1);
        split.destPath("C:\\Users\\ljh15\\Desktop\\split1", "sp");
        split.split().close();
    }

    @Test
    public void columnSplitTests() {
        CsvArrayReader reader = new CsvArrayReader(new CsvReader("C:\\Users\\ljh15\\Desktop\\1.csv"));
        reader.getOption().setSkipEmptyRows(false);
        new CsvColumnSplit(reader, 2, 3, 1).split("C:\\Users\\ljh15\\Desktop\\2.csv");
    }

}
