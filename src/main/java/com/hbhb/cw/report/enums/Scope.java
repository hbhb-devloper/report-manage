package com.hbhb.cw.report.enums;

/**
 * @author wangxiaogang
 */

public enum Scope {
    //分公司
    FILIALE(0, "分公司"),

    // 营业厅
    HALL(1, "营业厅");


    private final Integer key;
    private final String value;

    Scope(Integer key, String value) {
        this.value = value;
        this.key = key;
    }

    public Integer key() {
        return this.key;
    }

    public String value() {
        return this.value;
    }
}
