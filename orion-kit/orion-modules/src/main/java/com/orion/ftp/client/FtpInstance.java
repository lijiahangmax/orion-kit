package com.orion.ftp.client;

import com.orion.ftp.client.bigfile.FtpDownload;
import com.orion.ftp.client.bigfile.FtpUpload;
import com.orion.utils.Exceptions;
import com.orion.utils.Matches;
import com.orion.utils.collect.Lists;
import com.orion.utils.io.Files1;
import com.orion.utils.io.Streams;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * FTP操作实例
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/3/17 16:10
 */
@SuppressWarnings("ALL")
public class FtpInstance {

    private static final String SYMBOL = "/";

    /**
     * FTP配置
     */
    private FtpConfig config;

    /**
     * FTP连接
     */
    private FTPClient client;

    public FtpInstance(FtpConfig config, FTPClient client) {
        this.config = config;
        this.client = client;
        try {
            client.changeWorkingDirectory(serverCharset(config.getRemoteBaseDir()));
        } catch (Exception e) {
            throw Exceptions.ftp(e);
        }
    }

    /**
     * 切换至根目录
     */
    public void change() {
        try {
            client.changeWorkingDirectory(serverCharset(config.getRemoteBaseDir()));
        } catch (Exception e) {
            throw Exceptions.ftp(e);
        }
    }

    /**
     * 切换目录
     *
     * @param dir dir
     */
    public void change(String dir) {
        try {
            if (!client.changeWorkingDirectory(serverCharset(config.getRemoteBaseDir() + dir))) {
                mkdirs(dir);
                client.changeWorkingDirectory(serverCharset(config.getRemoteBaseDir() + dir));
            }
        } catch (Exception e) {
            throw Exceptions.ftp(e);
        }
    }

    /**
     * 获取上一次命令的replyCode
     *
     * @return replyCode
     */
    public int replyCode() {
        return client.getReplyCode();
    }

    /**
     * 获取上一次命令的replyCode
     *
     * @return 信息
     */
    public String replyMsg() {
        return FtpConst.REPLY_CODE.get(client.getReplyCode());
    }

    /**
     * 获取上一次命令的reply
     *
     * @return true执行成功
     */
    public boolean reply() {
        return FTPReply.isPositiveCompletion(client.getReplyCode());
    }

    /**
     * 文件是否存在
     *
     * @param file 文件
     * @return true存在
     */
    public boolean exist(String file) {
        String parentPath = Files1.getParentPath(file);
        List<FtpFile> list = listFiles(parentPath, false);
        for (FtpFile s : list) {
            if (Files1.getFileName(s.getPath()).endsWith(Files1.getFileName(file.trim()))) {
                return true;
            }
        }
        return false;
    }


    /**
     * 文件列表 遍历
     *
     * @return 文件列表
     */
    public List<FtpFile> listFiles() {
        return listFiles("", true, false);
    }

    /**
     * 文件列表
     *
     * @param child 是否遍历文件夹
     * @return 文件列表
     */
    public List<FtpFile> listFiles(boolean child) {
        return listFiles("", child, false);
    }

    /**
     * 文件列表
     *
     * @param path 路径
     * @return 文件列表
     */
    public List<FtpFile> listFiles(String path) {
        return listFiles(path, true, false);
    }

    /**
     * 文件列表
     *
     * @param path  路径
     * @param child 是否遍历文件夹
     * @return 文件列表
     */
    public List<FtpFile> listFiles(String path, boolean child) {
        return listFiles(path, child, false);
    }

    /**
     * 列出文件夹
     *
     * @return 文件夹
     */
    public List<FtpFile> listDirs() {
        return listDirs("", true);
    }

    /**
     * 列出文件夹
     *
     * @param child 是否遍历子文件夹
     * @return 文件夹
     */
    public List<FtpFile> listDirs(boolean child) {
        return listDirs("", child);
    }

    /**
     * 列出文件夹
     *
     * @param dir 文件夹
     * @return 文件夹
     */
    public List<FtpFile> listDirs(String dir) {
        return listDirs(dir, true);
    }

    /**
     * 列出文件夹
     *
     * @param path  路径
     * @param child 是否遍历
     * @return 文件夹
     */
    public List<FtpFile> listDirs(String path, boolean child) {
        String base = config.getRemoteBaseDir();
        List<FtpFile> list = new ArrayList<>();
        try {
            FTPFile[] files = client.listFiles(serverCharset(base + path));
            for (FTPFile file : files) {
                String t = Files1.getPath(path + SYMBOL + file.getName());
                if (file.isDirectory()) {
                    list.add(toFtpFile(t, file));
                    if (child) {
                        list.addAll(listDirs(Files1.getPath(t + SYMBOL), true));
                    }
                }
            }
        } catch (IOException e) {
            // ignore
        }
        return list;
    }

    /**
     * 文件和文件夹列表
     *
     * @return 文件列表
     */
    public List<FtpFile> listFilesAndDirs() {
        return listFiles("", true, true);
    }

