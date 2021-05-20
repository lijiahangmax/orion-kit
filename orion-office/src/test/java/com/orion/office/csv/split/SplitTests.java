package com.orion.office.csv.split;

import com.orion.office.csv.core.CsvReader;
import com.orion.office.csv.reader.CsvArrayReader;
import org.junit.Test;

/**
 * @author Jiahang Li
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
