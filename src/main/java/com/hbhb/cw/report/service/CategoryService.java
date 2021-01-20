package com.hbhb.cw.report.service;

import com.hbhb.api.core.bean.SelectVO;
import com.hbhb.cw.report.web.vo.CategoryResVO;

import java.util.List;

/**
 * @author wangxiaogang
 */
public interface CategoryService {
    /**
     * 获取报表名称列表
     *
     * @param manageId 管理内容id
     * @return 列表
     */
    List<CategoryResVO> getCategoryList(Long manageId);

    /**
     * 修改报表名称
     *
     * @param resVO  名称内容
     * @param userId 用户id
     */
    void updateCategory(CategoryResVO resVO, Integer userId);

    /**
     * 新增
     *
     * @param resVO  名称实体
     * @param userId 用户id
     */
    void saveCategory(CategoryResVO resVO, Integer userId);

    /**
     * 获取报表名称列表
     *
     * @param manageId 管理内容
     * @return 名称列表
     */
    List<SelectVO> getCategory(Long manageId);

    /**
     * 跟据报表名称id获取详情
     *
     * @param id id
     * @return 报表名称详情
     */
    CategoryResVO getCategoryInfo(Long id);
}
