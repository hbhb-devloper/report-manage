package com.hbhb.cw.report.service.impl;

import com.hbhb.core.bean.BeanConverter;
import com.hbhb.core.utils.DateUtil;
import com.hbhb.cw.report.mapper.ReportCategoryMapper;
import com.hbhb.cw.report.mapper.ReportPropertyMapper;
import com.hbhb.cw.report.model.ReportCategory;
import com.hbhb.cw.report.model.ReportProperty;
import com.hbhb.cw.report.rpc.FlowApiExp;
import com.hbhb.cw.report.rpc.FlowTypeApiExp;
import com.hbhb.cw.report.rpc.SysUserApiExp;
import com.hbhb.cw.report.service.CategoryService;
import com.hbhb.cw.report.web.vo.CategoryResVO;
import com.hbhb.cw.report.web.vo.CategoryVO;
import com.hbhb.cw.report.web.vo.PropertyVO;
import com.hbhb.cw.systemcenter.vo.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.query.Query;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

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
    @Resource
    private ReportPropertyMapper propertyMapper;
    @Resource
    private FlowApiExp flowApi;
    @Resource
    private FlowTypeApiExp typeApi;

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
        saveProperty(resVO.getPropertyList(), build.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
        saveProperty(resVO.getPropertyList(), build.getId());
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

    @Override
    public CategoryResVO getCategoryInfo(Long id) {
        CategoryResVO resVO = new CategoryResVO();
        // 获取报表名称内容
        ReportCategory select = categoryMapper
                .createLambdaQuery()
                .andEq(ReportCategory::getId, Query.filterNull(id))
                .andEq(ReportCategory::getState, true)
                .single();
        BeanUtils.copyProperties(select, resVO);

        // 获取报表名称属性列表
        List<ReportProperty> list = propertyMapper
                .createLambdaQuery()
                .andEq(ReportProperty::getCategoryId, id)
                .select();

        // 转换流程类型名称，流程名称
        List<Long> flowIds = new ArrayList<>();
        List<Long> flowTypeIds = new ArrayList<>();
        list.forEach(item -> {
            flowIds.add(item.getFlowId());
            flowTypeIds.add(item.getFlowTypeId());
        });
        // 去重流程id
        HashSet<Long> flowSet = new HashSet<>(flowIds);
        flowIds.clear();
        flowIds.addAll(flowSet);
        // 去重流程类型id
        HashSet<Long> flowTypeSet = new HashSet<>(flowTypeIds);
        flowIds.clear();
        flowIds.addAll(flowTypeSet);
        Map<Long, String> flowMapName = flowApi.getFlowMapName(flowIds);
        Map<Long, String> flowTypeMapName = typeApi.getFlowTypeMapName(flowTypeIds);
        // 组装流程名称，类型名称 及时间格式
        List<PropertyVO> propertyVoList = new ArrayList<>();
        for (ReportProperty property : list) {
            PropertyVO propertyVo = new PropertyVO();
            BeanUtils.copyProperties(property, propertyVo);
            propertyVo.setFlowTypeName(flowTypeMapName.get(property.getFlowTypeId()));
            propertyVo.setFlowName(flowMapName.get(property.getFlowId()));
            propertyVo.setStartTime(DateUtil.dateToString(property.getStartTime(), DateUtil.FORMAT_PATTERN_yyyy_MM_dd));
            propertyVo.setEndTime(DateUtil.dateToString(property.getEndTime(), DateUtil.FORMAT_PATTERN_yyyy_MM_dd));
            propertyVoList.add(propertyVo);
        }
        resVO.setPropertyList(propertyVoList);
        return resVO;
    }

    private void saveProperty(List<PropertyVO> propertyVoList, Long categoryId) {
        List<ReportProperty> properties = new ArrayList<>();
        if (propertyVoList.size() != 0) {
            for (PropertyVO propertyVo : propertyVoList) {
                ReportProperty property = new ReportProperty();
                BeanUtils.copyProperties(propertyVo, property);
                property.setStartTime(DateUtil.string2DateYMD(propertyVo.getStartTime()));
                property.setStartTime(DateUtil.string2DateYMD(propertyVo.getStartTime()));
                property.setCategoryId(categoryId);
                properties.add(property);
            }
            propertyMapper.insertBatch(properties);
        }
    }
}
