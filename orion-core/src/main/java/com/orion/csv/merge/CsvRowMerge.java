package com.orion.csv.merge;

import com.orion.csv.reader.CsvStream;
import com.orion.utils.Arrays1;
import com.orion.utils.Valid;
import com.orion.utils.io.Streams;

import java.io.OutputStream;
import java.util.List;

/**
 * CSV行合并器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/9/15 17:10
 */
public class CsvRowMerge {

    /**
     * 需要合并的csv
     */
    private List<CsvStream> mergeStreams;

    /**
     * 文件头
     */
    private String[] header;

    /**
     * 合并csv跳过的行
     */
    private int[] skip;

    /**
     * 输出流
     */
    private OutputStream out;

    public CsvRowMerge(List<CsvStream> mergeStreams) {
        Valid.notEmpty(mergeStreams, "merge streams is empty");
        this.mergeStreams = mergeStreams;
    }

    /**
     * 设置文件头
     *
     * @param header header
     * @return this
     */
    public CsvRowMerge header(String... header) {
        this.header = header;
        return this;
    }

    /**
     * 合并csv跳过行
     *
     * @param skip 文件跳过的行
     * @return this
     */
    public CsvRowMerge skip(int... skip) {
        this.skip = skip;
        return this;
    }
    //
    // /**
    //  * 合并后输出的目标文件
    //  *
    //  * @param file 文件
    //  * @return this
    //  */
    // public CsvRowMerge dist(CsvSymbol s, String file) {
    //     return dist(s, new File(file));
    // }
    //
    // /**
    //  * 合并后输出的目标文件
    //  *
    //  * @param file 文件
    //  * @return this
    //  */
    // public CsvRowMerge dist(CsvSymbol s, File file) {
    //     Valid.notNull(s, "csvSymbol is null");
    //     Files1.touch(file);
    //     this.out = Files1.openOutputStreamSafe(file);
    //     this.symbol = s;
    //     return this;
    // }
    //
    // /**
    //  * 合并后输出的目标流
    //  *
    //  * @param out 流
    //  * @return this
    //  */
    // public CsvRowMerge dist(CsvSymbol s, OutputStream out) {
    //     Valid.notNull(s, "csvSymbol is null");
    //     this.out = out;
    //     this.symbol = s;
    //     return this;
    // }
    //
    // /**
    //  * 执行合并
    //  *
    //  * @return this
    //  */
    // public CsvRowMerge execute() {
    //     Valid.notNull(out, "dist is null");
    //     CsvWriter csvWriter = new CsvWriter(out, symbol.getSymbol(), symbol.getCharset());
    //     try {
    //         if (!Arrays1.isEmpty(header)) {
    //             csvWriter.writeLine(header);
    //         }
    //         for (int i = 0; i < mergeStreams.size(); i++) {
    //             CsvStream csvStream = mergeStreams.get(i).skipLines(this.getSkip(i));
    //             for (String[] line : csvStream.readLines().lines()) {
    //                 csvWriter.writeLine(line);
    //             }
    //         }
    //         Streams.close(csvWriter);
    //     } catch (Exception e) {
    //         throw Exceptions.ioRuntime(e);
    //     }
    //     return this;
    // }

    /**
     * 获取csv需要跳过的行
     *
     * @param index index
     * @return skip line
     */
    private int getSkip(int index) {
        int length = Arrays1.length(skip);
        if (length == 0) {
            return 0;
        }
        if (length > index) {
            int i = skip[index];
            if (i > 0) {
                return i;
            }
        }
        return 0;
    }

    /**
     * 关闭流
     */
    public void close() {
        Streams.close(out);
    }

    public List<CsvStream> getMergeStreams() {
        return mergeStreams;
    }

}
