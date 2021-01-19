package com.hbhb.cw.report.web.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author wangxiaogang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryVO implements Serializable {
    private static final long serialVersionUID = 4753650714299733914L;
    private Long id;
    @Schema(description = "报表名称")
    private String reportName;
    @Schema(description = "启用按月打包(0-否，1-是)")
    private Boolean hasPack;
    @Schema(description = "是否启用（0-禁用，1-启用）")
    private Boolean hasEnable;
}
