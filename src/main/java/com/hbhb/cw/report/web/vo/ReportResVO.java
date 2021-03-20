package com.hbhb.cw.report.web.vo;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzc
 * @since 2021-01-11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportResVO implements Serializable {
    private static final long serialVersionUID = -2409693136355254223L;

    @Schema(description = "序号")
    private Long lineNumber;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "关联单名称(管理内容+报表名称+“审批流程”)")
    private String relationName;

    @Schema(description = "上报单位")
    private Integer unitId;

    @Schema(description = "上报单位名称")
    private String unitName;

    @Schema(description = "营业厅id")
    private Long hallId;

    @Schema(description = "营业厅名称")
    private String hallName;

    @Schema(description = "管理内容")
    private String manageName;

    @Schema(description = "报表名称")
    private String reportName;

    @Schema(description = "有无业务")
    private Boolean hasBiz;

    @Schema(description = "有无业务")
    private String hasBizName;

    @Schema(description = "报表周期")
    private String period;

    @Schema(description = "报表周期")
    private String periodName;

    @Schema(description = "创建人")
    private Integer founder;

    @Schema(description = "创建人")
    private String founderName;

    @Schema(description = "创建时间")
    private String launchTime;

    @Schema(description = "流程状态")
    private String state;

    @Schema(description = "流程状态")
    private String stateName;
}
