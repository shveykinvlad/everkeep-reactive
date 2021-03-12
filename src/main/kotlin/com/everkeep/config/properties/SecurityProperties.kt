package com.everkeep.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotNull

@ConfigurationProperties("security")
@ConstructorBinding
@Validated
class SecurityProperties(
    @field:NotNull
    val verification: VerificationProperties
) {
    class VerificationProperties(
        @field:NotNull
        val expirationTimeSec: Long
    )
}
