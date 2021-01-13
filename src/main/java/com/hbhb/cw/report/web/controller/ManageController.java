package com.hbhb.cw.report.web.controller;

import com.hbhb.api.core.bean.SelectVO;
import com.hbhb.cw.report.model.ReportManage;
import com.hbhb.cw.report.service.ManageService;
import com.hbhb.cw.report.web.vo.ManageResVO;
import com.hbhb.web.annotation.UserId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wangxiaogang
 */
@Tag(name = "报表管理-报表管理内容")
@RestController
@RequestMapping("/manage")
@Slf4j
public class ManageController {
    @Resource
    private ManageService manageService;

    @Operation(summary = "报表管理内容列表")
    @GetMapping("/list")
    public List<ManageResVO> getManageList() {
        return manageService.getManageList();
    }

    @Operation(summary = "报表管理id获取详情")
    @GetMapping("/list")
    public List<ReportManage> getManage(Long id) {
        return manageService.getManageInfo(id);
    }

    @Operation(summary = "修改报表管理内容")
    @PutMapping("")
    public void updateManage(@Parameter(description = "修改管理内容实体") @RequestBody ManageResVO resVO,
                             @Parameter(hidden = true) @UserId Integer userId) {
        manageService.updateManage(resVO, userId);
    }

    @Operation(summary = "新增表管理内容")
    @PostMapping("")
    public void saveManage(@Parameter(description = "修改管理内容实体") @RequestBody ManageResVO resVO,
                           @Parameter(hidden = true) @UserId Integer userId) {
        manageService.saveManage(resVO, userId);
    }

    @Operation(summary = "获取管理内容下拉框")
    @GetMapping("/name")
    public List<SelectVO> getManageName() {
        return manageService.getManageName();
    }


}