    /**
     * 文件和文件夹列表
     *
     * @param child 是否遍历文件夹
     * @return 文件列表
     */
    public List<FtpFile> listFilesAndDirs(boolean child) {
        return listFiles("", child, true);
    }

    /**
     * 文件和文件夹列表
     *
     * @param path 路径
     * @return 文件列表
     */
    public List<FtpFile> listFilesAndDirs(String path) {
        return listFiles(path, true, true);
    }

    /**
     * 文件和文件夹列表
     *
     * @param path  路径
     * @param child 是否遍历子文件夹
     * @return 文件列表
     */
    public List<FtpFile> listFilesAndDirs(String path, boolean child) {
        return listFiles(path, child, true);
    }

    /**
     * 文件和文件夹列表
     *
     * @param path  路径
     * @param child 是否遍历子目录
     * @param dir   是否添加文件夹
     * @return 文件列表
     */
    private List<FtpFile> listFiles(String path, boolean child, boolean dir) {
        String base = config.getRemoteBaseDir();
        List<FtpFile> list = new ArrayList<>();
        try {
            FTPFile[] files = client.listFiles(serverCharset(base + path));
            for (FTPFile file : files) {
                String t = Files1.getPath(path + SYMBOL + file.getName());
                if (file.isFile()) {
                    list.add(toFtpFile(t, file));
                } else if (file.isDirectory()) {
                    if (dir) {
                        list.add(toFtpFile(t, file));
                    }
                    if (child) {
                        list.addAll(listFiles(t + SYMBOL, true, dir));
                    }
                }
            }
        } catch (IOException e) {
            // ignore
        }
        return list;
    }

    /**
     * 列出根目录下的文件
     *
     * @param suffix 后缀
     * @return 文件
     */
    public List<FtpFile> listFilesSuffix(String suffix) {
        return this.listFilesSearch("", suffix, null, null, 1, true, false);
    }

    /**
     * 列出目录下的文件
     *
     * @param path   目录
     * @param suffix 后缀
     * @return 文件
     */
    public List<FtpFile> listFilesSuffix(String path, String suffix) {
        return this.listFilesSearch(path, suffix, null, null, 1, true, false);
    }

    /**
     * 列出根目录下的文件
     *
     * @param suffix 后缀
     * @param child  是否递归
     * @return 文件
     */
    public List<FtpFile> listFilesSuffix(String suffix, boolean child) {
        return this.listFilesSearch("", suffix, null, null, 1, child, false);
    }

    /**
     * 列出目录下的文件
     *
     * @param path   目录
     * @param suffix 后缀
     * @param child  是否递归
     * @return 文件
     */
    public List<FtpFile> listFilesSuffix(String path, String suffix, boolean child) {
        return this.listFilesSearch(path, suffix, null, null, 1, child, false);
    }

    /**
     * 列出根目录下的文件和文件夹
     *
     * @param suffix 后缀
     * @return 文件
     */
    public List<FtpFile> listFilesAndDirSuffix(String suffix) {
        return this.listFilesSearch("", suffix, null, null, 1, true, true);
    }

    /**
     * 列出目录下的文件和文件夹
     *
     * @param path   目录
     * @param suffix 后缀
     * @return 文件
     */
    public List<FtpFile> listFilesAndDirSuffix(String path, String suffix) {
        return this.listFilesSearch(path, suffix, null, null, 1, true, true);
    }

    /**
     * 列出根目录下的文件和文件夹
     *
     * @param suffix 后缀
     * @param child  是否递归
     * @return 文件
     */
    public List<FtpFile> listFilesAndDirSuffix(String suffix, boolean child) {
        return this.listFilesSearch("", suffix, null, null, 1, child, true);
    }

    /**
     * 列出目录下的文件和文件夹
     *
     * @param path   目录
     * @param suffix 后缀
     * @param child  是否递归
     * @return 文件
     */
    public List<FtpFile> listFilesAndDirSuffix(String path, String suffix, boolean child) {
        return this.listFilesSearch(path, suffix, null, null, 1, child, true);
    }

    /**
     * 列出根目录下的文件和文件夹
     *
     * @param match 名称
     * @return 文件
     */
    public List<FtpFile> listFilesMatch(String match) {
        return this.listFilesSearch("", match, null, null, 2, true, false);
    }

    /**
     * 列出目录下的文件
     *
     * @param path  目录
     * @param match 名称
     * @return 文件
     */
    public List<FtpFile> listFilesMatch(String path, String match) {
        return this.listFilesSearch(path, match, null, null, 2, true, false);
    }

    /**
     * 列出根目录下的文件
     *
     * @param match 名称
     * @param child 是否递归
     * @return 文件
     */
    public List<FtpFile> listFilesMatch(String match, boolean child) {
        return this.listFilesSearch("", match, null, null, 2, child, false);
    }

    /**
     * 列出目录下的文件
     *
     * @param path  目录
     * @param match 名称
     * @param child 是否递归
     * @return 文件
     */
    public List<FtpFile> listFilesMatch(String path, String match, boolean child) {
        return this.listFilesSearch(path, match, null, null, 2, child, false);
    }

