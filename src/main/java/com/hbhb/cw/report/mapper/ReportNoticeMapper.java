package com.hbhb.cw.report.mapper;

import com.hbhb.beetlsql.BaseMapper;
import com.hbhb.cw.report.model.ReportNotice;
import com.hbhb.cw.report.web.vo.NoticeReqVO;
import com.hbhb.cw.report.web.vo.NoticeResVO;

import org.beetl.sql.core.page.PageRequest;
import org.beetl.sql.core.page.PageResult;

/**
 * @author yzc
 * @since 2021-01-12
 */
public interface ReportNoticeMapper extends BaseMapper<ReportNotice> {

    PageResult<NoticeResVO> selectPageByCond(NoticeReqVO cond, PageRequest request);
}
