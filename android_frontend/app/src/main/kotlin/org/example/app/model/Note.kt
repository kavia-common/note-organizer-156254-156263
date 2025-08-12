package org.example.app.model

// PUBLIC_INTERFACE
data class Note(
    /** Entity representing a Note stored in the local SQLite database. */
    val id: Long? = null,
    var title: String,
    var content: String,
    val createdAt: Long,
    val updatedAt: Long
)
