package com.everkeep.controller

import com.everkeep.AbstractIntegrationTest
import com.everkeep.controller.UserController.Companion.CONFIRMATION
import com.everkeep.controller.UserController.Companion.REGISTRATION
import com.everkeep.controller.UserController.Companion.USERS
import com.everkeep.controller.dto.RegistrationRequest
import com.everkeep.model.User
import com.everkeep.model.VerificationToken
import com.everkeep.repository.UserRepository
import com.everkeep.repository.VerificationTokenRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import reactor.kotlin.core.publisher.toMono
import java.time.OffsetDateTime
import java.util.UUID

class UserControllerTest : AbstractIntegrationTest() {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var verificationTokenRepository: VerificationTokenRepository

    @BeforeEach
    fun setUp() = runBlocking {
        userRepository.deleteAll()
        verificationTokenRepository.deleteAll()
    }

    @Test
    fun register() = runBlocking {
        val registrationRequest = RegistrationRequest(
            email = "test@localhost",
            password = "@'fs;W9%",
            matchingPassword = "@'fs;W9%"
        )

        webClient.post()
            .uri(getPath() + REGISTRATION)
            .contentType(MediaType.APPLICATION_JSON)
            .body(registrationRequest.toMono(), RegistrationRequest::class.java)
            .exchange()
            .expectStatus().isOk

        assertNotNull(userRepository.findById(registrationRequest.email))
        assertTrue(greenMail.receivedMessages.isNotEmpty())
    }

    @Test
    fun confirm() = runBlocking {
        val userEmail = "test@localhost"
        userRepository.save(
            User(
                email = userEmail,
                password = "@'fs;W9%",
                roles = setOf(User.Role.ROLE_USER)
            )
        )

        val tokenValue = UUID.randomUUID().toString()
        verificationTokenRepository.save(
            VerificationToken(
                value = tokenValue,
                userEmail = userEmail,
                expiryTime = OffsetDateTime.now().plusYears(1),
                action = VerificationToken.Action.CONFIRM_ACCOUNT
            )
        )

        webClient.put()
            .uri { uriBuilder ->
                uriBuilder.path(getControllerPath() + CONFIRMATION)
                    .queryParam("token-value", tokenValue)
                    .build()
            }
            .exchange()
            .expectStatus().isOk

        assertTrue(userRepository.findById(userEmail)!!.enabled)
        assertFalse(verificationTokenRepository.findByValueAndAction(tokenValue, VerificationToken.Action.CONFIRM_ACCOUNT)!!.active)
    }

    override fun getControllerPath() = USERS
}
