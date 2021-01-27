package com.hbhb.cw.report.web.vo;

import java.io.Serializable;
import java.util.List;

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
public class ReportReqVO implements Serializable {
    private static final long serialVersionUID = -4592981921549286107L;

    @Schema(description = "上报单位")
    private Integer unitId;

    @Schema(description = "营业厅id")
    private Long hallId;

    @Schema(description = "管理内容Id")
    private Long manageId;

    @Schema(description = "报表名称")
    private Long categoryId;

    @Schema(description = "有误业务")
    private Boolean hasBiz;

    @Schema(description = "报表状态")
    private Integer categoryState;

    @Schema(description = "流程状态")
    private String state;

    @Schema(description = "周期")
    private String period;

    @Schema(description = "周期详细")
    private String periodInfo;

    @Schema(description = "上传时间")
    private String launchTime;

    @Schema(description = "关联单名称(管理内容+报表名称+“审批流程”)")
    private String relationName;

    @Schema(description = "报表类型（0-分公司报表信息，1-营业厅报表类型）")
    private Integer type;

    private List<Integer> unitIds;

    private List<Long> hallIds;
}
