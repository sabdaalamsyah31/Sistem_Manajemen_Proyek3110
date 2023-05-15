package com.kerja_praktek.sistem_manajemen_proyek.admin

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kerja_praktek.sistem_manajemen_proyek.Base.BaseActivity
import com.kerja_praktek.sistem_manajemen_proyek.Model.DetailInfo
import com.kerja_praktek.sistem_manajemen_proyek.R

class adminDetailstatus : BaseActivity() {

    private lateinit var  database: DatabaseReference
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_status_detail)
        val detail = findViewById<TextView>(R.id.detail_info)
        val statuscek = findViewById<CheckBox>(R.id.status)
        val btnSelesai = findViewById<Button>(R.id.btn_selesai)
        val txtStatus = findViewById<TextView>(R.id.txt_status)
        val deadline = findViewById<TextView>(R.id.deadline_Detail)





        var cekbox  = intent.getStringExtra("cekbox")
        var namaproyek = intent.getStringExtra("nmProyek")
        var status = intent.getStringExtra("status").toBoolean()
        var id = intent.getStringExtra("id")
        var tanggal = intent.getStringExtra("tanggal")
        var RBulan = intent.getStringExtra("bulan")
        var bulan = RBulan?.toInt()
        var tahun = intent.getStringExtra("tahun")


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
        deadline.text="$tanggal $BulanHuruf $tahun"

        detail.text = cekbox
        database = Firebase.database.reference

        database.child("DetailProyek").child(namaproyek.toString()).child(id.toString())
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val detailinfo = snapshot.getValue(DetailInfo::class.java)
                    if (detailinfo != null){
                        val resultcekbox = detailinfo.cekbox.toString()
                        val resultid = detailinfo.id.toString()
                        val resultstatus = detailinfo.status

                        txtStatus.text =resultstatus.toString()
                        statuscek.isChecked=detailinfo.status

                    }else{

                        Toast.makeText(this@adminDetailstatus,"Data Base Kosong", Toast.LENGTH_SHORT).show()
                    }




                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })




        btnSelesai.setOnClickListener{
            database=Firebase.database.reference
            var status =statuscek.isChecked

            val detailinfo = DetailInfo(cekbox = cekbox, id = id.toString(), status = status)
            database.child("DetailProyek").child(namaproyek.toString()).child(id.toString())
                .setValue(detailinfo)
                .addOnCompleteListener{ task->
                    if (task.isSuccessful){
                        Toast.makeText(this@adminDetailstatus,"Data Berhasil Diinput    ", Toast.LENGTH_LONG).show()
                        finish()
                    }else{
                        Toast.makeText(this@adminDetailstatus,"Data Gagal Diinput", Toast.LENGTH_LONG).show()
                    }
                }

        }




    }
}