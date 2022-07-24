package com.codetoanbug.myapplication.feature_note.presentation.util

sealed class Screen(val router: String) {
    object NoteScreen : Screen("notes_screen")
    object AddEditNoteScreen : Screen("add_edit_note_screen")

}