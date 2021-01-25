package com.hbhb.cw.report.service.impl;

import com.hbhb.api.core.bean.SelectVO;
import com.hbhb.core.bean.BeanConverter;
import com.hbhb.cw.report.mapper.ReportManageMapper;
import com.hbhb.cw.report.model.ReportManage;
import com.hbhb.cw.report.rpc.SysUserApiExp;
import com.hbhb.cw.report.service.ManageService;
import com.hbhb.cw.report.web.vo.ManageResVO;
import com.hbhb.cw.systemcenter.vo.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author wangxiaogang
 */
@Service
@Slf4j
public class ManageServiceImpl implements ManageService {
    @Resource
    private ReportManageMapper manageMapper;
    @Resource
    private SysUserApiExp userApi;

    @Override
    public List<ManageResVO> getManageList() {
        List<ReportManage> select = manageMapper.createLambdaQuery().select();
        return BeanConverter.copyBeanList(select, ManageResVO.class);
    }

    @Override
    public void updateManage(ManageResVO resVO, Integer userId) {
        UserInfo user = userApi.getUserInfoById(userId);
        ReportManage build = ReportManage.builder()
                .id(resVO.getId())
                .hasEnable(resVO.getHasEnable())
                .hasPack(resVO.getHasPack())
                .manageName(resVO.getManageName())
                .remark(resVO.getRemark())
                .updateBy(user.getNickName())
                .updateTime(new Date())
                .build();
        manageMapper.updateTemplateById(build);

    }

    @Override
    public void saveManage(ManageResVO resVO, Integer userId) {
        UserInfo user = userApi.getUserInfoById(userId);
        ReportManage build = ReportManage.builder()
                .id(resVO.getId())
                .hasEnable(resVO.getHasEnable())
                .hasPack(resVO.getHasPack())
                .manageName(resVO.getManageName())
                .remark(resVO.getRemark())
                .updateBy(user.getNickName())
                .updateTime(new Date())
                .build();
        manageMapper.insert(build);
    }

    @Override
    public List<SelectVO> getManageName() {
        List<ReportManage> select = manageMapper.createLambdaQuery()
                .andEq(ReportManage::getHasEnable, true)
                .select();
        return Optional.ofNullable(select)
                .orElse(new ArrayList<>())
                .stream()
                .map(item -> SelectVO.builder()
                        .id(item.getId())
                        .label(item.getManageName())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<ReportManage> getManageInfo(Long id) {
        return manageMapper.createLambdaQuery()
                .andEq(ReportManage::getHasEnable, true)
                .single();
    }
}
