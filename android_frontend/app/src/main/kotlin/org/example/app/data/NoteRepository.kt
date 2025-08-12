package org.example.app.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import org.example.app.data.DBHelper.Companion.COL_CONTENT
import org.example.app.data.DBHelper.Companion.COL_CREATED_AT
import org.example.app.data.DBHelper.Companion.COL_ID
import org.example.app.data.DBHelper.Companion.COL_TITLE
import org.example.app.data.DBHelper.Companion.COL_UPDATED_AT
import org.example.app.data.DBHelper.Companion.TABLE_NOTES
import org.example.app.model.Note

// PUBLIC_INTERFACE
class NoteRepository(context: Context) {
    /** Repository exposing high-level CRUD and search operations for notes stored in SQLite. */
    private val helper = DBHelper(context.applicationContext)

    // PUBLIC_INTERFACE
    fun getAllNotes(): List<Note> {
        /** Returns all notes ordered by most recently updated first. */
        val db = helper.readableDatabase
        val cursor = db.query(
            TABLE_NOTES,
            arrayOf(COL_ID, COL_TITLE, COL_CONTENT, COL_CREATED_AT, COL_UPDATED_AT),
            null, null, null, null,
            "$COL_UPDATED_AT DESC"
        )
        return cursor.use { c -> readNotesFromCursor(c) }
    }

    // PUBLIC_INTERFACE
    fun searchNotes(query: String): List<Note> {
        /** Returns notes where title or content match the query (case-insensitive). */
        val db = helper.readableDatabase
        val like = "%${query.trim()}%"
        val cursor = db.query(
            TABLE_NOTES,
            arrayOf(COL_ID, COL_TITLE, COL_CONTENT, COL_CREATED_AT, COL_UPDATED_AT),
            "$COL_TITLE LIKE ? OR $COL_CONTENT LIKE ?",
            arrayOf(like, like),
            null, null,
            "$COL_UPDATED_AT DESC"
        )
        return cursor.use { c -> readNotesFromCursor(c) }
    }

    // PUBLIC_INTERFACE
    fun getNoteById(id: Long): Note? {
        /** Fetch a single note by ID or null if not found. */
        val db = helper.readableDatabase
        val cursor = db.query(
            TABLE_NOTES,
            arrayOf(COL_ID, COL_TITLE, COL_CONTENT, COL_CREATED_AT, COL_UPDATED_AT),
            "$COL_ID = ?",
            arrayOf(id.toString()),
            null, null, null
        )
        cursor.use { c ->
            return if (c.moveToFirst()) readNote(c) else null
        }
    }

    // PUBLIC_INTERFACE
    fun insertNote(title: String, content: String): Long {
        /** Inserts a new note and returns the generated ID. */
        val now = System.currentTimeMillis()
        val values = ContentValues().apply {
            put(COL_TITLE, title)
            put(COL_CONTENT, content)
            put(COL_CREATED_AT, now)
            put(COL_UPDATED_AT, now)
        }
        val db = helper.writableDatabase
        return db.insert(TABLE_NOTES, null, values)
    }

    // PUBLIC_INTERFACE
    fun updateNote(id: Long, title: String, content: String): Boolean {
        /** Updates an existing note. Returns true if a row was actually updated. */
        val now = System.currentTimeMillis()
        val values = ContentValues().apply {
            put(COL_TITLE, title)
            put(COL_CONTENT, content)
            put(COL_UPDATED_AT, now)
        }
        val db = helper.writableDatabase
        val rows = db.update(TABLE_NOTES, values, "$COL_ID = ?", arrayOf(id.toString()))
        return rows > 0
    }

    // PUBLIC_INTERFACE
    fun deleteNote(id: Long): Boolean {
        /** Deletes a note by ID. Returns true if a row was actually deleted. */
        val db = helper.writableDatabase
        val rows = db.delete(TABLE_NOTES, "$COL_ID = ?", arrayOf(id.toString()))
        return rows > 0
    }

    private fun readNotesFromCursor(c: Cursor): List<Note> {
        val list = ArrayList<Note>(c.count)
        if (c.moveToFirst()) {
            do {
                list.add(readNote(c))
            } while (c.moveToNext())
        }
        return list
    }

    private fun readNote(c: Cursor): Note {
        val id = c.getLong(c.getColumnIndexOrThrow(COL_ID))
        val title = c.getString(c.getColumnIndexOrThrow(COL_TITLE))
        val content = c.getString(c.getColumnIndexOrThrow(COL_CONTENT)) ?: ""
        val createdAt = c.getLong(c.getColumnIndexOrThrow(COL_CREATED_AT))
        val updatedAt = c.getLong(c.getColumnIndexOrThrow(COL_UPDATED_AT))
        return Note(id, title, content, createdAt, updatedAt)
    }
}
