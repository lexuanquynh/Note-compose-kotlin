package com.codetoanbug.myapplication.feature_note.data.data_source

import androidx.room.*
import com.codetoanbug.myapplication.feature_note.domain.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao  {
    @Query("SELECT * FROM note")
    fun getNodes(): Flow<List<Note>>

    @Query("SELECT * FROM note WHERE id = :id")
    suspend fun getNodeById(id: Int): Note?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)
}