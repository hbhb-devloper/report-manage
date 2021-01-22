package com.hbhb.cw.report.web.controller;

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
import com.hbhb.core.utils.ExcelUtil;
import com.hbhb.cw.report.service.ReportService;
import com.hbhb.cw.report.web.vo.ExcelInfoVO;
import com.hbhb.cw.report.web.vo.ExcelVO;
import com.hbhb.cw.report.web.vo.ReportFileVO;
import com.hbhb.cw.report.web.vo.ReportReqVO;
import com.hbhb.cw.report.web.vo.ReportResVO;
import com.hbhb.cw.report.web.vo.ReportVO;
import com.hbhb.cw.report.web.vo.UserImageVO;
import com.hbhb.web.annotation.UserId;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.beetl.sql.core.page.PageResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wangxiaogang
 */
@Tag(name = "报表管理-报表信息")
@RestController
@RequestMapping("/report")
@Slf4j
public class ReportController {

    @Resource
    private ReportService reportService;

    @GetMapping("/list")
    @Operation(summary = "报表列表")
    public PageResult<ReportResVO> getReportList(
            @Parameter(description = "页码，默认为1") @RequestParam(required = false) Integer pageNum,
            @Parameter(description = "每页数量，默认为10") @RequestParam(required = false) Integer pageSize,
            ReportReqVO reportReqVO) {
        return reportService.getReportList(reportReqVO, pageNum, pageSize);
    }

    @Operation(summary = "添加报表信息")
    @PostMapping("")
    public void addReport(@Parameter(description = "实体") @RequestBody ReportVO reportVO,
                          @Parameter(hidden = true) @UserId Integer userId) {
        reportService.addReport(reportVO, userId);
    }

    @GetMapping("/info")
    @Operation(summary = "报表列表")
    public List<ReportFileVO> getReportInfo(
            Long reportId) {
        return reportService.getReportInfo(reportId);
    }

    @Operation(summary = "添加报表信息")
    @DeleteMapping("/move")
    public void moveReport(List<Long> list) {
        reportService.moveReportList(list);
    }

    @Operation(summary = "导出")
    @PostMapping("/export")
    public void exportBusiness(HttpServletRequest request, HttpServletResponse response, @RequestBody ExcelVO excelVO) {
        ExcelInfoVO excelInfo = reportService.getExcelInfo(excelVO.getFileId(), excelVO.getReportId());
        // 获取sheet数量
        int numberOfSheets = 0;
        try {
            XSSFWorkbook hs = new XSSFWorkbook(excelInfo.getInputStream());
            numberOfSheets = hs.getNumberOfSheets();
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<UserImageVO> list = new ArrayList<>();
        list.add(excelInfo.getImageVO());
        List<List> lists = new ArrayList<>();
        for (int i = 0; i < numberOfSheets; i++) {
            lists.add(list);
        }
        InputStream inStream = null;
        try {
            URL url = new URL(excelInfo.getPath());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            inStream = conn.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String fileName = ExcelUtil.encodingFileName(request, excelInfo.getFileName());
        exportManySheetWithTemplate(response, fileName, UserImageVO.class, inStream, numberOfSheets, lists);
        try {
            inStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 多sheet导出
     */
    public void exportManySheetWithTemplate(HttpServletResponse response,
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
    public class ImageModifyHandler implements CellWriteHandler {

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
