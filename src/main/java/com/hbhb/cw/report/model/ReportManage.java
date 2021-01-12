package com.hbhb.cw.report.model;

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
     * 是否启用
     */
    private boolean state;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 修改人
     */
    private String updateBy;
}
