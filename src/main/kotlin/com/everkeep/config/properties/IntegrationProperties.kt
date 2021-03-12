package com.everkeep.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotBlank

@ConfigurationProperties("integration")
@ConstructorBinding
@Validated
class IntegrationProperties(
    @field:NotBlank
    val uiUrl: String
)
