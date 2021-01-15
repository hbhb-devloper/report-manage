package com.hbhb.cw.report.web.vo;

import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;

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
@ContentRowHeight(50)
@ColumnWidth(30)
@HeadRowHeight(0)
public class UserImageVO implements Serializable {

    private static final long serialVersionUID = 1695798118204835105L;

    /**
     * 根据url导出
     *
     * @since 2.1.1
     */
    private String firstName;

    private URL url1;

    private String secondName;

    private URL url2;

    private String thirdName;

    private URL url3;

    private String fourthName;

    private URL url4;

    private String fifthName;

    private URL url5;
}
