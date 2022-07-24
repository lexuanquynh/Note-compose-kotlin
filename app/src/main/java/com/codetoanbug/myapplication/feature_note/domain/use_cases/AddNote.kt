package com.codetoanbug.myapplication.feature_note.domain.use_cases

import com.codetoanbug.myapplication.feature_note.domain.model.InvalidNoteException
import com.codetoanbug.myapplication.feature_note.domain.model.Note
import com.codetoanbug.myapplication.feature_note.domain.repository.NoteRepository

class AddNote (
    private val noteRepository: NoteRepository
        ) {
    @Throws(InvalidNoteException ::class)
    suspend operator fun invoke(note: Note) {
        if (note.title.isBlank()) {
            throw InvalidNoteException("The title of the note is required")
        }
        if (note.content.isBlank()) {
            throw InvalidNoteException("The content of the note is required")
        }
        noteRepository.insertNote(note)
    }
}