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
public class ReportNotice implements Serializable {
    private static final long serialVersionUID = 4463710430482254085L;

    private Long id;
    /**
     * 报表id
     */
    private Long reportId;
    /**
     * 接收人id
     */
    private Integer receiver;
    /**
     * 发起人id
     */
    private Integer promoter;
    /**
     * 提醒内容
     */
    private String content;
    /**
     * 提醒状态（0-未读、1-已读）
     */
    private Integer state;
    /**
     * 优先级（0-普通、1-紧急）
     */
    private Integer priority;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 流程类型id
     */
    private Long flowTypeId;
}