    /**
     * 列出根目录下的文件和文件夹
     *
     * @param match 名称
     * @return 文件
     */
    public List<FtpFile> listFilesAndDirMatch(String match) {
        return this.listFilesSearch("", match, null, null, 2, true, true);
    }

    /**
     * 列出目录下的文件和文件夹
     *
     * @param path  目录
     * @param match 名称
     * @return 文件
     */
    public List<FtpFile> listFilesAndDirMatch(String path, String match) {
        return this.listFilesSearch(path, match, null, null, 2, true, true);
    }

    /**
     * 列出根目录下的文件和文件夹
     *
     * @param match 名称
     * @param child 是否递归
     * @return 文件
     */
    public List<FtpFile> listFilesAndDirMatch(String match, boolean child) {
        return this.listFilesSearch("", match, null, null, 2, child, true);
    }

    /**
     * 列出目录下的文件和文件夹
     *
     * @param path  目录
     * @param match 名称
     * @param child 是否递归
     * @return 文件
     */
    public List<FtpFile> listFilesAndDirMatch(String path, String match, boolean child) {
        return this.listFilesSearch(path, match, null, null, 2, child, true);
    }

    /**
     * 列出根目录下的文件
     *
     * @param pattern 正则
     * @return 文件
     */
    public List<FtpFile> listFilesPattern(Pattern pattern) {
        return this.listFilesSearch("", null, pattern, null, 3, true, false);
    }

    /**
     * 列出目录下的文件
     *
     * @param path    目录
     * @param pattern 正则
     * @return 文件
     */
    public List<FtpFile> listFilesPattern(String path, Pattern pattern) {
        return this.listFilesSearch(path, null, pattern, null, 3, true, false);
    }

    /**
     * 列出根目录下的文件
     *
     * @param pattern 正则
     * @param child   是否递归
     * @return 文件
     */
    public List<FtpFile> listFilesPattern(Pattern pattern, boolean child) {
        return this.listFilesSearch("", null, pattern, null, 3, child, false);
    }

    /**
     * 列出目录下的文件
     *
     * @param path    目录
     * @param pattern 正则
     * @param child   是否递归
     * @return 文件
     */
    public List<FtpFile> listFilesPattern(String path, Pattern pattern, boolean child) {
        return this.listFilesSearch(path, null, pattern, null, 3, child, false);
    }

    /**
     * 列出根目录下的文件和文件夹
     *
     * @param pattern 正则
     * @return 文件
     */
    public List<FtpFile> listFilesAndDirPattern(Pattern pattern) {
        return this.listFilesSearch("", null, pattern, null, 3, true, true);
    }

    /**
     * 列出目录下的文件和文件夹
     *
     * @param path    目录
     * @param pattern 正则
     * @return 文件
     */
    public List<FtpFile> listFilesAndDirPattern(String path, Pattern pattern) {
        return this.listFilesSearch(path, null, pattern, null, 3, true, true);
    }

    /**
     * 列出根目录下的文件和文件夹
     *
     * @param pattern 正则
     * @param child   是否递归
     * @return 文件
     */
    public List<FtpFile> listFilesAndDirPattern(Pattern pattern, boolean child) {
        return this.listFilesSearch("", null, pattern, null, 3, child, true);
    }

    /**
     * 列出目录下的文件和文件夹
     *
     * @param path    目录
     * @param pattern 正则
     * @param child   是否递归
     * @return 文件
     */
    public List<FtpFile> listFilesAndDirPattern(String path, Pattern pattern, boolean child) {
        return this.listFilesSearch(path, null, pattern, null, 3, child, true);
    }

    /**
     * 列出根目录下的文件
     *
     * @param filter 过滤器
     * @return 文件
     */
    public List<FtpFile> listFilesFilter(FTPFileFilter filter) {
        return this.listFilesSearch("", null, null, filter, 4, true, false);
    }

    /**
     * 列出目录下的文件
     *
     * @param path   目录
     * @param filter 过滤器
     * @return 文件
     */
    public List<FtpFile> listFilesFilter(String path, FTPFileFilter filter) {
        return this.listFilesSearch(path, null, null, filter, 4, true, false);
    }

    /**
     * 列出根目录下的文件
     *
     * @param filter 过滤器
     * @param child  是否递归
     * @return 文件
     */
    public List<FtpFile> listFilesFilter(FTPFileFilter filter, boolean child) {
        return this.listFilesSearch("", null, null, filter, 4, child, false);
    }

    /**
     * 列出目录下的文件
     *
     * @param path   目录
     * @param filter 过滤器
     * @param child  是否递归
     * @return 文件
     */
    public List<FtpFile> listFilesFilter(String path, FTPFileFilter filter, boolean child) {
        return this.listFilesSearch(path, null, null, filter, 4, child, false);
    }

