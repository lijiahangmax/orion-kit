package com.orion.exception;

/**
 * 配置异常
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/3/2 17:20
 */
public class ConfigException extends Exception {

    public ConfigException() {
    }

    public ConfigException(String info) {
        super(info);
    }

    public ConfigException(Throwable res) {
        super(res);
    }

    public ConfigException(String info, Throwable res) {
        super(info, res);
    }

}
