package com.hbhb.cw.report.service;

import com.hbhb.api.core.bean.SelectVO;
import com.hbhb.cw.report.model.ReportManage;
import com.hbhb.cw.report.web.vo.ManageResVO;

import java.util.List;

/**
 * @author wangxiaogang
 */
public interface ManageService {
    /**
     * 获取报表管理内容列表
     *
     * @return 列表
     */
    List<ManageResVO> getManageList();

    /**
     * 修改报表管理内容
     *
     * @param resVO  管理内容
     * @param userId 用户id
     */
    void updateManage(ManageResVO resVO, Integer userId);

    /**
     * 新增报表管理内容
     *
     * @param resVO  管理内容
     * @param userId 用户id
     */
    void saveManage(ManageResVO resVO, Integer userId);

    /**
     * 获取报表管理内容下拉框
     *
     * @return 报表管理内容下拉框
     */
    List<SelectVO> getManageName();

    /**
     * 跟据id获取管理内容详情
     *
     * @param id id
     * @return 详情
     */
    List<ReportManage> getManageInfo(Long id);
}
