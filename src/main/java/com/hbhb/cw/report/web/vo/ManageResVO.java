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
public class ManageResVO implements Serializable {

    private static final long serialVersionUID = -7738661320365160788L;
    private Integer id;

    @Schema(description = "管理名称")
    private String manageName;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "修改时间")
    private Date updateTime;

    @Schema(description = "修改人")
    private String updateBy;

    @Schema(description = "是否启用（0-禁用，1-启用）")
    private Boolean hasEnable;

    @Schema(description = "启用按月打包(0-否，1-是)")
    private Boolean hasPage;

}
