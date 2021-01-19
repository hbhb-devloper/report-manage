package com.hbhb.cw.report.enums;

/**
 * @author wangxiaogang
 */

public enum NoticeType {
    /**
     * 物料制作提醒
     */
    MATERIALS(1, "物料制作"),
    /**
     * 印刷用品提醒
     */
    PRINT(2, "印刷品"),
    /**
     * 画面设计提醒
     */
    PICTURE(3, "画面设计"),
    /**
     * 画面设计提醒
     */
    GOODS(4, "物料费用签报"),
    /**
     * 画面设计提醒
     */
    CHECKER(4, "物料审核");
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
