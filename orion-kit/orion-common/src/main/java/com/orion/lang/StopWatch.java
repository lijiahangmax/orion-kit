package com.orion.lang;

import com.orion.utils.Dates;
import com.orion.utils.Strings;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 秒表
 *
 * @author Li
 * @version 1.0.0
 * @date 2019/07/26 15:17
 */
public class StopWatch {

    private boolean nano;

    private long from;

    private long to;

    private List<StopTag> tags;

    private StopTag lastTag;

    /**
     * 秒表开始计时, 计时时间的最小单位是毫秒
     *
     * @return 开始计时的秒表对象
     */
    public static StopWatch begin() {
        StopWatch sw = new StopWatch();
        sw.start();
        return sw;
    }

    /**
     * 秒表开始计时, 计时时间的最小单位是毫微秒
     *
     * @return 开始计时的秒表对象
     */
    public static StopWatch beginNano() {
        StopWatch sw = new StopWatch();
        sw.nano = true;
        sw.start();
        return sw;
    }

    /**
     * 创建一个秒表对象, 该对象的计时时间的最小单位是毫秒
     *
     * @return 秒表对象
     */
    public static StopWatch create() {
        return new StopWatch();
    }

    /**
     * 创建一个秒表对象, 该对象的计时时间的最小单位是毫微秒
     *
     * @return 秒表对象
     */
    public static StopWatch createNano() {
        StopWatch sw = new StopWatch();
        sw.nano = true;
        return sw;
    }

    public static StopWatch run(Runnable r) {
        StopWatch sw = begin();
        r.run();
        sw.stop();
        return sw;
    }

    public static StopWatch runNano(Runnable r) {
        StopWatch sw = beginNano();
        r.run();
        sw.stop();
        return sw;
    }

    /**
     * 开始计时, 并返回开始计时的时间, 该时间最小单位由创建秒表时确定
     *
     * @return 开始计时的时间
     */
    public long start() {
        from = currentTime();
        to = from;
        return from;
    }

    /**
     * @return 当前时间
     */
    private long currentTime() {
        return nano ? System.nanoTime() : System.currentTimeMillis();
    }

    /**
     * 记录停止时间, 该时间最小单位由创建秒表时确定
     *
     * @return 自身以便链式赋值
     */
    public long stop() {
        to = currentTime();
        return to;
    }

    /**
     * @return 计时结果(ms)
     */
    public long getDuration() {
        return to - from;
    }

    /**
     * @return 计时结果(ms)
     */
    public long du() {
        return to - from;
    }

    /**
     * 开始计时的时间, 该时间最小单位由创建秒表时确定
     *
     * @return 开始计时的时间
     */
    public long getStartTime() {
        return from;
    }

    /**
     * 停止计时的时间, 该时间最小单位由创建秒表时确定
     *
     * @return 停止计时的时间
     */
    public long getEndTime() {
        return to;
    }

    /**
     * 返回格式为 Total: [计时时间][计时时间单位] : [计时开始时间] => [计时结束时间] 的字符串
     *
     * @return 格式为 Total: [计时时间][计时时间单位] : [计时开始时间] => [计时结束时间] 的字符串
     */
    @Override
    public String toString() {
        String prefix = String.format("Total: %d%s : [%s] => [%s]",
                this.getDuration(),
                (nano ? "ns" : "ms"),
                Dates.format(new Date(from), "yyyy-MM-dd HH:mm:ss.SSS"),
                Dates.format(new Date(to), "yyyy-MM-dd HH:mm:ss.SSS"));
        if (tags == null) {
            return prefix;
        }
        StringBuilder sb = new StringBuilder(prefix).append("\n");
        for (int i = 0; i < tags.size(); i++) {
            StopTag tag = tags.get(i);
            sb.append(String.format("  -> %5s: %dms",
                    tag.name == null ? "TAG" + i : tag.name,
                    tag.du()));
            if (i < tags.size() - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
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
            tags = new LinkedList<>();
        }
        lastTag = new StopTag(name, System.currentTimeMillis(), lastTag);
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
        return tag(Strings.format(tpl, args));
    }

    class StopTag {
        private String name;
        private long tm;
        private StopTag pre;

        public StopTag(String name, long tm, StopTag pre) {
            super();
            this.name = name;
            this.tm = tm;
            this.pre = pre;
        }

        public long du() {
            if (pre == null) {
                return tm - from;
            }
            return tm - pre.tm;
        }

        public String getName() {
            return name;
        }

        public StopTag setName(String name) {
            this.name = name;
            return this;
        }

        public long getTm() {
            return tm;
        }

        public StopTag setTm(long tm) {
            this.tm = tm;
            return this;
        }

        public StopTag getPre() {
            return pre;
        }

        public StopTag setPre(StopTag pre) {
            this.pre = pre;
            return this;
        }

        @Override
        public String toString() {
            return "StopTag{" +
                    "name='" + name + '\'' +
                    ", tm=" + tm +
                    ", pre=" + pre +
                    '}';
        }
    }
}
