package com.hbhb.cw.report.web.vo;

import com.alibaba.excel.annotation.ExcelProperty;
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
@HeadRowHeight(0)
@ContentRowHeight(50)
public class UserImageVO implements Serializable {

    private static final long serialVersionUID = 1695798118204835105L;

    /**
     * 根据url导出
     *
     * @since 2.1.1
     */
    @ExcelProperty(value = "角色名称", index = 0)
    private String firstName;

    @ExcelProperty(value = "签名", index = 1)
    private URL url1;

    @ExcelProperty(value = "角色名称", index = 3)
    private String secondName;

    @ExcelProperty(value = "签名", index = 4)
    private URL url2;

    @ExcelProperty(value = "角色名称", index = 6)
    private String thirdName;

    @ExcelProperty(value = "签名", index = 7)
    private URL url3;

    @ExcelProperty(value = "角色名称", index = 9)
    private String fourthName;

    @ExcelProperty(value = "签名", index = 10)
    private URL url4;

    @ExcelProperty(value = "角色名称", index = 12)
    private String fifthName;

    @ExcelProperty(value = "签名", index = 13)
    private URL url5;
}
