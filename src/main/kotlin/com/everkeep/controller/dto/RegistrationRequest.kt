package com.everkeep.controller.dto

import com.everkeep.annotation.PasswordMatches
import com.everkeep.annotation.ValidPassword
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

@PasswordMatches
data class RegistrationRequest(
    @field:Email
    val email: String,

    @field:NotBlank
    @field:ValidPassword
    val password: String,

    @field:NotBlank
    @field:ValidPassword
    val matchingPassword: String
)
