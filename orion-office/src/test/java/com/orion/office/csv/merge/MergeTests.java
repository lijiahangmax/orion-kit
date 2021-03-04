package com.orion.office.csv.merge;

import com.orion.office.csv.core.CsvReader;
import com.orion.office.csv.reader.CsvArrayReader;
import com.orion.office.csv.writer.CsvArrayWriter;
import org.junit.Test;

/**
 * @author ljh15
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
