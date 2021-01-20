package com.hbhb.cw.report.web.controller;

import com.hbhb.cw.report.service.ReportService;
import com.hbhb.cw.report.web.vo.ReportReqVO;
import com.hbhb.cw.report.web.vo.ReportResVO;

import org.beetl.sql.core.page.PageResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
    public PageResult<ReportResVO> getReportList(
            @Parameter(description = "页码，默认为1") @RequestParam(required = false) Integer pageNum,
            @Parameter(description = "每页数量，默认为10") @RequestParam(required = false) Integer pageSize,
            ReportReqVO reportReqVO, Boolean flag) {
        return reportService.getReportCountList(flag, reportReqVO, pageNum, pageSize);
    }
}
