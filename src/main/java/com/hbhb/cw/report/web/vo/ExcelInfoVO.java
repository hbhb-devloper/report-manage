package com.hbhb.cw.report.web.vo;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzc
 * @since 2021-01-13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExcelInfoVO implements Serializable {
    private static final long serialVersionUID = 7230656804451100381L;

    @Schema(description = "文件路径列表")
    private String path;

    @Schema(description = "名称列表")
    private String fileName;

    @Schema(description = "图片列表")
    private UserImageVO imageVO;
}
