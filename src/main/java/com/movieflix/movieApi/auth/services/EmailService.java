package com.movieflix.movieApi.auth.services;

import com.movieflix.movieApi.dto.MailBody;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class EmailService {
    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendSimpleMessage(MailBody mailBody){
        SimpleMailMessage message=new SimpleMailMessage();
        message.setTo(mailBody.to());
        message.setFrom(" ");
        message.setSubject(mailBody.subject());
        message.setText(mailBody.text());

        javaMailSender.send(message);
    }
}
