package com.codetoanbug.myapplication.feature_note.presentation.add_edit_note

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codetoanbug.myapplication.feature_note.domain.model.InvalidNoteException
import com.codetoanbug.myapplication.feature_note.domain.model.Note
import com.codetoanbug.myapplication.feature_note.domain.use_cases.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private  val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    // For title
    private  val _noteTitle = mutableStateOf(NoteTextFieldState(
        hint = "Enter title...",
    ))
    val noteTitle: State<NoteTextFieldState> = _noteTitle
    // For content
    private  val _noteContent = mutableStateOf(NoteTextFieldState(
        hint = "Enter some content",
    ))
    val noteContent: State<NoteTextFieldState> = _noteContent
    // For color
    private  val _noteColor = mutableStateOf(Note.noteColors.random().toArgb())
    val noteColor: State<Int> = _noteColor

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentNodeId: Int? = null

    init {
        savedStateHandle.get<Int>("NoteId")?.let { noteId ->
            if (noteId != -1) {
                viewModelScope.launch {
                    noteUseCases.getNote(noteId)?.also { note ->
                        currentNodeId = note.id
                        // for title
                        _noteTitle.value = noteTitle.value.copy(
                            text = note.title,
                            isHintVisible = false,
                        )
                        // for content
                        _noteContent.value = noteContent.value.copy(
                            text = note.content,
                            isHintVisible = false,
                        )
                        // for color
                        _noteColor.value = note.color
                    }
                }
            }
        }
    }

    fun onEvent(event: AddEditNoteEvent) {
        when (event) {
            // For title
            is AddEditNoteEvent.EnteredTitle -> {
                _noteTitle.value = noteTitle.value.copy(
                    text = event.value
                )
            }

            is AddEditNoteEvent.ChangeTitleFocus ->  {
                _noteTitle.value = noteTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            noteTitle.value.text.isBlank()
                )
            }
            // For content
            is AddEditNoteEvent.EnteredContent -> {
                _noteContent.value = noteContent.value.copy(
                    text = event.value
                )
            }
            is AddEditNoteEvent.ChangeContentFocus -> {
                _noteContent.value = noteContent.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            noteContent.value.text.isBlank()
                )
            }

            is AddEditNoteEvent.ChangeColor -> {
                _noteColor.value = event.color
            }

            is AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        noteUseCases.addNote(
                            Note(
                                title = noteTitle.value.text,
                                content = noteContent.value.text,
                                timestamp = System.currentTimeMillis(),
                                color = noteColor.value,
                                id = currentNodeId
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveNote)
                    } catch (e: InvalidNoteException){
                        _eventFlow.emit(UiEvent.ShowSnackbar(
                            message = e.message ?: "Error while saving note"
                        ))
                    }
                }
            }
        }
    }

    sealed class  UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object SaveNote : UiEvent()
    }
}