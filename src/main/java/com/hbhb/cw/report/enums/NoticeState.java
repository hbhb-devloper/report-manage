package com.hbhb.cw.report.enums;

public enum NoticeState {

    /**
     * 未读
     */
    UN_READ(0),
    /**
     * 已读
     */
    READ(1),

    ;

    private final Integer value;

    NoticeState(Integer value) {
        this.value = value;
    }

    public Integer value() {
        return this.value;
    }
}
