package com.orion.utils.io;

import com.orion.constant.StandardContentType;
import com.orion.utils.Exceptions;
import com.orion.utils.Strings;
import com.orion.utils.math.Hex;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 文件类型推断工具 不精确
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/27 14:40
 */
public class FileTypes {

    private FileTypes() {
    }

    /**
     * 文件头
     */
    private static final Map<String, String> FILE_HEAD_MAP = new LinkedHashMap<>();

    static {
        FILE_HEAD_MAP.put("doc", "D0CF11E0A1B11AE10000000000000000");
        FILE_HEAD_MAP.put("xls", "D0CF11E0A1B11AE10000000000000000");
        FILE_HEAD_MAP.put("exe", "4D5A90000300000004000000FFFF0000");
        FILE_HEAD_MAP.put("dll", "4D5A90000300000004000000FFFF0000");
        FILE_HEAD_MAP.put("png", "89504E470D0A1A0A0000000D49484452");
        FILE_HEAD_MAP.put("lnk", "4C0000000114020000000000C0000000");
        FILE_HEAD_MAP.put("xsd", "3C3F786D6C2076657273696F6E3D2231");
        FILE_HEAD_MAP.put("rmvb", "2E524D46000000120001000000000000");
        FILE_HEAD_MAP.put("avi", "5249464616BD5301415649204C495354");
        FILE_HEAD_MAP.put("mkv", "1A45DFA3A34286810142F7810142F281");
        FILE_HEAD_MAP.put("gif", "474946383961E0010E01E70000000000");
        FILE_HEAD_MAP.put("wmv", "3026B2758E66CF11A6D900AA0062CE6C");
        FILE_HEAD_MAP.put("eml", "52656365697665643A2066726F6D2073");
        FILE_HEAD_MAP.put("psd", "38425053000100000000000000030000");
        FILE_HEAD_MAP.put("xmind", "504B0304140008080800");
        FILE_HEAD_MAP.put("mdb", "5374616E64617264204A");
        FILE_HEAD_MAP.put("eps", "252150532D41646F6265");
        FILE_HEAD_MAP.put("ps", "252150532D41646F6265");
        FILE_HEAD_MAP.put("asf", "3026B2758E66CF11");
        FILE_HEAD_MAP.put("dbx", "CFAD12FEC5FD746F");
        FILE_HEAD_MAP.put("7z", "377ABCAF271C0004");
        FILE_HEAD_MAP.put("pdf", "255044462D312E3");
        FILE_HEAD_MAP.put("zip", "504B03040A00000");
        FILE_HEAD_MAP.put("rar", "526172211A070");
        FILE_HEAD_MAP.put("rtf", "7B5C727466");
        FILE_HEAD_MAP.put("tif", "49492A00");
        FILE_HEAD_MAP.put("dwg", "41433130");
        FILE_HEAD_MAP.put("pst", "2142444E");
        FILE_HEAD_MAP.put("docx", "504B0304");
        FILE_HEAD_MAP.put("xlsx", "504B0304");
        FILE_HEAD_MAP.put("jar", "504B0304");
        FILE_HEAD_MAP.put("war", "504B0304");
        FILE_HEAD_MAP.put("wpd", "FF575043");
        FILE_HEAD_MAP.put("class", "CAFEBABE");
        FILE_HEAD_MAP.put("ram", "2E7261FD");
        FILE_HEAD_MAP.put("qdf", "AC9EBD8F");
        FILE_HEAD_MAP.put("pwl", "E3828596");
        FILE_HEAD_MAP.put("rm", "2E524D46");
        FILE_HEAD_MAP.put("html", "3C21444F");
        FILE_HEAD_MAP.put("mpg", "000001BA");
        FILE_HEAD_MAP.put("mov", "6D6F6F76");
        FILE_HEAD_MAP.put("mid", "4D546864");
        FILE_HEAD_MAP.put("jpg", "FFD8FFE");
        FILE_HEAD_MAP.put("mp4", "000000");
        FILE_HEAD_MAP.put("bmp", "424D");
        FILE_HEAD_MAP.put("xml", "3C");
    }

    /**
     * 设置文件头
     *
     * @param suffix 文件后缀
     * @param bs     头
     */
    public static void put(String suffix, byte[] bs) {
        FILE_HEAD_MAP.put(suffix, Hex.bytesToHex(bs));
    }

    /**
     * 设置文件头
     *
     * @param suffix 文件后缀
     * @param hex    头
     */
    public static void put(String suffix, String hex) {
        FILE_HEAD_MAP.put(suffix, hex);
    }

    /**
     * 通过文件头推算文件类型
     *
     * @param file 文件
     * @return 类型
     */
    public static String getFileType(String file) {
        return getFileType(new File(file));
    }

    /**
     * 通过文件头推算文件类型
     *
     * @param file 文件
     * @return 类型
     */
    public static String getFileType(File file) {
        String type = null;
        byte[] b = new byte[20];
        try (FileInputStream in = Files1.openInputStream(file)) {
            if (in.read(b) != -1) {
                type = getFileType(b);
            }
        } catch (IOException e) {
            // ignore
        }
        return type;
    }

    /**
     * 通过文件头推算文件类型
     *
     * @param in 文件
     * @return 类型
     */
    public static String getFileType(InputStream in) {
        String type = null;
        byte[] b = new byte[20];
        try {
            if (in.read(b) != -1) {
                type = getFileType(b);
            }
        } catch (IOException e) {
            // ignore
        }
        return type;
    }

    /**
     * 通过文件头推算文件类型 不准确
     *
     * @param bs 文件头
     * @return 类型
     */
    public static String getFileType(byte[] bs) {
        for (Map.Entry<String, String> entry : FILE_HEAD_MAP.entrySet()) {
            String hexValue = entry.getValue();
            if (Hex.bytesToHex(bs).toUpperCase().startsWith(hexValue)) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * 获取文件内容类型 默认 text/plain
     *
     * @param file 文件
     * @return 文件类型
     */
    public static String getContentType(String file) {
        return getContentType(Paths.get(file));
    }

    /**
     * 获取文件内容类型 默认 text/plain
     *
     * @param file 文件
     * @return 文件类型
     */
    public static String getContentType(File file) {
        return getContentType(Paths.get(file.getAbsolutePath()));
    }

    /**
     * 获取文件内容类型 默认 text/plain
     *
     * @param file 文件
     * @return 文件类型
     */
    public static String getContentType(Path file) {
        try {
            return Strings.def(Files.probeContentType(file), StandardContentType.TEXT_PLAIN);
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

}
