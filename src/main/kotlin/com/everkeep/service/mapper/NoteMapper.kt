package com.everkeep.service.mapper

import com.everkeep.controller.dto.NoteDto
import com.everkeep.model.Note

object NoteMapper {

    fun Note.toNoteDto() = NoteDto(
        id = this.id,
        title = this.title,
        text = this.text,
        priority = this.priority
    )
}
