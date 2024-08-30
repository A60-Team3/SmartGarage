package org.example.smartgarage.events.listeners;

import com.itextpdf.text.Document;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.example.smartgarage.events.EmailReportEvent;
import org.example.smartgarage.models.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.io.UnsupportedEncodingException;

public class EmailReportEventListener {
    private final JavaMailSender mailSender;

    @Value("${email.sender.email}")
    private String senderEmail;

    private Document document;
    private UserEntity user;


    public EmailReportEventListener(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void onEmailReportGenerationSuccess(EmailReportEvent event) {
        document = event.getPdfDocument();
        user = event.getUser();


        try {
            sendCredentialsEmail();
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendCredentialsEmail() throws MessagingException, UnsupportedEncodingException {
        String subject = "Smart Garage Inc service report";
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
