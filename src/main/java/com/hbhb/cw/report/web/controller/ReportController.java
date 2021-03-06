package com.hbhb.cw.report.web.controller;

import com.hbhb.core.utils.ExcelUtil;
import com.hbhb.cw.report.Util.EasyExcelUtil;
import com.hbhb.cw.report.service.ReportService;
import com.hbhb.cw.report.web.vo.ExcelInfoVO;
import com.hbhb.cw.report.web.vo.ExcelVO;
import com.hbhb.cw.report.web.vo.ReportFileVO;
import com.hbhb.cw.report.web.vo.ReportReqVO;
import com.hbhb.cw.report.web.vo.ReportResVO;
import com.hbhb.cw.report.web.vo.ReportVO;
import com.hbhb.cw.report.web.vo.UserImageVO;
import com.hbhb.web.annotation.UserId;

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
            ReportReqVO reportReqVO,
            @Parameter(hidden = true) @UserId Integer userId) {
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 20 : pageSize;
        return reportService.getReportList(reportReqVO, pageNum, pageSize, userId);
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
    public void moveReport(@RequestBody List<Long> list) {
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
        EasyExcelUtil.exportManySheetWithTemplate(response, fileName, UserImageVO.class, inStream, numberOfSheets, lists);
        try {
            inStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
