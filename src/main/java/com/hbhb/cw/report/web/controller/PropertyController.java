package com.hbhb.cw.report.web.controller;

import com.hbhb.api.core.bean.SelectVO;
import com.hbhb.cw.report.service.PropertyService;
import com.hbhb.cw.report.web.vo.PropertyCondVO;
import com.hbhb.cw.report.web.vo.PropertyReqVO;
import com.hbhb.cw.report.web.vo.PropertyResVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.page.PageResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wangxiaogang
 */
@Tag(name = "报表管理-报表名称属性")
@RestController
@RequestMapping("/property")
@Slf4j
public class PropertyController {
    @Resource
    private PropertyService propertyService;

    @GetMapping("/list")
    @Operation(summary = "报表列表")
    public PageResult<PropertyResVO> getPropertyList(
            @Parameter(description = "页码，默认为1") @RequestParam(required = false) Integer pageNum,
            @Parameter(description = "每页数量，默认为10") @RequestParam(required = false) Integer pageSize,
            @Parameter(description = "报表名称id") Long categoryId
    ) {
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 20 : pageSize;
        return propertyService.getPropertyList(categoryId, pageNum, pageSize);
    }

    @Operation(summary = "批量修改报表名称属性")
    @PutMapping("")
    public void updateProperty(@RequestBody List<PropertyReqVO> list) {
        propertyService.updateProperty(list);
    }

    @Operation(summary = "跟据条件获取流程id")
    @GetMapping("/flow")
    public List<Long> getFlowId(PropertyCondVO cond) {
        return propertyService.getFlowId(cond);
    }

    @Operation(summary = "删除报表名称属性")
    @DeleteMapping("/{id}")
    public void deleteProp(@PathVariable("id") Long id) {
        propertyService.deleteProp(id);
    }

    @Operation(summary = "跟据条件获取起止时间")
    @GetMapping("/time")
    public List<PropertyReqVO> getStartTime(PropertyCondVO cond) {
        return propertyService.getStartTime(cond);
    }

    @Operation(summary = "跟据报表名称id获取周期")
    @GetMapping("/period")
    public List<SelectVO> getReportPeriod(@Parameter(description = "报表名称id") Long categoryId) {
        return propertyService.getReportPeriod(categoryId);
    }

}
