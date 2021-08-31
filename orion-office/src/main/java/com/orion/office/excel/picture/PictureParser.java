package com.orion.office.excel.picture;

import com.orion.able.Analysable;
import com.orion.lang.collect.MultiHashMap;
import com.orion.utils.collect.Lists;
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
    private MultiHashMap<Integer, Integer, PictureData> picturePosition;

    private Workbook workbook;

    private Sheet sheet;

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

