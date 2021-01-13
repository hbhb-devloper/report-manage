package com.hbhb.cw.report.service;

/**
 * @author yzc
 * @since 2021-01-13
 */
public interface MailService {

    /**
     * 发送邮件提醒
     *
     * @param receiver 接收人邮箱
     * @param name     接收人姓名
     * @param content  流程名称
     */
    void postMail(String receiver, String name, String content);
}
