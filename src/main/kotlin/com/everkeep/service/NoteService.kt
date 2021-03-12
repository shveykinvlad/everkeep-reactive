package com.everkeep.service

import com.everkeep.controller.dto.NoteDto
import com.everkeep.exception.NotFoundException
import com.everkeep.model.Note
import com.everkeep.repository.NoteRepository
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Service

@Service
class NoteService(private val noteRepository: NoteRepository) {

    suspend fun getAll() =
        noteRepository.findAll()
            .map { it.toNoteDto() }

    suspend fun getById(id: String) =
        noteRepository.findById(id)
            ?.toNoteDto()
            ?: throw NotFoundException("Note with id = $id not found")

    suspend fun getByTitle(title: String) =
        noteRepository.findByTitleContains(title)
            .map { it.toNoteDto() }

    suspend fun save(noteDto: NoteDto): NoteDto {
        val note = noteDto.toNote()
        return noteRepository.save(note)
            .toNoteDto()
    }

    suspend fun update(id: String, noteDto: NoteDto): NoteDto {
        if (id != noteDto.id) {
            throw IllegalArgumentException("Path variable 'id' doesn't match body field 'id'")
        }
        val note = noteDto.toNote()
        return noteRepository.save(note)
            .toNoteDto()
    }

    suspend fun delete(id: String) =
        noteRepository.deleteById(id)

    private fun Note.toNoteDto() = NoteDto(
        id = this.id,
        title = this.title,
        text = this.text,
        priority = this.priority
    )

    private fun NoteDto.toNote() = Note(
        id = this.id,
        title = this.title,
        text = this.text,
        priority = this.priority
    )
}

