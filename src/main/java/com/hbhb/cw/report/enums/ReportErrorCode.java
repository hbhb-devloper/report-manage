package com.hbhb.cw.report.enums;

import lombok.Getter;

/**
 * @author wangxiaogang
 */
@Getter
public enum ReportErrorCode {
    // 该类型不存在流程
    NOT_EXIST_FLOW("86002", "not.exist.flow"),
    // 该类型下的流程超过限定流程
    EXCEED_LIMIT_FLOW("86003", "exceed.limit.flow"),
    // 没有对应的流程
    LACK_OF_FLOW("86004", "lack.of.flow"),
    // 没有对应的节点属性
    LACK_OF_NODE_PROP("86005", "lack.of.node.prop"),
    // 没有权限发起流程
    LACK_OF_FLOW_ROLE("86007", "lack.of.flow.role"),
    // 没有审批权限
    LOCK_OF_APPROVAL_ROLE("86008", "lock.of.approval.role"),
    // 请指定所有审批人
    NOT_ALL_APPROVERS_ASSIGNED("86009", "not.all.approvers.assigned"),
    // 下一节点未指定审批人，请联系管理员
    NEXT_NODE_NO_USER("86010", "next.node.no.user"),;

    private String code;

    private String message;

    ReportErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
