package com.hbhb.cw.report.service;

import com.hbhb.cw.flowcenter.vo.FlowApproveVO;
import com.hbhb.cw.flowcenter.vo.FlowWrapperVO;

/**
 * @author yzc
 * @since 2021-01-12
 */
public interface ReportFlowService {

    /**
     * 节点列表
     */
    FlowWrapperVO getReportNodeList(Long materialsId, Integer userId);

    /**
     * 审批
     *
     * @param vo     条件
     * @param userId id
     */
    void approve(FlowApproveVO vo, Integer userId);
}
