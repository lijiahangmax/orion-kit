package com.orion.net.base.file.sftp;

import com.orion.constant.Const;
import com.orion.lang.StreamEntry;
import com.orion.utils.Exceptions;
import com.orion.utils.collect.Lists;
import com.orion.utils.io.Files1;
import com.orion.utils.io.Streams;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/5/18 15:23
 */
public abstract class BaseSftpExecutor implements ISftpExecutor {

    /**
     * 编码格式
     */
    protected String charset;

    /**
     * 默认缓冲区大小
     */
    protected int bufferSize;

    protected BaseSftpExecutor() {
        this.bufferSize = Const.BUFFER_KB_32;
    }

    @Override
    public void mkdirs(String path) {
        this.doMakeDirs(path, this::mkdir);
    }

    @Override
    public void rm(String path) {
        try {
            SftpFile file = this.getFile(path);
            if (file == null) {
                return;
            }
            if (file.isDirectory()) {
                List<SftpFile> files = this.ll(path);
                for (SftpFile f : files) {
                    if (f.isDirectory()) {
                        this.rm(f.getPath());
                    } else {
                        this.removeFile(f.getPath());
                    }
                }
                this.removeDir(path);
            } else {
                this.removeFile(path);
            }
        } catch (Exception e) {
            if (SftpErrorMessage.NO_SUCH_FILE.isCause(e)) {
                return;
            }
            throw Exceptions.sftp(e);
        }
    }

    @Override
    public void mv(String source, String target) {
        try {
            source = Files1.getPath(source);
            target = Files1.getPath(target);
            if (target.charAt(0) == '/') {
                // 检查是否需要创建目标文件目录
                if (!this.isSameParentPath(source, target)) {
                    this.mkdirs(Files1.getParentPath(target));
                }
                // 绝对路径
                this.doMove(source, Files1.normalize(target));
            } else {
                // 相对路径
                this.doMove(source, Files1.normalize(Files1.getPath(source + "/../" + target)));
            }
        } catch (Exception e) {
            throw Exceptions.sftp(e);
        }
    }

    // -------------------- read --------------------

    @Override
    public int read(String path, byte[] bs) throws IOException {
        return this.read(path, 0, bs, 0, bs.length);
    }

    @Override
    public int read(String path, byte[] bs, int offset, int len) throws IOException {
        return this.read(path, 0, bs, offset, len);
    }

    @Override
    public int read(String path, long skip, byte[] bs) throws IOException {
        return this.read(path, skip, bs, 0, bs.length);
    }

    // -------------------- transfer --------------------

    @Override
    public long transfer(String path, OutputStream out) throws IOException {
        return this.doTransfer(path, out, 0, -1, false);
    }

    @Override
    public long transfer(String path, OutputStream out, long skip) throws IOException {
        return this.doTransfer(path, out, skip, -1, false);
    }

    @Override
    public long transfer(String path, OutputStream out, long skip, int size) throws IOException {
        return this.doTransfer(path, out, skip, size, false);
    }

    @Override
    public long transfer(String path, String file) throws IOException {
        Files1.touch(file);
        return this.doTransfer(path, Files1.openOutputStream(file), 0, -1, true);
    }

    @Override
    public long transfer(String path, File file) throws IOException {
        Files1.touch(file);
        return this.doTransfer(path, Files1.openOutputStream(file), 0, -1, true);
    }

    @Override
    public long transfer(String path, String file, long skip) throws IOException {
        Files1.touch(file);
        return this.doTransfer(path, Files1.openOutputStream(file), skip, -1, true);
    }

    @Override
    public long transfer(String path, File file, long skip) throws IOException {
        Files1.touch(file);
        return this.doTransfer(path, Files1.openOutputStream(file), skip, -1, true);
    }

    // -------------------- write --------------------

    @Override
    public void write(String path, InputStream in) throws IOException {
        this.doWrite(path, in, null, null);
    }

    @Override
    public void write(String path, byte[] bs) throws IOException {
        this.doWrite(path, null, new StreamEntry(bs), null);
    }

