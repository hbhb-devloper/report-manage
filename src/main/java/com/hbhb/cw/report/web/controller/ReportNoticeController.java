package com.hbhb.cw.report.web.controller;

import com.hbhb.cw.report.service.ReportNoticeService;
import com.hbhb.cw.report.web.vo.NoticeReqVO;
import com.hbhb.cw.report.web.vo.NoticeResVO;
import com.hbhb.cw.report.web.vo.NoticeVO;
import com.hbhb.cw.reportmanage.api.reportApi;
import com.hbhb.web.annotation.UserId;

import org.beetl.sql.core.page.PageResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import javax.annotation.Resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

/**
 * @author yzc
 * @since 2021-01-18
 */
public class ReportNoticeController implements reportApi {

    @Resource
    private ReportNoticeService noticeService;

    @Operation(summary = "统计待办提醒数量")
    @Override
    public Long countNotice(@Parameter(description = "用户id") Integer userId) {
        return noticeService.countNotice(userId);
    }

    @Operation(summary = "获取登录用户的待办提醒")
    @GetMapping("/summary")
    public List<NoticeVO> getUserInvoiceNotice(@Parameter(hidden = true) @UserId Integer userId) {
        return noticeService.listReportNotice(userId);
    }

    @Operation(summary = "根据接收人id查询提醒列表")
    @GetMapping("/list")
    public PageResult<NoticeResVO> getBudgetProjectNoticeList(
            @Parameter(hidden = true) @UserId Integer userId,
            @Parameter(description = "筛选条件") NoticeReqVO noticeVO,
            @Parameter(description = "页码，默认为1") @RequestParam(required = false) Integer pageNum,
            @Parameter(description = "每页数量，默认为10") @RequestParam(required = false) Integer pageSize) {
        noticeVO.setUserId(userId);
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 10 : pageSize;
        return noticeService.pageReportNotice(noticeVO, pageNum, pageSize);
    }

    @Operation(summary = "更新提醒消息为已读")
    @PutMapping("/{id}")
    public void changeNoticeState(@Parameter(description = "id") @PathVariable Long id) {
        noticeService.changeNoticeState(id);
    }
}
