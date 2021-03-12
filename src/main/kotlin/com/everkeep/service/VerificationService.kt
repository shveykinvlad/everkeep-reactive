package com.everkeep.service

import com.everkeep.config.properties.SecurityProperties
import com.everkeep.exception.NotFoundException
import com.everkeep.exception.VerificationTokenExpiredException
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

    suspend fun validateAndUtilize(value: String, action: VerificationToken.Action): VerificationToken {
        val verificationToken = get(value, action)
        validate(verificationToken)

        return utilize(verificationToken)
    }

    private suspend fun get(value: String, action: VerificationToken.Action) =
        verificationTokenRepository.findByValueAndAction(value, action)
            ?: let { throw NotFoundException("VerificationToken with value = $value and action = $action not found") }

    private fun validate(verificationToken: VerificationToken) {
        if (OffsetDateTime.now().isAfter(verificationToken.expiryTime)) {
            throw VerificationTokenExpiredException("Verification token with value = ${verificationToken.value} is expired")
        }
    }

    private suspend fun utilize(verificationToken: VerificationToken): VerificationToken {
        verificationToken.active = false
        return verificationTokenRepository.save(verificationToken)
    }
}
