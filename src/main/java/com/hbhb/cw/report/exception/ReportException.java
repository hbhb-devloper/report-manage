package com.hbhb.cw.report.exception;

import com.hbhb.core.bean.MessageConvert;
import com.hbhb.cw.report.enums.ReportErrorCode;
import com.hbhb.web.exception.BusinessException;
import lombok.Getter;

/**
 * @author wangxiaogang
 */
@Getter
public class ReportException extends BusinessException {

    private final String code;

    public ReportException(ReportErrorCode code) {
        super(code.getCode(), MessageConvert.convert(code.getMessage()));
        this.code = code.getCode();
    }
}
