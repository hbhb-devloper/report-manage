package com.hbhb.cw.report.model;

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
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportFile implements Serializable {
    private static final long serialVersionUID = 7307601269482989164L;
    /**
     * id（主键）
     */
    private Long id;
    /**
     * 分公司报表id
     */
    private Long reportId;
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 是否是必传（0-非必传，1-必传）
     */
    private Integer required;
    /**
     * 上传时间
     */
    private Date createTime;
    /**
     * 上传人
     */
    private String createBy;
    /**
     * 文件id
     */
    private Integer fileId;
}
