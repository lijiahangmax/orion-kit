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
package com.orion.test.file;

import com.orion.lang.utils.io.Files1;
import com.orion.lang.utils.io.compress.CompressTypeEnum;
import com.orion.lang.utils.io.compress.Compresses;
import com.orion.lang.utils.io.compress.FileCompressor;
import com.orion.lang.utils.io.compress.FileDecompressor;
import com.orion.lang.utils.io.compress.bz2.Bz2Compressor;
import com.orion.lang.utils.io.compress.bz2.Bz2Decompressor;
import com.orion.lang.utils.io.compress.gz.GzCompressor;
import com.orion.lang.utils.io.compress.gz.GzDecompressor;
import com.orion.lang.utils.io.compress.jar.JarCompressor;
import com.orion.lang.utils.io.compress.jar.JarDecompressor;
import com.orion.lang.utils.io.compress.mix.TarBz2Compressor;
import com.orion.lang.utils.io.compress.mix.TarBz2Decompressor;
import com.orion.lang.utils.io.compress.mix.TarGzCompressor;
import com.orion.lang.utils.io.compress.mix.TarGzDecompressor;
import com.orion.lang.utils.io.compress.tar.TarCompressor;
import com.orion.lang.utils.io.compress.tar.TarDecompressor;
import com.orion.lang.utils.io.compress.z7.Z7Compressor;
import com.orion.lang.utils.io.compress.z7.Z7Decompressor;
import com.orion.lang.utils.io.compress.zip.ZipCompressor;
import com.orion.lang.utils.io.compress.zip.ZipDecompressor;
import org.junit.Test;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/27 19:19
 */
public class CompressTests {

    private String dir = "C:\\Users\\ljh15\\Desktop\\3.0";

    private String desktop = "C:\\Users\\ljh15\\Desktop";

    private String target = "C:\\Users\\ljh15\\Desktop\\target1";

    @Test
    public void zipCompress() throws Exception {
        ZipCompressor c = new ZipCompressor();
        c.addFile(dir);
        c.addFile("REAMDE", "readme".getBytes());
        c.addFile("_/REAMDE", "readme".getBytes());
        c.setCompressPath(desktop);
        c.setFileName("3.0");
        c.compress();
        String absoluteCompressPath = c.getAbsoluteCompressPath();
        System.out.println(absoluteCompressPath);
    }

    @Test
    public void zipDecompress() throws Exception {
        ZipDecompressor d = new ZipDecompressor();
        d.setDecompressFile("C:\\Users\\ljh15\\Desktop\\3.0.zip");
        d.setDecompressTargetPath(target);
        d.decompress();
        Files1.delete(target);
        Files1.delete(d.getDecompressFile());
    }

    @Test
    public void jarCompress() throws Exception {
        JarCompressor c = new JarCompressor();
        c.addFile(dir);
        c.addFile("REAMDE", "readme".getBytes());
        c.addFile("_/REAMDE", "readme".getBytes());
        c.setCompressPath(desktop);
        c.setFileName("3.0");
        c.compress();
        String absoluteCompressPath = c.getAbsoluteCompressPath();
        System.out.println(absoluteCompressPath);
    }

    @Test
    public void jarDecompress() throws Exception {
        JarDecompressor d = new JarDecompressor();
        d.setDecompressFile("C:\\Users\\ljh15\\Desktop\\3.0.jar");
        d.setDecompressTargetPath(target);
        d.decompress();
        Files1.delete(target);
        Files1.delete(d.getDecompressFile());
    }

    @Test
    public void z7Compress() throws Exception {
        Z7Compressor c = new Z7Compressor();
        c.addFile(dir);
        c.addFile("REAMDE", "readme".getBytes());
        c.addFile("_/REAMDE", "readme".getBytes());
        c.setCompressPath(desktop);
        c.setFileName("3.0");
        c.compress();
        String absoluteCompressPath = c.getAbsoluteCompressPath();
        System.out.println(absoluteCompressPath);
    }

    @Test
    public void z7Decompress() throws Exception {
        Z7Decompressor d = new Z7Decompressor();
        d.setDecompressFile("C:\\Users\\ljh15\\Desktop\\3.0.7z");
        d.setDecompressTargetPath(target);
        d.decompress();
        Files1.delete(target);
        Files1.delete(d.getDecompressFile());
    }

