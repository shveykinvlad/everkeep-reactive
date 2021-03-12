package com.everkeep.config

import org.springframework.boot.autoconfigure.mail.MailProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSenderImpl

@Configuration
class MailConfig {

    @Bean
    fun javaMailSender(mailProperties: MailProperties) =
        JavaMailSenderImpl().apply {
            host = mailProperties.host
            port = mailProperties.port
            protocol = mailProperties.protocol
            username = mailProperties.username
            password = mailProperties.password
        }
}
