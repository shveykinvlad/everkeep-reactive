package com.everkeep.controller

import com.everkeep.AbstractIntegrationTest
import com.everkeep.controller.NoteController.Companion.ID
import com.everkeep.controller.NoteController.Companion.NOTES
import com.everkeep.controller.NoteController.Companion.SEARCH
import com.everkeep.controller.dto.NoteDto
import com.everkeep.enums.NotePriority
import com.everkeep.model.Note
import com.everkeep.repository.NoteRepository
import com.everkeep.service.mapper.NoteMapper.toNoteDto
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import reactor.kotlin.core.publisher.toMono

internal class NoteControllerTest : AbstractIntegrationTest() {

    @Autowired
    private lateinit var noteRepository: NoteRepository

    @BeforeEach
    internal fun setUp() = runBlocking {
        noteRepository.deleteAll()
    }

    @Test
    fun getAll() = runBlocking<Unit> {
        val expected = noteRepository.save(createNote())

        val response = webClient.get()
            .uri(getPath())
            .exchange()

        response
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$[0].id").exists()
            .jsonPath("$[0].title").isEqualTo(expected.title)
            .jsonPath("$[0].text").isEqualTo(expected.text)
            .jsonPath("$[0].priority").isEqualTo(expected.priority.name)
    }

    @Test
    fun getById() = runBlocking<Unit> {
        val expected = noteRepository.save(createNote())

        val response = webClient.get()
            .uri(getPath() + ID, expected.id)
            .exchange()

        response
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id").exists()
            .jsonPath("$.title").isEqualTo(expected.title)
            .jsonPath("$.text").isEqualTo(expected.text)
            .jsonPath("$.priority").isEqualTo(expected.priority.name)
    }

    @Test
    fun getByTitle() = runBlocking<Unit> {
        val expected = noteRepository.save(createNote())

        val response = webClient.get()
            .uri { uriBuilder ->
                uriBuilder.path(getControllerPath() + SEARCH)
                    .queryParam("title", expected.title)
                    .build()
            }
            .exchange()

        response
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$[0].id").exists()
            .jsonPath("$[0].title").isEqualTo(expected.title)
            .jsonPath("$[0].text").isEqualTo(expected.text)
            .jsonPath("$[0].priority").isEqualTo(expected.priority.name)
    }

    @Test
    fun save() = runBlocking<Unit> {
        val expected = createNoteDto()

        val response = webClient.post()
            .uri(getPath())
            .contentType(MediaType.APPLICATION_JSON)
            .body(expected.toMono(), NoteDto::class.java)
            .exchange()

        response
            .expectStatus().isCreated
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id").exists()
            .jsonPath("$.title").isEqualTo(expected.title)
            .jsonPath("$.text").isEqualTo(expected.text)
            .jsonPath("$.priority").isEqualTo(expected.priority.name)
    }

    @Test
    fun update() = runBlocking<Unit> {
        val note = noteRepository.save(createNote())
        val expected = note.copy(title = "updated title")
            .toNoteDto()

        val response = webClient.put()
            .uri(getPath() + ID, expected.id)
            .body(expected.toMono(), NoteDto::class.java)
            .exchange()

        response
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id").exists()
            .jsonPath("$.title").isEqualTo(expected.title)
            .jsonPath("$.text").isEqualTo(expected.text)
            .jsonPath("$.priority").isEqualTo(expected.priority.name)
    }

    @Test
    fun delete() = runBlocking {
        val expected = noteRepository.save(createNote())

        val response = webClient.delete()
            .uri(getPath() + ID, expected.id)
            .exchange()

        response
            .expectStatus().isNoContent
            .expectHeader().contentType(MediaType.APPLICATION_JSON)

        assertNull(noteRepository.findById(expected.id!!))
    }

    override fun getControllerPath() = NOTES

    private fun createNote() =
        Note(title = "title", text = "text", priority = NotePriority.NONE)

    private fun createNoteDto() =
        NoteDto(title = "title", text = "text", priority = NotePriority.NONE)
}