    /**
     * 列出根目录下的文件和文件夹
     *
     * @param filter 过滤器
     * @return 文件
     */
    public List<FtpFile> listFilesAndDirFilter(FTPFileFilter filter) {
        return this.listFilesSearch("", null, null, filter, 4, true, true);
    }

    /**
     * 列出目录下的文件和文件夹
     *
     * @param path   目录
     * @param filter 过滤器
     * @return 文件
     */
    public List<FtpFile> listFilesAndDirFilter(String path, FTPFileFilter filter) {
        return this.listFilesSearch(path, null, null, filter, 4, true, true);
    }

    /**
     * 列出根目录下的文件和文件夹
     *
     * @param filter 过滤器
     * @param child  是否递归
     * @return 文件
     */
    public List<FtpFile> listFilesAndDirFilter(FTPFileFilter filter, boolean child) {
        return this.listFilesSearch("", null, null, filter, 4, child, true);
    }

    /**
     * 列出目录下的文件和文件夹
     *
     * @param path   目录
     * @param filter 过滤器
     * @param child  是否递归
     * @return 文件
     */
    public List<FtpFile> listFilesAndDirFilter(String path, FTPFileFilter filter, boolean child) {
        return this.listFilesSearch(path, null, null, filter, 4, child, true);
    }

    /**
     * 文件列表搜索
     *
     * @param path    列表
     * @param search  搜索
     * @param pattern 正则
     * @param filter  过滤器
     * @param type    类型 1后缀 2匹配 3正则 4过滤器
     * @param child   是否递归
     * @param dir     是否添加文件夹
     * @return 匹配的列表
     */
    private List<FtpFile> listFilesSearch(String path, String search, Pattern pattern, FTPFileFilter filter, int type, boolean child, boolean dir) {
        String base = config.getRemoteBaseDir();
        List<FtpFile> list = new ArrayList<>();
        try {
            FTPFile[] files = client.listFiles(serverCharset(Files1.getPath(base + path)));
            for (FTPFile file : files) {
                String fn = file.getName();
                String t = Files1.getPath(path + SYMBOL + fn);
                boolean isDir = file.isDirectory();
                if (!isDir || dir) {
                    boolean add = false;
                    if (type == 1 && fn.toLowerCase().endsWith(search.toLowerCase())) {
                        add = true;
                    } else if (type == 2 && fn.toLowerCase().contains(search.toLowerCase())) {
                        add = true;
                    } else if (type == 3 && Matches.test(fn, pattern)) {
                        add = true;
                    } else if (type == 4 && filter.accept(file)) {
                        add = true;
                    }
                    if (add) {
                        list.add(toFtpFile(t, file));
                    }
                }
                if (isDir && child) {
                    list.addAll(listFilesSearch(t + SYMBOL, search, pattern, filter, type, true, dir));
                }
            }
        } catch (IOException e) {
            // ignore
        }
        return list;
    }

    /**
     * FTPFile -> FtpFile
     *
     * @param path    文件路径
     * @param ftpFile FTPFile
     * @return FtpFile
     */
    private FtpFile toFtpFile(String path, FTPFile ftpFile) {
        FtpFile f = new FtpFile().setDate(ftpFile.getTimestamp())
                .setGroup(ftpFile.getGroup()).setHardLinkCount(ftpFile.getHardLinkCount())
                .setLink(ftpFile.getLink()).setName(ftpFile.getName()).setPath(path)
                .setRawListing(ftpFile.getRawListing()).setSize(ftpFile.getSize())
                .setType(ftpFile.getType()).setUser(ftpFile.getUser());
        f.getPermission()[0] = ftpFile.hasPermission(0, 0);
        f.getPermission()[1] = ftpFile.hasPermission(0, 1);
        f.getPermission()[2] = ftpFile.hasPermission(0, 2);
        f.getPermission()[3] = ftpFile.hasPermission(1, 0);
        f.getPermission()[4] = ftpFile.hasPermission(1, 1);
        f.getPermission()[5] = ftpFile.hasPermission(1, 2);
        f.getPermission()[6] = ftpFile.hasPermission(2, 0);
        f.getPermission()[7] = ftpFile.hasPermission(2, 1);
        f.getPermission()[8] = ftpFile.hasPermission(2, 2);
        return f;
    }

    /**
     * 获取文件属性
     *
     * @param file 文件
     * @return 未找到返回null
     */
    public FtpFileAttr getFileAttr(String file) {
        String parentPath = Files1.getParentPath(file);
        Map<String, FtpFileAttr> map = listFilesAttr(parentPath, false);
        for (Map.Entry<String, FtpFileAttr> entry : map.entrySet()) {
            FtpFileAttr value = entry.getValue();
            if (Files1.getFileName(value.getPath()).endsWith(Files1.getFileName(file))) {
                return value;
            }
        }
        return null;
    }

    /**
     * 获取文件属性列表 递归
     *
     * @return 属性列表
     */
    public Map<String, FtpFileAttr> listFilesAttr() {
        return listFilesAttr("", true);
    }

    /**
     * 获取文件属性列表 递归
     *
     * @param path 路径
     * @return 属性列表
     */
    public Map<String, FtpFileAttr> listFilesAttr(String path) {
        return listFilesAttr(path, true);
    }

