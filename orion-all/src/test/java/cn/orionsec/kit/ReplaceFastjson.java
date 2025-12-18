package cn.orionsec.kit;

import cn.orionsec.kit.lang.define.StopWatch;
import cn.orionsec.kit.lang.utils.io.FileReaders;
import cn.orionsec.kit.lang.utils.io.FileWriters;
import cn.orionsec.kit.lang.utils.io.Files1;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 替换 fastjson 版本
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/12/23 10:21
 */
public class ReplaceFastjson {

    private static final String PATH = new File("").getAbsolutePath();

    public static void main(String[] args) {
        StopWatch sw = StopWatch.begin();
        // 扫描文件
        List<File> files = Files1.listFilesFilter(PATH, file -> file.isFile()
                && (file.getName().endsWith(".java"))
                && !file.getAbsolutePath().contains("generated-sources"), true, false);
        sw.tag("   list");
        // 添加头
        files.forEach(ReplaceFastjson::replaceFastjson2ToFile);
        sw.tag("replace");
        sw.stop();
        System.out.println();
        System.out.println(sw);
    }

    /**
     * 替换 license
     *
     * @param file file
     */
    private static void replaceFastjson2ToFile(File file) {
        String path = file.getAbsolutePath().substring(PATH.length());
        if (path.contains("ReplaceFastjson")) {
            return;
        }
        try {
            String line = FileReaders.readLine(file);
            if (line == null) {
                return;
            }
            // 替换文件内容
            byte[] bytes = new String(FileReaders.readAllBytesFast(file))
                    .replaceAll("com.alibaba.fastjson.JSON", "com.alibaba.fastjson2.JSON")
                    .getBytes(StandardCharsets.UTF_8);
            // 写入
            FileWriters.writeFast(file, bytes);
            System.out.println("OK     " + path);
        } catch (Exception e) {
            System.err.println("Failed " + path);
        }
    }

}
