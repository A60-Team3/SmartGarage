package org.example.smartgarage.events.listeners;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.example.smartgarage.events.CustomerRegistrationEvent;
import org.example.smartgarage.models.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
public class CustomerRegistrationEventListener{
    private final JavaMailSender mailSender;

    private UserEntity user;
    private String password;
    @Value("${email.sender.email}")
    private String senderEmail;
    public CustomerRegistrationEventListener(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @EventListener
    public void onCustomerRegistrationSuccess(CustomerRegistrationEvent event) {
        password = event.password();
        user = event.user();
        String url = "https://localhost:8080/garage/login";

        try {
            sendCredentialsEmail(url);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendCredentialsEmail(String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Registration email";
        String senderName = "A60 Team 3 Smart Garage App";
        String mailContent = "<p> Greetings, Mr./Mrs. " + user.getFirstName() + " " + user.getLastName() + ", </p>" +
                "<p>Thank you for choosing Smart Garage Inc for your 'precious' maintenance.</p>" +
                "<p> To login into our platform use the following credentials:</p>" +
                "<p> Username: " + user.getEmail() + "</p>" +
                "<p> Password: " + password + "</p>" +
                "<p>Please, change your password at first opportunity!</p>" +
                "<a href=\"" + url + "\">Login now</a>" +
                "<p> Thank you <br> Smart Garage Team</p>";
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom(senderEmail, senderName);
        messageHelper.setTo(user.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }
}
