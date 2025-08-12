package org.example.app

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.appbar.MaterialToolbar
import org.example.app.data.NoteRepository
import org.example.app.model.Note
import org.example.app.ui.NoteAdapter
import org.example.app.ui.ViewNoteActivityExtras

// PUBLIC_INTERFACE
class MainActivity : AppCompatActivity() {
    /** Entry activity that displays a list of notes with search and an action button to add new notes. */

    private lateinit var repository: NoteRepository
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NoteAdapter
    private lateinit var toolbar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        repository = NoteRepository(this)

        toolbar = findViewById(R.id.topAppBar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.app_name)

        recyclerView = findViewById(R.id.notesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = NoteAdapter { note: Note ->
            val intent = Intent(this, org.example.app.ui.ViewNoteActivity::class.java)
            intent.putExtra(ViewNoteActivityExtras.EXTRA_NOTE_ID, note.id)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        val fab: FloatingActionButton = findViewById(R.id.addNoteFab)
        fab.setOnClickListener {
            val intent = Intent(this, org.example.app.ui.EditNoteActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadAllNotes()
    }

    private fun loadAllNotes() {
        val notes = repository.getAllNotes()
        adapter.submitList(notes)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchItem: MenuItem? = menu.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as? SearchView
        searchView?.queryHint = getString(R.string.search_hint)
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterNotes(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterNotes(newText)
                return true
            }
        })
        searchItem?.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean = true
            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                loadAllNotes()
                return true
            }
        })
        return true
    }

    private fun filterNotes(query: String?) {
        val trimmed = (query ?: "").trim()
        if (trimmed.isEmpty()) {
            loadAllNotes()
        } else {
            val result = repository.searchNotes(trimmed)
            adapter.submitList(result)
        }
    }
}
