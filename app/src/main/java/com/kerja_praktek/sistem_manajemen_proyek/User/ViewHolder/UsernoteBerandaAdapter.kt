package com.kerja_praktek.sistem_manajemen_proyek.User.ViewHolder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kerja_praktek.sistem_manajemen_proyek.Model.NotesDates
import com.kerja_praktek.sistem_manajemen_proyek.R

class UsernoteBerandaAdapter (private val ListNotes : ArrayList<NotesDates>): RecyclerView.Adapter<UsernoteBerandaAdapter.Viewholder>() {


    private lateinit var mListener: onItemClickListener


    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener) {
        mListener = clickListener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int): Viewholder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_note_01, parent, false)

        return Viewholder(itemView, mListener)
    }

    override fun getItemCount(): Int {
        return ListNotes.size
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val currentitem = ListNotes[position]

        var RBulan = currentitem.bulan
        var bulan = RBulan?.toInt()
        var tanggal = currentitem.tanggal
        var tahun = currentitem.tahun
        var BulanHuruf = ""
        if (bulan == 1) {
            BulanHuruf = "Januari"
        } else if (bulan == 2) {
            BulanHuruf = "Februari"
        } else if (bulan == 3) {
            BulanHuruf = "Maret"
        } else if (bulan == 4) {
            BulanHuruf = "April"
        } else if (bulan == 5) {
            BulanHuruf = "Mei"
        } else if (bulan == 6) {
            BulanHuruf = "Juni"
        } else if (bulan == 7) {
            BulanHuruf = "Juli"
        } else if (bulan == 8) {
            BulanHuruf = "Agustus"
        } else if (bulan == 9) {
            BulanHuruf = "September"
        } else if (bulan == 10) {
            BulanHuruf = "Oktober"
        } else if (bulan == 11) {
            BulanHuruf = "November"
        } else if (bulan == 12) {
            BulanHuruf = "Desember"
        }

        holder.date.text = "$tanggal $BulanHuruf $tahun"

    }

    class Viewholder(itemview: View, clickListener: onItemClickListener) :
        RecyclerView.ViewHolder(itemview) {
        val date: TextView = itemview.findViewById(R.id.DateNotes)


        init {
            itemview.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }

        }

    }
}