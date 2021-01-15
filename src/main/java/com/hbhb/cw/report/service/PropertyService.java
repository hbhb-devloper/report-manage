package com.hbhb.cw.report.service;

import com.hbhb.cw.report.web.vo.PropertyCondVO;
import com.hbhb.cw.report.web.vo.PropertyReqVO;
import com.hbhb.cw.report.web.vo.PropertyResVO;
import org.beetl.sql.core.page.PageResult;

import java.util.List;

/**
 * @author wangxiaogang
 */
public interface PropertyService {
    /**
     * 跟据报表名称id获取报表名称属性列表
     *
     * @param categoryId 报表名称id
     * @param pageNum    页数
     * @param pageSize   条数
     * @return 列表
     */
    PageResult<PropertyResVO> getPropertyList(Long categoryId, Integer pageNum, Integer pageSize);

    /**
     * 批量修改报表名称属性
     *
     * @param list 修改集合
     */
    void updateProperty(List<PropertyReqVO> list);

    /**
     * 跟据条件获取流程id
     *
     * @param cond 条件
     * @return 流程id
     */
    List<Long> getFlowId(PropertyCondVO cond);
}
