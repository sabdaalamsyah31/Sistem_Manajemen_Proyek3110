package com.kerja_praktek.sistem_manajemen_proyek.admin.ViewHolder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kerja_praktek.sistem_manajemen_proyek.Model.DetailInfo
import com.kerja_praktek.sistem_manajemen_proyek.R

class adminDetailtugasAdapter(private val CekboxList : ArrayList<DetailInfo>): RecyclerView.Adapter<adminDetailtugasAdapter.ViewHolder>() {
//    private lateinit var database: DatabaseReference
    private lateinit var btneditListener: btneditClickListener
    private lateinit var btnhapusListener: btnhapusClickListener
    private lateinit var mListener: onItemClickListener

    interface btnhapusClickListener{
        fun onbtnhapusClick(position: Int)
    }
    fun setOnHapusClickListener(clicklistener: btnhapusClickListener){
        btnhapusListener = clicklistener
    }

    interface btneditClickListener{
        fun onbtneditClick(position: Int)
    }
    fun setOnEditClickListener(clickListener:btneditClickListener){
        btneditListener = clickListener
    }
    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener) {
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.item_cekbox, parent, false)
        return ViewHolder(item, mListener,btneditListener,btnhapusListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = CekboxList[position]
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



        var text :String
        val statusbaru = if (currentItem.status == true){
            text = "Sudah DiSelesaikan"
        }else{
            text = "Belum DiSelesaikan"
        }
        holder.cekbox.text = currentItem.cekbox
        holder.id.text = currentItem.id.toString()
        holder.status.text = text
        holder.deadline.text = "$tanggal $BulanHuruf $tahun"
//        holder.itemView.setOnClickListener {
//
//        }

    }

    override fun getItemCount(): Int {
        return CekboxList.size
    }

    class ViewHolder(item: View, clickListener: onItemClickListener,btneditListener: btneditClickListener,btnhapusListener: btnhapusClickListener) :
        RecyclerView.ViewHolder(item) {


        var cekbox: TextView = item.findViewById(R.id.cxProyek)
        var id: TextView = item.findViewById(R.id.id)
        var status : TextView = item.findViewById(R.id.itemDetailStatus)
        var deadline : TextView = item.findViewById(R.id.DL_cekbox)


        val btnedit: ImageButton = item.findViewById(R.id.btnEditDetail)
        val btnhapus: ImageButton = item.findViewById(R.id.btnHapusDetail)


//        private fun editDetail(parent: ViewGroup, viewType: Int) {
//            val v =
//                LayoutInflater.from(parent.context).inflate(R.layout.item_editdetail, parent, false)
//
//        }

        init {
            item.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }

            btnedit.setOnClickListener {
                btneditListener.onbtneditClick(adapterPosition)
            }
            btnhapus.setOnClickListener{
                btnhapusListener.onbtnhapusClick(adapterPosition)
            }


        }
    }
}