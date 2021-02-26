package com.everkeep.repository

import com.everkeep.model.Note
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface NoteRepository : CoroutineCrudRepository<Note, String> {

    suspend fun findByTitleContains(title: String): Flow<Note>
}
