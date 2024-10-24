/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
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
package com.orion.office.support;

import com.orion.lang.able.SafeCloseable;
import com.orion.lang.constant.Const;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.Valid;
import com.orion.lang.utils.collect.Lists;
import com.orion.lang.utils.io.Files1;
import com.orion.lang.utils.io.Streams;

import java.io.File;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

/**
 * 目标输出流生成器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/2/22 17:24
 */
public abstract class SplitTargetGenerator implements SafeCloseable {

    /**
     * 拆分输出的流
     */
    protected List<OutputStream> targets;

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
    protected int currentTargetIndex;

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
    public SplitTargetGenerator autoClose(boolean autoClose) {
        this.autoClose = autoClose;
        return this;
    }

    /**
     * 设置拆分文件输出流
     *
     * @param target target
     * @return this
     */
    public SplitTargetGenerator target(OutputStream... target) {
        Valid.notEmpty(target, "target file is empty");
        this.targets = Lists.of(target);
        this.generatorPathDir = null;
        this.generatorBaseName = null;
        return this;
    }

    /**
     * 设置拆分文件输出文件
     *
     * @param target target
     * @return this
     */
    public SplitTargetGenerator target(File... target) {
        Valid.notEmpty(target, "target file is empty");
        OutputStream[] streams = Arrays.stream(target)
                .map(Files1::openOutputStreamSafe)
                .toArray(OutputStream[]::new);
        return this.target(streams);
    }

    /**
     * 设置拆分文件输出文件路径
     *
     * @param target target
     * @return this
     */
    public SplitTargetGenerator target(String... target) {
        Valid.notEmpty(target, "target file is empty");
        OutputStream[] streams = Arrays.stream(target)
                .map(Files1::openOutputStreamSafe)
                .toArray(OutputStream[]::new);
        return this.target(streams);
    }

    /**
     * 设置拆分文件输出文件路径
     *
     * @param pathDir  目标文件目录
     * @param baseName 文件名称
     * @return this
     */
    public SplitTargetGenerator targetPath(String pathDir, String baseName) {
        return this.targetPath(pathDir, baseName, null);
    }

    /**
     * 设置拆分文件输出文件路径
     *
     * @param pathDir    目标文件目录
     * @param baseName   文件名称
     * @param nameSuffix 文件名称后缀
     * @return this
     */
    public SplitTargetGenerator targetPath(String pathDir, String baseName, String nameSuffix) {
        Valid.notNull(pathDir, "target path dir is null");
        Valid.notNull(baseName, "target file base name is null");
        this.targets = null;
        this.generatorPathDir = pathDir;
        this.generatorBaseName = baseName;
        this.generatorNameSuffix = Strings.def(nameSuffix);
        this.autoClose = true;
        return this;
    }

    /**
     * 生成 OutputStream
     *
     * @return ignore
     */
    protected OutputStream generatorOutputStream() {
        String path = Files1.getPath(generatorPathDir + Const.SEPARATOR + generatorBaseName + generatorNameSuffix + (++currentTargetIndex) + "." + suffix);
        Files1.touch(path);
        return Files1.openOutputStreamSafe(path);
    }

    /**
     * 是否还有下一个
     *
     * @return ignore
     */
    protected boolean hasNext() {
        if (targets == null) {
            return true;
        }
        return targets.size() > currentTargetIndex;
    }

    /**
     * 获取下一个流
     */
    protected void next() {
        if (this.hasNext()) {
            if (targets == null) {
                this.currentOutputStream = this.generatorOutputStream();
            } else {
                this.currentOutputStream = targets.get(currentTargetIndex++);
            }
        } else {
            this.currentOutputStream = null;
        }
    }

    @Override
    public void close() {
        if (autoClose && targets != null) {
            targets.forEach(Streams::close);
        }
    }

}
