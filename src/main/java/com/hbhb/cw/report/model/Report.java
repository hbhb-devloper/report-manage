package com.hbhb.cw.report.model;

import org.beetl.sql.annotation.entity.AutoID;

import java.io.Serializable;
import java.util.Date;

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
public class Report implements Serializable {
    private static final long serialVersionUID = -5833182136301658132L;
    @AutoID
    private Long id;
    /**
     * 上报单位
     */
    private Integer unitId;
    /**
     * 营业厅id
     */
    private Long hallId;
    /**
     * 负责人
     */
    private Integer userId;
    /**
     * 管理内容id
     */
    private Long manageId;
    /**
     * 报表内容id
     */
    private Long categoryId;
    /**
     * 周期
     */
    private String period;
    /**
     * 创建人
     */
    private Integer founder;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 审批发起时间
     */
    private Date launchTime;
    /**
     * 流程状态（字典：10-流转中，20-通过，30-拒绝）
     */
    private Integer state;
    /**
     * 有无业务（0-没有，1-有）
     */
    private Boolean hasBiz;
    /**
     * 报表类型（0-分公司报表信息，1-营业厅报表类型）
     */
    private Integer type;
}
