package com.qdone.common.util.mail;

/**
 * @author 付为地
 *  邮件工具类
 */
public interface MailService {
    /*简单邮件*/
    public void sendSimpleMail(String to, String subject, String content);
    /*html邮件*/
    public void sendHtmlMail(String to, String subject, String content);
    /*带附件的邮件*/
    public void sendAttachmentsMail(String to, String subject, String content, String filePath);
    /*带图片的邮件*/
    public void sendInlineResourceMail(String to, String subject, String content, String rscPath, String rscId);
}