    @Override
    public void write(String path, byte[] bs, int off, int len) throws IOException {
        this.doWrite(path, null, new StreamEntry(bs, off, len), null);
    }

    @Override
    public void writeLine(String path, String line) throws IOException {
        this.doWrite(path, null, null, Lists.singleton(line));
    }

    @Override
    public void writeLines(String path, List<String> lines) throws IOException {
        this.doWrite(path, null, null, lines);
    }

    // -------------------- append --------------------

    @Override
    public void append(String path, InputStream in) throws IOException {
        this.doAppend(path, in, null, null);
    }

    @Override
    public void append(String path, byte[] bs) throws IOException {
        this.doAppend(path, null, new StreamEntry(bs), null);
    }

    @Override
    public void append(String path, byte[] bs, int off, int len) throws IOException {
        this.doAppend(path, null, new StreamEntry(bs, off, len), null);
    }

    @Override
    public void appendLine(String path, String line) throws IOException {
        this.doAppend(path, null, null, Lists.singleton(line));
    }

    @Override
    public void appendLines(String path, List<String> lines) throws IOException {
        this.doAppend(path, null, null, lines);
    }

    // -------------------- upload --------------------

    @Override
    public void uploadFile(String remoteFile, String localFile) throws IOException {
        this.uploadFile(remoteFile, Files1.openInputStreamFast(localFile), true);
    }

    @Override
    public void uploadFile(String remoteFile, File localFile) throws IOException {
        this.uploadFile(remoteFile, Files1.openInputStreamFast(localFile), true);
    }

    @Override
    public void uploadFile(String remoteFile, InputStream in) throws IOException {
        this.uploadFile(remoteFile, in, false);
    }

    @Override
    public void uploadFile(String remoteFile, InputStream in, boolean close) throws IOException {
        BufferedInputStream buffer = null;
        try {
            this.doWrite(remoteFile, buffer = new BufferedInputStream(in, bufferSize), null, null);
        } finally {
            if (close) {
                Streams.close(in);
                Streams.close(buffer);
            }
        }
    }

    @Override
    public void uploadDir(String remoteDir, File localDir) throws IOException {
        this.uploadDir(remoteDir, localDir.getAbsolutePath(), true);
    }

    @Override
    public void uploadDir(String remoteDir, String localDir) throws IOException {
        this.uploadDir(remoteDir, localDir, true);
    }

    @Override
    public void uploadDir(String remoteDir, File localDir, boolean child) throws IOException {
        this.uploadDir(remoteDir, localDir.getAbsolutePath(), child);
    }

    @Override
    public void uploadDir(String remoteDir, String localDir, boolean child) throws IOException {
        localDir = Files1.getPath(localDir);
        List<File> dirs = Files1.listDirs(localDir, child);
        List<File> files = Files1.listFiles(localDir, child);
        for (File dir : dirs) {
            this.mkdirs(Files1.getPath(remoteDir, dir.getAbsolutePath().substring(localDir.length())));
        }
        for (File file : files) {
            String path = Files1.getPath(remoteDir, file.getAbsolutePath().substring(localDir.length()));
            this.uploadFile(path, file);
        }
    }

    // -------------------- download --------------------

    @Override
    public void downloadFile(String remoteFile, String localFile) throws IOException {
        Files1.touch(localFile);
        this.downloadFile(remoteFile, Files1.openOutputStreamSafe(localFile), true);
    }

    @Override
    public void downloadFile(String remoteFile, File localFile) throws IOException {
        Files1.touch(localFile);
        this.downloadFile(remoteFile, Files1.openOutputStreamSafe(localFile), true);
    }

    @Override
    public void downloadFile(String remoteFile, OutputStream out) throws IOException {
        this.downloadFile(remoteFile, out, false);
    }

    @Override
    public void downloadFile(String remoteFile, OutputStream out, boolean close) throws IOException {
        this.doTransfer(remoteFile, out, 0, -1, close);
    }

