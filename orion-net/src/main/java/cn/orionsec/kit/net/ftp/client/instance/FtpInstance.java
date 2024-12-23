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
package cn.orionsec.kit.net.ftp.client.instance;

import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.define.StreamEntry;
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.io.Files1;
import cn.orionsec.kit.lang.utils.io.Streams;
import cn.orionsec.kit.lang.utils.time.Dates;
import cn.orionsec.kit.net.ftp.client.FtpFile;
import cn.orionsec.kit.net.ftp.client.FtpFileFilter;
import cn.orionsec.kit.net.ftp.client.config.FtpConfig;
import cn.orionsec.kit.net.ftp.client.pool.FtpClientPool;
import cn.orionsec.kit.net.ftp.client.transfer.FtpDownloader;
import cn.orionsec.kit.net.ftp.client.transfer.FtpUploader;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * FTP 操作实例
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/3/17 16:10
 */
@SuppressWarnings("ALL")
public class FtpInstance extends BaseFtpInstance {

    public FtpInstance(FtpClientPool pool) {
        super(pool);
    }

    public FtpInstance(FTPClient client, FtpConfig config) {
        super(client, config);
    }

    @Override
    public void change() {
        try {
            client.changeWorkingDirectory(this.serverCharset(config.getRemoteRootDir()));
        } catch (Exception e) {
            throw Exceptions.ftp(e);
        }
    }

    @Override
    public void change(String dir) {
        try {
            if (!client.changeWorkingDirectory(this.serverCharset(config.getRemoteRootDir() + dir))) {
                this.makeDirectories(dir);
                client.changeWorkingDirectory(this.serverCharset(config.getRemoteRootDir() + dir));
            }
        } catch (Exception e) {
            throw Exceptions.ftp(e);
        }
    }

    @Override
    public String getWorkDirectory() {
        try {
            return client.printWorkingDirectory();
        } catch (Exception e) {
            throw Exceptions.ftp(e);
        }
    }

