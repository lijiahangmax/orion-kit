/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
 *
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package cn.orionsec.kit.lang.define;

import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.math.Numbers;
import cn.orionsec.kit.lang.utils.time.Dates;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 计时器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2019/07/26 15:17
 */
public class StopWatch {

    private long from;

    private long to;

    private boolean nano;

    private List<StopTag> tags;

    private StopTag lastTag;

    public StopWatch() {
    }

    public StopWatch(boolean nano) {
        this.nano = nano;
    }

    /**
     * 计时器开始计时
     *
     * @return StopWatch
     */
    public static StopWatch begin() {
        StopWatch sw = new StopWatch();
        sw.start();
        return sw;
    }

    /**
     * 计时器开始计时
     *
     * @param nano 是否精确到纳秒
     * @return StopWatch
     */
    public static StopWatch begin(boolean nano) {
        StopWatch sw = new StopWatch(nano);
        sw.start();
        return sw;
    }

    /**
     * 创建一个计时器
     *
     * @return StopWatch
     */
    public static StopWatch create() {
        return new StopWatch();
    }

    /**
     * 创建一个计时器
     *
     * @param nano 是否精确到纳秒
     * @return StopWatch
     */
    public static StopWatch create(boolean nano) {
        return new StopWatch(nano);
    }

    /**
     * 计时运行
     *
     * @param r runnable
     * @return StopWatch
     */
    public static StopWatch run(Runnable r) {
        StopWatch sw = begin();
        r.run();
        sw.stop();
        return sw;
    }

    /**
     * 计时运行
     *
     * @param r    runnable
     * @param nano nano
     * @return StopWatch
     */
    public static StopWatch run(Runnable r, boolean nano) {
        StopWatch sw = new StopWatch(nano);
        r.run();
        sw.stop();
        return sw;
    }

    /**
     * 开始计时
     *
     * @return 开始计时的时间
     */
    public long start() {
        this.from = this.current();
        this.to = from;
        return from;
    }

    /**
     * 停止记时
     *
     * @return 结束时间
     */
    public long stop() {
        this.to = this.current();
        return to;
    }

    /**
     * @return 计时结果(ms / ns)
     */
    public long getDuration() {
        return to - from;
    }

    /**
     * 是否纳秒计算
     *
     * @return ignore
     */
    public boolean isNano() {
        return nano;
    }

    /**
     * 开始计时的时间
     *
     * @return 开始计时的时间
     */
    public long getStartTime() {
        return from;
    }

    /**
     * 停止计时的时间
     *
     * @return 停止计时的时间
     */
    public long getEndTime() {
        return to;
    }

    /**
     * 获取tag使用的时间半分比
     *
     * @param tag tag
     * @return use%
     */
    private String getUse(StopTag tag) {
        return Numbers.setScale(((double) tag.getDuration() / (double) (to - from)) * 100, 6);
    }

    /**
     * 当前时间
     *
     * @return now
     */
    private long current() {
        return nano ? System.nanoTime() : System.currentTimeMillis();
    }

    /**
     * @return 记录
     */
    public StopTag tag() {
        return tag(null);
    }

    /**
     * @param name 记录名称
     * @return 记录
     */
    public StopTag tag(String name) {
        if (tags == null) {
            this.tags = new LinkedList<>();
        }
        this.lastTag = new StopTag(name, this.current(), lastTag);
        tags.add(lastTag);
        return lastTag;
    }

    /**
     * 记录, 模板字符串命名
     *
     * @param tpl  模板
     * @param args 参数
     * @return ignore
     */
    public StopTag tag(String tpl, Object... args) {
        return this.tag(Strings.format(tpl, args));
    }

    /**
     * @return 格式为 Total: [计时时间][计时时间单位] : [计时开始时间] => [计时结束时间] 的字符串
     */
    @Override
    public String toString() {
        String prefix = String.format("Total: %d%s; [%s] => [%s]",
                this.getDuration(),
                nano ? "ns" : "ms",
                nano ? from : Dates.format(new Date(from), "HH:mm:ss.SSS"),
                nano ? to : Dates.format(new Date(to), "HH:mm:ss.SSS"));
        if (tags == null) {
            return prefix;
        }
        StringBuilder sb = new StringBuilder(prefix).append(Const.LF);
        for (int i = 0; i < tags.size(); i++) {
            StopTag tag = tags.get(i);
            sb.append(String.format("  -> %4s: %d%s  %4s%%",
                    tag.name == null ? "TAG" + i : tag.name,
                    tag.getDuration(),
                    nano ? "ns" : "ms",
                    this.getUse(tag)
            ));
            if (i < tags.size() - 1) {
                sb.append(Const.LF);
            }
        }
        return sb.toString();
    }

    /**
     * TAG
     */
    public class StopTag {

        private String name;

        private long tm;

        private StopTag pre;

        public StopTag(String name, long tm, StopTag pre) {
            super();
            this.name = name;
            this.tm = tm;
            this.pre = pre;
        }

        public long getDuration() {
            if (pre == null) {
                return tm - from;
            }
            return tm - pre.tm;
        }

        private long getStart() {
            if (pre == null) {
                return from;
            }
            return pre.tm;
        }

        public String getName() {
            return name;
        }

        public long getTm() {
            return tm;
        }

        public StopTag getPre() {
            return pre;
        }

        @Override
        public String toString() {
            return String.format("%s: [%d] => [%s] %2d%s",
                    name == null ? "TAG" : name,
                    this.getStart(),
                    tm,
                    this.getDuration(),
                    nano ? "ns" : "ms");
        }
    }

}