    @Override
    public void downloadDir(String remoteDir, File localDir) throws IOException {
        this.downloadDir(remoteDir, localDir.getAbsolutePath(), true);
    }

    @Override
    public void downloadDir(String remoteDir, String localDir) throws IOException {
        this.downloadDir(remoteDir, localDir, true);
    }

    @Override
    public void downloadDir(String remoteDir, File localDir, boolean child) throws IOException {
        this.downloadDir(remoteDir, localDir.getAbsolutePath(), child);
    }

    @Override
    public void downloadDir(String remoteDir, String localDir, boolean child) throws IOException {
        remoteDir = Files1.getPath(remoteDir);
        if (!child) {
            List<SftpFile> list = this.listFiles(remoteDir, false);
            for (SftpFile s : list) {
                this.downloadFile(s.getPath(), Files1.getPath(localDir, Files1.getFileName(s.getPath())));
            }
        } else {
            List<SftpFile> list = this.listDirs(remoteDir, true);
            for (SftpFile s : list) {
                Files1.mkdirs(Files1.getPath(localDir, s.getPath().substring(remoteDir.length())));
            }
            list = this.listFiles(remoteDir, true);
            for (SftpFile s : list) {
                this.downloadFile(s.getPath(), Files1.getPath(localDir, s.getPath().substring(remoteDir.length())));
            }
        }
    }

    // -------------------- list --------------------

    @Override
    public List<SftpFile> listFiles(String path) {
        return this.listFiles(path, false, false);
    }

    @Override
    public List<SftpFile> listFiles(String path, boolean child) {
        return this.listFiles(path, child, false);
    }

    @Override
    public List<SftpFile> listFiles(String path, boolean child, boolean dir) {
        List<SftpFile> list = new ArrayList<>();
        try {
            List<SftpFile> ls = this.ll(path);
            for (SftpFile l : ls) {
                if (l.isDirectory()) {
                    if (dir) {
                        list.add(l);
                    }
                    if (child) {
                        list.addAll(this.listFiles(Files1.getPath(path, l.getName()), true, dir));
                    }
                } else {
                    list.add(l);
                }
            }
        } catch (Exception e) {
            throw Exceptions.sftp(e);
        }
        return list;
    }

    @Override
    public List<SftpFile> listDirs(String path) {
        return this.listDirs(path, false);
    }

    @Override
    public List<SftpFile> listDirs(String path, boolean child) {
        List<SftpFile> list = new ArrayList<>();
        try {
            List<SftpFile> ls = this.ll(path);
            for (SftpFile l : ls) {
                if (l.isDirectory()) {
                    list.add(l);
                    if (child) {
                        list.addAll(this.listDirs(Files1.getPath(path, l.getName()), true));
                    }
                }
            }
        } catch (Exception e) {
            throw Exceptions.sftp(e);
        }
        return list;
    }

    @Override
    public List<SftpFile> listFilesSuffix(String path, String suffix) {
        return this.listFilesSuffix(path, suffix, false, false);
    }

    @Override
    public List<SftpFile> listFilesSuffix(String path, String suffix, boolean child) {
        return this.listFilesSuffix(path, suffix, child, false);
    }

    @Override
    public List<SftpFile> listFilesSuffix(String path, String suffix, boolean child, boolean dir) {
        return this.doListFiles(path, SftpFileAttributeFilter.suffix(suffix), child, dir);
    }

    @Override
    public List<SftpFile> listFilesMatch(String path, String match) {
        return this.listFilesMatch(path, match, false, false);
    }

    @Override
    public List<SftpFile> listFilesMatch(String path, String match, boolean child) {
        return this.listFilesMatch(path, match, child, false);
    }

    @Override
    public List<SftpFile> listFilesMatch(String path, String match, boolean child, boolean dir) {
        return this.doListFiles(path, SftpFileAttributeFilter.match(match), child, dir);
    }

    @Override
    public List<SftpFile> listFilesPattern(String path, Pattern pattern) {
        return this.listFilesPattern(path, pattern, false, false);
    }

