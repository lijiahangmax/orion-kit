package com.orion.remote.connection.scp;

import ch.ethz.ssh2.SCPClient;
import com.orion.constant.Const;
import com.orion.utils.io.Files1;
import com.orion.utils.io.Streams;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SCP 执行器
 * 上传文件时, 远程文件夹必须存在, 否则会异常
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/4/28 18:25
 */
public class ScpExecutor {

    /**
     * 分隔符
     */
    private static final String SEPARATOR = Const.SLASH;

    private SCPClient client;

    public ScpExecutor(SCPClient client) {
        this.client = client;
    }

    // -------------------- download --------------------

    /**
     * 下载文件到本地文件夹
     *
     * @param remoteFile 远程文件
     * @param localDir   本地文件夹
     * @return 是否下载成功
     */
    public boolean downloadFile(String remoteFile, String localDir) {
        return this.downloadFile(remoteFile, new File(Files1.getPath(localDir + SEPARATOR + Files1.getFileName(remoteFile))));
    }

    /**
     * 下载文件到本地文件
     *
     * @param remoteFile 远程文件
     * @param localFile  本地文件
     * @return 是否下载成功
     */
    public boolean downloadFile(String remoteFile, File localFile) {
        try {
            Files1.touch(localFile);
            this.downloadFile(remoteFile, Files1.openOutputStream(localFile), null, true);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 下载文件
     *
     * @param remoteFile 远程文件
     * @param out        out
     * @throws IOException IOException
     */
    public void downloadFile(String remoteFile, OutputStream out) throws IOException {
        downloadFile(remoteFile, out, null, false);
    }

    /**
     * 下载文件
     *
     * @param remoteFile 远程文件
     * @param writer     writer
     * @throws IOException IOException
     */
    public void downloadFile(String remoteFile, Writer writer) throws IOException {
        downloadFile(remoteFile, null, writer, false);
    }

    /**
     * 下载文件
     *
     * @param remoteFile 远程文件
     * @param out        out
     * @param writer     writer
     * @param close      是否关闭 out/writer
     * @throws IOException IOException
     */
    public void downloadFile(String remoteFile, OutputStream out, Writer writer, boolean close) throws IOException {
        InputStream in = this.getFileInputStream(remoteFile);
        try {
            if (out != null) {
                Streams.transfer(in, out);
            } else {
                Streams.transfer(in, writer);
            }
        } finally {
            Streams.close(in);
            if (close) {
                Streams.close(out);
                Streams.close(writer);
            }
        }
    }

    /**
     * 批量下载文件
     *
     * @param localDir    本地文件夹
     * @param remoteFiles 远程文件
     * @return remoteFile, downResult
     */
    public Map<String, Boolean> downloadFiles(File localDir, List<String> remoteFiles) {
        return this.downloadFiles(localDir.getAbsolutePath(), remoteFiles);
    }

    /**
     * 批量下载文件
     *
     * @param localDir    本地文件夹
     * @param remoteFiles 远程文件
     * @return remoteFile, downResult
     */
    public Map<String, Boolean> downloadFiles(String localDir, List<String> remoteFiles) {
        Map<String, Boolean> result = new HashMap<>();
        for (String remoteFile : remoteFiles) {
            File localFile = new File(Files1.getPath(localDir + SEPARATOR + Files1.getFileName(remoteFile)));
            result.put(remoteFile, this.downloadFile(remoteFile, localFile));
        }
        return result;
    }

    /**
     * 获取远程文件的输入流
     *
     * @param remoteFile 远程文件
     * @return InputStream
     * @throws IOException IOException
     */
    public InputStream getFileInputStream(String remoteFile) throws IOException {
        return this.client.get(remoteFile);
    }

    // -------------------- upload --------------------

    /**
     * 上传文件
     *
     * @param localFile 本地文件
     * @param remoteDir 远程文件目录
     * @return 是否上传成功
     */
    public boolean uploadFile(File localFile, String remoteDir) {
        return this.uploadFile(localFile, remoteDir, null);
    }

    /**
     * 上传文件
     *
     * @param localFile 本地文件
     * @param remoteDir 远程文件目录
     * @return 是否上传成功
     */
    public boolean uploadFile(String localFile, String remoteDir) {
        return this.uploadFile(new File(localFile), remoteDir, null);
    }

    /**
     * 上传文件
     *
     * @param localFile      本地文件
     * @param remoteDir      远程文件目录
     * @param remoteFileName 远程文件名称
     * @return 是否上传成功
     */
    public boolean uploadFile(String localFile, String remoteDir, String remoteFileName) {
        return this.uploadFile(new File(localFile), remoteDir, remoteFileName);
    }

    /**
     * 上传文件
     *
     * @param localFile      本地文件
     * @param remoteDir      远程文件目录
     * @param remoteFileName 远程文件名称
     * @return 是否上传成功
     */
    public boolean uploadFile(File localFile, String remoteDir, String remoteFileName) {
        if (remoteFileName == null) {
            remoteFileName = localFile.getName();
        }
        try {
            this.uploadFile(Files1.openInputStream(localFile), localFile.length(), remoteDir, remoteFileName, true);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 上传文件
     *
     * @param in             流
     * @param len            远程文件长度
     * @param remoteDir      远程文件目录
     * @param remoteFileName 远程文件名称
     * @throws IOException IOException
     */
    public void uploadFile(InputStream in, long len, String remoteDir, String remoteFileName) throws IOException {
        this.uploadFile(in, len, remoteDir, remoteFileName, false);
    }

    /**
     * 上传文件
     *
     * @param in             流
     * @param remoteDir      远程文件目录
     * @param remoteFileName 远程文件名称
     * @throws IOException IOException
     */
    public void uploadFile(InputStream in, String remoteDir, String remoteFileName) throws IOException {
        this.uploadFile(in, in.available(), remoteDir, remoteFileName, false);
    }

    /**
     * 上传文件
     *
     * @param in             流
     * @param len            远程文件长度
     * @param remoteDir      远程文件目录
     * @param remoteFileName 远程文件名称
     * @param close          是否关闭in流
     * @throws IOException IOException
     */
    public void uploadFile(InputStream in, long len, String remoteDir, String remoteFileName, boolean close) throws IOException {
        OutputStream out = null;
        try {
            out = this.client.put(remoteFileName, len, remoteDir, "0600");
            Streams.transfer(in, out);
            out.flush();
        } finally {
            Streams.close(out);
            if (close) {
                in.close();
            }
        }
    }

    /**
     * 批量上传文件
     *
     * @param remoteDir  远程文件夹
     * @param localFiles 本地文件
     * @return localFile, uploadResult
     */
    public Map<String, Boolean> uploadFilesPath(String remoteDir, List<String> localFiles) {
        return this.uploadFiles(remoteDir, localFiles, null);
    }

    /**
     * 批量上传文件
     *
     * @param remoteDir  远程文件夹
     * @param localFiles 本地文件
     * @return localFile, uploadResult
     */
    public Map<String, Boolean> uploadFiles(String remoteDir, List<File> localFiles) {
        return this.uploadFiles(remoteDir, null, localFiles);
    }

    /**
     * 批量上传文件
     *
     * @param remoteDir   远程文件夹
     * @param localFiles  本地文件
     * @param localFiles1 本地文件
     * @return localFile, uploadResult
     */
    private Map<String, Boolean> uploadFiles(String remoteDir, List<String> localFiles, List<File> localFiles1) {
        Map<String, Boolean> result = new HashMap<>();
        if (localFiles != null) {
            for (String localFile : localFiles) {
                result.put(localFile, this.uploadFile(new File(localFile), remoteDir));
            }
        } else {
            for (File file : localFiles1) {
                result.put(file.getAbsolutePath(), this.uploadFile(file, remoteDir));
                this.uploadFile(file, remoteDir);
            }
        }
        return result;
    }

    /**
     * 上传文件夹
     *
     * @param remoteDir 远程文件夹
     * @param localDir  本地文件夹
     * @return localFile, uploadResult
     */
    public Map<String, Boolean> uploadFiles(String remoteDir, String localDir) {
        return this.uploadFiles(remoteDir, new File(localDir));
    }

    /**
     * 上传文件夹
     *
     * @param remoteDir 远程文件夹
     * @param localDir  本地文件夹
     * @return localFile, uploadResult
     */
    public Map<String, Boolean> uploadFiles(String remoteDir, File localDir) {
        Map<String, Boolean> result = new HashMap<>();
        List<File> files = Files1.listFiles(localDir, true);
        String path = localDir.getAbsolutePath();
        for (File file : files) {
            String filePath = file.getAbsolutePath();
            boolean upload = this.uploadFile(file, Files1.getPath(remoteDir + SEPARATOR + filePath.substring(path.length(), filePath.length() - file.getName().length())), file.getName());
            result.put(filePath, upload);
        }
        return result;
    }

    /**
     * 获取编码格式
     *
     * @return 编码格式
     */
    public String getCharset() {
        return client.getCharset();
    }

    /**
     * 设置编码格式
     *
     * @param charset 编码格式
     * @throws IOException IOException
     */
    public ScpExecutor setCharset(String charset) throws IOException {
        client.setCharset(charset);
        return this;
    }

    /**
     * SCP连接
     *
     * @return 连接
     */
    public SCPClient getClient() {
        return client;
    }

}
