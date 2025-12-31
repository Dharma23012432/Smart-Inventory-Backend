package com.example.demo.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final JavaMailSender javaMailSender;
    private final Logger log = LoggerFactory.getLogger(MailService.class);

    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    // ---------------- BASIC SEND ----------------
    public boolean sendMail(String to, String subject, String body) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom("smkd3081@gmail.com");
            msg.setTo(to);
            msg.setSubject(subject);
            msg.setText(body);

            javaMailSender.send(msg);
            log.info("Mail sent to {}", to);
            return true;

        } catch (MailException ex) {
            log.error("Mail failed to {} — {}", to, ex.getMessage());
            log.debug("Stacktrace:", ex);
            return false;
        }
    }

    // ---------------- ASYNC INVOICE ----------------
    @Async
    public void sendInvoiceAsync(String to, String subject, String body) {
        sendMail(to, subject, body);
    }

    // ---------------- ASYNC LOW STOCK ----------------
    @Async
    public void sendLowStockEmailAsync(String productName, String supplierMail, int currentStock) {
        String subject = "Low Stock Alert - " + productName;
        String body =
                "Dear Supplier,\n\n" +
                "Product \"" + productName + "\" is low on stock.\n" +
                "Current stock: " + currentStock + "\n\n" +
                "Please restock soon.\n\n" +
                "Regards,\nSmart Inventory System";

        sendMail(supplierMail, subject, body);
    }

    // ---------------- SMTP TEST ----------------
    public boolean testMailConnection() {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo("dlingam850@gmail.com");
            msg.setSubject("SMTP Test - Smart Inventory");
            msg.setText("✅ SMTP connection working");

            javaMailSender.send(msg);
            return true;

        } catch (Exception e) {
            log.error("SMTP test failed", e);
            return false;
        }
    }
}
