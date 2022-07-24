package com.codetoanbug.myapplication.feature_note.presentation.notes

import com.codetoanbug.myapplication.feature_note.domain.model.Note
import com.codetoanbug.myapplication.feature_note.domain.util.NoteOrder
import com.codetoanbug.myapplication.feature_note.domain.util.OrderType

data class NotesState (
    val notes: List<Note> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Ascending),
    val isOrderSectionVisible: Boolean = false,
)