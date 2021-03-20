package com.hbhb.cw.report.web.controller;

import com.hbhb.api.core.bean.SelectVO;
import com.hbhb.cw.report.service.CategoryService;
import com.hbhb.cw.report.web.vo.CategoryResVO;
import com.hbhb.web.annotation.UserId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@Tag(name = "报表管理-报表名称")
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @Operation(summary = "报表名称内容分页列表")
    @GetMapping("/list")
    public List<CategoryResVO> getCategoryList(@Parameter(description = "管理内容id") Long manageId) {
        return categoryService.getCategoryList(manageId);
    }

    @Operation(summary = "修改报表名称内容")
    @PutMapping("")
    public void updateCategory(@Parameter(description = "修改名称实体") @RequestBody CategoryResVO resVO,
                               @Parameter(hidden = true) @UserId Integer userId) {
        categoryService.updateCategory(resVO, userId);
    }

    @Operation(summary = "修改报表管理内容")
    @PostMapping("")
    public void saveCategory(@Parameter(description = "添加报表名称实体") @RequestBody CategoryResVO resVO,
                             @Parameter(hidden = true) @UserId Integer userId) {
        categoryService.saveCategory(resVO, userId);
    }

    @Operation(summary = "跟据管理内容获取报表名称信息")
    @GetMapping("/name")
    public List<SelectVO> getCategory(Long manageId) {
        return categoryService.getCategory(manageId);
    }

    @Operation(summary = "跟据管理内容获取报表名称信息详情")
    @GetMapping("/{id}")
    public CategoryResVO getCategoryInfo(@PathVariable Long id) {
        return categoryService.getCategoryInfo(id);
    }
}
