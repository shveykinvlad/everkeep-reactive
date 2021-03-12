package com.everkeep.controller

import com.everkeep.AbstractIntegrationTest
import com.everkeep.controller.UserController.Companion.REGISTRATION
import com.everkeep.controller.UserController.Companion.USERS
import com.everkeep.controller.dto.RegistrationRequest
import com.everkeep.repository.UserRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import reactor.kotlin.core.publisher.toMono

class UserControllerTest : AbstractIntegrationTest() {

    @Autowired
    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun setUp() = runBlocking {
        userRepository.deleteAll()
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

    override fun getControllerPath() = USERS
}
