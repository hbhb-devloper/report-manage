package com.hbhb.cw.report.enums;

/**
 * @author wangxiaogang
 */

public enum Suggestion {
    /**
     * 同意
     */
    AGREE("{approve}已同意{title}，请查阅！"),
    /**
     * 拒绝
     */
    REFUSE("{title}被{approve}否决，原因是{cause}，请修改相关信息，重新开始审批！"),
    ;

    public String getValue() {
        return value;
    }

    private final String value;

    Suggestion(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
