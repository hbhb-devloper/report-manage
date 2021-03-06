package com.hbhb.cw.report.mapper;

import com.hbhb.beetlsql.BaseMapper;
import com.hbhb.cw.report.model.Report;
import com.hbhb.cw.report.web.vo.ReportReqVO;
import com.hbhb.cw.report.web.vo.ReportResVO;

import org.beetl.sql.core.page.PageRequest;
import org.beetl.sql.core.page.PageResult;
import org.beetl.sql.mapper.annotation.Param;

import java.util.List;

/**
 * @author wangxiaogang
 */
public interface ReportMapper extends BaseMapper<Report> {

    PageResult<ReportResVO> selectPageByCond(PageRequest<ReportResVO> request, @Param("cond") ReportReqVO reportReqVO);

    List<ReportResVO> selectListByCond(@Param("cond") ReportReqVO reportReqVO);
}
