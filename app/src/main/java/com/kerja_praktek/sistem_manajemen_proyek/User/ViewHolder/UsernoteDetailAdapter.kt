package com.kerja_praktek.sistem_manajemen_proyek.User.ViewHolder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kerja_praktek.sistem_manajemen_proyek.Model.NotesInfo
import com.kerja_praktek.sistem_manajemen_proyek.R

class UsernoteDetailAdapter(private val ListDetailNotes : ArrayList<NotesInfo>): RecyclerView.Adapter<UsernoteDetailAdapter.Viewholder>() {
private lateinit var delete : DeleteClickListener

    interface DeleteClickListener {
        fun onDeleteClick (position: Int)
    }
    fun setOnDeleteClickListener(clickListener: DeleteClickListener){
        delete = clickListener
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Viewholder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_note_02, parent, false)
        return Viewholder(itemView, delete)
    }
    override fun getItemCount(): Int {
        return ListDetailNotes.size
    }
    override fun onBindViewHolder(holder: Viewholder, position: Int) {

        val currentitem = ListDetailNotes[position]
        holder.detail.text = currentitem.notes
        holder.author.text = currentitem.author
    }


    class Viewholder(itemView : View, deleteClickListener: DeleteClickListener):RecyclerView.ViewHolder(itemView) {

        val detail : TextView = itemView.findViewById(R.id.TheNotes)
        val author :TextView = itemView.findViewById(R.id.AuthorNotes)

        val delete_notes: ImageButton = itemView.findViewById(R.id.note_delete)


        init{
            delete_notes.setOnClickListener {
                deleteClickListener.onDeleteClick(adapterPosition)

            }

        }

    }
}