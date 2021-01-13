package com.hbhb.cw.report.web.vo;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class CategoryResVO implements Serializable {
    private static final long serialVersionUID = -8678613066066862682L;
    private Long id;

    @Schema(description = "报表名称")
    private String reportName;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "报表内容id")
    private Long manageId;

    @Schema(description = "是否启用")
    private Boolean state;

    @Schema(description = "修改时间")
    private Date updateTime;

    @Schema(description = "修改人")
    private String updateBy;

}