    /**
     * 获取文件属性列表
     *
     * @param path  路径
     * @param child 是否递归
     * @return 属性列表
     */
    public Map<String, FtpFileAttr> listFilesAttr(String path, boolean child) {
        String base = config.getRemoteBaseDir();
        Map<String, FtpFileAttr> map = new HashMap<>();
        try {
            FTPFile[] files = client.listFiles(serverCharset(base + path));
            for (FTPFile file : files) {
                if (file.isFile()) {
                    String fileName = Files1.getPath(path + SYMBOL + file.getName());
                    FtpFileAttr attr = new FtpFileAttr();
                    attr.setPath(fileName);
                    attr.setModifyTime(file.getTimestamp().getTime());
                    attr.setSize(file.getSize());
                    attr.setPermission(file.getRawListing().split(" ")[0]);
                    map.put(fileName, attr);
                } else if (file.isDirectory() && child) {
                    map.putAll(listFilesAttr(path + SYMBOL + file.getName(), true));
                }
            }
        } catch (IOException e) {
            // ignore
        }
        return map;
    }

    /**
     * 下载文件
     *
     * @param file      远程文件路径
     * @param localFile 本地文件路径
     */
    public void download(String file, String localFile) {
        download(file, new File(localFile));
    }

    /**
     * 下载文件
     *
     * @param file      远程文件路径
     * @param localFile 本地文件路径
     */
    public void download(String file, File localFile) {
        OutputStream out = null;
        try {
            Files1.touch(localFile);
            client.retrieveFile(serverCharset(config.getRemoteBaseDir() + file), out = new BufferedOutputStream(Files1.openOutputStream(localFile)));
            out.flush();
        } catch (IOException e) {
            throw Exceptions.ftp(e);
        } finally {
            Streams.close(out);
        }
    }

    /**
     * 下载文件夹
     *
     * @param dir      远程文件夹
     * @param localDir 本地文件夹
     */
    public void downloadDir(String dir, String localDir) {
        downloadDir(dir, localDir, true);
    }

    /**
     * 下载文件夹
     *
     * @param dir      远程文件夹
     * @param localDir 本地文件夹
     * @param child    是否递归下载
     */
    public void downloadDir(String dir, String localDir, boolean child) {
        if (!child) {
            List<FtpFile> list = listFiles(dir, false);
            for (FtpFile s : list) {
                download(s.getPath(), Files1.getPath(localDir + "/" + Files1.getFileName(s.getPath())));
            }
        } else {
            List<FtpFile> list = listFiles(dir, true);
            for (FtpFile s : list) {
                download(s.getPath(), Files1.getPath(localDir + "/" + s.getPath()));
            }
            list = listDirs(dir, true);
            for (FtpFile s : list) {
                Files1.mkdirs(Files1.getPath(localDir + "/" + s.getPath()));
            }
        }
    }

    /**
     * 上传文件
     *
     * @param localFile  本地文件
     * @param remoteFile 远程文件
     */
    public void upload(String localFile, String remoteFile) {
        upload(new File(localFile), remoteFile);
    }

    /**
     * 上传文件
     *
     * @param localFile  本地文件
     * @param remoteFile 远程文件
     */
    public void upload(File localFile, String remoteFile) {
        InputStream in = null;
        try {
            String parentPath = Files1.getParentPath(remoteFile);
            mkdirs(parentPath);
            client.storeFile(serverCharset(config.getRemoteBaseDir() + remoteFile), new BufferedInputStream(in = Files1.openInputStream(localFile)));
        } catch (Exception e) {
            throw Exceptions.ftp(e);
        } finally {
            Streams.close(in);
        }
    }

    /**
     * 上传文件夹
     *
     * @param localDir  本地文件夹
     * @param remoteDir 远程文件夹
     */
    public void uploadDir(String localDir, String remoteDir) {
        uploadDir(localDir, remoteDir, true);
    }

    /**
     * 上传文件夹
     *
     * @param localDir  本地文件夹
     * @param remoteDir 远程文件夹
     * @param child     是否遍历上传
     */
    public void uploadDir(String localDir, String remoteDir, boolean child) {
        String localPrefix = localDir.substring(new File(localDir).getParent().length());
        localDir = Files1.getPath(localDir);
        List<File> dirs = Files1.listDirs(localDir, child);
        List<File> files = Files1.listFiles(localDir, child);
        for (File dir : dirs) {
            mkdirs(Files1.getPath(remoteDir + localPrefix + (dir.getAbsolutePath().substring(localDir.length()))));
        }
        for (File file : files) {
            String path = Files1.getPath(remoteDir + localPrefix + (file.getAbsolutePath().substring(localDir.length())));
            change(Files1.getParentPath(path));
            upload(file, path);
        }
    }

