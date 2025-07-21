package com.example.rentalcarsystem.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    /**
     * Method to send email
     * @param email
     */
    public void  sendEmail(Email email) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom("dohoanganh92004@gmail.com");
    message.setTo(email.getToEmail());
    message.setSubject(email.getSubject());
    message.setText(email.getBody());
    mailSender.send(message);
}
}
