package com.everkeep.model

import com.everkeep.enums.NotePriority
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Note(
    @Id
    val id: String? = null,
    val title: String,
    val text: String,
    val priority: NotePriority
)
