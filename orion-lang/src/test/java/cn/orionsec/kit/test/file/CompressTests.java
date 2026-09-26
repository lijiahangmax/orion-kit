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
package cn.orionsec.kit.test.file;

import cn.orionsec.kit.lang.utils.Systems;
import cn.orionsec.kit.lang.utils.io.Files1;
import cn.orionsec.kit.lang.utils.io.compress.CompressTypeEnum;
import cn.orionsec.kit.lang.utils.io.compress.Compresses;
import cn.orionsec.kit.lang.utils.io.compress.FileCompressor;
import cn.orionsec.kit.lang.utils.io.compress.FileDecompressor;
import cn.orionsec.kit.lang.utils.io.compress.bz2.Bz2Compressor;
import cn.orionsec.kit.lang.utils.io.compress.bz2.Bz2Decompressor;
import cn.orionsec.kit.lang.utils.io.compress.gz.GzCompressor;
import cn.orionsec.kit.lang.utils.io.compress.gz.GzDecompressor;
import cn.orionsec.kit.lang.utils.io.compress.jar.JarCompressor;
import cn.orionsec.kit.lang.utils.io.compress.jar.JarDecompressor;
import cn.orionsec.kit.lang.utils.io.compress.mix.TarBz2Compressor;
import cn.orionsec.kit.lang.utils.io.compress.mix.TarBz2Decompressor;
import cn.orionsec.kit.lang.utils.io.compress.mix.TarGzCompressor;
import cn.orionsec.kit.lang.utils.io.compress.mix.TarGzDecompressor;
import cn.orionsec.kit.lang.utils.io.compress.tar.TarCompressor;
import cn.orionsec.kit.lang.utils.io.compress.tar.TarDecompressor;
import cn.orionsec.kit.lang.utils.io.compress.z7.Z7Compressor;
import cn.orionsec.kit.lang.utils.io.compress.z7.Z7Decompressor;
import cn.orionsec.kit.lang.utils.io.compress.zip.ZipCompressor;
import cn.orionsec.kit.lang.utils.io.compress.zip.ZipDecompressor;
import org.junit.Assume;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.File;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/27 19:19
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CompressTests {

    private String fileName = "3";

    private String dir = Systems.HOME_DIR + "\\orion-kit-test\\" + fileName;

    private String desktop = Systems.HOME_DIR + "\\orion-kit-test";

    private String target = Systems.HOME_DIR + "\\orion-kit-test\\target1";

    /**
     * 文件不存在则跳过测试
     */
    private void assumeExists(String... files) {
        for (String file : files) {
            Assume.assumeTrue(new File(file).exists());
        }
    }

    @Test
    public void zipCompress() throws Exception {
        assumeExists(dir);
        ZipCompressor c = new ZipCompressor();
        c.addFile(dir);
        c.addFile("REAMDE", "readme".getBytes());
        c.addFile("_/REAMDE", "readme".getBytes());
        c.setCompressPath(desktop);
        c.setFileName(fileName);
        c.compress();
        String absoluteCompressPath = c.getAbsoluteCompressPath();
        System.out.println(absoluteCompressPath);
    }

    @Test
    public void zipDecompress() throws Exception {
        assumeExists(dir + ".zip");
        ZipDecompressor d = new ZipDecompressor();
        d.setDecompressFile(dir + ".zip");
        d.setDecompressTargetPath(target);
        d.decompress();
        Files1.delete(target);
        Files1.delete(d.getDecompressFile());
    }

    @Test
    public void jarCompress() throws Exception {
        assumeExists(dir);
        JarCompressor c = new JarCompressor();
        c.addFile(dir);
        c.addFile("REAMDE", "readme".getBytes());
        c.addFile("_/REAMDE", "readme".getBytes());
        c.setCompressPath(desktop);
        c.setFileName(fileName);
        c.compress();
        String absoluteCompressPath = c.getAbsoluteCompressPath();
        System.out.println(absoluteCompressPath);
    }

    @Test
    public void jarDecompress() throws Exception {
        assumeExists(dir + ".jar");
        JarDecompressor d = new JarDecompressor();
        d.setDecompressFile(dir + ".jar");
        d.setDecompressTargetPath(target);
        d.decompress();
        Files1.delete(target);
        Files1.delete(d.getDecompressFile());
    }

    @Test
    public void z7Compress() throws Exception {
        assumeExists(dir);
        Z7Compressor c = new Z7Compressor();
        c.addFile(dir);
        c.addFile("REAMDE", "readme".getBytes());
        c.addFile("_/REAMDE", "readme".getBytes());
        c.setCompressPath(desktop);
        c.setFileName(fileName);
        c.compress();
        String absoluteCompressPath = c.getAbsoluteCompressPath();
        System.out.println(absoluteCompressPath);
    }

    @Test
    public void z7Decompress() throws Exception {
        assumeExists(dir + ".7z");
        Z7Decompressor d = new Z7Decompressor();
        d.setDecompressFile(dir + ".7z");
        d.setDecompressTargetPath(target);
        d.decompress();
        Files1.delete(target);
        Files1.delete(d.getDecompressFile());
    }

    @Test
    public void tarCompress() throws Exception {
        assumeExists(dir);
        TarCompressor c = new TarCompressor();
        c.addFile(dir);
        c.addFile("REAMDE", "readme".getBytes());
        c.addFile("_/REAMDE", "readme".getBytes());
        c.setCompressPath(desktop);
        c.setFileName(fileName);
        c.compress();
        String absoluteCompressPath = c.getAbsoluteCompressPath();
        System.out.println(absoluteCompressPath);
    }

    @Test
    public void tarDecompress() throws Exception {
        assumeExists(dir + ".tar");
        TarDecompressor d = new TarDecompressor();
        d.setDecompressFile(dir + ".tar");
        d.setDecompressTargetPath(target);
        d.decompress();
        Files1.delete(target);
        Files1.delete(d.getDecompressFile());
    }

    @Test
    public void gzCompress() throws Exception {
        GzCompressor c = new GzCompressor();
        c.setCompressFile("REAMDE", "readme".getBytes());
        c.setCompressPath(desktop);
        c.setFileName(fileName);
        c.compress();
        String absoluteCompressPath = c.getAbsoluteCompressPath();
        System.out.println(absoluteCompressPath);
    }

    @Test
    public void gzDecompress() throws Exception {
        assumeExists(dir + ".gz");
        GzDecompressor d = new GzDecompressor();
        d.setDecompressFile(dir + ".gz");
        d.setDecompressTargetPath(target);
        d.decompress();
        System.out.println(d.getDecompressTargetFile());
        Files1.delete(target);
        Files1.delete(d.getDecompressFile());
    }

    @Test
    public void bz2Compress() throws Exception {
        Bz2Compressor c = new Bz2Compressor();
        c.setCompressFile("readme".getBytes());
        c.setCompressPath(desktop);
        c.setFileName(fileName);
        c.compress();
        String absoluteCompressPath = c.getAbsoluteCompressPath();
        System.out.println(absoluteCompressPath);
    }

    @Test
    public void bz2Decompress() throws Exception {
        assumeExists(dir + ".bz2");
        Bz2Decompressor d = new Bz2Decompressor();
        d.setDecompressFile(dir + ".bz2");
        d.setDecompressTargetPath(target);
        d.setDecompressTargetFileName("README");
        d.decompress();
        System.out.println(d.getDecompressTargetFile());
        Files1.delete(target);
        Files1.delete(d.getDecompressFile());
    }

    @Test
    public void tarGzCompress() throws Exception {
        assumeExists(dir);
        TarGzCompressor c = new TarGzCompressor();
        c.addFile(dir);
        c.addFile("REAMDE", "readme".getBytes());
        c.addFile("_/REAMDE", "readme".getBytes());
        c.setCompressPath(desktop);
        c.setFileName(fileName);
        c.compress();
        String absoluteCompressPath = c.getAbsoluteCompressPath();
        System.out.println(absoluteCompressPath);
    }

    @Test
    public void tarGzDecompress() throws Exception {
        assumeExists(dir + ".tar.gz");
        TarGzDecompressor d = new TarGzDecompressor();
        d.setDecompressFile(dir + ".tar.gz");
        d.setDecompressTargetPath(target);
        d.decompress();
        Files1.delete(target);
        Files1.delete(d.getDecompressFile());
    }

    @Test
    public void tarBz2Compress() throws Exception {
        assumeExists(dir);
        TarBz2Compressor c = new TarBz2Compressor();
        c.addFile(dir);
        c.addFile("REAMDE", "readme".getBytes());
        c.addFile("_/REAMDE", "readme".getBytes());
        c.setCompressPath(desktop);
        c.setFileName(fileName);
        c.compress();
        String absoluteCompressPath = c.getAbsoluteCompressPath();
        System.out.println(absoluteCompressPath);
    }

    @Test
    public void tarBz2Decompress() throws Exception {
        assumeExists(dir + ".tar.bz2");
        TarBz2Decompressor d = new TarBz2Decompressor();
        d.setDecompressFile(dir + ".tar.bz2");
        d.setDecompressTargetPath(target);
        d.decompress();
        Files1.delete(target);
        Files1.delete(d.getDecompressFile());
    }

    @Test
    public void zip() {
        assumeExists(dir);
        Compresses.zip(dir, dir + ".zip");
    }

    @Test
    public void unzip() {
        assumeExists(dir);
        Compresses.zip(dir, dir + ".zip");
        Compresses.unzip(dir + ".zip", target);
    }

    @Test
    public void compressAll() throws Exception {
        this.zipCompress();
        this.jarCompress();
        this.z7Compress();
        this.tarCompress();
        this.gzCompress();
        this.bz2Compress();
        this.tarGzCompress();
        this.tarBz2Compress();
    }

    @Test
    public void decompressAll() throws Exception {
        this.zipDecompress();
        this.jarDecompress();
        this.z7Decompress();
        this.tarDecompress();
        this.gzDecompress();
        this.bz2Decompress();
        this.tarGzDecompress();
        this.tarBz2Decompress();
    }

    @Test
    public void testEnum() throws Exception {
        assumeExists(dir);
        CompressTypeEnum zip = CompressTypeEnum.ZIP;
        FileCompressor c = zip.compressor().get();
        c.addFile(dir);
        c.addFile("REAMDE", "readme".getBytes());
        c.addFile("_/REAMDE", "readme".getBytes());
        c.setCompressPath(desktop);
        c.setFileName(fileName);
        c.compress();
        String absoluteCompressPath = c.getAbsoluteCompressPath();
        System.out.println(absoluteCompressPath);

        FileDecompressor d = zip.decompressor().get();
        d.setDecompressFile(dir + ".zip");
        d.setDecompressTargetPath(target);
        d.decompress();
        Files1.delete(target);
        Files1.delete(d.getDecompressFile());
    }

}
