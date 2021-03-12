package com.everkeep.config

import com.everkeep.config.properties.IntegrationProperties
import com.everkeep.config.properties.SecurityProperties
import org.springframework.boot.autoconfigure.mail.MailProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(
    value = [
        IntegrationProperties::class,
        MailProperties::class,
        SecurityProperties::class
    ]
)
class PropertiesConfig
