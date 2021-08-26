package com.orion.utils.time.cron;

import com.orion.utils.Exceptions;
import com.orion.utils.Valid;

/**
 * cron 工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/25 19:05
 */
public class CornSupport {

    private static final String[] MACROS = new String[]{
            "@yearly", "0 0 0 1 1 *",
            "@annually", "0 0 0 1 1 *",
            "@monthly", "0 0 0 1 * *",
            "@weekly", "0 0 0 * * 0",
            "@daily", "0 0 0 * * *",
            "@midnight", "0 0 0 * * *",
            "@hourly", "0 0 * * * *"
    };

    private CornSupport() {
    }

    /**
     * 解析cron表达式
     *
     * @param expression expression
     * @return cron
     */
    public static Cron parse(String expression) {
        Valid.notBlank(expression, "expression string must not be empty");
        expression = resolveMacros(expression);
        String[] fields = Util.tokenizeToStringArray(expression, " ");
        if (fields.length != 6) {
            throw Exceptions.argument(String.format("cron expression must consist of 6 fields (found %d in \"%s\")", fields.length, expression));
        }
        try {
            CronField seconds = CronField.parseSeconds(fields[0]);
            CronField minutes = CronField.parseMinutes(fields[1]);
            CronField hours = CronField.parseHours(fields[2]);
            CronField daysOfMonth = CronField.parseDaysOfMonth(fields[3]);
            CronField months = CronField.parseMonth(fields[4]);
            CronField daysOfWeek = CronField.parseDaysOfWeek(fields[5]);
            return new Cron(seconds, minutes, hours, daysOfMonth, months, daysOfWeek, expression);
        } catch (IllegalArgumentException ex) {
            throw Exceptions.argument(ex.getMessage() + " in cron expression \"" + expression + "\"", ex);
        }
    }

    /**
     * 确定给定的字符串是否代表有效的 cron 表达式
     *
     * @param expression 要计算的表达式
     * @return 是否有效
     */
    public static boolean isValid(String expression) {
        if (expression == null) {
            return false;
        }
        try {
            parse(expression);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    private static String resolveMacros(String expression) {
        expression = expression.trim();
        for (int i = 0; i < MACROS.length; i = i + 2) {
            if (MACROS[i].equalsIgnoreCase(expression)) {
                return MACROS[i + 1];
            }
        }
        return expression;
    }

}
