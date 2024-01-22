package com.my.service;

import org.springframework.stereotype.Service;

public interface MailService {
    void sendEmail(String subject,String content,String to);
}
