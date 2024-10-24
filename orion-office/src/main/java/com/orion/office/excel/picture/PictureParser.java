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
package com.orion.office.excel.picture;

import com.orion.lang.able.Analysable;
import com.orion.lang.define.collect.MultiHashMap;
import com.orion.lang.utils.collect.Lists;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.*;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker;

import java.util.List;

/**
 * 图片解析器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/1/3 13:45
 */
public class PictureParser implements Analysable {

    /**
     * 文件坐标
     * k1: row
     * k2: column
     * value: PictureData
     */
    private final MultiHashMap<Integer, Integer, PictureData> picturePosition;

    private final Workbook workbook;

    private final Sheet sheet;

    public PictureParser(Workbook workbook, Sheet sheet) {
        this.workbook = workbook;
        this.sheet = sheet;
        this.picturePosition = new MultiHashMap<>();
    }

    @Override
    public void analysis() {
        List<? extends PictureData> pictures = workbook.getAllPictures();
        if (Lists.isEmpty(pictures)) {
            return;
        }
        if (workbook instanceof HSSFWorkbook) {
            HSSFSheet s = (HSSFSheet) this.sheet;
            for (HSSFShape shape : s.getDrawingPatriarch().getChildren()) {
                HSSFClientAnchor anchor = (HSSFClientAnchor) shape.getAnchor();
                if (shape instanceof HSSFPicture) {
                    PictureData pictureData = pictures.get(((HSSFPicture) shape).getPictureIndex() - 1);
                    picturePosition.put(anchor.getRow1(), (int) anchor.getCol1(), pictureData);
                }
            }
        } else if (workbook instanceof XSSFWorkbook) {
            XSSFSheet s = (XSSFSheet) this.sheet;
            for (POIXMLDocumentPart part : s.getRelations()) {
                if (part instanceof XSSFDrawing) {
                    XSSFDrawing drawing = (XSSFDrawing) part;
                    List<XSSFShape> shapes = drawing.getShapes();
                    for (XSSFShape shape : shapes) {
                        if (shape instanceof XSSFPicture) {
                            XSSFClientAnchor anchor = ((XSSFPicture) shape).getPreferredSize();
                            CTMarker ctMarker = anchor.getFrom();
                            picturePosition.put(ctMarker.getRow(), ctMarker.getCol(), ((XSSFPicture) shape).getPictureData());
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取图片data
     *
     * @param row row
     * @param col col
     * @return PictureData
     */
    public PictureData getPicture(int row, int col) {
        return picturePosition.get(row, col);
    }

}

