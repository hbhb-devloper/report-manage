package com.hbhb.cw.report.web.vo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzc
 * @since 2021-01-22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExcelVO implements Serializable {
    private static final long serialVersionUID = 385638749503377712L;

    private Integer fileId;

    private  Long reportId;
}
