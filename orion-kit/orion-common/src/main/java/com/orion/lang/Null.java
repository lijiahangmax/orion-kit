package com.orion.lang;

import java.io.Serializable;

/**
 * null
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/2/6 23:13
 */
public class Null implements Serializable {

    private static final long serialVersionUID = 1523490364340556L;

    public static final Null VALUE = new Null();

    private Null() {
    }

}
