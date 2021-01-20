package com.hbhb.cw.report.service;

import com.hbhb.cw.report.web.vo.ExcelInfoVO;
import com.hbhb.cw.report.web.vo.ReportReqVO;
import com.hbhb.cw.report.web.vo.ReportResVO;
import com.hbhb.cw.report.web.vo.ReportVO;

import org.beetl.sql.core.page.PageResult;

/**
 * @author wangxiaogang
 */
public interface ReportService {

    PageResult<ReportResVO> getReportList(ReportReqVO reportReqVO, Integer pageNum, Integer pageSize);

    PageResult<ReportResVO> getReportCountList(Boolean flag, ReportReqVO reportReqVO, Integer pageNum, Integer pageSize);

    void addReport(ReportVO reportVO, Integer userId);

//    void toApprover(ReportInitVO reportApproveVO, Integer userId);

    ExcelInfoVO getExcelInfo(Integer fileId, Long reportId);
}
