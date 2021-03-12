package com.everkeep.service

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
