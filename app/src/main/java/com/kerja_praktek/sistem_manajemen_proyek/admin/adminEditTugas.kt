package com.kerja_praktek.sistem_manajemen_proyek.admin

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kerja_praktek.sistem_manajemen_proyek.Base.BaseActivity
import com.kerja_praktek.sistem_manajemen_proyek.Model.DetailInfo
import com.kerja_praktek.sistem_manajemen_proyek.Model.ProyekInfo
import com.kerja_praktek.sistem_manajemen_proyek.R
import com.kerja_praktek.sistem_manajemen_proyek.admin.ViewHolder.adminDetailtugasAdapter

class adminEditTugas : BaseActivity() {
    private lateinit var ListDetailProyek:ArrayList<DetailInfo>
    private lateinit var database:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_edit_tugas)
        val btnbatal = findViewById<Button>(R.id.btnBatal)
        val btnselesai = findViewById<Button>(R.id.btn_Selesai)

        val edtnmProyek = findViewById<TextView>(R.id.txt_namaProyek)
        val edtDeadline = findViewById<EditText>(R.id.edt_deadline)
        val edtManager = findViewById<EditText>(R.id.edt_managerProyek)
        val edtProgrammer1 = findViewById<EditText>(R.id.edt_Programmer_1)
        val edtProgrammer2 = findViewById<EditText>(R.id.edt_Programmer_2)
        val edtProgrammer3 = findViewById<EditText>(R.id.edt_Programmer_3)
        val edtProgrammer4 = findViewById<EditText>(R.id.edt_Programmer_4)


        val nama = intent.getStringExtra("namaProyek").toString()
        val deadline = intent.getStringExtra("deadline").toString()
        val manager = intent.getStringExtra("managerProyek").toString()
        val programmer1 = intent.getStringExtra("programmer_1").toString()
        val programmer2 = intent.getStringExtra("programmer_2").toString()
        val programmer3 = intent.getStringExtra("programmer_3").toString()
        val programmer4 = intent.getStringExtra("programmer_4").toString()

        edtnmProyek.text = nama
        edtDeadline.setText(deadline)
        edtManager.setText(manager)
        edtProgrammer1.setText(programmer1)
        edtProgrammer2.setText(programmer2)
        edtProgrammer3.setText(programmer3)
        edtProgrammer4.setText(programmer4)
        ListDetailProyek = arrayListOf<DetailInfo>()

//        getCekboxDetail()


        database = Firebase.database.reference

        btnbatal.setOnClickListener {
            finish()
        }

        btnselesai.setOnClickListener {
            val nama = edtnmProyek.text.toString()
            val deadline = edtDeadline.text.toString()
            val manager = edtManager.text.toString()
            val programmer1 = edtProgrammer1.text.toString()
            val programmer2 = edtProgrammer2.text.toString()
            val programmer3 = edtProgrammer3.text.toString()
            val programmer4 = edtProgrammer4.text.toString()
            val detail = "detail"
            val detailProyek =database.child("Proyek").child(nama).child(detail)

            if(nama.isEmpty()&&deadline.isEmpty()&&manager.isEmpty()&&programmer1.isEmpty()&&programmer2.isEmpty()&&programmer3.isEmpty()&&programmer4.isEmpty())
            {
                Toast.makeText(this@adminEditTugas,"Kolom Data Kosong",Toast.LENGTH_SHORT).show()
            }else{
                val proyek = ProyekInfo(nama,deadline,manager,programmer1,programmer2,programmer3,programmer4,
//                    ListDetailProyek
                )
                val proyekname = nama

                database.child("Proyek")
                    .child(proyekname)
                    .setValue(proyek)
                    .addOnCompleteListener{ task ->
                        if(task.isSuccessful){
                            Toast.makeText(this@adminEditTugas, "Data Proyek Berhasil Diubah",Toast.LENGTH_SHORT).show()
                            val beranda = Intent(this@adminEditTugas,adminBeranda::class.java)
                            startActivity(beranda)
                            finish()
                        }else{
                            Toast.makeText(this@adminEditTugas, "Data Gagal Diubah",Toast.LENGTH_SHORT).show()
                            setResult(Activity.RESULT_CANCELED)
                            finish()
                        }

                    }
            }

        }



    }
    private fun getCekboxDetail() {
        database = Firebase.database.reference
// note pada database nama child harus sama dengan nama proyek
        val namaproyek = intent.getStringExtra("namaProyek").toString()
        val child = "detail"
        database.child("Proyek")
            .child(namaproyek).child(child).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    ListDetailProyek.clear()
                    if(snapshot.exists()){
                        for(snap in snapshot.children){
                            val data = snap.getValue(DetailInfo::class.java)
                            ListDetailProyek.add(data!!)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        return
    }


}