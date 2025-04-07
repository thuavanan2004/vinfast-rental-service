package com.vinfast.rental_service.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendResetPasswordEmail(String emailTo) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(emailTo);
            helper.setSubject("Đặt lại mật khẩu của bạn");
            helper.setText(
                    "<p>Đội ngũ hỗ trợ sẽ sớm liên hệ tới bạn trong thời gian sớm nhất!</p>",
                    true // Kích hoạt HTML trong email
            );
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Lỗi gửi email", e);
        }
    }
}
