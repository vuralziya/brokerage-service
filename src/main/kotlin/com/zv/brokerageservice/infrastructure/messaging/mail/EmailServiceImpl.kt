package com.zv.brokerageservice.infrastructure.messaging.mail

import com.zv.brokerageservice.domain.Notifier
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component

@Component
class EmailService(private val javaMailSender: JavaMailSender) : Notifier {

    override suspend fun sendSimpleMessage(to: String, subject: String, text: String) {
        var message = SimpleMailMessage()
        message.setFrom("noreply@baeldung.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message)
    }
}