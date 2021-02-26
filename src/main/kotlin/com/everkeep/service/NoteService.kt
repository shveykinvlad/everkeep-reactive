package com.everkeep.service

import com.everkeep.controller.dto.NoteDto
import com.everkeep.exception.NotFoundException
import com.everkeep.model.Note
import com.everkeep.repository.NoteRepository
import com.everkeep.service.mapper.NoteMapper.toNoteDto
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
        val note = Note(
            title = noteDto.title,
            text = noteDto.text,
            priority = noteDto.priority
        )
        return noteRepository.save(note)
            .toNoteDto()
    }

    suspend fun update(noteDto: NoteDto): NoteDto {
        val note = Note(
            id = noteDto.id,
            title = noteDto.title,
            text = noteDto.text,
            priority = noteDto.priority
        )
        return noteRepository.save(note)
            .toNoteDto()
    }

    suspend fun delete(id: String) =
        noteRepository.deleteById(id)
}

