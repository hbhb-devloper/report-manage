package com.hbhb.cw.report.service.impl;

import com.hbhb.core.utils.DateUtil;
import com.hbhb.cw.report.enums.NoticeState;
import com.hbhb.cw.report.enums.NoticeType;
import com.hbhb.cw.report.mapper.ReportNoticeMapper;
import com.hbhb.cw.report.model.ReportNotice;
import com.hbhb.cw.report.rpc.FlowTypeApiExp;
import com.hbhb.cw.report.rpc.SysDictApiExp;
import com.hbhb.cw.report.rpc.SysUserApiExp;
import com.hbhb.cw.report.rpc.UnitApiExp;
import com.hbhb.cw.report.service.ReportNoticeService;
import com.hbhb.cw.report.web.vo.NoticeReqVO;
import com.hbhb.cw.report.web.vo.NoticeResVO;
import com.hbhb.cw.report.web.vo.NoticeVO;
import com.hbhb.cw.report.web.vo.ReportNoticeVO;
import com.hbhb.cw.systemcenter.enums.DictCode;
import com.hbhb.cw.systemcenter.enums.TypeCode;
import com.hbhb.cw.systemcenter.vo.DictVO;

import org.beetl.sql.core.page.DefaultPageRequest;
import org.beetl.sql.core.page.PageRequest;
import org.beetl.sql.core.page.PageResult;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

/**
 * @author yzc
 * @since 2021-01-18
 */
@Service
@Slf4j
public class ReportNoticeServiceImpl implements ReportNoticeService {

    @Resource
    private ReportNoticeMapper noticeMapper;
    @Resource
    private SysUserApiExp userApi;
    @Resource
    private SysDictApiExp dictApi;
    @Resource
    private FlowTypeApiExp typeApi;
    @Resource
    private UnitApiExp unitApi;

    @Override
    public void addReportNotice(ReportNoticeVO build) {
        ReportNotice notice = new ReportNotice();
        BeanUtils.copyProperties(build, notice);
        noticeMapper.insert(notice);
    }

    @Override
    public void changeNoticeState(Long id) {
        ReportNotice printNotice = new ReportNotice();
        printNotice.setId(id);
        printNotice.setState(1);
        noticeMapper.updateTemplateById(printNotice);
    }

    @Override
    public PageResult<NoticeResVO> pageReportNotice(NoticeReqVO noticeVo, Integer pageNum, Integer pageSize) {
        PageRequest request = DefaultPageRequest.of(pageNum, pageSize);
        PageResult<NoticeResVO> page = noticeMapper.selectPageByCond(noticeVo, request);
        // 项目状态字典
        List<DictVO> stateList = dictApi.getDict(TypeCode.FUND.value(), DictCode.FUND_INVOICE_STATUS.value());
        Map<String, String> stateMap = stateList.stream().collect(
                Collectors.toMap(DictVO::getValue, DictVO::getLabel));

        // 组装
        page.getList().forEach(item -> {
            item.setStateLabel(stateMap.get(item.getState().toString()));
            item.setFlowType(typeApi.getNameById(item.getFlowTypeId()));
            item.setUnitName(unitApi.getUnitInfo(item.getUnitId()).getUnitName());
        });
        return page;
    }

    @Override
    public List<NoticeVO> listReportNotice(Integer userId) {
        List<ReportNotice> list = noticeMapper.createLambdaQuery()
                .andEq(ReportNotice::getReceiver, userId)
                .andEq(ReportNotice::getState, NoticeState.UN_READ.value())
                .desc(ReportNotice::getCreateTime)
                .select();
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        List<Integer> userIds = list.stream().map(ReportNotice::getPromoter).collect(Collectors.toList());
        Map<Integer, String> userMap = userApi.getUserMapById(userIds);
        return list.stream()
                .map(notice -> NoticeVO.builder()
                        .id(notice.getId())
                        .content(notice.getContent())
                        .businessId(notice.getReportId())
                        .date(DateUtil.dateToString(notice.getCreateTime()))
                        .userName(userMap.get(notice.getPromoter()))
                        .noticeType(NoticeType.PRINT.value())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public Long countNotice(Integer userId) {
        return noticeMapper.createLambdaQuery()
                .andEq(ReportNotice::getReceiver, userId)
                .andEq(ReportNotice::getState, NoticeState.UN_READ.value())
                .count();
    }
}
