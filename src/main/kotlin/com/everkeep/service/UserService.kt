package com.everkeep.service

import com.everkeep.exception.NotFoundException
import com.everkeep.exception.UserAlreadyExistsException
import com.everkeep.model.User
import com.everkeep.model.VerificationToken
import com.everkeep.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val verificationService: VerificationService,
    private val mailService: MailService
) {
    suspend fun register(email: String, password: String) {
        if (isExisted(email)) {
            throw UserAlreadyExistsException("User with email = $email already exists")
        }
        val user = createUser(email, password)
        val token = verificationService.create(user, VerificationToken.Action.CONFIRM_ACCOUNT)

        mailService.sendUserConfirmationMail(user.email, token.value)
    }

    suspend fun confirm(tokenValue: String) {
        val verificationToken = verificationService.validateAndUtilize(tokenValue, VerificationToken.Action.CONFIRM_ACCOUNT)
        val user = get(verificationToken.userEmail)
        user.enabled = true

        userRepository.save(user)
    }

    private suspend fun get(email: String) =
        userRepository.findById(email) ?: throw NotFoundException("User with email = $email not found")

    private suspend fun isExisted(email: String) =
        userRepository.findById(email) != null

    private suspend fun createUser(email: String, password: String): User {
        val user = User(
            email = email,
            password = passwordEncoder.encode(password),
            roles = setOf(User.Role.ROLE_USER)
        )
        return userRepository.save(user)
    }
}
