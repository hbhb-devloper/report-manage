package com.hbhb.cw.report.service;

import com.hbhb.cw.report.web.vo.ExcelInfoVO;
import com.hbhb.cw.report.web.vo.ReportCountHallExportVO;
import com.hbhb.cw.report.web.vo.ReportCountResVO;
import com.hbhb.cw.report.web.vo.ReportCountUnitExportVO;
import com.hbhb.cw.report.web.vo.ReportFileVO;
import com.hbhb.cw.report.web.vo.ReportReqVO;
import com.hbhb.cw.report.web.vo.ReportResVO;
import com.hbhb.cw.report.web.vo.ReportVO;

import org.beetl.sql.core.page.PageResult;

import java.util.List;

/**
 * @author wangxiaogang
 */
public interface ReportService {

    /**
     * 报表信息列表
     */
    PageResult<ReportResVO> getReportList(ReportReqVO reportReqVO, Integer pageNum, Integer pageSize);

    /**
     * 附件详情
     */
    List<ReportFileVO> getReportInfo(Long reportId);

    /**
     * 报表管理上传统计
     */
    ReportCountResVO getReportCountList(ReportReqVO reportReqVO, Integer pageNum, Integer pageSize, Integer userId);

    /**
     * 报表管理上传统计导出(营业厅)
     */
    List<ReportCountHallExportVO> getReportCountHallExcel(ReportReqVO reportReqVO, Integer userId);

    /**
     * 报表管理上传统计导出(单位)
     */
    List<ReportCountUnitExportVO> getReportCountUnitExcel(ReportReqVO reportReqVO, Integer userId);

    /**
     * 新增报表信息
     */
    void addReport(ReportVO reportVO, Integer userId);

    /**
     * 导出 excel报表信息
     */
    ExcelInfoVO getExcelInfo(Integer fileId, Long reportId);

    /**
     * 删除报表信息列表
     */
    void moveReportList(List<Long> list);
}
