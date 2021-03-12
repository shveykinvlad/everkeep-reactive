package com.everkeep.repository

import com.everkeep.model.VerificationToken
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface VerificationTokenRepository : CoroutineCrudRepository<VerificationToken, String> {

    suspend fun findByValueAndAction(value: String, action: VerificationToken.Action): VerificationToken?
}
