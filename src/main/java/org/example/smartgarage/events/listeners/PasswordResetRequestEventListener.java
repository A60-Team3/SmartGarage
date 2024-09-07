package org.example.smartgarage.events.listeners;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.example.smartgarage.events.PasswordResetRequestEvent;
import org.example.smartgarage.models.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
public class PasswordResetRequestEventListener {

    private final JavaMailSender mailSender;
    private UserEntity user;
    @Value("${email.sender.email}")
    private String senderEmail;

    public PasswordResetRequestEventListener(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @EventListener
    public void sendResetPasswordEmail(PasswordResetRequestEvent event) {
        user = event.user();

        String resetUrl = event.url() + "/garage/password/" + user.getId() + "?token=" + event.token();
        String loginUrl = event.url() + "/garage/login";

        try {
            sendEmail(resetUrl, loginUrl);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendEmail(String resetUrl, String loginUrl) throws MessagingException, UnsupportedEncodingException {
        String subject = "Forgotten password reset request";
        String senderName = "A60 Team 3 Smart Garage App";
        String mailContent = "<p> Greetings, Mr./Mrs. " + String.format("%s %s", user.getFirstName(), user.getLastName()) + ", </p>" +
                "<p>We received request to reset the password of your account with us!</p>" +
                "<p>Please, follow the link below to complete your request.</p>" +
                "<a href=\"" + resetUrl + "\">Reset password</a>" +
                "<p> Thank you <br> A60 Team 3 Smart Garage App" +
                "<br/><br/>" +
                "<br/><br/>" +
                "<p style=\"color: red\">If you are not the one to send this request, consider changing your password.</p>" +
                "<p>You can login from here: <a href=\"" + loginUrl + "\">Login now</a></p>";
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
        messageHelper.setFrom(senderEmail, senderName);
        messageHelper.setTo(user.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);

        mailSender.send(message);
    }
}
