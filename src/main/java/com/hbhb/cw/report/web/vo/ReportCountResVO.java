package com.hbhb.cw.report.web.vo;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzc
 * @since 2021-01-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportCountResVO implements Serializable {

    private static final long serialVersionUID = 2341750788509347072L;

    private List<ReportResVO> list;

    private Integer total;
}
