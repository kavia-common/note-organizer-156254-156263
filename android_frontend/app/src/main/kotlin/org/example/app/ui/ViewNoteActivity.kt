package org.example.app.ui

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import org.example.app.R
import org.example.app.data.NoteRepository
import org.example.app.model.Note

object ViewNoteActivityExtras {
    const val EXTRA_NOTE_ID = "extra_note_id"
}

// PUBLIC_INTERFACE
class ViewNoteActivity : AppCompatActivity() {
    /** Displays a single note in read-only mode with options to edit or delete. */

    private lateinit var repository: NoteRepository
    private var noteId: Long = -1L
    private var note: Note? = null

    private lateinit var toolbar: MaterialToolbar
    private lateinit var title: TextView
    private lateinit var content: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_note)

        repository = NoteRepository(this)

        toolbar = findViewById(R.id.topAppBar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        title = findViewById(R.id.viewTitle)
        content = findViewById(R.id.viewContent)

        noteId = intent.getLongExtra(ViewNoteActivityExtras.EXTRA_NOTE_ID, -1L)
    }

    override fun onResume() {
        super.onResume()
        loadNoteOrFinish()
    }

    private fun loadNoteOrFinish() {
        if (noteId <= 0L) {
            finish()
            return
        }
        note = repository.getNoteById(noteId)
        if (note == null) {
            finish()
            return
        }
        supportActionBar?.title = getString(R.string.app_name)
        title.text = note?.title ?: ""
        content.text = note?.content ?: ""
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_view_note, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_edit -> {
                val i = Intent(this, EditNoteActivity::class.java)
                i.putExtra(ViewNoteActivityExtras.EXTRA_NOTE_ID, noteId)
                startActivity(i)
                true
            }
            R.id.action_delete -> {
                confirmDelete()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun confirmDelete() {
        AlertDialog.Builder(this)
            .setTitle(R.string.confirm_delete_title)
            .setMessage(R.string.confirm_delete_message)
            .setPositiveButton(R.string.yes) { _: DialogInterface, _: Int ->
                repository.deleteNote(noteId)
                finish()
            }
            .setNegativeButton(R.string.no, null)
            .show()
    }
}
