package com.kerja_praktek.sistem_manajemen_proyek.User.ViewHolder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kerja_praktek.sistem_manajemen_proyek.Model.DetailInfo
import com.kerja_praktek.sistem_manajemen_proyek.R
import com.kerja_praktek.sistem_manajemen_proyek.User.ViewHolder.UsrDetailTugasViewHolder.holder
import com.kerja_praktek.sistem_manajemen_proyek.admin.ViewHolder.adminDetailtugasAdapter

class UsrDetailTugasViewHolder(private val UserCekbox : ArrayList<DetailInfo>): RecyclerView.Adapter<holder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: UsrDetailTugasViewHolder.onItemClickListener) {
        mListener = clickListener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): holder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.item_user_detail_tugas, parent, false)
        return holder(item)
    }

    override fun onBindViewHolder(holder: holder, position: Int) {
        val currentItem = UserCekbox[position]
        var text : String
        val status = if(currentItem.status == true){
            text = "Sudah Diselesaikan"
        } else {
            text = "Belum Diselesaikan"
        }
        holder.status.text = text
        holder.id.text = currentItem.id
        holder.nmTugas.text = currentItem.cekbox
    }

    override fun getItemCount(): Int {
        return UserCekbox.size
    }

    class holder (item:View):RecyclerView.ViewHolder(item){
        var status : TextView = item.findViewById(R.id.tv_user_statustugas)
        var nmTugas : TextView = item.findViewById(R.id.tv_User_tugas)
        var id : TextView = item.findViewById(R.id.usr_id)

    }
}