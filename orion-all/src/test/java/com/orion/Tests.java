package com.orion;

import com.orion.lang.define.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/6/2 12:32
 */
public class Tests {

    /**
     * LOG
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Tests.class);

    public static void main(String[] args) throws Exception {
        StopWatch w = StopWatch.begin();

        w.stop();
        System.out.println("\n\n\n");
        LOGGER.info(w.toString());
    }

}
