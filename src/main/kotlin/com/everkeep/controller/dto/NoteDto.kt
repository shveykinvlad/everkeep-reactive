package com.everkeep.controller.dto

import com.everkeep.enums.NotePriority
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class NoteDto(
    val id: String? = null,

    @field:NotEmpty
    val title: String,

    @field:NotEmpty
    val text: String,

    @field:NotNull
    val priority: NotePriority
)
