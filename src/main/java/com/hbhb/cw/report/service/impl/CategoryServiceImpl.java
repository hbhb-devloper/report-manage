package com.hbhb.cw.report.service.impl;

import com.hbhb.api.core.bean.SelectVO;
import com.hbhb.core.bean.BeanConverter;
import com.hbhb.core.utils.DateUtil;
import com.hbhb.cw.report.enums.Scope;
import com.hbhb.cw.report.mapper.ReportCategoryMapper;
import com.hbhb.cw.report.mapper.ReportManageMapper;
import com.hbhb.cw.report.mapper.ReportPropertyMapper;
import com.hbhb.cw.report.model.ReportCategory;
import com.hbhb.cw.report.model.ReportManage;
import com.hbhb.cw.report.model.ReportProperty;
import com.hbhb.cw.report.rpc.FlowApiExp;
import com.hbhb.cw.report.rpc.FlowTypeApiExp;
import com.hbhb.cw.report.rpc.SysDictApiExp;
import com.hbhb.cw.report.rpc.SysUserApiExp;
import com.hbhb.cw.report.service.CategoryService;
import com.hbhb.cw.report.web.vo.CategoryResVO;
import com.hbhb.cw.report.web.vo.PropertyVO;
import com.hbhb.cw.systemcenter.enums.DictCode;
import com.hbhb.cw.systemcenter.enums.TypeCode;
import com.hbhb.cw.systemcenter.vo.DictVO;
import com.hbhb.cw.systemcenter.vo.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.query.Query;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.alibaba.excel.util.StringUtils.isEmpty;

/**
 * @author wangxiaogang
 */
@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    @Resource
    private ReportCategoryMapper categoryMapper;
    @Resource
    private ReportManageMapper manageMapper;
    @Resource
    private SysUserApiExp userApi;
    @Resource
    private ReportPropertyMapper propertyMapper;
    @Resource
    private FlowApiExp flowApi;
    @Resource
    private FlowTypeApiExp typeApi;
    @Resource
    private SysDictApiExp dictApi;

    @Override
    public List<CategoryResVO> getCategoryList(Long manageId) {
        // 报表名称列
        List<ReportCategory> select = categoryMapper
                .createLambdaQuery()
                .andEq(ReportCategory::getManageId, Query.filterNull(manageId)).select();
        if (select.size() != 0) {
            List<Long> manageIds = new ArrayList<>();
            select.forEach(item -> manageIds.add(item.getManageId()));
            // 去除重复报表内容id
            HashSet<Long> flowSet = new HashSet<>(manageIds);
            manageIds.clear();
            manageIds.addAll(flowSet);
            // 获取报表内容id
            List<ReportManage> manageList = manageMapper
                    .createLambdaQuery()
                    .andIn(ReportManage::getId, manageIds)
                    .select();
            // 组装报表名称id
            Map<Long, String> collect = manageList.stream()
                    .collect(Collectors.toMap(ReportManage::getId, ReportManage::getManageName));
            List<CategoryResVO> categoryResVo = BeanConverter.copyBeanList(select, CategoryResVO.class);
            categoryResVo.forEach(item -> item.setManageName(collect.get(item.getManageId())));
            return categoryResVo;
        }
        return null;
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
    public List<SelectVO> getCategory(Long manageId) {
        List<ReportCategory> select = categoryMapper
                .createLambdaQuery()
                .andEq(ReportCategory::getManageId, Query.filterNull(manageId))
                .andEq(ReportCategory::getState, true)
                .select();
        return Optional.ofNullable(select)
                .orElse(new ArrayList<>())
                .stream()
                .map(item -> SelectVO.builder()
                        .id(item.getId())
                        .label(item.getReportName())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResVO getCategoryInfo(Long id) {
        CategoryResVO resVO = new CategoryResVO();
        // 获取报表名称内容
        ReportCategory select = categoryMapper
                .createLambdaQuery()
                .andEq(ReportCategory::getId, Query.filterNull(id))
                .single();
        BeanUtils.copyProperties(select, resVO);

        // 获取报表名称属性列表
        List<ReportProperty> list = propertyMapper
                .createLambdaQuery()
                .andEq(ReportProperty::getCategoryId, id)
                .select();

        // 转换流程类型名称，流程名称
        if (list.size() != 0) {
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
            flowTypeIds.clear();
            flowTypeIds.addAll(flowTypeSet);
            Map<Long, String> flowMapName = flowApi.getFlowMapName(flowIds);
            Map<Long, String> flowTypeMapName = typeApi.getFlowTypeMapName(flowTypeIds);

            // 周期 范围
            Map<Integer, String> scopeMap = new HashMap<>(5);
            scopeMap.put(Scope.FILIALE.key(), Scope.FILIALE.value());
            scopeMap.put(Scope.HALL.key(), Scope.HALL.value());
            // 周期
            List<DictVO> dict = dictApi.getDict(TypeCode.REPORT.value(), DictCode.REPORT_PERIOD.value());
            Map<String, String> periodMap = dict.stream().collect(Collectors.toMap(DictVO::getValue, DictVO::getLabel));

            // 组装流程名称，类型名称 及时间格式
            List<PropertyVO> propertyVoList = new ArrayList<>();
            for (ReportProperty property : list) {
                PropertyVO propertyVo = new PropertyVO();
                BeanUtils.copyProperties(property, propertyVo);
                propertyVo.setPeriodName(periodMap.get(property.getPeriod().toString()));
                propertyVo.setScopeName(scopeMap.get(property.getScope()));
                propertyVo.setFlowTypeName(flowTypeMapName.get(property.getFlowTypeId()));
                propertyVo.setFlowName(flowMapName.get(property.getFlowId()));
                propertyVo.setStartTime(DateUtil.dateToString(property.getStartTime(), DateUtil.FORMAT_PATTERN_yyyy_MM_dd));
                propertyVo.setEndTime(DateUtil.dateToString(property.getEndTime(), DateUtil.FORMAT_PATTERN_yyyy_MM_dd));
                propertyVoList.add(propertyVo);
            }
            resVO.setPropertyList(propertyVoList);
        }
        return resVO;
    }

    private void saveProperty(List<PropertyVO> propertyVoList, Long categoryId) {
        List<ReportProperty> properties = new ArrayList<>();
        if (!isEmpty(propertyVoList)) {
            for (PropertyVO propertyVo : propertyVoList) {
                if (isEmpty(propertyVo.getId())) {
                    ReportProperty property = new ReportProperty();
                    BeanUtils.copyProperties(propertyVo, property);
                    property.setStartTime(DateUtil.string2DateYMD(propertyVo.getStartTime()));
                    property.setEndTime(DateUtil.string2DateYMD(propertyVo.getEndTime()));
                    property.setCategoryId(categoryId);
                    properties.add(property);
                }
            }
            propertyMapper.insertBatch(properties);
        }
    }
}
