package com.phdhuy.stock_alert.infrastructure.external.notification.adapter;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailSenderAdapter {

  private final JavaMailSender javaMailSender;

  private final TemplateEngine templateEngine;

  @Async("asyncExecutor")
  public void sendEmail(String to, String subject, String templateName, Map<String, Object> vars) {
    try {
      Context context = new Context();
      context.setVariables(vars);
      String htmlContent = templateEngine.process(templateName, context);

      MimeMessage mimeMessage = javaMailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(htmlContent, true);

      javaMailSender.send(mimeMessage);
    } catch (MessagingException e) {
      log.error("Error while sending email: {}", e.getMessage());
    }
  }
}
