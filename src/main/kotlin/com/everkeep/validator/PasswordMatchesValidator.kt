package com.everkeep.validator

import com.everkeep.annotation.PasswordMatches
import com.everkeep.controller.dto.RegistrationRequest
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class PasswordMatchesValidator : ConstraintValidator<PasswordMatches, RegistrationRequest> {

    override fun isValid(registrationRequest: RegistrationRequest, context: ConstraintValidatorContext) =
        registrationRequest.password == registrationRequest.matchingPassword
}
