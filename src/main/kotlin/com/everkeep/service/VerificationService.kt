package com.everkeep.service

import com.everkeep.config.properties.SecurityProperties
import com.everkeep.model.User
import com.everkeep.model.VerificationToken
import com.everkeep.repository.VerificationTokenRepository
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.util.UUID

@Service
class VerificationService(
    private val verificationTokenRepository: VerificationTokenRepository,
    private val securityProperties: SecurityProperties
) {
    suspend fun create(user: User, action: VerificationToken.Action): VerificationToken {
        val verificationToken = VerificationToken(
            value = UUID.randomUUID().toString(),
            expiryTime = OffsetDateTime.now().plusSeconds(securityProperties.verification.expirationTimeSec),
            action = action,
            userEmail = user.email
        )
        return verificationTokenRepository.save(verificationToken)
    }
}
