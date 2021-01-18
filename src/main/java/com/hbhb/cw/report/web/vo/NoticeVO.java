package com.hbhb.cw.report.web.vo;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class NoticeVO implements Serializable {
    private static final long serialVersionUID = 7668254396519171210L;
    private Long id;
    @Schema(description = "内容")
    private String content;
    @Schema(description = "项目id")
    private Long businessId;
    @Schema(description = "日期")
    private String date;
    @Schema(description = "签报人")
    private String userName;
    @Schema(description = "提醒类型")
    private String noticeType;
}
