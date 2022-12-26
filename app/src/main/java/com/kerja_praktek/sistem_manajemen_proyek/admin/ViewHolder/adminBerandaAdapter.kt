package com.kerja_praktek.sistem_manajemen_proyek.admin.ViewHolder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kerja_praktek.sistem_manajemen_proyek.Model.ProyekInfo
import com.kerja_praktek.sistem_manajemen_proyek.R

class adminBerandaAdapter(private val proyekList : ArrayList<ProyekInfo>): RecyclerView.Adapter<adminBerandaAdapter.ViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(clickListener: onItemClickListener){
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemview =
            LayoutInflater.from(parent.context).inflate(R.layout.item_tugas, parent, false)
        return ViewHolder(itemview, mListener)


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentitem = proyekList[position]
        holder.proyekname.text = currentitem.NamaProyek

        holder.proyekname.text = currentitem.NamaProyek
    }

    override fun getItemCount(): Int {
        return proyekList.size
    }


    class ViewHolder(itemview: View, clickListener: onItemClickListener) :
        RecyclerView.ViewHolder(itemview) {

        val proyekname: TextView = itemview.findViewById(R.id.tvnamaProyek)

        init {
            itemview.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }

        }


    }
}