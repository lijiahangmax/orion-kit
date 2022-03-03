package com.orion.utils.time.cron;

import com.orion.utils.time.Dates;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * cron工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/26 18:46
 */
public class CronSupport {

    private static final String CRON_FORMAT = "ss mm HH dd MM ? yyyy";

    private CronSupport() {
    }

    /**
     * 获取时间点的 cron 表达式
     *
     * @param date date
     * @return 表达式
     */
    public static String getCronExpression(Date date) {
        return Dates.format(date, CRON_FORMAT);
    }

    /**
     * 获取时间点的 cron 对象
     *
     * @param date date
     * @return cron
     */
    public static Cron getCron(Date date) {
        return new Cron(Dates.format(date, CRON_FORMAT));
    }

    /**
     * 获取 cron 对象
     *
     * @param expression expression
     * @return cron
     */
    public static Cron getCron(String expression) {
        return new Cron(expression);
    }

    /**
     * 判断是否是合法的 cron 表达式
     *
     * @param expression expression
     * @return 是否合法
     */
    public static boolean isValidExpression(String expression) {
        try {
            new Cron(expression);
            return true;
        } catch (Exception pe) {
            return false;
        }
    }

    /**
     * 获取下次的执行时间
     *
     * @param cron cron
     * @return 执行时间
     */
    public static Date getNextTime(Cron cron) {
        return cron.getNextValidTimeAfter(new Date());
    }

    /**
     * 获取下次的执行时间
     *
     * @param cron cron
     * @param date 目标时间
     * @return 执行时间
     */
    public static Date getNextTime(Cron cron, Date date) {
        return cron.getNextValidTimeAfter(date);
    }

    /**
     * 获取下几次的执行时间
     *
     * @param cron  cron
     * @param times 次数
     * @return 执行时间
     */
    public static List<Date> getNextTime(Cron cron, int times) {
        return getNextTime(cron, new Date(), times);
    }

    /**
     * 获取下几次的执行时间
     *
     * @param cron  cron
     * @param date  目标时间
     * @param times 次数
     * @return 执行时间
     */
    public static List<Date> getNextTime(Cron cron, Date date, int times) {
        List<Date> list = new ArrayList<>();
        for (int i = 0; i < times; i++) {
            date = cron.getNextValidTimeAfter(date);
            if (date != null) {
                list.add(date);
            } else {
                break;
            }
        }
        return list;
    }

}
