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
import com.hbhb.cw.report.rpc.HallApiExp;
import com.hbhb.cw.report.rpc.SysDictApiExp;
import com.hbhb.cw.report.rpc.SysUserApiExp;
import com.hbhb.cw.report.rpc.UnitApiExp;
import com.hbhb.cw.report.service.PropertyService;
import com.hbhb.cw.report.service.ReportService;
import com.hbhb.cw.report.web.vo.PropertyCondVO;
import com.hbhb.cw.report.web.vo.PropertyReqVO;
import com.hbhb.cw.report.web.vo.ReportCountResVO;
import com.hbhb.cw.report.web.vo.ReportFileVO;
import com.hbhb.cw.report.web.vo.ReportInitVO;
import com.hbhb.cw.report.web.vo.ReportReqVO;
import com.hbhb.cw.report.web.vo.ReportResVO;
import com.hbhb.cw.report.web.vo.ReportVO;
import com.hbhb.cw.systemcenter.enums.DictCode;
import com.hbhb.cw.systemcenter.enums.TypeCode;
import com.hbhb.cw.systemcenter.enums.UnitEnum;
import com.hbhb.cw.systemcenter.model.SysFile;
import com.hbhb.cw.systemcenter.vo.DictVO;
import com.hbhb.cw.systemcenter.vo.UserInfo;

import org.beetl.sql.core.page.DefaultPageRequest;
import org.beetl.sql.core.page.PageRequest;
import org.beetl.sql.core.page.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    @Resource
    private PropertyService propertyService;
    @Resource
    private HallApiExp hallApi;
    @Resource
    private SysDictApiExp dictApi;

    @Override
    public PageResult<ReportResVO> getReportList(ReportReqVO reportReqVO, Integer pageNum, Integer pageSize) {
        reportReqVO.setType(reportReqVO.getHallId() == null ? 1 : 2);
        // 周期字典
        List<DictVO> dict = dictApi.getDict(TypeCode.REPORT.value(), DictCode.REPORT_PERIOD.value());
        Map<String, String> periodMap = dict.stream().collect(Collectors.toMap(DictVO::getValue, DictVO::getLabel));
        // 流程状态字典
        List<DictVO> stateDict = dictApi.getDict(TypeCode.REPORT.value(), DictCode.REPORT_APPROVER_STATE.value());
        Map<String, String> stateMap = stateDict.stream().collect(Collectors.toMap(DictVO::getValue, DictVO::getLabel));
        // 人物名称
        Map<Integer, String> userMap = sysUserApi.getUserByUnitIds(reportReqVO.getUnitId());
        PageRequest<ReportResVO> request = DefaultPageRequest.of(pageNum, pageSize);
        PageResult<ReportResVO> reportList = reportMapper.selectPageByCond(request, reportReqVO);
        for (int i = 0; i < reportList.getList().size(); i++) {
            reportList.getList().get(i).setLineNumber(i + 1L);
            reportList.getList().get(i).setPeriodName(periodMap.get(reportList.getList().get(i).getPeriod()));
            reportList.getList().get(i).setHasBizName(reportList.getList().get(i).getHasBiz() ? "有业务" : "无业务");
            reportList.getList().get(i).setStateName(stateMap.get(reportList.getList().get(i).getState()));
            reportList.getList().get(i).setFounderName(userMap.get(reportList.getList().get(i).getFounder()));
        }
        return reportList;
    }

    @Override
    public List<ReportFileVO> getReportInfo(Long reportId) {
        List<ReportFile> reportFiles = reportFileMapper.createLambdaQuery().andEq(ReportFile::getReportId, reportId).select();
        List<Integer> fileIds = new ArrayList<>();
        // fileId => ReportFile
        Map<Integer, ReportFile> map = new HashMap<>();
        for (ReportFile reportFile : reportFiles) {
            fileIds.add(reportFile.getFileId());
            map.put(reportFile.getFileId(), reportFile);
        }
        List<SysFile> fileList = fileApiExp.getFileInfoBatch(fileIds);
        List<ReportFileVO> reportFileVOList = new ArrayList<>();
        for (SysFile sysFile : fileList) {
            ReportFile reportFile = map.get(Math.toIntExact(sysFile.getId()));
            reportFileVOList.add(ReportFileVO.builder()
                    .createTime(DateUtil.dateToString(reportFile.getCreateTime()))
                    .fileName(reportFile.getFileName())
                    .fileSize(sysFile.getFileSize())
                    .author(reportFile.getCreateBy())
                    .fileId(Long.valueOf(reportFile.getFileId()))
                    .filePath(sysFile.getFilePath())
                    .reportId(reportId)
                    .required(true)
                    .build());
        }
        return reportFileVOList;
    }

    @Override
    public ReportCountResVO getReportCountList(ReportReqVO reportReqVO, Integer pageNum, Integer pageSize) {
        List<ReportResVO> list = getReportCount(reportReqVO);
        // 分页
        return ReportCountResVO.builder()
                .list(list.stream().sorted(Comparator.comparing(ReportResVO::getLineNumber))
                        .skip((pageNum) * (pageSize - 1)).limit(pageSize - 1).collect(Collectors.toList()))
                .total(list.size())
                .build();
    }

