package com.hbhb.cw.report.enums;

/**
 * @author wangxiaogang
 */

public enum NoticeType {
    /**
     * 报表
     */
    REPORT(1, "报表管理");
    private final Integer key;
    private final String value;

    public Integer key() {
        return this.key;
    }

    public String value() {
        return this.value;
    }

    NoticeType(Integer key, String value) {
        this.key = key;
        this.value = value;
    }
}
