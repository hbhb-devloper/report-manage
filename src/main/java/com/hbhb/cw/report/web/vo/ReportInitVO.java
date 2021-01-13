package com.hbhb.cw.report.web.vo;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzc
 * @since 2021-01-12
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportInitVO implements Serializable {
    private static final long serialVersionUID = 5055368662525204854L;

    @Schema(description = "项目签报id")
    Long reportId;

    @Schema(description = "流程类型id")
    Long flowTypeId;

    @Schema(description = "用户id")
    Integer userId;
}