    @Override
    public boolean isExist(String file) {
        String parentPath = Files1.getParentPath(file);
        List<FtpFile> list = this.listFiles(parentPath, false, true);
        for (FtpFile s : list) {
            if (Files1.getFileName(s.getPath()).equals(Files1.getFileName(file.trim()))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public FtpFile getFile(String file) {
        String parentPath = Files1.getParentPath(file);
        List<FtpFile> files = this.listFiles(parentPath, false, true);
        for (FtpFile ftpFile : files) {
            if (ftpFile.getName().equals(Files1.getFileName(file))) {
                return ftpFile;
            }
        }
        return null;
    }

    @Override
    public long getSize(String file) {
        FtpFile ftpFile = this.getFile(file);
        if (ftpFile == null) {
            return -1;
        }
        return ftpFile.getSize();
    }

    @Override
    public long getModifyTime(String file) {
        try {
            // .e.g 213 20200202020202.000
            String modificationTime = client.getModificationTime(file);
            if (modificationTime == null) {
                return -1;
            }
            String time = modificationTime.split(Const.SPACE)[1];
            return Dates.parse(time, Dates.YMD_HMS2).getTime();
        } catch (Exception e) {
            throw Exceptions.ftp(e);
        }
    }

    @Override
    public void setModifyTime(String file, Date time) {
        try {
            client.setModificationTime(file, Dates.format(time, Dates.YMD_HMS2));
        } catch (Exception e) {
            throw Exceptions.ftp(e);
        }
    }

    @Override
    public void truncate(String file) throws IOException {
        OutputStream out = null;
        try {
            out = client.storeFileStream(this.serverCharset(config.getRemoteRootDir() + file));
            out.flush();
        } catch (Exception e) {
            throw Exceptions.ftp(e);
        } finally {
            Streams.close(out);
            if (out != null) {
                client.completePendingCommand();
            }
        }
    }

    @Override
    public void remove(String file) {
        FtpFile ftpFile = this.getFile(file);
        if (ftpFile == null) {
            return;
        }
        if (ftpFile.isDirectory()) {
            this.removeDir(file);
        } else {
            this.removeFile(file);
        }
    }

    @Override
    public void removeFile(String file) {
        try {
            client.deleteFile(this.serverCharset(file));
        } catch (Exception e) {
            throw Exceptions.ftp(e);
        }
    }

    @Override
    public void removeDir(String dir) {
        try {
            List<FtpFile> list = this.listFiles(dir, false, true);
            for (FtpFile s : list) {
                String path = s.getPath();
                if (s.isDirectory()) {
                    this.removeDir(path);
                    client.removeDirectory(this.serverCharset(Files1.getPath(config.getRemoteRootDir(), path)));
                } else {
                    client.deleteFile(this.serverCharset(Files1.getPath(config.getRemoteRootDir(), path)));
                }
            }
            client.changeWorkingDirectory(this.serverCharset(config.getRemoteRootDir() + Files1.getParentPath(dir)));
            client.removeDirectory(this.serverCharset(Files1.getFileName(dir)));
        } catch (Exception e) {
            throw Exceptions.ftp(e);
        }
    }

    @Override
    public void makeDirectories(String dir) {
        try {
            String[] dirs = Files1.getPath(dir).split(SEPARATOR);
            String base = config.getRemoteRootDir();
            for (String d : dirs) {
                if (d == null || Strings.EMPTY.equals(d)) {
                    continue;
                }
                base = this.serverCharset(base + SEPARATOR + d);
                if (!client.changeWorkingDirectory(base)) {
                    client.makeDirectory(base);
                    client.changeWorkingDirectory(base);
                }
            }
        } catch (Exception e) {
            throw Exceptions.ftp(e);
        }
    }

    @Override
    public void touch(String file) {
        String fileName = Files1.getFileName(file);
        String filePath = this.serverCharset(config.getRemoteRootDir() + file);
        String parentPath = Files1.getParentPath(Files1.getPath(file));
        this.makeDirectories(parentPath);
        for (FtpFile s : this.listFiles(parentPath, false)) {
            if (Files1.getFileName(s.getPath()).equals(fileName)) {
                return;
            }
        }
        this.change(parentPath);
        try {
            client.storeFile(this.serverCharset(filePath), new ByteArrayInputStream(new byte[]{}));
        } catch (IOException e) {
            throw Exceptions.ftp(e);
        }
    }

    @Override
    public void move(String source, String target) {
        source = Files1.getPath(source);
        target = Files1.getPath(target);
        try {
            if (target.charAt(0) == '/') {
                // 绝对路径
                target = Files1.getPath(config.getRemoteRootDir() + Files1.normalize(target));
            } else {
                // 相对路径
                target = Files1.getPath(Files1.normalize(Files1.getPath(source + "/../" + target)));
            }
            this.change(Files1.getParentPath(source));
            this.makeDirectories(Files1.getParentPath(target));
            client.rename(source, target);
        } catch (Exception e) {
            throw Exceptions.ftp(e);
        }
    }

    // -------------------- stream --------------------

    @Override
    public InputStream openInputStream(String file, long skip) throws IOException {
        try {
            client.setRestartOffset(skip);
            InputStream in = client.retrieveFileStream(this.serverCharset(config.getRemoteRootDir() + file));
            if (in == null) {
                throw Exceptions.ftp("not found file " + file);
            }
            return in;
        } catch (Exception e) {
            throw Exceptions.io("cannot get file input stream " + file, e);
        } finally {
            client.setRestartOffset(0);
        }
    }

    @Override
    public OutputStream openOutputStream(String file, boolean append) throws IOException {
        this.makeDirectories(Files1.getParentPath(file));
        try {
            if (append) {
                return client.appendFileStream(this.serverCharset(config.getRemoteRootDir() + file));
            } else {
                return client.storeFileStream(this.serverCharset(config.getRemoteRootDir() + file));
            }
        } catch (Exception e) {
            throw Exceptions.io("cannot get file out stream " + file, e);
        }
    }

    // -------------------- read --------------------

    @Override
    public int read(String file, long skip, byte[] bs, int off, int len) throws IOException {
        InputStream in = null;
        try {
            in = this.openInputStream(file, skip);
            return in.read(bs, off, len);
        } finally {
            Streams.close(in);
            client.setRestartOffset(0);
            if (in != null) {
                client.completePendingCommand();
            }
        }
    }

    // -------------------- transfer --------------------

    @Override
    protected long doTransfer(String path, OutputStream out, long skip, int size, boolean close) throws IOException {
        long r = 0;
        byte[] bs = new byte[bufferSize];
        InputStream in = null;
        try {
            in = this.openInputStream(path, skip);
            if (size != -1) {
                boolean mod = size % bufferSize == 0;
                long readTimes = size / bufferSize;
                if (mod || readTimes == 0) {
                    readTimes++;
                }
                for (int i = 0; i < readTimes; i++) {
                    if (readTimes == 1) {
                        int read = in.read(bs, 0, size);
                        out.write(bs, 0, read);
                        r += read;
                    } else {
                        int read = in.read(bs, 0, bufferSize);
                        if (read != -1) {
                            out.write(bs, 0, read);
                            r += read;
                        } else {
                            break;
                        }
                    }
                }
            } else {
                int read;
                while ((read = in.read(bs, 0, bufferSize)) != -1) {
                    out.write(bs, 0, read);
                    r += read;
                }
            }
        } finally {
            Streams.close(in);
            client.setRestartOffset(0);
            if (in != null) {
                client.completePendingCommand();
            }
            if (close) {
                Streams.close(out);
            }
        }
        return r;
    }

    // -------------------- write --------------------

    @Override
    protected void doWrite(String path, InputStream in, StreamEntry entry) throws IOException {
        this.write(path, in, entry, false);
    }

    // -------------------- append --------------------

    @Override
    protected void doAppend(String path, InputStream in, StreamEntry entry) throws IOException {
        this.write(path, in, entry, true);
    }

    private void write(String path, InputStream in, StreamEntry entry, boolean append) throws IOException {
        OutputStream out = null;
        try {
            out = this.openOutputStream(path, append);
            if (in != null) {
                byte[] bs = new byte[bufferSize];
                int read;
                while ((read = in.read(bs)) != -1) {
                    out.write(bs, 0, read);
                }
            } else if (entry != null) {
                out.write(entry.getBytes(), entry.getOff(), entry.getLen());
            }
            out.flush();
        } finally {
            Streams.close(out);
            if (out != null) {
                client.completePendingCommand();
            }
        }
    }

    // -------------------- upload --------------------

    @Override
    public void uploadFile(String remoteFile, InputStream in, boolean close) throws IOException {
        BufferedInputStream buffer = null;
        try {
            String parentPath = Files1.getParentPath(remoteFile);
            this.makeDirectories(parentPath);
            client.storeFile(this.serverCharset(config.getRemoteRootDir() + remoteFile), buffer = new BufferedInputStream(in));
        } finally {
            if (close) {
                Streams.close(in);
                Streams.close(buffer);
            }
        }
    }

    @Override
    public void uploadDir(String remoteDir, String localDir, boolean child) throws IOException {
        localDir = Files1.getPath(localDir);
        List<File> dirs = Files1.listDirs(localDir, child);
        List<File> files = Files1.listFiles(localDir, child);
        for (File dir : dirs) {
            this.makeDirectories(Files1.getPath(remoteDir, dir.getAbsolutePath().substring(localDir.length())));
        }
        for (File file : files) {
            String path = Files1.getPath(remoteDir, file.getAbsolutePath().substring(localDir.length()));
            this.change(Files1.getParentPath(path));
            this.uploadFile(path, file);
        }
    }

    // -------------------- download --------------------

    @Override
    public void downloadFile(String remoteFile, OutputStream out, boolean close) throws IOException {
        InputStream in = null;
        try {
            in = this.openInputStream(remoteFile);
            Streams.transfer(in, out);
        } finally {
            if (close) {
                Streams.close(out);
            }
            Streams.close(in);
            client.setRestartOffset(0);
            if (in != null) {
                client.completePendingCommand();
            }
        }
    }

    @Override
    public void downloadDir(String remoteDir, String localDir, boolean child) throws IOException {
        remoteDir = Files1.getPath(remoteDir);
        if (!child) {
            List<FtpFile> list = this.listFiles(remoteDir, false);
            for (FtpFile s : list) {
                this.downloadFile(s.getPath(), Files1.getPath(localDir, Files1.getFileName(s.getPath())));
            }
        } else {
            List<FtpFile> list = this.listDirs(remoteDir, true);
            for (FtpFile s : list) {
                Files1.mkdirs(Files1.getPath(localDir, s.getPath().substring(remoteDir.length())));
            }
            list = this.listFiles(remoteDir, true);
            for (FtpFile s : list) {
                this.downloadFile(s.getPath(), Files1.getPath(localDir, s.getPath().substring(remoteDir.length())));
            }
        }
    }

    // -------------------- big file --------------------

    @Override
    public FtpUploader upload(String remote, String local) {
        return new FtpUploader(this, remote, local);
    }

    @Override
    public FtpUploader upload(String remote, File local) {
        return new FtpUploader(this, remote, local);
    }

    @Override
    public FtpDownloader download(String remote, String local) {
        return new FtpDownloader(this, remote, local);
    }

    @Override
    public FtpDownloader download(String remote, File local) {
        return new FtpDownloader(this, remote, local);
    }

    // -------------------- list --------------------

    @Override
    public List<FtpFile> listFilesFilter(String path, FtpFileFilter filter, boolean child, boolean dir) {
        String base = config.getRemoteRootDir();
        List<FtpFile> list = new ArrayList<>();
        try {
            FTPFile[] files = client.listFiles(this.serverCharset(Files1.getPath(base, path)));
            for (FTPFile file : files) {
                String fn = file.getName();
                String t = Files1.getPath(path, fn);
                boolean isDir = file.isDirectory();
                if (!isDir || dir) {
                    FtpFile f = new FtpFile(t, file);
                    if (filter.test(f)) {
                        list.add(f);
                    }
                }
                if (isDir && child) {
                    list.addAll(this.listFilesFilter(t + SEPARATOR, filter, true, dir));
                }
            }
        } catch (IOException e) {
            throw Exceptions.ftp(e);
        }
        return list;
    }

}
