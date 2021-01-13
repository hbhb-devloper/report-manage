package com.hbhb.cw.report.web.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangxiaogang
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportVO implements Serializable {
    private static final long serialVersionUID = 5848407463211510961L;

    @Schema(description = "上报单位")
    private Integer unitId;

    @Schema(description = "上报营业厅")
    private Long hallId;

    @Schema(description = "管理内容Id")
    private Long manageId;

    @Schema(description = "报表名称")
    private Long categoryId;

    @Schema(description = "报表状态")
    private Integer state;

    @Schema(description = "有无业务")
    private Boolean hasBiz;

    @Schema(description = "周期")
    private String period;

    @Schema(description = "报表类型（0-分公司报表信息，1-营业厅报表类型）")
    private Integer type;

    @Schema(description = "流程类型id")
    Long flowTypeId;

    @Schema(description = "附件")
    private List<ReportFileVO> files;

}
