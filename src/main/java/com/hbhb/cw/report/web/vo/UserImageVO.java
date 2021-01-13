package com.hbhb.cw.report.web.vo;

import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;

import java.io.Serializable;
import java.net.URL;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzc
 * @since 2021-01-13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ContentRowHeight(100)
@ColumnWidth(100 / 8)
public class UserImageVO implements Serializable {

    private static final long serialVersionUID = 1695798118204835105L;

    /**
     * 根据url导出
     *
     * @since 2.1.1
     */
    private URL url1;

    private URL url2;

    private URL url3;
}
