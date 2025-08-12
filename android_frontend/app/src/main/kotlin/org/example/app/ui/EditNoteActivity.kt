package org.example.app.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputLayout
import org.example.app.R
import org.example.app.data.NoteRepository

// PUBLIC_INTERFACE
class EditNoteActivity : AppCompatActivity() {
    /** Activity to add a new note or edit an existing one, with a simple form and save action. */

    private lateinit var repository: NoteRepository
    private var noteId: Long? = null

    private lateinit var toolbar: MaterialToolbar
    private lateinit var titleInputLayout: TextInputLayout
    private lateinit var titleEdit: EditText
    private lateinit var contentEdit: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)

        repository = NoteRepository(this)

        toolbar = findViewById(R.id.topAppBar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        titleInputLayout = findViewById(R.id.inputLayoutTitle)
        titleEdit = findViewById(R.id.editTitle)
        contentEdit = findViewById(R.id.editContent)

        noteId = intent.getLongExtra(ViewNoteActivityExtras.EXTRA_NOTE_ID, -1L).takeIf { it > 0 }

        if (noteId != null) {
            supportActionBar?.title = getString(R.string.edit_note)
            val note = repository.getNoteById(noteId!!)
            if (note == null) {
                finish()
                return
            } else {
                titleEdit.setText(note.title)
                contentEdit.setText(note.content)
            }
        } else {
            supportActionBar?.title = getString(R.string.new_note)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_edit_note, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_save -> {
                saveNote()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveNote() {
        val title = titleEdit.text?.toString()?.trim().orEmpty()
        val content = contentEdit.text?.toString()?.trim().orEmpty()

        if (title.isEmpty()) {
            titleInputLayout.error = getString(R.string.error_title_required)
            return
        } else {
            titleInputLayout.error = null
        }

        if (noteId == null) {
            repository.insertNote(title, content)
        } else {
            repository.updateNote(noteId!!, title, content)
        }

        finish()
    }
}
