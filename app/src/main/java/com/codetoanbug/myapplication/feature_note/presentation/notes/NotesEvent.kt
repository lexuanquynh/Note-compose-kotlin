package com.codetoanbug.myapplication.feature_note.presentation.notes

import com.codetoanbug.myapplication.feature_note.domain.model.Note
import com.codetoanbug.myapplication.feature_note.domain.util.NoteOrder

sealed class NotesEvent {
    data class Order(val noteOrder: NoteOrder) : NotesEvent()
    data class DeleteNote(val note: Note) : NotesEvent()
    object RestoreNotes : NotesEvent()
    object ToggleOrderSection : NotesEvent()
}
