package com.hbhb.cw.report.service.impl;

import com.hbhb.core.utils.DateUtil;
import com.hbhb.cw.flowcenter.enums.FlowNodeNoticeState;
import com.hbhb.cw.flowcenter.enums.FlowNodeNoticeTemp;
import com.hbhb.cw.flowcenter.model.Flow;
import com.hbhb.cw.flowcenter.vo.FlowNodePropVO;
import com.hbhb.cw.report.enums.NodeState;
import com.hbhb.cw.report.enums.OperationState;
import com.hbhb.cw.report.enums.ReportErrorCode;
import com.hbhb.cw.report.exception.ReportException;
import com.hbhb.cw.report.mapper.ReportCategoryMapper;
import com.hbhb.cw.report.mapper.ReportFileMapper;
import com.hbhb.cw.report.mapper.ReportFlowMapper;
import com.hbhb.cw.report.mapper.ReportManageMapper;
import com.hbhb.cw.report.mapper.ReportMapper;
import com.hbhb.cw.report.mapper.ReportNoticeMapper;
import com.hbhb.cw.report.model.Report;
import com.hbhb.cw.report.model.ReportCategory;
import com.hbhb.cw.report.model.ReportFile;
import com.hbhb.cw.report.model.ReportFlow;
import com.hbhb.cw.report.model.ReportManage;
import com.hbhb.cw.report.model.ReportNotice;
import com.hbhb.cw.report.rpc.FileApiExp;
import com.hbhb.cw.report.rpc.FlowApiExp;
import com.hbhb.cw.report.rpc.FlowNodeApiExp;
import com.hbhb.cw.report.rpc.FlowNodePropApiExp;
import com.hbhb.cw.report.rpc.FlowNoticeApiExp;
import com.hbhb.cw.report.rpc.FlowRoleUserApiExp;
import com.hbhb.cw.report.rpc.SysUserApiExp;
import com.hbhb.cw.report.rpc.UnitApiExp;
import com.hbhb.cw.report.service.ReportService;
import com.hbhb.cw.report.web.vo.ExcelInfoVO;
import com.hbhb.cw.report.web.vo.ReportFileVO;
import com.hbhb.cw.report.web.vo.ReportInitVO;
import com.hbhb.cw.report.web.vo.ReportReqVO;
import com.hbhb.cw.report.web.vo.ReportResVO;
import com.hbhb.cw.report.web.vo.ReportVO;
import com.hbhb.cw.report.web.vo.UserImageVO;
import com.hbhb.cw.systemcenter.enums.UnitEnum;
import com.hbhb.cw.systemcenter.vo.UserInfo;

import org.beetl.sql.core.page.DefaultPageRequest;
import org.beetl.sql.core.page.PageRequest;
import org.beetl.sql.core.page.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wangxiaogang
 */
@Service
@Slf4j
@SuppressWarnings(value = {"unchecked"})
public class ReportServiceImpl implements ReportService {

    @Resource
    private ReportMapper reportMapper;
    @Resource
    private ReportFileMapper reportFileMapper;
    @Resource
    private SysUserApiExp sysUserApi;
    @Resource
    private FlowApiExp flowApi;
    @Resource
    private FlowNodeApiExp nodeApi;
    @Resource
    private FlowNodePropApiExp propApi;
    @Resource
    private FlowRoleUserApiExp roleUserApi;
    @Resource
    private UnitApiExp unitApi;
    @Resource
    private ReportFlowMapper reportFlowMapper;
    @Resource
    private FlowNoticeApiExp noticeApi;
    @Resource
    private ReportManageMapper reportManageMapper;
    @Resource
    private ReportCategoryMapper reportCategoryMapper;
    @Resource
    private ReportNoticeMapper reportNoticeMapper;
    @Resource
    private FileApiExp fileApiExp;