//    @Override
//    public List<ReportCountHallExportVO> getReportCountHallExcel(ReportReqVO reportReqVO) {
//        List<ReportResVO> list = getReportCount(reportReqVO);
//        return BeanConverter.copyBeanList(list, ReportCountHallExportVO.class);
//    }
//
//    @Override
//    public List<ReportCountUnitExportVO> getReportCountUnitExcel(ReportReqVO reportReqVO) {
//        List<ReportResVO> list = getReportCount(reportReqVO);
//        return BeanConverter.copyBeanList(list, ReportCountUnitExportVO.class);
//    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addReport(ReportVO reportVO, Integer userId) {
        UserInfo user = sysUserApi.getUserInfoById(userId);
        // 报表信息
        Report report = Report.builder()
                .categoryId(reportVO.getCategoryId())
                .unitId(reportVO.getUnitId())
                .founder(userId)
                .createTime(new Date())
                .hallId(reportVO.getHallId())
                .hasBiz(reportVO.getHasBiz())
                .state(10)
                .manageId(reportVO.getManageId())
                .period(reportVO.getPeriod())
                .periodInfo(reportVO.getPeriodInfo())
                .type(reportVO.getHallId() == null ? 1 : 2)
                .launchTime(reportVO.getLaunchTime())
                .build();
        List<Report> reportList = reportMapper.createLambdaQuery()
                .andEq(Report::getCategoryId, reportVO.getCategoryId())
                .andEq(Report::getFounder, userId)
                .andEq(Report::getHallId, reportVO.getHallId())
                .andEq(Report::getHasBiz, reportVO.getHasBiz())
                .andEq(Report::getPeriodInfo, reportVO.getPeriodInfo())
                .andEq(Report::getLaunchTime, reportVO.getLaunchTime())
                .andEq(Report::getManageId, reportVO.getManageId())
                .andEq(Report::getPeriod, reportVO.getPeriod())
                .andEq(Report::getType, reportVO.getHallId() == null ? 1 : 2)
                .select();
        if (reportList != null && reportList.size() != 0) {
            throw new ReportException(ReportErrorCode.EXCEED_LIMIT_FLOW);
        }
        reportMapper.insert(report);
        List<ReportFileVO> files = reportVO.getFiles();
        // 判断是否上传附件，新建附件
        if (files != null && files.size() != 0) {
            List<ReportFile> reportFiles = new ArrayList<>();
            for (ReportFileVO file : files) {
                reportFiles.add(ReportFile.builder()
                        .createTime(DateUtil.stringToDate(file.getCreateTime()))
                        .fileName(file.getFileName())
                        .createBy(user.getNickName())
                        .createTime(new Date())
                        .fileId(Math.toIntExact(file.getFileId()))
                        .reportId(report.getId())
                        .required(1)
                        .build());
            }
            reportFileMapper.insertBatch(reportFiles);
        }
        if (reportVO.getHasBiz()) {
            List<Long> flowIds = propertyService.getFlowId(PropertyCondVO.builder()
                    .categoryId(reportVO.getCategoryId())
                    .flowTypeId(40L)
                    .period(Integer.valueOf(reportVO.getPeriod()))
                    .scope(reportVO.getHallId() == null ? 1 : 2)
                    .build());
            if (flowIds == null || flowIds.size() != 1) {
                throw new ReportException(ReportErrorCode.EXCEED_LIMIT_FLOW);
            }
            toApprover(ReportInitVO.builder()
                    .flowId(flowIds.get(0))
                    .reportId(report.getId())
                    .userId(userId).build());
        } else {
            reportMapper.createLambdaQuery().updateSelective(Report.builder()
                    .id(report.getId())
                    .state(20).build());
        }
    }

    //    @Override
    @Transactional(rollbackFor = Exception.class)
    public void toApprover(ReportInitVO initVO) {
        Report report = reportMapper.single(initVO.getReportId());
        //  1.判断登录用户是否与申请人一致
        UserInfo user = sysUserApi.getUserInfoById(initVO.getUserId());
        UserInfo userInfo = sysUserApi.getUserInfoById(report.getFounder());
        if (!user.getNickName().equals(userInfo.getNickName())) {
            throw new ReportException(ReportErrorCode.LOCK_OF_APPROVAL_ROLE);
        }
        //  3.获取流程id
        Long flowId = initVO.getFlowId();
        // 通过流程id得到流程节点属性
        List<FlowNodePropVO> flowProps = propApi.getNodeProps(flowId);
        //  4.校验用户发起审批权限
        boolean hasAccess = hasAccess2Approve(flowProps, user.getUnitId(), initVO.getUserId());
        if (!hasAccess) {
            throw new ReportException(ReportErrorCode.LOCK_OF_APPROVAL_ROLE);
        }
        //  5.同步节点属性
        syncReportFlow(flowProps, report.getId(), initVO.getUserId());
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
                        .flowTypeId(flow.getFlowTypeId())
                        .build());
        //  6.更改报表流程状态
        report.setId(initVO.getReportId());
        report.setState(NodeState.APPROVING.value());
        report.setFounder(initVO.getUserId());
        reportMapper.updateTemplateById(report);
    }

