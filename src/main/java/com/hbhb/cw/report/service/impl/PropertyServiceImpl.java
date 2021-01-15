package com.hbhb.cw.report.service.impl;

import com.hbhb.core.bean.BeanConverter;
import com.hbhb.cw.report.mapper.ReportPropertyMapper;
import com.hbhb.cw.report.model.ReportProperty;
import com.hbhb.cw.report.rpc.FlowApiExp;
import com.hbhb.cw.report.rpc.FlowTypeApiExp;
import com.hbhb.cw.report.service.PropertyService;
import com.hbhb.cw.report.web.vo.PropertyCondVO;
import com.hbhb.cw.report.web.vo.PropertyReqVO;
import com.hbhb.cw.report.web.vo.PropertyResVO;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.page.DefaultPageRequest;
import org.beetl.sql.core.page.PageRequest;
import org.beetl.sql.core.page.PageResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

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
        flowIds.clear();
        flowIds.addAll(flowTypeSet);
        Map<Long, String> flowMapName = flowApi.getFlowMapName(flowIds);
        Map<Long, String> flowTypeMapName = typeApi.getFlowTypeMapName(flowTypeIds);

        // 组装
        page.getList().forEach(item -> {
            item.setFlowName(flowMapName.get(item.getFlowId()));
            item.setFlowTypeName(flowTypeMapName.get(item.getFlowTypeId()));
        });
        return page;
    }

    @Override
    public void updateProperty(List<PropertyReqVO> list) {
        List<ReportProperty> properties = BeanConverter.copyBeanList(list, ReportProperty.class);
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
}
