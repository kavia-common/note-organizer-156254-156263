package org.example.app.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.example.app.R
import org.example.app.model.Note

// PUBLIC_INTERFACE
class NoteAdapter(private val onClick: (Note) -> Unit) :
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    /** RecyclerView adapter for displaying notes in a simple, minimalistic list. */

    private val items = mutableListOf<Note>()

    // PUBLIC_INTERFACE
    fun submitList(data: List<Note>) {
        /** Replace the dataset and refresh the list. */
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class NoteViewHolder(itemView: View, private val onClick: (Note) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        private val title: TextView = itemView.findViewById(R.id.noteTitle)
        private val content: TextView = itemView.findViewById(R.id.noteContent)

        private var current: Note? = null

        init {
            itemView.setOnClickListener {
                current?.let(onClick)
            }
        }

        fun bind(note: Note) {
            current = note
            title.text = note.title
            val excerpt = note.content.trim().replace(Regex("\\s+"), " ")
            content.text = if (excerpt.length > 120) excerpt.substring(0, 117) + "…" else excerpt
        }
    }
}
