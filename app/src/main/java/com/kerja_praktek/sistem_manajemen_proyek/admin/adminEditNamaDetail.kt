package com.kerja_praktek.sistem_manajemen_proyek.admin

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kerja_praktek.sistem_manajemen_proyek.Base.BaseActivity
import com.kerja_praktek.sistem_manajemen_proyek.Model.DetailInfo
import com.kerja_praktek.sistem_manajemen_proyek.R

class adminEditNamaDetail : BaseActivity() {
    private lateinit var database : DatabaseReference
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_edit_nama_detail)


        var edtdetail = findViewById<EditText>(R.id.edt_DetailNama)
        var ID = findViewById<TextView>(R.id.editID)
        var nmProyek = findViewById<TextView>(R.id.nmProyek_onDetail)
        var selesai = findViewById<Button>(R.id.bt_selesai)


        var getnmProyek = intent.getStringExtra("nmProyek")
        var getcekbox = intent.getStringExtra("cekbox")
        var getid = intent.getStringExtra("id")
        var getstatus = intent.getStringExtra("Status")

        edtdetail.hint = getcekbox
        nmProyek.text = getnmProyek
        ID.text = getid.toString()

        database = Firebase.database.reference

        selesai.setOnClickListener {

            val proyek = getnmProyek.toString()
            val id = getid.toString()
//            val status = getstatus.toBoolean()
            var detailname =  edtdetail.text.toString()
            if (detailname.isEmpty()){
                Toast.makeText(this@adminEditNamaDetail,"Data Kosong Mohon Diisi", Toast.LENGTH_LONG).show()
            }else{
                val detail = DetailInfo(detailname,id = id, status = getstatus.toBoolean())
                database.child("DetailProyek").child(proyek).child(id)
                    .setValue(detail)
                    .addOnCompleteListener{task->
                        if(task.isSuccessful){
                            Toast.makeText(this@adminEditNamaDetail,"Data Berhasil Diubah", Toast.LENGTH_LONG).show()
                            finish()
                        }else{
                            Toast.makeText(this@adminEditNamaDetail,"Data Gagal Dirubah", Toast.LENGTH_LONG).show()
                            edtdetail.setText("")
                        }
                    }

            }
        }




    }

}