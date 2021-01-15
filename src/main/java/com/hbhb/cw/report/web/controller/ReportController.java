package com.hbhb.cw.report.web.controller;

import com.hbhb.core.utils.ExcelUtil;
import com.hbhb.cw.report.service.ReportService;
import com.hbhb.cw.report.web.vo.ExcelInfoVO;
import com.hbhb.cw.report.web.vo.ReportInitVO;
import com.hbhb.cw.report.web.vo.ReportReqVO;
import com.hbhb.cw.report.web.vo.ReportResVO;
import com.hbhb.cw.report.web.vo.ReportVO;
import com.hbhb.cw.report.web.vo.UserImageVO;
import com.hbhb.web.annotation.UserId;

import org.beetl.sql.core.page.PageResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @Operation(summary = "发起审批")
    @PostMapping("/to-approve")
    public void toApprover(@RequestBody ReportInitVO reportApproveVO,
                           @Parameter(hidden = true) @UserId Integer userId) {
        reportService.toApprover(reportApproveVO, userId);
    }

    @Operation(summary = "导出")
    @PostMapping("/export")
    public void exportBusiness(HttpServletRequest request, HttpServletResponse response, Integer fileId, Long reportId) {
        ExcelInfoVO excelInfo = reportService.getExcelInfo(fileId, reportId);
        List<UserImageVO> list = new ArrayList<>();
        list.add(excelInfo.getImageVO());
        String fileName = ExcelUtil.encodingFileName(request, excelInfo.getFileName());
        ExcelUtil.export2TemplateWithDate(response, UserImageVO.class, fileName, "Sheet1",
                excelInfo.getPath(), list);
    }
}
