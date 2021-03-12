package com.everkeep.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class User(
    @Id
    val email: String,
    val password: String,
    val roles: Set<Role>,
    var enabled: Boolean = false
) {
    enum class Role {
        ROLE_USER,
        ROLE_ADMIN
    }
}
