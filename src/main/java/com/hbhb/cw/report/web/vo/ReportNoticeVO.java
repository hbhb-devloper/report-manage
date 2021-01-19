package com.hbhb.cw.report.web.vo;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzc
 * @since 2021-01-18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportNoticeVO implements Serializable {
    private static final long serialVersionUID = 6342182871983849572L;
    private Long id;

    @Schema(description = "印刷品id")
    private Long reportId;

    @Schema(description = "接收人id")
    private Integer receiver;

    @Schema(description = "发起人id")
    private Integer promoter;

    @Schema(description = "提醒内容")
    private String content;

    @Schema(description = "状态")
    private Integer state;

    @Schema(description = "优先级")
    private Integer priority;

    @Schema(description = "流程类型id")
    private Long flowTypeId;
}