    /**
     * 获取文件拼接流
     * <p>
     * 使用完毕需要调用 client.completePendingCommand();
     * 这个操作在关闭io之后
     *
     * @param file 文件
     * @return OutputStream
     * @throws IOException IOException
     */
    public OutputStream getOutputStreamAppend(String file) throws IOException {
        return client.appendFileStream(serverCharset(config.getRemoteBaseDir() + file));
    }

    /**
     * 拼接流到文件
     *
     * @param file 文件
     * @param in   输入流
     */
    public void appendStream(String file, InputStream in) throws IOException {
        client.appendFile(serverCharset(config.getRemoteBaseDir() + file), in);
    }

    /**
     * 拼接字节数组到文件
     *
     * @param file 文件
     * @param bs   字节数组
     * @throws IOException IOException
     */
    public void append(String file, byte[] bs) throws IOException {
        append(file, bs, 0, bs.length);
    }

    /**
     * 拼接字节数组到文件
     *
     * @param file 文件
     * @param bs   字节数组
     * @param off  偏移量
     * @param len  长度
     * @throws IOException IOException
     */
    public void append(String file, byte[] bs, int off, int len) throws IOException {
        OutputStream out = null;
        try {
            out = client.appendFileStream(serverCharset(config.getRemoteBaseDir() + file));
            out.write(bs, off, len);
        } finally {
            Streams.close(out);
            if (out != null) {
                client.completePendingCommand();
            }
        }
    }

    /**
     * 拼接一行
     *
     * @param file 文件
     * @param line 行
     * @throws IOException IOException
     */
    public void appendLine(String file, String line) throws IOException {
        appendLines(file, Lists.of(line));
    }

    /**
     * 拼接多行
     *
     * @param file  文件
     * @param lines 行
     * @throws IOException IOException
     */
    public void appendLines(String file, List<String> lines) throws IOException {
        OutputStream out = null;
        try {
            out = client.appendFileStream(serverCharset(config.getRemoteBaseDir() + file));
            for (String line : lines) {
                out.write(line.getBytes());
                out.write(13);
            }
        } finally {
            Streams.close(out);
            if (out != null) {
                client.completePendingCommand();
            }
        }
    }

    /**
     * 获取文写入接流
     * <p>
     * 使用完毕需要调用 client.completePendingCommand();
     * 这个操作在关闭io之后
     *
     * @param file 文件
     * @return OutputStream
     * @throws IOException IOException
     */
    public OutputStream getOutputStreamWrite(String file) throws IOException {
        return client.storeFileStream(serverCharset(config.getRemoteBaseDir() + file));
    }

    /**
     * 写入流到文件
     *
     * @param file 文件
     * @param in   输入流
     */
    public void writeStream(String file, InputStream in) throws IOException {
        client.storeFile(serverCharset(config.getRemoteBaseDir() + file), in);
    }

    /**
     * 写入字节数组到文件
     *
     * @param file 文件
     * @param bs   字节数组
     * @throws IOException IOException
     */
    public void write(String file, byte[] bs) throws IOException {
        write(file, bs, 0, bs.length);
    }

    /**
     * 写入字节数组到文件
     *
     * @param file 文件
     * @param bs   字节数组
     * @param off  偏移量
     * @param len  长度
     * @throws IOException IOException
     */
    public void write(String file, byte[] bs, int off, int len) throws IOException {
        OutputStream out = null;
        try {
            out = client.storeFileStream(serverCharset(config.getRemoteBaseDir() + file));
            out.write(bs, off, len);
        } finally {
            Streams.close(out);
            if (out != null) {
                client.completePendingCommand();
            }
        }
    }

    /**
     * 写入一行
     *
     * @param file 文件
     * @param line 行
     * @throws IOException IOException
     */
    public void writeLine(String file, String line) throws IOException {
        writeLines(file, Lists.of(line));
    }

    /**
     * 写入多行
     *
     * @param file  文件
     * @param lines 行
     * @throws IOException IOException
     */
    public void writeLines(String file, List<String> lines) throws IOException {
        OutputStream out = null;
        try {
            out = client.storeFileStream(serverCharset(config.getRemoteBaseDir() + file));
            for (String line : lines) {
                out.write(line.getBytes());
                out.write(13);
            }
        } finally {
            Streams.close(out);
            if (out != null) {
                client.completePendingCommand();
            }
        }
    }

    /**
     * 获取文件输入流
     * <p>
     * 使用完毕需要调用 client.completePendingCommand();
     * 这个操作在关闭io之后
     *
     * @param file 文件
     * @return InputStream
     * @throws IOException IOException
     */
    public InputStream getInputStreamRead(String file) throws IOException {
        return client.retrieveFileStream(serverCharset(config.getRemoteBaseDir() + file));
    }

    /**
     * 获取文件输入流
     * <p>
     * 使用完毕需要调用 client.completePendingCommand();
     * 这个操作在关闭io之后
     *
     * @param file 文件
     * @param skip 跳过字节数
     * @return InputStream
     * @throws IOException IOException
     */
    public InputStream getInputStreamRead(String file, long skip) throws IOException {
        try {
            return client.retrieveFileStream(serverCharset(config.getRemoteBaseDir() + file));
        } finally {
            client.setRestartOffset(0);
        }
    }

