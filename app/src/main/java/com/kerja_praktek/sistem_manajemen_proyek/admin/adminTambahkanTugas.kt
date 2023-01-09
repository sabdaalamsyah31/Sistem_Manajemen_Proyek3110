package com.kerja_praktek.sistem_manajemen_proyek.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kerja_praktek.sistem_manajemen_proyek.Base.BaseActivity
import com.kerja_praktek.sistem_manajemen_proyek.Model.ProyekInfo
import com.kerja_praktek.sistem_manajemen_proyek.R

class adminTambahkanTugas : BaseActivity() {
    private lateinit var database: DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_tambahkan_tugas)






        val nmProyek = findViewById<EditText>(R.id.edtNamaProjek)
        val nmManager = findViewById<EditText>(R.id.edtManagerProyek)
        val Deadline = findViewById<EditText>(R.id.edt_Deadline)
        val anggota_1 = findViewById<EditText>(R.id.edt_Programmer1)
        val anggota_2 = findViewById<EditText>(R.id.edt_Programmer2)
        val anggota_3 = findViewById<EditText>(R.id.edt_Programmer3)
        val anggota_4 = findViewById<EditText>(R.id.edt_Programmer4)
        val btnSelanjutnya = findViewById<Button>(R.id.btnSelanjutnya)


        database = Firebase.database.reference


        btnSelanjutnya.setOnClickListener{
            val NamaProyek = nmProyek.text.toString()
            val Manager = nmManager.text.toString()
            val Deadline = Deadline.text.toString()
            val Programmer_1 = anggota_1.text.toString()
            val Programmer_2 = anggota_2.text.toString()
            val Programmer_3 = anggota_3.text.toString()
            val Programmer_4 = anggota_4.text.toString()
            if(NamaProyek.isEmpty()&& Manager.isEmpty()&& Programmer_1.isEmpty()&& Programmer_2.isEmpty()&& Programmer_3.isEmpty()){
                Toast.makeText(this@adminTambahkanTugas,"Terdapat Kolom Kosong",Toast.LENGTH_SHORT).show()
            }else{

                val Proyek = ProyekInfo(NamaProyek,Deadline,Manager,Programmer_1,Programmer_2,Programmer_3,Programmer_4)
                val Proyekname = NamaProyek
                database.child("Proyek")
                    .child(Proyekname)
                    .setValue(Proyek)
                    .addOnCompleteListener{ task ->
                        if (task.isSuccessful){
//                            database.child("DetailProyek").child(nmProyek.toString()).setValue(nmProyek)
                            Toast.makeText(this@adminTambahkanTugas,"Selanjutnya Tambahkan Detail", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@adminTambahkanTugas,adminTambahTugas_Detail::class.java)
                            intent.putExtra("namaProyek",NamaProyek)
                            startActivity(intent)
                        }else{
                            Toast.makeText(this@adminTambahkanTugas,"Data Gagal Ditambahkan", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}