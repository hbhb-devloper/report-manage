package com.hbhb.cw.report.service;

import com.hbhb.cw.report.web.vo.CategoryResVO;
import com.hbhb.cw.report.web.vo.CategoryVO;

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
    List<CategoryVO> getCategory(Long manageId);
}
