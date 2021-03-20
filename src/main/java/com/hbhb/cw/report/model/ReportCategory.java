package com.hbhb.cw.report.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.beetl.sql.annotation.entity.AutoID;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wangxiaogang
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportCategory  implements Serializable {
    private static final long serialVersionUID = -3985217336222172555L;
    @AutoID
    private Long id;
    /**
     * 报表名称
     */
    private String reportName;
    /**
     * 备注
     */
    private String remark;
    /**
     * 报表内容id
     */
    private Long manageId;
    /**
     * 是否启用
     */
    private Boolean state;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 修改人
     */
    private String updateBy;

}
