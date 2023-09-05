package com.programming.techie.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import static com.programming.techie.constant.Constant.MAIL_USERNAME;

@Service
@RequiredArgsConstructor
public class MailSender {

    private final JavaMailSender javaMailSender;

    public void sendMail(String subject, String message, String sendTo) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(sendTo);
        simpleMailMessage.setFrom(MAIL_USERNAME);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);
        javaMailSender.send(simpleMailMessage);
    }
}
