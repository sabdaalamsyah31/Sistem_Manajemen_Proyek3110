package com.kerja_praktek.sistem_manajemen_proyek.admin

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kerja_praktek.sistem_manajemen_proyek.Base.BaseActivity
import com.kerja_praktek.sistem_manajemen_proyek.Model.DetailInfo
import com.kerja_praktek.sistem_manajemen_proyek.R
import kotlinx.coroutines.Dispatchers.Main

class adminTambahTugas_Detail : BaseActivity() {


    private lateinit var database: DatabaseReference
    private lateinit var databaseID : DatabaseReference
    private lateinit var nmProyek: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_tambah_tugas_detail)




        val btnTambahkan = findViewById<Button>(R.id.btnTambahkan)
        val btnSelesai = findViewById<Button>(R.id.btnSelesai)
        val edtDetail = findViewById<EditText>(R.id.detail_1)
        nmProyek = findViewById(R.id.nmProyek)

        nmProyek.text = intent.getStringExtra("namaProyek")



//        val nmproyekdetail = nmProyek.toString()
//        var ID:Int = 0
//        databaseID = FirebaseDatabase.getInstance().getReference("DetailProyek").child(nmproyekdetail)
//        database.addValueEventListener(object : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.exists()){
//                    ID = snapshot.childrenCount.toInt()
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//        })





    database = Firebase.database.reference



        var autoincrement = 0

        btnTambahkan.setOnClickListener{
            database = Firebase.database.reference
            autoincrement++
            val adddetail = edtDetail.text.toString()
//            val id = database.addValueEventListener()
            val Proyek = nmProyek.text.toString()

            if (adddetail.isEmpty()) {
                Toast.makeText(this@adminTambahTugas_Detail,"Tolong Tambahkan Detail Proyek", Toast.LENGTH_SHORT).show()

            }else{
                val detail = DetailInfo(cekbox = adddetail,status = false,id = autoincrement.toString())
                database.child("DetailProyek")
                    .child(Proyek)
                    .child(autoincrement.toString())
                    .setValue(detail)
                    .addOnCompleteListener{task->
                        if (task.isSuccessful){
                            Toast.makeText(this@adminTambahTugas_Detail,"Data Berhasil Ditambahkan",Toast.LENGTH_LONG).show()
                            edtDetail.setText("")
                        }else{
                            Toast.makeText(this@adminTambahTugas_Detail,"Data Gagal Ditambahkan",Toast.LENGTH_LONG).show()
                        }
                    }
            }



        }

        btnSelesai.setOnClickListener {
            val buildMessage = AlertDialog.Builder(this@adminTambahTugas_Detail)
            buildMessage.setMessage("Apakah semua data telah ditambahkan proyek ? ")
                .setTitle("Peringatan!")
                .setCancelable(false)
                .setPositiveButton("Ya"){dialog,id->
                    val intent = Intent(this@adminTambahTugas_Detail,adminBeranda::class.java)
                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("Tidak"){dialog,id->
                    Toast.makeText(applicationContext,"Silahkan Tambahkan Data Lagi",Toast.LENGTH_LONG).show()
                }
            val alert = buildMessage.create()
            alert.show()
        }




    }
}