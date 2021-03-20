package com.hbhb.cw.report.web.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangxiaogang
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeReqVO implements Serializable {
    private static final long serialVersionUID = -5409894629170434147L;
    @Schema(description = "业务编号")
    private String num;
    @Schema(description = "金额最小值")
    private BigDecimal amountMin;
    @Schema(description = "金额最大值")
    private BigDecimal amountMax;
    @Schema(description = "用户id")
    private Integer userId;


}
