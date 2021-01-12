package com.hbhb.cw.report.model;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzc
 * @since 2021-01-12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportFlow implements Serializable {
    private static final long serialVersionUID = -6561560210552946085L;
    private Long id;
    /**
     * 报表id
     */
    private Long reportId;
    /**
     * 流程节点id
     */
    private String flowNodeId;
    /**
     * 流程角色id
     */
    private Long flowRoleId;
    /**
     * 审批者
     */
    private Integer userId;
    /**
     * 角色详情
     */
    private String roleDesc;
    /**
     * 分配者
     */
    private Long assigner;
    /**
     * 是否能够自定义流程（0-否、1-是）
     */
    private Boolean controlAccess;
    /**
     * 是否允许被设置不参与流程（0-不参与、1-参与）
     */
    private Boolean isJoin;
    /**
     * 操作（0-拒绝、1-同意）
     */
    private Integer operation;
    /**
     * 审批意见
     */
    private String suggestion;
    /**
     * 是否删除（0无/1删）
     */
    private Integer isDelete;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 修改者
     */
    private String updateBy;
    /**
     * 启用条件
     */
    private Integer enableCond;
}
