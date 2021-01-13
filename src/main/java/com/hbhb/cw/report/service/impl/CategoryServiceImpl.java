package com.hbhb.cw.report.service.impl;

import com.hbhb.core.bean.BeanConverter;
import com.hbhb.cw.report.mapper.ReportCategoryMapper;
import com.hbhb.cw.report.model.ReportCategory;
import com.hbhb.cw.report.rpc.SysUserApiExp;
import com.hbhb.cw.report.service.CategoryService;
import com.hbhb.cw.report.web.vo.CategoryResVO;
import com.hbhb.cw.report.web.vo.CategoryVO;
import com.hbhb.cw.systemcenter.vo.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author wangxiaogang
 */
@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    @Resource
    private ReportCategoryMapper categoryMapper;
    @Resource
    private SysUserApiExp userApi;

    @Override
    public List<CategoryResVO> getCategoryList(Long manageId) {
        List<ReportCategory> select = categoryMapper
                .createLambdaQuery()
                .andEq(ReportCategory::getManageId, Query.filterNull(manageId)).select();
        return BeanConverter.copyBeanList(select, CategoryResVO.class);
    }

    @Override
    public void updateCategory(CategoryResVO resVO, Integer userId) {
        UserInfo user = userApi.getUserInfoById(userId);
        ReportCategory build = ReportCategory.builder()
                .id(resVO.getId())
                .manageId(resVO.getManageId())
                .reportName(resVO.getReportName())
                .state(resVO.getState())
                .remark(resVO.getRemark())
                .updateBy(user.getNickName())
                .updateTime(new Date())
                .build();
        categoryMapper.updateTemplateById(build);
    }

    @Override
    public void saveCategory(CategoryResVO resVO, Integer userId) {
        UserInfo user = userApi.getUserInfoById(userId);
        ReportCategory build = ReportCategory.builder()
                .id(resVO.getId())
                .manageId(resVO.getManageId())
                .reportName(resVO.getReportName())
                .state(resVO.getState())
                .remark(resVO.getRemark())
                .updateBy(user.getNickName())
                .updateTime(new Date())
                .build();
        categoryMapper.insert(build);
    }

    @Override
    public List<CategoryVO> getCategory(Long manageId) {
        List<ReportCategory> select = categoryMapper
                .createLambdaQuery()
                .andEq(ReportCategory::getManageId, Query.filterNull(manageId))
                .andEq(ReportCategory::getState, true)
                .select();
        return BeanConverter.copyBeanList(select, CategoryVO.class);
    }


}
