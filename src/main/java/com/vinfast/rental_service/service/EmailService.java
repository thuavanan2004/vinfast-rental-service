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

    public void replySupport(String emailTo) {
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

    public void sendAdminSupportInfo(String emailTo, String adminName, String adminEmail, String adminPhone) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(emailTo);
            helper.setSubject("Thông tin hỗ trợ từ Admin");
            String content = "<p>Xin chào,</p>"
                    + "<p>Dưới đây là thông tin admin hỗ trợ của chúng tôi:</p>"
                    + "<p><strong>Tên:</strong> " + adminName + "</p>"
                    + "<p><strong>Email:</strong> " + adminEmail + "</p>"
                    + "<p><strong>Số điện thoại:</strong> " + adminPhone + "</p>"
                    + "<p>Vui lòng liên hệ trực tiếp với admin nếu bạn cần hỗ trợ thêm.</p>"
                    + "<p>Trân trọng,</p>"
                    + "<p>Đội ngũ Hỗ trợ</p>";
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Lỗi gửi email", e);
        }
    }

}
