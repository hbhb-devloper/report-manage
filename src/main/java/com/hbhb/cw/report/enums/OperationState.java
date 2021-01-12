package com.hbhb.cw.report.enums;

/**
 * @author yzc
 * @since 2020-12-03
 */
public enum OperationState {
    /**
     * 拒绝
     */
    REJECT(0),

    /**
     * 同意
     */
    AGREE(1),

    /**
     * 未执行
     */
    UN_EXECUTED(2),
    ;

    private final Integer value;

    OperationState(Integer value) {
        this.value = value;
    }

    public Integer value() {
        return this.value;
    }
}

