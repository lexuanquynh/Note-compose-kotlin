package com.codetoanbug.myapplication.feature_note.domain.use_cases

import com.codetoanbug.myapplication.feature_note.domain.model.Note
import com.codetoanbug.myapplication.feature_note.domain.repository.NoteRepository

class DeleteNote(
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(note: Note){
        noteRepository.deleteNote(note)
    }
}