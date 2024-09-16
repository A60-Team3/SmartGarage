package org.example.smartgarage.events.listeners;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.example.smartgarage.events.EmailReportEvent;
import org.example.smartgarage.models.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class EmailReportEventListener{
    private final JavaMailSender mailSender;

    @Value("${email.sender.email}")
    private String senderEmail;

    private byte[] document;
    private UserEntity user;

    public EmailReportEventListener(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @EventListener
    public void onEmailReportGenerationSuccess(EmailReportEvent event) {
        document = event.pdfDocument();
        user = event.user();

        try {
            sendCredentialsEmail();
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendCredentialsEmail() throws MessagingException, UnsupportedEncodingException {
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String subject = "Smart Garage Inc service report";
        String senderName = "A60 Team 3 Smart Garage App";
        String mailContent = "<p> Greetings, Mr./Mrs. " + String.format("%s %s", user.getFirstName(), user.getLastName()) + ", </p>" +
                "<p>Thank you for choosing Smart Garage Inc for your 'precious' maintenance.</p>" +
                "<br/><br/>" +
                "<p> Find attached the report you requested</p>" +
                "<p> It is encrypted. The password is your registered phone number.</p>" +
                "<p> To view the report, you have to save it to your computer.</p>" +
                "<br/><br/>" +
                "<p> Thank you <br> Smart Garage Team</p>";


        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
        messageHelper.setFrom(senderEmail, senderName);
        messageHelper.setTo(user.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        messageHelper.addAttachment(
                String.format("Visit_Report_%s.pdf", currentTime),
                new ByteArrayDataSource(document, "application/pdf")
        );
        mailSender.send(message);
    }
}
