package com.everkeep.controller

import com.everkeep.controller.NoteController.Companion.NOTES
import com.everkeep.controller.dto.NoteDto
import com.everkeep.service.NoteService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping(NOTES)
class NoteController(private val noteService: NoteService) {

    @GetMapping
    suspend fun getAll() =
        noteService.getAll()

    @GetMapping(ID)
    suspend fun getById(@PathVariable("id") id: String) =
        noteService.getById(id)

    @GetMapping(SEARCH)
    suspend fun getByTitle(@RequestParam title: String) =
        noteService.getByTitle(title)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun save(@RequestBody @Valid noteDto: NoteDto) =
        noteService.save(noteDto)

    @PutMapping(ID)
    suspend fun update(@PathVariable("id") id: String, @RequestBody @Valid noteDto: NoteDto) =
        noteService.update(id, noteDto)

    @DeleteMapping(ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    suspend fun delete(@PathVariable("id") id: String) =
        noteService.delete(id)

    companion object {
        const val NOTES = "/api/notes"
        const val ID = "/{id}"
        const val SEARCH = "/search"
    }
}
