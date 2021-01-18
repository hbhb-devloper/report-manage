package com.hbhb.cw.report.web.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangxiaogang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeResVO implements Serializable {
    private static final long serialVersionUID = 4768733585157666207L;
    private Long id;

    @Schema(description = "发票id")
    private Integer businessId;

    @Schema(description = "优先级")
    private Integer priority;

    @Schema(description = "提醒内容")
    private String content;

    @Schema(description = "发起时金额")
    private String amount;

    @JsonIgnore
    @Schema(description = "流程类型id")
    private Long flowTypeId;

    @Schema(description = "流程类型名称")
    private String flowType;

    @Schema(description = "发起人")
    private String userName;

    @JsonIgnore
    @Schema(description = "发起单位id")
    private Integer unitId;

    @Schema(description = "发起单位名称")
    private String unitName;

    @Schema(description = "发起时间")
    private String createTime;

    @Schema(description = "状态值")
    private Integer state;

    @Schema(description = "状态名称")
    private String stateLabel;

    @Schema(description = "提醒类型")
    private String noticeType;
}
