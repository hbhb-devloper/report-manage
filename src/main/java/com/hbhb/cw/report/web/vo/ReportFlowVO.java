package com.hbhb.cw.report.web.vo;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzc
 * @since 2021-01-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportFlowVO implements Serializable {
    private static final long serialVersionUID = -7978742820466083442L;

    private Long id;
    @Schema(description = "报表信息id")
    private Long reportId;
    @Schema(description = "流程节点id")
    private String flowNodeId;
    @Schema(description = "流程角色id")
    private Long flowRoleId;
    @Schema(description = "")
    private String approverRole;
    @Schema(description = "用户名称")
    private String nickName;
    @Schema(description = "")
    private Integer approver;
    @Schema(description = "操作（0-拒绝、1-同意）")
    private Integer operation;
    @Schema(description = "意见")
    private String suggestion;
    @Schema(description = "是否能够自定义流程（0-否、1-是）")
    private Boolean controlAccess;
    @Schema(description = "是否允许被设置不参与流程（0-不参与、1-参与）")
    private Boolean isJoin;
    @Schema(description = "分配者")
    private Long assigner;
    @Schema(description = "角色详情")
    private String roleDesc;
    @Schema(description = "更新时间")
    private String updateTime;
}
