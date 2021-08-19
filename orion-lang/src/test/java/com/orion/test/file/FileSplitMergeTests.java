package com.orion.test.file;

import com.orion.utils.io.Files1;
import com.orion.utils.io.split.FileMerge;
import com.orion.utils.io.split.FileSplit;
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