    /**
     * 读取文件到数组
     *
     * @param file 文件
     * @param bs   数组
     * @return 读取的长度
     * @throws IOException IOException
     */
    public int read(String file, byte[] bs) throws IOException {
        return read(file, 0, bs, 0, bs.length);
    }

    /**
     * 读取文件到数组
     *
     * @param file 文件
     * @param skip 跳过字节数
     * @param bs   数组
     * @return 读取的长度
     * @throws IOException IOException
     */
    public int read(String file, long skip, byte[] bs) throws IOException {
        return read(file, skip, bs, 0, bs.length);
    }

    /**
     * 读取文件到数组
     *
     * @param file 文件
     * @param bs   数组
     * @param off  偏移量
     * @param len  长度
     * @return 读取的长度
     * @throws IOException IOException
     */
    public int read(String file, byte[] bs, int off, int len) throws IOException {
        return read(file, 0, bs, off, len);
    }

    /**
     * 读取文件到数组
     *
     * @param file 文件
     * @param skip 跳过字节数
     * @param bs   数组
     * @param off  偏移量
     * @param len  长度
     * @return 读取的长度
     * @throws IOException IOException
     */
    public int read(String file, long skip, byte[] bs, int off, int len) throws IOException {
        InputStream in = null;
        try {
            client.setRestartOffset(skip);
            in = client.retrieveFileStream(serverCharset(config.getRemoteBaseDir() + file));
            return in.read(bs, off, len);
        } finally {
            Streams.close(in);
            client.setRestartOffset(0);
            if (in != null) {
                client.completePendingCommand();
            }
        }
    }

    /**
     * 读取一行
     *
     * @param file 文件
     * @return 行
     * @throws IOException IOException
     */
    public String readLine(String file) throws IOException {
        return readLine(file, 0);
    }

    /**
     * 读取一行
     *
     * @param file 文件
     * @param skip 跳过字节数
     * @return 行
     * @throws IOException IOException
     */
    public String readLine(String file, long skip) throws IOException {
        BufferedReader in = null;
        try {
            client.setRestartOffset(skip);
            in = new BufferedReader(new InputStreamReader(client.retrieveFileStream(serverCharset(config.getRemoteBaseDir() + file))));
            return in.readLine();
        } finally {
            Streams.close(in);
            client.setRestartOffset(0);
            if (in != null) {
                client.completePendingCommand();
            }
        }
    }

    /**
     * 读取多行
     *
     * @param file 文件
     * @param skip 跳过字节数
     * @return 行
     * @throws IOException IOException
     */
    public List<String> readLines(String file, long skip) throws IOException {
        return readLines(file, skip, 0);
    }

    /**
     * 读取多行
     *
     * @param file  文件
     * @param lines 读取行数
     * @return 行
     * @throws IOException IOException
     */
    public List<String> readLines(String file, int lines) throws IOException {
        return readLines(file, 0, lines);
    }

    /**
     * 读取多行
     *
     * @param file  文件
     * @param skip  跳过字节数
     * @param lines 读取行数
     * @return 行
     * @throws IOException IOException
     */
    public List<String> readLines(String file, long skip, int lines) throws IOException {
        BufferedReader in = null;
        try {
            client.setRestartOffset(skip);
            in = new BufferedReader(new InputStreamReader(client.retrieveFileStream(serverCharset(config.getRemoteBaseDir() + file))));
            List<String> list = new ArrayList<>();
            if (lines > 0) {
                String line;
                for (int i = 0; i < lines && null != (line = in.readLine()); i++) {
                    list.add(line);
                }
            } else {
                String line;
                while (null != (line = in.readLine())) {
                    list.add(line);
                }
            }
            return list;
        } finally {
            Streams.close(in);
            client.setRestartOffset(0);
            if (in != null) {
                client.completePendingCommand();
            }
        }
    }

    /**
     * 获取大文件下载线程
     *
     * @param remoteFile 远程文件
     * @param localFile  本地文件
     * @return 线程
     */
    public FtpDownload getDownloadBigFileRunnable(String remoteFile, File localFile) {
        return new FtpDownload(this, remoteFile, localFile);
    }

    /**
     * 获取大文件下载线程
     *
     * @param remoteFile 远程文件
     * @param localFile  本地文件
     * @return 线程
     */
    public FtpDownload getDownloadBigFileRunnable(String remoteFile, String localFile) {
        return new FtpDownload(this, remoteFile, new File(localFile));
    }

    /**
     * 获取大文件上传线程
     *
     * @param remoteFile 远程文件
     * @param localFile  本地文件
     * @return 线程
     */
    public FtpUpload getUploadBigFileRunnable(String remoteFile, File localFile) {
        return new FtpUpload(this, remoteFile, localFile);
    }

    /**
     * 获取大文件上传线程
     *
     * @param remoteFile 远程文件
     * @param localFile  本地文件
     * @return 线程
     */
    public FtpUpload getUploadBigFileRunnable(String remoteFile, String localFile) {
        return new FtpUpload(this, remoteFile, new File(localFile));
    }

