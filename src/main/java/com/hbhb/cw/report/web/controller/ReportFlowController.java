package com.hbhb.cw.report.web.controller;

import com.hbhb.cw.flowcenter.vo.FlowApproveVO;
import com.hbhb.cw.flowcenter.vo.FlowWrapperVO;
import com.hbhb.cw.report.service.ReportFlowService;
import com.hbhb.web.annotation.UserId;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yzc
 * @since 2021-01-12
 */
@Tag(name = "报表管理-报表信息流程")
@RestController
@RequestMapping("/report/flow")
@Slf4j
public class ReportFlowController {

    @Resource
    private ReportFlowService reportFlowService;

    @Operation(summary = "获取宣传物料设计流程详情")
    @GetMapping("/list/{reportId}")
    public FlowWrapperVO getReportFlow(@PathVariable Long reportId,
                                       @Parameter(hidden = true) @UserId Integer userId) {
        return reportFlowService.getReportNodeList(reportId, userId);
    }

    @Operation(summary = "审批流程")
    @PostMapping("/approve")
    public void approve(@Parameter(description = "审批") @RequestBody FlowApproveVO flowApproveVO,
                        @Parameter(hidden = true) @UserId Integer userId) {
        reportFlowService.approve(flowApproveVO, userId);
    }
}
