package com.kerja_praktek.sistem_manajemen_proyek.User.ViewHolder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kerja_praktek.sistem_manajemen_proyek.Model.DetailInfo
import com.kerja_praktek.sistem_manajemen_proyek.R
import com.kerja_praktek.sistem_manajemen_proyek.User.ViewHolder.UsrDetailTugasAdapter.holder

class UsrDetailTugasAdapter(private val UserCekbox : ArrayList<DetailInfo>): RecyclerView.Adapter<holder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: UsrDetailTugasAdapter.onItemClickListener) {
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



        var tanggal = currentItem.tanggal
        var RBulan = currentItem.bulan
        var bulan = RBulan?.toInt()
        var tahun = currentItem.tahun
        var BulanHuruf = ""
        if(bulan == 1){
            BulanHuruf = "Januari"
        }else if(bulan == 2){
            BulanHuruf = "Februari"
        }else if(bulan == 3){
            BulanHuruf = "Maret"
        }else if (bulan == 4){
            BulanHuruf = "April"
        }else if (bulan == 5){
            BulanHuruf = "Mei"
        }else if(bulan == 6){
            BulanHuruf = "Juni"
        }else if(bulan == 7){
            BulanHuruf ="Juli"
        } else if(bulan == 8){
            BulanHuruf = "Agustus"
        }else if(bulan == 9){
            BulanHuruf = "September"
        }else if (bulan == 10){
            BulanHuruf = "Oktober"
        }else if (bulan == 11){
            BulanHuruf = "November"
        }else if(bulan == 12){
            BulanHuruf = "Desember"
        }
        holder.deadline.text = "$tanggal $BulanHuruf $tahun"
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
        var deadline : TextView = item.findViewById(R.id.deadlineDuser)

    }
}