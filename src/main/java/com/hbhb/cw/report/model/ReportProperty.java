package com.hbhb.cw.report.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wangxiaogang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportProperty implements Serializable {
    private static final long serialVersionUID = -2805285236781731973L;
    private Long id;
    /**
     * 报表名称id
     */
    private Long categoryId;
    /**
     * 编报范围（0-分公司、1-营业厅）
     */
    private Integer scope;
    /**
     * 周期（0-年、1-季、2-月、3-旬、4-日）
     */
    private Integer period;
    /**
     * 流程类型id
     */
    private Long flowTypeId;
    /**
     * 流程id
     */
    private Long flowId;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;
}