//    @Override
//    public ExcelInfoVO getExcelInfo(Integer fileId, Long reportId) {
//        // 通过fileId得到file路径
//        SysFile fileInfo = fileApiExp.getFileInfo(fileId);
//        ExcelInfoVO excelInfoVO = new ExcelInfoVO();
//        String filePath = fileInfo.getFilePath();
//        String templatePath = "C:/Users/cn/Desktop/ppp";
//        filePath = templatePath + File.separator + "增值税专票模板.xlsx";
//        if (filePath == null) {
//            throw new ReportException(ReportErrorCode.LOCK_OF_APPROVAL_ROLE);
//        }
//        // 判断是否为excel
//        String excel = filePath.substring(filePath.lastIndexOf("."));
//        if (!".xlsx".equals(excel) && !".xls".equals(excel)) {
//            throw new ReportException(ReportErrorCode.LOCK_OF_APPROVAL_ROLE);
//        }
//        excelInfoVO.setPath(filePath);
//        // 通过fileId得到reportFile的fileName
////        List<ReportFile> reportFileList = reportFileMapper.createLambdaQuery()
////                .andEq(ReportFile::getReportId, reportId)
////                .select();
////        if (reportFileList.size() == 0) {
////            throw new ReportException(ReportErrorCode.LOCK_OF_APPROVAL_ROLE);
////        }
////        excelInfoVO.setFileName(reportFileList.get(0).getFileName());
//        excelInfoVO.setFileName("lall");
//        // 通过报表信息id得到节点信息和相关节点的审批用户
//        List<ReportFlow> flowList = reportFlowMapper.createLambdaQuery()
//                .andEq(ReportFlow::getReportId, reportId)
//                .select();
//        // userId => image
//        Map<Integer, String> map = sysUserApi.getUserSignature(null);
////        Map<Integer, String> map = new HashMap<>();
////        map.put(27,"https://zhcw.file.iishoni.com/test/QQ图片20200909100351(1).jpg");
////        map.put(579,"https://zhcw.file.iishoni.com/test/QQ图片20200909100351(1).jpg");
//        UserImageVO userImageVO = new UserImageVO();
//        // 获取图片赋值
//        if (flowList.size() != 0) {
//            userImageVO.setFirstName(flowList.get(0).getRoleDesc());
//            try {
//                userImageVO.setUrl1(new URL(map.get(flowList.get(0).getUserId())));
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            }
//        }
//        if (flowList.size() >= 2) {
//            userImageVO.setSecondName(flowList.get(1).getRoleDesc());
//            try {
//                userImageVO.setUrl2(new URL(map.get(flowList.get(1).getUserId())));
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            }
//        }
//        if (flowList.size() >= 3) {
//            userImageVO.setThirdName(flowList.get(2).getRoleDesc());
//            try {
//                userImageVO.setUrl3(new URL(map.get(flowList.get(2).getUserId())));
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            }
//        }
//        if (flowList.size() >= 4) {
//            userImageVO.setFourthName(flowList.get(3).getRoleDesc());
//            try {
//                userImageVO.setUrl4(new URL(map.get(flowList.get(3).getUserId())));
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            }
//        }
//        if (flowList.size() >= 5) {
//            userImageVO.setFifthName(flowList.get(4).getRoleDesc());
//            try {
//                userImageVO.setUrl5(new URL(map.get(flowList.get(4).getUserId())));
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            }
//        }
//        excelInfoVO.setImageVO(userImageVO);
//        return excelInfoVO;
//    }

    @Override
    public void moveReportList(List<Long> list) {
        reportMapper.createLambdaQuery().andIn(Report::getId, list).delete();
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

    /**
     * 通过条件得到上传统计list
     */
    private List<ReportResVO> getReportCount(ReportReqVO reportReqVO) {
        reportReqVO.setType(reportReqVO.getHallId() == null ? 1 : 2);
        // 得到所有营业厅
        Map<Integer, String> hallMap = hallApi.selectHallByUnitId(reportReqVO.getUnitId());
        // 得到报表内容详情
        ReportManage manage = reportManageMapper.single(reportReqVO.getManageId());
        // 得到报表名称详情
        ReportCategory category = reportCategoryMapper.single(reportReqVO.getCategoryId());
        // 获取所有单位
        List<Integer> unitIds = unitApi.getAllUnitId();
        Map<Integer, String> unitMap = unitApi.getUnitMapById();

        List<ReportResVO> list = reportMapper.selectListByCond(reportReqVO);
        ;
        // 如果为单位
        if (reportReqVO.getHallId() == null) {
            // unitId => ReportResVO
            Map<Integer, ReportResVO> map = list.stream()
                    .collect(Collectors.toMap(ReportResVO::getUnitId, Function.identity()));
            // 判断是否通过
            for (Integer unitId : unitIds) {
                if (map.get(unitId) == null) {
                    list.add(ReportResVO.builder()
                            .manageName(manage.getManageName())
                            .reportName(category.getReportName())
                            .relationName(manage.getManageName() + category.getReportName())
                            .unitId(unitId)
                            .hasBiz(reportReqVO.getHasBiz())
                            .period(reportReqVO.getPeriod())
                            .build());
                }
            }
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setLineNumber(i + 1L);
                list.get(i).setUnitName(unitMap.get(list.get(i).getUnitId()));
            }
            // 如果为营业厅
        } else {
            // hallId => ReportResVO
            Map<Long, ReportResVO> map = list.stream()
                    .collect(Collectors.toMap(ReportResVO::getHallId, Function.identity()));
            // 判断是否通过
            for (Integer hallId : hallMap.keySet()) {
                if (map.get(Long.valueOf(hallId)) == null) {
                    list.add(ReportResVO.builder()
                            .manageName(manage.getManageName())
                            .reportName(category.getReportName())
                            .relationName(manage.getManageName() + category.getReportName())
                            .hasBiz(reportReqVO.getHasBiz())
                            .hallId(Long.valueOf(hallId))
                            .period(reportReqVO.getPeriod())
                            .build());
                }
            }
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setLineNumber(i + 1L);
                list.get(i).setHallName(hallMap.get(Math.toIntExact(list.get(i).getHallId())));
            }
        }
        // 周期字典
        List<DictVO> dict = dictApi.getDict(TypeCode.REPORT.value(), DictCode.REPORT_PERIOD.value());
        Map<String, String> periodMap = dict.stream().collect(Collectors.toMap(DictVO::getValue, DictVO::getLabel));
        // 流程状态字典
        List<DictVO> stateDict = dictApi.getDict(TypeCode.REPORT.value(), DictCode.REPORT_APPROVER_STATE.value());
        Map<String, String> stateMap = stateDict.stream().collect(Collectors.toMap(DictVO::getValue, DictVO::getLabel));
        // 人物名称
        Map<Integer, String> userMap = sysUserApi.getUserByUnitIds(reportReqVO.getUnitId());
        // 通过报表名称id得到开始结束时间
        List<PropertyReqVO> propertyReqVOList = propertyService.getStartTime(PropertyCondVO.builder()
                .categoryId(reportReqVO.getCategoryId())
                .flowTypeId(40L)
                .period(Integer.valueOf(reportReqVO.getPeriod()))
                .scope(reportReqVO.getHallId() == null ? 1 : 2)
                .build());
        if (propertyReqVOList == null || propertyReqVOList.size() != 1) {
            return new ArrayList<>();
        }
        // 判断报表状态
        PropertyReqVO propertyReqVO = propertyReqVOList.get(0);
//        if (category.getState()) {
//            for (ReportResVO reportResVO : list) {
//                if (reportResVO.getState() != null && DateUtil.isExpired(propertyReqVO.getStartTime(),
//                        propertyReqVO.getEndTime(), DateUtil.dateToString(new Date()))) {
//                    reportResVO.setStateName("未提交");
//                } else {
//                    reportResVO.setStateName("已过期");
//                }
//            }
//        } else {
//            for (ReportResVO reportResVO : list) {
//                if (reportResVO.getState() != null) {
//                    reportResVO.setStateName("未启用");
//                }
//            }
//        }
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setLineNumber(i + 1L);
            list.get(i).setPeriodName(periodMap.get(list.get(i).getPeriod()));
            if (list.get(i).getStateName() == null) {
                list.get(i).setStateName(stateMap.get(list.get(i).getState()));
            }
            list.get(i).setFounderName(userMap.get(list.get(i).getFounder()));
        }
        return list;
    }
}
