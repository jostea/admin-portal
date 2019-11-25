package com.internship.adminpanel.service;

import com.internship.adminpanel.config.NotificationConfiguration;
import com.internship.adminpanel.model.dto.users.UserDTOFromUI;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Data
@AllArgsConstructor
@Service
public class NotificationService {
    private NotificationConfiguration notificationConfiguration;

    public void sendMessage(UserDTOFromUI recipient, String password) {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(notificationConfiguration.getHost());
        javaMailSender.setUsername(notificationConfiguration.getUsername());
        javaMailSender.setPassword(notificationConfiguration.getPassword());
        javaMailSender.setHost(notificationConfiguration.getHost());
        Properties props = javaMailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(recipient.getEmail());
        mailMessage.setSubject("Your Credentials for Quiz Admin Panel");
        mailMessage.setText("Username: " + recipient.getUsername() + "\n" +
                "Password: " + password);
        javaMailSender.send(mailMessage);
    }
}
