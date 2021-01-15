package com.hbhb.cw.report.mapper;

import com.hbhb.beetlsql.BaseMapper;
import com.hbhb.cw.report.model.ReportProperty;
import com.hbhb.cw.report.web.vo.PropertyResVO;
import org.beetl.sql.core.page.PageRequest;
import org.beetl.sql.core.page.PageResult;

/**
 * @author wangxiaogang
 */
public interface ReportPropertyMapper extends BaseMapper<ReportProperty> {

    PageResult<PropertyResVO> selectPropertyListByCategoryId(PageRequest<PropertyResVO> request, Long categoryId);
}
