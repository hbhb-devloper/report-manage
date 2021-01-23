package com.hbhb.cw.report.Util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * @author yzc
 * @since 2021-01-23
 */
@Slf4j
@SuppressWarnings(value = {"rawtypes"})
public class EasyExcelUtil {

    /**
     * 多sheet导出
     */
    public static void exportManySheetWithTemplate(HttpServletResponse response,
                                            String fileName,
                                            Class clazz,
                                            InputStream path,
                                            Integer sheetNum,
                                            List<List> dataList) {
        fileName += ExcelTypeEnum.XLSX.getValue();
        ExcelWriter excelWriter = null;
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
            excelWriter = EasyExcel.write(response.getOutputStream(), clazz)
                    .registerWriteHandler(new ImageModifyHandler())
                    .withTemplate(path)
                    .build();
            for (int i = 0; i < sheetNum; i++) {
                WriteSheet writeSheet = EasyExcel.writerSheet(i).build();
                excelWriter.write(dataList.get(i), writeSheet);
            }
        } catch (Exception e) {
            log.error("导出Excel异常{}", e.getMessage());
            throw new RuntimeException("导出Excel失败，请联系网站管理员！");
        } finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    /**
     * 图片修改拦截器
     *
     * @author JiaJu Zhuang
     * @date 2020/7/23 10:20 上午
     **/
    public static class ImageModifyHandler implements CellWriteHandler {

        @Override
        public void beforeCellCreate(WriteSheetHolder writeSheetHolder,
                                     WriteTableHolder writeTableHolder, Row row,
                                     Head head, Integer columnIndex, Integer relativeRowIndex, Boolean isHead) {

        }

        @Override
        public void afterCellCreate(WriteSheetHolder writeSheetHolder,
                                    WriteTableHolder writeTableHolder, Cell cell, Head head,
                                    Integer relativeRowIndex, Boolean isHead) {

        }

        @Override
        public void afterCellDataConverted(WriteSheetHolder writeSheetHolder,
                                           WriteTableHolder writeTableHolder, CellData cellData,
                                           Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
            //  在 数据转换成功后 ，修改第一列 当然这里也可以根据其他判断  然后不是头  就把类型设置成空 这样easyexcel 不会去处理该单元格 || head.getColumnIndex() != 3 || head.getColumnIndex() != 7 || head.getColumnIndex() != 10
            if (head.getColumnIndex() != 1
                    && head.getColumnIndex() != 4
                    && head.getColumnIndex() != 7
                    && head.getColumnIndex() != 10
                    && head.getColumnIndex() != 13
                    || isHead) {
                return;
            }
            cellData.setType(CellDataTypeEnum.EMPTY);
        }

        @Override
        public void afterCellDispose(WriteSheetHolder writeSheetHolder,
                                     WriteTableHolder writeTableHolder, List<CellData> cellDataList, Cell cell,
                                     Head head, Integer relativeRowIndex, Boolean isHead) {
            //  在 单元格写入完毕后 ，自己填充图片
            if (head.getColumnIndex() != 1
                    && head.getColumnIndex() != 4
                    && head.getColumnIndex() != 7
                    && head.getColumnIndex() != 10
                    && head.getColumnIndex() != 13
                    || isHead || cellDataList.isEmpty()) {
                return;
            }
            Sheet sheet = cell.getSheet();
            // cellDataList 是list的原因是 填充的情况下 可能会多个写到一个单元格 但是如果普通写入 一定只有一个
            Workbook workbook = sheet.getWorkbook();
            byte[] imageValue = cellDataList.get(0).getImageValue();
            if (imageValue == null) {
                return;
            }
            int index = workbook.addPicture(imageValue, HSSFWorkbook.PICTURE_TYPE_PNG);
            Drawing drawing = sheet.getDrawingPatriarch();
            if (drawing == null) {
                drawing = sheet.createDrawingPatriarch();
            }
            CreationHelper helper = sheet.getWorkbook().getCreationHelper();
            ClientAnchor anchor = helper.createClientAnchor();
            // 设置图片坐标
            anchor.setDx1(0);
            anchor.setDx2(0);
            anchor.setDy1(0);
            anchor.setDy2(0);
            //设置图片位置
            anchor.setCol1(cell.getColumnIndex());
            anchor.setCol2(cell.getColumnIndex() + 2);
            anchor.setRow1(cell.getRowIndex());
            anchor.setRow2(cell.getRowIndex() + 1);
            // 设置图片可以随着单元格移动
            anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);
            drawing.createPicture(anchor, index);
        }
    }

}
