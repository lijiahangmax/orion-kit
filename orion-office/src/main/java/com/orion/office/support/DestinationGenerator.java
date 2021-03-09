package com.orion.office.support;

import com.orion.able.SafeCloseable;
import com.orion.constant.Const;
import com.orion.utils.Strings;
import com.orion.utils.Valid;
import com.orion.utils.collect.Lists;
import com.orion.utils.io.Files1;
import com.orion.utils.io.Streams;

import java.io.File;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

/**
 * 目标输出流生成器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/2/22 17:24
 */
public abstract class DestinationGenerator implements SafeCloseable {

    /**
     * 拆分输出的流
     */
    protected List<OutputStream> dest;

    /**
     * 自动生成的文件目录
     */
    protected String generatorPathDir;

    /**
     * 自动生成的文件名称
     */
    protected String generatorBaseName;

    /**
     * 自动生成的文件名称后缀
     */
    protected String generatorNameSuffix;

    /**
     * 当前目标下标
     */
    protected int currentDestIndex;

    /**
     * 文件后缀
     */
    protected String suffix;

    /**
     * 是否自动关闭流
     */
    protected boolean autoClose;

    /**
     * 当前流
     */
    protected OutputStream currentOutputStream;

    /**
     * 是否自动关闭流
     *
     * @param autoClose 是否自动关闭
     * @return this
     */
    public DestinationGenerator autoClose(boolean autoClose) {
        this.autoClose = autoClose;
        return this;
    }

    /**
     * 设置拆分文件输出流
     *
     * @param dest dest
     * @return this
     */
    public DestinationGenerator dest(OutputStream... dest) {
        Valid.notEmpty(dest, "dest file is empty");
        this.dest = Lists.of(dest);
        this.generatorPathDir = null;
        this.generatorBaseName = null;
        return this;
    }

    /**
     * 设置拆分文件输出文件
     *
     * @param dest dest
     * @return this
     */
    public DestinationGenerator dest(File... dest) {
        Valid.notEmpty(dest, "dest file is empty");
        OutputStream[] streams = Arrays.stream(dest)
                .map(Files1::openOutputStreamSafe)
                .toArray(OutputStream[]::new);
        return this.dest(streams);
    }

    /**
     * 设置拆分文件输出文件路径
     *
     * @param dest dest
     * @return this
     */
    public DestinationGenerator dest(String... dest) {
        Valid.notEmpty(dest, "dest file is empty");
        OutputStream[] streams = Arrays.stream(dest)
                .map(Files1::openOutputStreamSafe)
                .toArray(OutputStream[]::new);
        return this.dest(streams);
    }

    /**
     * 设置拆分文件输出文件路径
     *
     * @param pathDir  目标文件目录
     * @param baseName 文件名称
     * @return this
     */
    public DestinationGenerator destPath(String pathDir, String baseName) {
        return this.destPath(pathDir, baseName, null);
    }

    /**
     * 设置拆分文件输出文件路径
     *
     * @param pathDir    目标文件目录
     * @param baseName   文件名称
     * @param nameSuffix 文件名称后缀
     * @return this
     */
    public DestinationGenerator destPath(String pathDir, String baseName, String nameSuffix) {
        Valid.notNull(pathDir, "dest path dir is null");
        Valid.notNull(baseName, "dest file base name is null");
        this.dest = null;
        this.generatorPathDir = pathDir;
        this.generatorBaseName = baseName;
        this.generatorNameSuffix = Strings.def(nameSuffix);
        this.autoClose = true;
        return this;
    }

    /**
     * 生成OutputStream
     *
     * @return ignore
     */
    protected OutputStream generatorOutputStream() {
        String path = Files1.getPath(generatorPathDir + Const.SEPARATOR + generatorBaseName + generatorNameSuffix + (++currentDestIndex) + "." + suffix);
        Files1.touch(path);
        return Files1.openOutputStreamSafe(path);
    }

    /**
     * 是否还有下一个
     *
     * @return ignore
     */
    protected boolean hasNext() {
        if (dest == null) {
            return true;
        }
        return dest.size() > currentDestIndex;
    }

    /**
     * 获取下一个流
     */
    protected void next() {
        if (this.hasNext()) {
            if (dest == null) {
                currentOutputStream = generatorOutputStream();
            } else {
                currentOutputStream = dest.get(currentDestIndex++);
            }
        } else {
            currentOutputStream = null;
        }
    }

    @Override
    public void close() {
        if (autoClose && dest != null) {
            dest.forEach(Streams::close);
        }
    }

}
