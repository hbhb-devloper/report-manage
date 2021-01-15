package com.hbhb.cw.report.web.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author wangxiaogang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyResVO implements Serializable {
    private static final long serialVersionUID = 1871757028892693118L;
    private Long id;

    @Schema(description = "报表名称id")
    private Long categoryId;

    @Schema(description = "报表名称id")
    private Long categoryName;

    @Schema(description = "编报范围（0-分公司、1-营业厅）")
    private Integer scope;

    @Schema(description = "周期（0-年、1-季、2-月、3-旬、4-日）")
    private Integer period;

    @Schema(description = "流程类型id")
    private Long flowTypeId;

    @Schema(description = "流程类型名称")
    private String flowTypeName;

    @Schema(description = "流程id")
    private Long flowId;

    @Schema(description = "流程名称")
    private String flowName;

    @Schema(description = "开始时间")
    private String startTime;

    @Schema(description = "结束时间")
    private String endTime;
}
