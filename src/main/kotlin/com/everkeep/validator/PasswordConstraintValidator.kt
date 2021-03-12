package com.everkeep.validator

import com.everkeep.annotation.ValidPassword
import org.passay.CharacterRule
import org.passay.EnglishCharacterData
import org.passay.LengthRule
import org.passay.PasswordData
import org.passay.PasswordValidator
import org.passay.WhitespaceRule
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class PasswordConstraintValidator : ConstraintValidator<ValidPassword, String> {

    override fun isValid(password: String, context: ConstraintValidatorContext): Boolean {
        val passwordValidator = PasswordValidator(createRules())
        val result = passwordValidator.validate(PasswordData(password))

        return result.isValid
    }

    private fun createRules() =
        listOf(
            LengthRule(PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH),
            WhitespaceRule(),
            CharacterRule(EnglishCharacterData.UpperCase, 1),
            CharacterRule(EnglishCharacterData.LowerCase, 1),
            CharacterRule(EnglishCharacterData.Digit, 1),
            CharacterRule(EnglishCharacterData.Special, 1)
        )

    companion object {
        const val PASSWORD_MIN_LENGTH = 8
        const val PASSWORD_MAX_LENGTH = 16
    }
}
