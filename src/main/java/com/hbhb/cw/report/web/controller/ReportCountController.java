package com.hbhb.cw.report.web.controller;

import com.hbhb.core.utils.ExcelUtil;
import com.hbhb.cw.report.service.ReportService;
import com.hbhb.cw.report.web.vo.ReportCountHallExportVO;
import com.hbhb.cw.report.web.vo.ReportCountResVO;
import com.hbhb.cw.report.web.vo.ReportCountUnitExportVO;
import com.hbhb.cw.report.web.vo.ReportReqVO;
import com.hbhb.web.annotation.UserId;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yzc
 * @since 2021-01-19
 */
@Tag(name = "报表管理-报表信息上传统计")
@RestController
@RequestMapping("/report/count")
@Slf4j
public class ReportCountController {

    @Resource
    private ReportService reportService;

    @GetMapping("/unit/list")
    @Operation(summary = "报表列表")
    public ReportCountResVO getReportList(
            @Parameter(description = "页码，默认为1") @RequestParam(required = false) Integer pageNum,
            @Parameter(description = "每页数量，默认为10") @RequestParam(required = false) Integer pageSize,
            ReportReqVO reportReqVO,
            @Parameter(hidden = true) @UserId Integer userId) {
        pageNum = pageNum == null ? 0 : pageNum - 1;
        pageSize = pageSize == null ? 20 : pageSize;
        return reportService.getReportCountList(reportReqVO, pageNum, pageSize, userId);
    }

    @Operation(summary = "导出")
    @PostMapping("/export")
    public void exportBusiness(HttpServletRequest request, HttpServletResponse response,
                               @Parameter(description = "条件") @RequestBody ReportReqVO reportReqVO,
                               @Parameter(hidden = true) @UserId Integer userId) {
        ;
        if (reportReqVO.getHallId() == null) {
            List<ReportCountUnitExportVO> list = reportService.getReportCountUnitExcel(reportReqVO, userId);
            String fileName = ExcelUtil.encodingFileName(request, "报表上传统计");
            ExcelUtil.export2Web(response, fileName, fileName, ReportCountUnitExportVO.class, list);
        } else {
            List<ReportCountHallExportVO> list = reportService.getReportCountHallExcel(reportReqVO, userId);
            String fileName = ExcelUtil.encodingFileName(request, "报表上传统计");
            ExcelUtil.export2Web(response, fileName, fileName, ReportCountHallExportVO.class, list);
        }
    }
}
