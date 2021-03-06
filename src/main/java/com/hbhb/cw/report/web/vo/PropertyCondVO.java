package com.hbhb.cw.report.web.vo;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangxiaogang
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyCondVO implements Serializable {

    private static final long serialVersionUID = 5393820746167610063L;
    @Schema(description = "编报范围（0-分公司、1-营业厅）")
    private Integer scope;

    @Schema(description = "周期（0-年、1-季、2-月、3-旬、4-日）")
    private Integer period;

    @Schema(description = "流程类型id")
    private Long flowTypeId;

    @Schema(description = "报表名称id")
    private Long categoryId;
}
