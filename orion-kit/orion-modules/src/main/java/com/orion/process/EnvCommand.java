package com.orion.process;

import com.orion.utils.Systems;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统命令
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/4/17 13:51
 */
@SuppressWarnings("ALL")
public class EnvCommand {

    /**
     * 系统命令
     */
    private static final ArrayList<String> COMMAND = new ArrayList<>();

    /**
     * 空格
     */
    static final String SPACE = " ";

    static {
        if (Systems.OS_NAME.toLowerCase().contains("windows")) {
            COMMAND.add("cmd");
            COMMAND.add("/c");
        } else {
            COMMAND.add("/bin/sh");
            COMMAND.add("-c");
        }
    }

    /**
     * 获取系统命令前缀
     *
     * @return 前缀list
     */
    public static List<String> getCommand() {
        return (List<String>) COMMAND.clone();
    }

}
