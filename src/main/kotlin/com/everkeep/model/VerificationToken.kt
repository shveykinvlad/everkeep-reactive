package com.everkeep.model

import org.springframework.data.annotation.Id
import java.time.OffsetDateTime

data class VerificationToken(
    @Id
    val value: String,
    val userEmail: String,
    val expiryTime: OffsetDateTime,
    val action: Action,
    var active: Boolean = true
) {
    enum class Action {
        RESET_PASSWORD,
        CONFIRM_ACCOUNT,
        REFRESH_ACCESS
    }
}