    /**
     * 删除文件
     *
     * @param file 文件
     */
    public void delete(String file) {
        try {
            client.deleteFile(serverCharset(config.getRemoteBaseDir() + file));
        } catch (Exception e) {
            throw Exceptions.ftp(e);
        }
    }

    /**
     * 删除目录及文件
     *
     * @param dir 目录
     */
    public void deleteDir(String dir) {
        try {
            String d = serverCharset(config.getRemoteBaseDir() + dir);
            List<FtpFile> list = listFiles(dir);
            for (FtpFile s : list) {
                client.deleteFile(serverCharset(Files1.getPath(config.getRemoteBaseDir() + s.getPath())));
            }
            list = listDirs(dir, true);
            for (FtpFile s : list) {
                deleteDir(s.getPath());
            }
            client.removeDirectory(d);
        } catch (Exception e) {
            throw Exceptions.ftp(e);
        }
    }

    /**
     * 创建文件夹
     *
     * @param dir 文件夹
     */
    public void mkdirs(String dir) {
        try {
            String[] dirs = Files1.getPath(dir).split(SYMBOL);
            String base = config.getRemoteBaseDir();
            for (String d : dirs) {
                if (null == d || "".equals(d)) {
                    continue;
                }
                base = serverCharset(base + SYMBOL + d);
                if (!client.changeWorkingDirectory(base)) {
                    client.makeDirectory(base);
                    client.changeWorkingDirectory(base);
                }
            }
        } catch (Exception e) {
            throw Exceptions.ftp(e);
        }
    }

    /**
     * 创建文件
     *
     * @param file 文件
     */
    public void touch(String file) {
        String filePath = serverCharset(config.getRemoteBaseDir() + file);
        String parentPath = Files1.getParentPath(Files1.getPath(file));
        mkdirs(parentPath);
        for (FtpFile s : listFiles(parentPath, false)) {
            if (Files1.getFileName(s.getPath()).endsWith(file.trim())) {
                return;
            }
        }
        change(parentPath);
        try {
            client.storeFile(serverCharset(filePath), new ByteArrayInputStream(new byte[]{}));
        } catch (IOException e) {
            throw Exceptions.ftp(e);
        }
    }

    /**
     * 文件移动
     *
     * @param file 原文件名称
     * @param name 移动后的名称 如果不加目录为重命名
     */
    public void mv(String file, String name) {
        try {
            mkdirs(Files1.getParentPath(name));
            change(Files1.getParentPath(Files1.getPath(file)));
            String target = serverCharset(config.getRemoteBaseDir() + name);
            String source = serverCharset(Files1.getFileName(file));
            client.rename(source, target);
        } catch (Exception e) {
            throw Exceptions.ftp(e);
        }
    }

    /**
     * 等待io
     *
     * @throws IOException IOException
     */
    public void pending() throws IOException {
        client.completePendingCommand();
    }

    /**
     * 重置io
     */
    public void reset() {
        client.setRestartOffset(0);
    }

    /**
     * 获取系统类型
     *
     * @return 系统类型
     */
    public String getSystemType() {
        try {
            return client.getSystemType();
        } catch (IOException e) {
            throw Exceptions.ftp(e);
        }
    }

    /**
     * 获取文件夹状态
     *
     * @return 状态
     */
    public String getStatus() {
        try {
            return client.getStatus();
        } catch (IOException e) {
            throw Exceptions.ftp(e);
        }
    }

    /**
     * 获取文件夹状态 会获取文件列表
     *
     * @param path 文件夹
     * @return 状态
     */
    public String getStatus(String path) {
        try {
            return client.getStatus(new String(Files1.getPath(config.getRemoteBaseDir() + path).getBytes(), config.getRemoteFileNameCharset()));
        } catch (IOException e) {
            throw Exceptions.ftp(e);
        }
    }

    /**
     * 获取链接
     *
     * @return client
     */
    public FTPClient client() {
        return client;
    }

    /**
     * 获取配置
     *
     * @return config
     */
    public FtpConfig config() {
        return config;
    }

    /**
     * 发送心跳
     *
     * @return true存活
     * @throws IOException IOException
     */
    public boolean sendNoop() throws IOException {
        return client.sendNoOp();
    }

    /**
     * ftp编码
     *
     * @param chars chars
     * @return 编码
     */
    public String serverCharset(String chars) {
        try {
            return new String(Files1.getPath(chars).getBytes(), config.getRemoteFileNameCharset());
        } catch (UnsupportedEncodingException e) {
            throw Exceptions.unEnding(e);
        }
    }

    /**
     * 本地编码
     *
     * @param chars chars
     * @return 编码
     */
    public String localCharset(String chars) {
        try {
            return new String(Files1.getPath(chars).getBytes(), config.getLocalFileNameCharset());
        } catch (UnsupportedEncodingException e) {
            throw Exceptions.unEnding(e);
        }
    }

}
