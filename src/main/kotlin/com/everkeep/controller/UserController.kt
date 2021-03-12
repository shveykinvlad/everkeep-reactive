package com.everkeep.controller

import com.everkeep.controller.UserController.Companion.USERS
import com.everkeep.controller.dto.RegistrationRequest
import com.everkeep.service.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping(USERS)
class UserController(private val userService: UserService) {

    @PostMapping(REGISTRATION)
    suspend fun register(@RequestBody @Valid registrationRequest: RegistrationRequest) =
        userService.register(registrationRequest.email, registrationRequest.password)

    @PutMapping(CONFIRMATION)
    suspend fun confirm(@RequestParam("token-value") tokenValue: String) =
        userService.confirm(tokenValue)

    companion object {
        const val USERS = "/api/users"
        const val REGISTRATION = "/registration"
        const val CONFIRMATION = "/confirmation"
    }
}
