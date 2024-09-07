package org.example.smartgarage.events.listeners;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.example.smartgarage.events.PasswordResetCompleteEvent;
import org.example.smartgarage.models.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
public class PasswordResetCompleteEventListener {
    private final JavaMailSender mailSender;
    private UserEntity user;
    @Value("${email.sender.email}")
    private String senderEmail;

    public PasswordResetCompleteEventListener(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @EventListener
    public void sendResetPasswordCompletedEmail(PasswordResetCompleteEvent event) {
        user = event.user();
        String loginUrl = event.url() + "/garage/login";

        try {
            sendEmail(loginUrl);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendEmail(String loginUrl) throws MessagingException, UnsupportedEncodingException {
        String subject = "Forgotten password reset completed";
        String senderName = "A60 Team 3 Smart Garage App";
        String mailContent = "<p> Greetings, Mr./Mrs. " + String.format("%s %s", user.getFirstName(), user.getLastName()) + ", </p>" +
                "<p>Password successfully updated.</p>" +
                "<br/><br/>" +
                "<p>You can login from here: <a href=\"" + loginUrl + "\">Login now</a>" +
                "<br/><br/>" +
                "<p> Thank you <br> A60 Team 3 Smart Garage App";
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
        messageHelper.setFrom(senderEmail, senderName);
        messageHelper.setTo(user.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);

        mailSender.send(message);
    }
}
