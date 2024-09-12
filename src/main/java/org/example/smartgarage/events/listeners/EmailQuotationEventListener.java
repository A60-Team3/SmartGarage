package org.example.smartgarage.events.listeners;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.example.smartgarage.events.EmailQuotationEvent;
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
public class EmailQuotationEventListener {
    private final JavaMailSender mailSender;

    @Value("${email.sender.email}")
    private String senderEmail;

    private ByteArrayOutputStream document;
    private UserEntity user;

    public EmailQuotationEventListener(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @EventListener
    public void onEmailQuotationGenerationSuccess(EmailQuotationEvent event) {
        document = event.pdfDocument();
        user = event.user();

        try {
            sendQuotation();
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendQuotation() throws MessagingException, UnsupportedEncodingException {
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String subject = "Smart Garage Inc quotation";
        String senderName = "A60 Team 3 Smart Garage App";
        String clientName = String.format("%s %s", user.getFirstName(), user.getLastName());
        String mailContent = "<p> Greetings, Mr./Mrs. " + clientName + ", </p>" +
                "<p>Thank you for your interest in Smart Garage Inc</p>" +
                "<br/><br/>" +
                "<p> Find attached the quotation you requested</p>" +
                "<p> It is encrypted. The password is the family name you provided.</p>" +
                "<p> To view the quotation, you have to save it to your computer.</p>" +
                "<br/><br/>" +
                "<p> Thank you <br> Smart Garage Team</p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
        messageHelper.setFrom(senderEmail, senderName);
        messageHelper.setTo(user.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        messageHelper.addAttachment(
                String.format("Quotation_%s_%s.pdf", clientName, currentTime),
                new ByteArrayDataSource(document.toByteArray(), "application/pdf")
        );
        mailSender.send(message);
    }
}
