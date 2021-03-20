package com.hbhb.cw.report.web.vo;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzc
 * @since 2021-01-12
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportFileVO implements Serializable {
    private static final long serialVersionUID = 3369309107160016536L;

    private Long id;

    @Schema(description = "签报id")
    private Long reportId;

    @Schema(description = "附件id")
    private Long fileId;

    @Schema(description = "文件名称")
    private String fileName;

    @Schema(description = "文件路径")
    private String filePath;

    @Schema(description = "文件大小")
    private String fileSize;

    @Schema(description = "是否为必传")
    private Boolean required;

    @Schema(description = "作者")
    private String author;

    @Schema(description = "sheet熟料")
    private Integer sheetNum;

    @Schema(description = "创建时间")
    private String createTime;
}
