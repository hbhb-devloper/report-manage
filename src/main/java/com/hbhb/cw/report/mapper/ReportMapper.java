package com.hbhb.cw.report.mapper;

import com.hbhb.beetlsql.BaseMapper;
import com.hbhb.cw.report.model.Report;
import com.hbhb.cw.report.web.vo.ReportReqVO;
import com.hbhb.cw.report.web.vo.ReportResVO;

import org.beetl.sql.core.page.PageRequest;
import org.beetl.sql.core.page.PageResult;

/**
 * @author wangxiaogang
 */
public interface ReportMapper extends BaseMapper<Report> {

    PageResult<ReportResVO> selectListByCond(PageRequest<ReportResVO> request, ReportReqVO reportReqVO);
}
