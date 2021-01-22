package com.hbhb.cw.report.service.impl;

import com.hbhb.api.core.bean.SelectVO;
import com.hbhb.core.utils.DateUtil;
import com.hbhb.cw.report.enums.Scope;
import com.hbhb.cw.report.mapper.ReportPropertyMapper;
import com.hbhb.cw.report.model.ReportProperty;
import com.hbhb.cw.report.rpc.FlowApiExp;
import com.hbhb.cw.report.rpc.FlowTypeApiExp;
import com.hbhb.cw.report.rpc.SysDictApiExp;
import com.hbhb.cw.report.service.PropertyService;
import com.hbhb.cw.report.web.vo.PropertyCondVO;
import com.hbhb.cw.report.web.vo.PropertyReqVO;
import com.hbhb.cw.report.web.vo.PropertyResVO;
import com.hbhb.cw.systemcenter.enums.DictCode;
import com.hbhb.cw.systemcenter.enums.TypeCode;
import com.hbhb.cw.systemcenter.vo.DictVO;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.page.DefaultPageRequest;
import org.beetl.sql.core.page.PageRequest;
import org.beetl.sql.core.page.PageResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author wangxiaogang
 */
@Service
@Slf4j
@SuppressWarnings(value = {"unchecked"})
public class PropertyServiceImpl implements PropertyService {
    @Resource
    private ReportPropertyMapper propertyMapper;
    @Resource
    private FlowApiExp flowApi;
    @Resource
    private FlowTypeApiExp typeApi;
    @Resource
    private SysDictApiExp dictApi;

    @Override
    public PageResult<PropertyResVO> getPropertyList(Long categoryId, Integer pageNum, Integer pageSize) {
        PageRequest<PropertyResVO> request = DefaultPageRequest.of(pageNum, pageSize);
        PageResult<PropertyResVO> page = propertyMapper.selectPropertyListByCategoryId(request, categoryId);

        // 转换流程类型名称，流程名称
        List<Long> flowIds = new ArrayList<>();
        List<Long> flowTypeIds = new ArrayList<>();
        page.getList().forEach(item -> {
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
        Map<Integer, String> scopeMap = new HashMap<>(5);
        scopeMap.put(Scope.FILIALE.key(), Scope.FILIALE.value());
        scopeMap.put(Scope.HALL.key(), Scope.HALL.value());
        // 周期
        List<DictVO> dict = dictApi.getDict(TypeCode.REPORT.value(), DictCode.REPORT_PERIOD.value());
        Map<String, String> periodMap = dict.stream().collect(Collectors.toMap(DictVO::getValue, DictVO::getLabel));
        // 组装
        page.getList().forEach(item -> {
            item.setFlowName(flowMapName.get(item.getFlowId()));
            item.setFlowTypeName(flowTypeMapName.get(item.getFlowTypeId()));
            item.setScopeName(scopeMap.get(item.getScope()));
            item.setPeriodName(periodMap.get(item.getPeriod().toString()));
        });
        return page;
    }

    @Override
    public void updateProperty(List<PropertyReqVO> list) {
        List<ReportProperty> properties = new ArrayList<>();
        list.forEach(item -> properties.add(ReportProperty.builder()
                .id(item.getId())
                .startTime(DateUtil.string2DateYMD(item.getStartTime()))
                .endTime(DateUtil.string2DateYMD(item.getEndTime()))
                .build()));
        propertyMapper.updateBatchTempById(properties);
    }

    @Override
    public List<Long> getFlowId(PropertyCondVO cond) {
        List<ReportProperty> select = propertyMapper.createLambdaQuery()
                .andEq(ReportProperty::getFlowTypeId, cond.getFlowTypeId())
                .andEq(ReportProperty::getPeriod, cond.getPeriod())
                .andEq(ReportProperty::getScope, cond.getScope())
                .andEq(ReportProperty::getCategoryId, cond.getCategoryId())
                .select(ReportProperty::getFlowId);
        List<Long> flowIds = new ArrayList<>();
        select.forEach(item -> flowIds.add(item.getFlowId()));
        return flowIds;
    }

    @Override
    public List<PropertyReqVO> getStartTime(PropertyCondVO cond) {
        List<ReportProperty> select = propertyMapper.createLambdaQuery()
                .andEq(ReportProperty::getFlowTypeId, cond.getFlowTypeId())
                .andEq(ReportProperty::getPeriod, cond.getPeriod())
                .andEq(ReportProperty::getScope, cond.getScope())
                .andEq(ReportProperty::getCategoryId, cond.getCategoryId())
                .select();
        List<PropertyReqVO> property = new ArrayList<>();
        select.forEach(item -> property.add(PropertyReqVO.builder()
                .id(item.getId())
                .startTime(DateUtil.dateToString(item.getStartTime()))
                .endTime(DateUtil.dateToString(item.getEndTime()))
                .build()));
        return property;
    }

    @Override
    public void deleteProp(Long id) {
        propertyMapper.deleteById(id);
    }

    @Override
    public List<SelectVO> getReportPeriod(Long categoryId) {
        List<ReportProperty> select = propertyMapper.createLambdaQuery()
                .andEq(ReportProperty::getCategoryId, categoryId)
                .select();
        List<DictVO> dict = dictApi.getDict(TypeCode.REPORT.value(), DictCode.REPORT_PERIOD.value());
        Map<String, String> periodMap = dict.stream().collect(Collectors.toMap(DictVO::getValue, DictVO::getLabel));
        return Optional.ofNullable(select)
                .orElse(new ArrayList<>())
                .stream()
                .map(item -> SelectVO.builder()
                        .id(Long.valueOf(item.getPeriod()))
                        .label(periodMap.get(item.getPeriod().toString()))
                        .build())
                .collect(Collectors.toList());
    }
}
