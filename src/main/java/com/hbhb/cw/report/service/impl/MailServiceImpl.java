package com.hbhb.cw.report.service.impl;

import com.hbhb.cw.messagehub.vo.MailVO;
import com.hbhb.cw.report.rpc.MailApiExp;
import com.hbhb.cw.report.service.MailService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

/**
 * @author yzc
 * @since 2021-01-13
 */
@Service
@Slf4j
public class MailServiceImpl implements MailService {
    @Resource
    private MailApiExp mailApi;

    @Value("${mail.title}")
    private String title;
    @Value("${mail.content}")
    private String content;

    @Override
    public void postMail(String receiver, String name, String flowName) {
        mailApi.postMail(MailVO.builder()
                .receiver(receiver)
                .title(String.format(title, flowName))
                .content(String.format(content, name, flowName))
                .build());
    }
}
