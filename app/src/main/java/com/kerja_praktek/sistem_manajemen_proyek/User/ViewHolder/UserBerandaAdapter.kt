package com.kerja_praktek.sistem_manajemen_proyek.User.ViewHolder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kerja_praktek.sistem_manajemen_proyek.Model.ProyekInfo
import com.kerja_praktek.sistem_manajemen_proyek.R
import com.kerja_praktek.sistem_manajemen_proyek.admin.ViewHolder.adminBerandaAdapter

class UserBerandaAdapter(private var UserproyekList : ArrayList<ProyekInfo>):RecyclerView.Adapter<UserBerandaAdapter.ViewHolder>() {


    private  lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(clickListener: onItemClickListener){
        mListener = clickListener
    }
    fun setFilteredList(UserproyekList: ArrayList<ProyekInfo>){
        this.UserproyekList = UserproyekList
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemview = LayoutInflater.from(parent.context).inflate(R.layout.item_tugas, parent, false)
        return ViewHolder(itemview, mListener)
    }

    override fun getItemCount(): Int {
        return UserproyekList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = UserproyekList[position]
        holder.proyekname.text = currentItem.namaProyek
        holder.Mproyek.text = currentItem.manager
        holder.Progrmmr1.text = currentItem.programmer_1
        holder.Progrmmr2.text = currentItem.programmer_2
        holder.progrmmr3.text = currentItem.programmer_3
        holder.progrmmr4.text = currentItem.programmer_4
    }

    class ViewHolder(itemview: View,clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemview){
        val proyekname : TextView = itemview.findViewById(R.id.tvnamaProyek)
        val Mproyek : TextView = itemview.findViewById(R.id.tv_Mproyek)
        val Progrmmr1: TextView = itemview.findViewById(R.id.tv_Prog1)
        val Progrmmr2: TextView = itemview.findViewById(R.id.tv_Prog2)
        val progrmmr3: TextView = itemview.findViewById(R.id.tv_Prog3)
        val progrmmr4: TextView = itemview.findViewById(R.id.tv_Prog4)

        init{
            itemview.setOnClickListener{
                clickListener.onItemClick(adapterPosition)
            }
        }
    }
}