package com.orion.spring;

import com.orion.constant.Const;
import com.orion.constant.StandardContentType;
import com.orion.utils.Strings;
import com.orion.utils.io.Streams;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;

/**
 * MultipartFile 工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/3/8 14:01
 */
public class MultipartFiles {

    private MultipartFiles() {
    }

    /**
     * 将File文件转化为 MultiPartFile文件
     *
     * @param file 文件
     * @return MultipartFile
     */
    public static MultipartFile toMultiPartFile(File file) {
        return toMultiPartFile(file, Const.UTF_8);
    }

    /**
     * 将File文件转化为 MultiPartFile文件
     *
     * @param file    文件
     * @param charset 编码格式
     * @return MultipartFile
     */
    public static MultipartFile toMultiPartFile(File file, String charset) {
        if (file == null) {
            return null;
        }
        try {
            String fileName = file.getName();
            int suffixIndex = file.getName().indexOf(".");
            String field;
            if (suffixIndex == -1) {
                field = fileName;
            } else {
                field = fileName.substring(0, suffixIndex);
            }
            return toMultiPartFile(new FileInputStream(file), charset, field, fileName);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将src路径转化为 MultiPartFile文件
     *
     * @param src 文件路径
     * @return MultipartFile
     */
    public static MultipartFile toMultiPartFile(String src) {
        return toMultiPartFile(src, Const.UTF_8);
    }

    /**
     * 将src路径转化为 MultiPartFile文件
     *
     * @param src     文件路径
     * @param charset 编码格式
     * @return MultipartFile
     */
    public static MultipartFile toMultiPartFile(String src, String charset) {
        if (Strings.isBlank(src)) {
            return null;
        }
        try {
            File file = new File(src);
            String fileName = file.getName();
            int suffixIndex = file.getName().indexOf(".");
            String field;
            if (suffixIndex == -1) {
                field = fileName;
            } else {
                field = fileName.substring(0, suffixIndex);
            }
            return toMultiPartFile(MultipartFiles.class.getClassLoader().getResourceAsStream(src), charset, field, fileName);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将inputStream MultiPartFile文件
     *
     * @param in       InputStream
     * @param field    文件字段
     * @param fileName 文件名称
     * @return MultipartFile
     */
    public static MultipartFile toMultiPartFile(InputStream in, String field, String fileName) {
        return toMultiPartFile(in, Const.UTF_8, field, fileName);
    }

    /**
     * 将inputStream MultiPartFile文件
     *
     * @param in       InputStream
     * @param charset  编码格式
     * @param field    文件字段
     * @param fileName 文件名称
     * @return MultipartFile
     */
    public static MultipartFile toMultiPartFile(InputStream in, String charset, String field, String fileName) {
        OutputStream os = null;
        try {
            DiskFileItemFactory factory = new DiskFileItemFactory(16, null);
            factory.setDefaultCharset(charset);
            FileItem item = factory.createItem(field, StandardContentType.TEXT_PLAIN, true, fileName);
            int bytesRead;
            byte[] buffer = new byte[Const.BUFFER_KB_8];
            try {
                os = item.getOutputStream();
                while ((bytesRead = in.read(buffer, 0, Const.BUFFER_KB_8)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                os.close();
                in.close();
            } catch (IOException e) {
                return null;
            }
            return new CommonsMultipartFile(item);
        } catch (Exception e) {
            return null;
        } finally {
            Streams.close(in);
            Streams.close(os);
        }
    }

}