    @Override
    public PageResult<ReportResVO> getReportList(ReportReqVO reportReqVO, Integer pageNum, Integer pageSize) {
        PageRequest<ReportResVO> request = DefaultPageRequest.of(pageNum, pageSize);
        PageResult<ReportResVO> reportList = reportMapper.selectListByCond(request, reportReqVO);
        for (int i = 0; i < reportList.getList().size(); i++) {
            reportList.getList().get(i).setLineNumber(i + 1L);
        }
        return reportList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addReport(ReportVO reportVO, Integer userId) {
        Report report = Report.builder()
                .categoryId(reportVO.getCategoryId())
                .founder(userId)
                .createTime(new Date())
                .hallId(reportVO.getHallId())
                .hasBiz(reportVO.getHasBiz())
                .state(10)
                .manageId(reportVO.getManageId())
                .period(reportVO.getPeriod())
                .type(reportVO.getType())
                .launchTime(new Date())
                .build();
        reportMapper.insert(report);
        List<ReportFileVO> files = reportVO.getFiles();
        if (files.size() != 0) {
            List<ReportFile> reportFiles = new ArrayList<>();
            for (ReportFileVO file : files) {
                reportFiles.add(ReportFile.builder()
                        .createTime(DateUtil.stringToDate(file.getCreateTime()))
                        .fileName(file.getFileName())
                        .createBy(file.getAuthor())
                        .fileId(Math.toIntExact(file.getFileId()))
                        .reportId(report.getId())
                        .required(1)
                        .build());
            }
            reportFileMapper.insertBatch(reportFiles);
        }
        toApprover(ReportInitVO.builder()
                .flowTypeId(reportVO.getFlowTypeId())
                .reportId(report.getId())
                .userId(userId).build(), userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void toApprover(ReportInitVO initVO, Integer userId) {
        Report report = reportMapper.single(initVO.getReportId());
        //  1.判断登录用户是否与申请人一致
        UserInfo user = sysUserApi.getUserInfoById(userId);
        UserInfo userInfo = sysUserApi.getUserInfoById(report.getFounder());
        if (!user.getNickName().equals(userInfo.getNickName())) {
            throw new ReportException(ReportErrorCode.LOCK_OF_APPROVAL_ROLE);
        }
        //  3.获取流程id
        Long flowId = getRelatedFlow(initVO.getFlowTypeId());
        // 通过流程id得到流程节点属性
        List<FlowNodePropVO> flowProps = propApi.getNodeProps(flowId);
        //  4.校验用户发起审批权限
        boolean hasAccess = hasAccess2Approve(flowProps, user.getUnitId(), initVO.getUserId());
        if (!hasAccess) {
            throw new ReportException(ReportErrorCode.LOCK_OF_APPROVAL_ROLE);
        }
        //  5.同步节点属性
        syncReportFlow(flowProps, report.getId(), userId);
        // 得到推送模板
        String inform = noticeApi.getInform(flowProps.get(0).getFlowNodeId()
                , FlowNodeNoticeState.DEFAULT_REMINDER.value());
        if (inform == null) {
            return;
        }
        // 通过报表内容id和报表名称id得到关联单名称
        ReportManage reportManage = reportManageMapper.single(report.getManageId());
        ReportCategory reportCategory = reportCategoryMapper.single(report.getCategoryId());
        // 跟据流程id获取流程名称
        Flow flow = flowApi.getFlowById(flowId);
        inform = inform.replace(FlowNodeNoticeTemp.TITLE.value()
                , reportManage.getManageName() + reportCategory.getReportName()
                        + "_" + "_" + flow.getFlowName());
        reportNoticeMapper.insert(
                ReportNotice.builder()
                        .reportId(report.getId())
                        .receiver(initVO.getUserId())
                        .promoter(initVO.getUserId())
                        .content(inform)
                        .flowTypeId(initVO.getFlowTypeId())
                        .build());

        //  6.更改发票流程状态
        report.setId(initVO.getReportId());
        report.setState(NodeState.APPROVING.value());
        report.setFounder(initVO.getUserId());
        reportMapper.updateTemplateById(report);
    }

    @Override
    public ExcelInfoVO getExcelInfo(Long reportId) {
        // 通过id得到文件路径
        List<ReportFile> list = reportFileMapper.createLambdaQuery()
                .andEq(ReportFile::getReportId, reportId)
                .select();
        List<String> pathList = new ArrayList<>();
        List<String> nameList = new ArrayList<>();
        List<String> imageList = new ArrayList<>();
        for (ReportFile reportFile : list) {
            pathList.add(fileApiExp.getFileInfo(reportFile.getFileId()).getFilePath());
            nameList.add(reportFile.getFileName());
        }
        ExcelInfoVO excelInfoVO = new ExcelInfoVO();
//        excelInfoVO.setPathList(pathList);
//        excelInfoVO.setFileNameList(nameList);
        // 通过报表信息id得到节点信息和相关节点的审批用户
        List<ReportFlow> flowList = reportFlowMapper.createLambdaQuery()
                .andEq(ReportFlow::getReportId, reportId)
                .select();
        UserImageVO userImageVO = new UserImageVO();
        for (ReportFlow reportFlow : flowList) {
            if (reportFlow.getUserId() == null) {
                break;
            }
            imageList.add(reportFlow.getUserId().toString());
        }
        return null;
    }

    /**
     * 校验流程是否匹配
     */
    private Long getRelatedFlow(Long flowTypeId) {
        // 流程节点数量 => 流程id
        Map<Long, Long> flowMap = new HashMap<>(5);
        List<Flow> flowList = flowApi.getFlowsByTypeId(flowTypeId);
        // 流程有效性校验（物料制作流程存在1条）
        if (flowList.size() == 0) {
            throw new ReportException(ReportErrorCode.NOT_EXIST_FLOW);
        } else if (flowList.size() > 1) {
            throw new ReportException(ReportErrorCode.EXCEED_LIMIT_FLOW);
        }
        flowList.forEach(flow -> flowMap.put(nodeApi.getNodeNum(flow.getId()), flow.getId()));
        // 物料制作流程默认为4个节点流程
        Long flowId;
        flowId = flowMap.get(4L);
        if (flowId == null) {
            throw new ReportException(ReportErrorCode.LACK_OF_FLOW);
        }
        // 校验流程是否匹配，如果没有匹配的流程，则抛出提示
        return flowId;
    }

    /**
     * 判断角色发起权限
     */
    private boolean hasAccess2Approve(List<FlowNodePropVO> flowProps, Integer unitId, Integer userId) {
        List<Long> flowRoleIds = roleUserApi.getRoleIdByUserId(userId);
        // 第一个节点属性
        FlowNodePropVO firstNodeProp = flowProps.get(0);
        // 判断是有默认用户
        // 如果设定了默认用户，且为当前登录用户，则有发起权限
        if (firstNodeProp.getUserId() != null) {
            return firstNodeProp.getUserId().equals(userId);
        }
        // 如果没有设定默认用户，则通过流程角色判断
        else {
            // 如果角色范围为全杭州
            if (UnitEnum.HANGZHOU.value().equals(firstNodeProp.getUnitId()) || firstNodeProp.getUnitId().equals(0)) {
                return flowRoleIds.contains(firstNodeProp.getFlowRoleId());
            }
            // 如果角色范围为本部
            else if (UnitEnum.BENBU.value().equals(firstNodeProp.getUnitId())) {
                List<Integer> unitIds = unitApi.getSubUnit(UnitEnum.BENBU.value());
                return unitIds.contains(unitId) && flowRoleIds.contains(firstNodeProp.getFlowRoleId());
            }
            // 如果为确定某个单位
            else {
                // 必须单位和流程角色都匹配，才可判定为有发起权限
                return unitId.equals(firstNodeProp.getUnitId()) && flowRoleIds.contains(firstNodeProp.getFlowRoleId());
            }
        }
    }

    /**
     * 同步节点属性
     */
    private void syncReportFlow(List<FlowNodePropVO> flowProps, Long id, Integer userId) {

        // 用来存储同步节点的list
        List<ReportFlow> reportFlowList = new ArrayList<>();
        // 判断节点是否有保存属性
        for (FlowNodePropVO flowPropVO : flowProps) {
            if (flowPropVO.getIsJoin() == null || flowPropVO.getControlAccess() == null) {
                throw new ReportException(ReportErrorCode.LACK_OF_NODE_PROP);
            }
        }
        // 判断第一个节点是否有默认用户，如果没有则为当前用户
        if (flowProps.get(0).getUserId() == null) {
            flowProps.get(0).setUserId(userId);
        }
        // 所以需要先清空节点，再同步节点。
        reportFlowMapper.createLambdaQuery()
                .andEq(ReportFlow::getReportId, id)
                .delete();
        for (FlowNodePropVO flowProp : flowProps) {
            reportFlowList.add(ReportFlow.builder()
                    .flowNodeId(flowProp.getFlowNodeId())
                    .reportId(id)
                    .userId(flowProp.getUserId())
                    .flowRoleId(flowProp.getFlowRoleId())
                    .roleDesc(flowProp.getRoleDesc())
                    .controlAccess(flowProp.getControlAccess())
                    .isJoin(flowProp.getIsJoin())
                    .assigner(flowProp.getAssigner())
                    .operation(OperationState.UN_EXECUTED.value())
                    .createTime(new Date())
                    .build());
        }
        reportFlowMapper.insertBatch(reportFlowList);
    }
}
