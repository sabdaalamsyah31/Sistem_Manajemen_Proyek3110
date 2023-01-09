package com.kerja_praktek.sistem_manajemen_proyek.admin

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kerja_praktek.sistem_manajemen_proyek.Base.BaseActivity
import com.kerja_praktek.sistem_manajemen_proyek.Model.DetailInfo
import com.kerja_praktek.sistem_manajemen_proyek.R

class adminTambahTugas_DetailLagi : BaseActivity() {
    private lateinit var database:DatabaseReference
    private lateinit var ListDetailProyek : ArrayList<DetailInfo>
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_tambah_tugas_detail_lagi)
        val btnadd = findViewById<Button>(R.id.btnAdd)
        val btndone = findViewById<Button>(R.id.btnDone)
        val edtTugas = findViewById<EditText>(R.id.adddetail)
        val nmProyek = findViewById<TextView>(R.id.namaProyek)
        val Proyek = intent.getStringExtra("nmProyek").toString()
        var count = 0
        database=Firebase.database.reference
        var id =
            database.child("DetailProyek").child(Proyek)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    count = snapshot.childrenCount.toInt()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        ListDetailProyek = arrayListOf<DetailInfo>()

        btnadd.setOnClickListener {
            id
            val satu = count*2 // ini masih error id countnya masih bisa sama cari cara ya guys ya
            count+satu
            val detail = edtTugas.text.toString()

            database = Firebase.database.reference
            database.child("DetailProyek").child(Proyek.toString())
            if(detail.isEmpty()){

            }else{
                val adddetail = DetailInfo(cekbox = detail,id =  count.toString(), status = false)
                database.child("DetailProyek").child(Proyek).child(count.toString())
                    .setValue(adddetail)
                    .addOnCompleteListener{task->
                        if (task.isSuccessful){
                            Toast.makeText(this@adminTambahTugas_DetailLagi,"Data Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
                            edtTugas.setText("")
                        }else{
                            Toast.makeText(this@adminTambahTugas_DetailLagi,"Data Gagal Ditambahkan", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }


    }
}