    @Override
    public List<SftpFile> listFilesPattern(String path, Pattern pattern, boolean child) {
        return this.listFilesPattern(path, pattern, child, false);
    }

    @Override
    public List<SftpFile> listFilesPattern(String path, Pattern pattern, boolean child, boolean dir) {
        return this.doListFiles(path, SftpFileAttributeFilter.pattern(pattern), child, dir);
    }

    @Override
    public List<SftpFile> listFilesFilter(String path, SftpFileAttributeFilter filter) {
        return this.listFilesFilter(path, filter, false, false);
    }

    @Override
    public List<SftpFile> listFilesFilter(String path, SftpFileAttributeFilter filter, boolean child) {
        return this.listFilesFilter(path, filter, child, false);
    }

    @Override
    public List<SftpFile> listFilesFilter(String path, SftpFileAttributeFilter filter, boolean child, boolean dir) {
        return this.doListFiles(path, filter, child, dir);
    }

    // -------------------- abstract --------------------

    /**
     * 移动文件
     *
     * @param source 原文件绝对路径
     * @param target 目标文件 绝对路径 相对路径都可以
     */
    protected abstract void doMove(String source, String target);

    /**
     * 执行创建文件夹
     *
     * @param path     path
     * @param creative 创建器
     */
    protected void doMakeDirs(String path, Consumer<String> creative) {
        SftpFile file = this.getFile(path, false);
        if (file != null && file.isDirectory()) {
            return;
        }
        List<String> parentPaths = Files1.getParentPaths(path);
        parentPaths.add(path);
        boolean check = true;
        for (String parentPath : parentPaths) {
            if (check) {
                SftpFile parent = this.getFile(parentPath, false);
                if (parent == null || !parent.isDirectory()) {
                    check = false;
                }
            }
            if (!check) {
                try {
                    creative.accept(parentPath);
                } catch (Exception e) {
                    throw Exceptions.sftp(e);
                }
            }
        }
    }

    /**
     * 执行传输
     *
     * @param path  path
     * @param out   out
     * @param skip  skip
     * @param size  size
     * @param close close
     * @return read
     * @throws IOException IOException
     */
    protected abstract long doTransfer(String path, OutputStream out, long skip, int size, boolean close) throws IOException;

    /**
     * 执行写入
     *
     * @param path  path
     * @param in    in
     * @param entry entry
     * @param lines lines
     * @throws IOException IOException
     */
    protected abstract void doWrite(String path, InputStream in, StreamEntry entry, List<String> lines) throws IOException;

    /**
     * 执行拼接
     *
     * @param path  path
     * @param in    in
     * @param entry entry
     * @param lines lines
     * @throws IOException IOException
     */
    protected abstract void doAppend(String path, InputStream in, StreamEntry entry, List<String> lines) throws IOException;

    /**
     * 查询文件列表
     *
     * @param path   path
     * @param filter filter
     * @param child  是否递归
     * @param dir    是否添加文件夹
     * @return files
     */
    private List<SftpFile> doListFiles(String path, SftpFileAttributeFilter filter, boolean child, boolean dir) {
        List<SftpFile> list = new ArrayList<>();
        try {
            List<SftpFile> ls = this.ll(path);
            for (SftpFile l : ls) {
                String fn = l.getName();
                boolean isDir = l.isDirectory();
                if (!isDir || dir) {
                    if (filter.accept(l)) {
                        list.add(l);
                    }
                }
                if (isDir && child) {
                    list.addAll(this.doListFiles(Files1.getPath(path, fn), filter, true, dir));
                }
            }
        } catch (Exception e) {
            throw Exceptions.sftp(e);
        }
        return list;
    }

    /**
     * 检查是否为相同父级目录
     *
     * @param source source
     * @param target target
     * @return 是否为父级目录
     */
    protected boolean isSameParentPath(String source, String target) {
        return Files1.getParentPath(source).equals(Files1.getParentPath(target));
    }

    // -------------------- other --------------------

    @Override
    public int getBufferSize() {
        return bufferSize;
    }

    @Override
    public String getCharset() {
        return charset;
    }

}
