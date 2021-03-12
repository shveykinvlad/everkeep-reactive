package com.everkeep.service

import com.everkeep.config.properties.IntegrationProperties
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.boot.autoconfigure.mail.MailProperties
import org.springframework.context.MessageSource
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import java.util.Locale

@Service
class MailService(
    private val mailSender: JavaMailSender,
    private val mailProperties: MailProperties,
    private val integrationProperties: IntegrationProperties,
    private val messageSource: MessageSource
) {
    suspend fun sendUserConfirmationMail(mailTo: String, tokenValue: String) {
        val subject = getDefaultMessage("user.confirm.email.subject")
        val message = getDefaultMessage("user.confirm.email.message", integrationProperties.uiUrl, tokenValue)

        send(mailTo, subject, message)
    }

    suspend fun sendResetPasswordMail(mailTo: String, tokenValue: String) {
        val subject = getDefaultMessage("user.password.reset.email.subject")
        val message = getDefaultMessage("user.password.reset.email.message", integrationProperties.uiUrl, mailTo, tokenValue)

        send(mailTo, subject, message)
    }

    private suspend fun send(mailTo: String, subject: String, message: String) {
        val mailMessage = SimpleMailMessage()
            .apply {
                setFrom(mailProperties.username)
                setTo(mailTo)
                setSubject(subject)
                setText(message)
            }
        return withContext(Dispatchers.IO) {
            mailSender.send(mailMessage)
        }
    }

    private fun getDefaultMessage(code: String, vararg params: String) =
        messageSource.getMessage(code, params, Locale.getDefault())
}
