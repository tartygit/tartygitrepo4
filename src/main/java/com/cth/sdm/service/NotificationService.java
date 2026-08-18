package com.cth.sdm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    @Value("${app.notifications.email-enabled:false}")
    private boolean emailEnabled;

    @Value("${app.notifications.sms-enabled:false}")
    private boolean smsEnabled;

    public void sendEmail(String recipient, String subject, String body) {
        if (emailEnabled) {
            log.info("[EMAIL SENT] To: {} | Subject: {} | Body: {}", recipient, subject, body);
        } else {
            log.info("[EMAIL DISABLED - DEMO MODE] Would send To: {} | Subject: {}", recipient, subject);
        }
    }

    public void sendSms(String phoneNumber, String message) {
        if (smsEnabled) {
            log.info("[SMS SENT] To: {} | Message: {}", phoneNumber, message);
        } else {
            log.info("[SMS DISABLED - DEMO MODE] Would send To: {} | Message: {}", phoneNumber, message);
        }
    }

    public boolean isEmailEnabled() { return emailEnabled; }
    public void setEmailEnabled(boolean emailEnabled) { this.emailEnabled = emailEnabled; }

    public boolean isSmsEnabled() { return smsEnabled; }
    public void setSmsEnabled(boolean smsEnabled) { this.smsEnabled = smsEnabled; }
}
