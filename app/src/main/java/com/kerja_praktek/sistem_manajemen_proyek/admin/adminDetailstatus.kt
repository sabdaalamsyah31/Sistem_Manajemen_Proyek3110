package com.kerja_praktek.sistem_manajemen_proyek.admin

import androidx.appcompat.app.AppCompatActivity
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_edit_detail)
        val detail = findViewById<TextView>(R.id.detail_info)
        val statuscek = findViewById<CheckBox>(R.id.status)
        val btnSelesai = findViewById<Button>(R.id.btn_selesai)
        val txtStatus = findViewById<TextView>(R.id.txt_status)





        var cekbox  = intent.getStringExtra("cekbox")
        var namaproyek = intent.getStringExtra("nmProyek")
        var status = intent.getStringExtra("status").toBoolean()
        var id = intent.getStringExtra("id")


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