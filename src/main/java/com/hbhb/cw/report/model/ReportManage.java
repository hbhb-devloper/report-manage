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
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ReportManage implements Serializable {
    private static final long serialVersionUID = -7126654032346120936L;
    private Long id;
    /**
     * 管理名称
     */
    private String manageName;
    /**
     * 备注
     */
    private String remark;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 是否启用（0-禁用，1-启用）
     */
    private Boolean hasEnable;
    /**
     * 启用按月打包(0-否，1-是)
     */
    private Boolean hasPack;
}