    @Test
    public void tarCompress() throws Exception {
        TarCompressor c = new TarCompressor();
        c.addFile(dir);
        c.addFile("REAMDE", "readme".getBytes());
        c.addFile("_/REAMDE", "readme".getBytes());
        c.setCompressPath(desktop);
        c.setFileName("3.0");
        c.compress();
        String absoluteCompressPath = c.getAbsoluteCompressPath();
        System.out.println(absoluteCompressPath);
    }

    @Test
    public void tarDecompress() throws Exception {
        TarDecompressor d = new TarDecompressor();
        d.setDecompressFile("C:\\Users\\ljh15\\Desktop\\3.0.tar");
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
        c.setFileName("3.0");
        c.compress();
        String absoluteCompressPath = c.getAbsoluteCompressPath();
        System.out.println(absoluteCompressPath);
    }

    @Test
    public void gzDecompress() throws Exception {
        GzDecompressor d = new GzDecompressor();
        d.setDecompressFile("C:\\Users\\ljh15\\Desktop\\3.0.gz");
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
        c.setFileName("3.0");
        c.compress();
        String absoluteCompressPath = c.getAbsoluteCompressPath();
        System.out.println(absoluteCompressPath);
    }

    @Test
    public void bz2Decompress() throws Exception {
        Bz2Decompressor d = new Bz2Decompressor();
        d.setDecompressFile("C:\\Users\\ljh15\\Desktop\\3.0.bz2");
        d.setDecompressTargetPath(target);
        d.setDecompressTargetFileName("README");
        d.decompress();
        System.out.println(d.getDecompressTargetFile());
        Files1.delete(target);
        Files1.delete(d.getDecompressFile());
    }

    @Test
    public void tarGzCompress() throws Exception {
        TarGzCompressor c = new TarGzCompressor();
        c.addFile(dir);
        c.addFile("REAMDE", "readme".getBytes());
        c.addFile("_/REAMDE", "readme".getBytes());
        c.setCompressPath(desktop);
        c.setFileName("3.0");
        c.compress();
        String absoluteCompressPath = c.getAbsoluteCompressPath();
        System.out.println(absoluteCompressPath);
    }

    @Test
    public void tarGzDecompress() throws Exception {
        TarGzDecompressor d = new TarGzDecompressor();
        d.setDecompressFile("C:\\Users\\ljh15\\Desktop\\3.0.tar.gz");
        d.setDecompressTargetPath(target);
        d.decompress();
        Files1.delete(target);
        Files1.delete(d.getDecompressFile());
    }

    @Test
    public void tarBz2Compress() throws Exception {
        TarBz2Compressor c = new TarBz2Compressor();
        c.addFile(dir);
        c.addFile("REAMDE", "readme".getBytes());
        c.addFile("_/REAMDE", "readme".getBytes());
        c.setCompressPath(desktop);
        c.setFileName("3.0");
        c.compress();
        String absoluteCompressPath = c.getAbsoluteCompressPath();
        System.out.println(absoluteCompressPath);
    }

    @Test
    public void tarBz2Decompress() throws Exception {
        TarBz2Decompressor d = new TarBz2Decompressor();
        d.setDecompressFile("C:\\Users\\ljh15\\Desktop\\3.0.tar.bz2");
        d.setDecompressTargetPath(target);
        d.decompress();
        Files1.delete(target);
        Files1.delete(d.getDecompressFile());
    }

    @Test
    public void zip() {
        Compresses.zip(dir, desktop + "/3.0.zip");
    }

    @Test
    public void unzip() {
        Compresses.unzip(desktop + "/3.0.zip", target);
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
        CompressTypeEnum zip = CompressTypeEnum.ZIP;
        FileCompressor c = zip.compressor().get();
        c.addFile(dir);
        c.addFile("REAMDE", "readme".getBytes());
        c.addFile("_/REAMDE", "readme".getBytes());
        c.setCompressPath(desktop);
        c.setFileName("3.0");
        c.compress();
        String absoluteCompressPath = c.getAbsoluteCompressPath();
        System.out.println(absoluteCompressPath);

        FileDecompressor d = zip.decompressor().get();
        d.setDecompressFile("C:\\Users\\ljh15\\Desktop\\3.0.zip");
        d.setDecompressTargetPath(target);
        d.decompress();
        Files1.delete(target);
        Files1.delete(d.getDecompressFile());
    }

}
