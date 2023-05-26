package com.kerja_praktek.sistem_manajemen_proyek.admin

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.kerja_praktek.sistem_manajemen_proyek.Base.BaseActivity
import com.kerja_praktek.sistem_manajemen_proyek.Model.DetailInfo
import com.kerja_praktek.sistem_manajemen_proyek.R
import java.util.Calendar


class
adminTambahTugas_DetailLagi : BaseActivity() {
    private lateinit var database:DatabaseReference
    private lateinit var ListDetailProyek : ArrayList<DetailInfo>
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_tambah_tugas_detail_lagi)
        // date picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val btnadd = findViewById<Button>(R.id.btnAdd)
        val btndone = findViewById<Button>(R.id.btnDone)
        val edtTugas = findViewById<EditText>(R.id.adddetail)
        val nmProyek = findViewById<TextView>(R.id.namaProyek)
        val autoid = findViewById<TextView>(R.id.autoId)
        var datepicker = findViewById<Button>(R.id.datePicker3)




        var tgl = ""
        var bln = ""
        var HurufBulan = ""
        var thn = ""

        val Proyek = intent.getStringExtra("nmProyek").toString()
        var counter : String? = null

        database = Firebase.database.reference
        datepicker.setOnClickListener {
            val dpd = DatePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog_MinWidth, DatePickerDialog.OnDateSetListener{ view, mYear:Int, mMonth:Int, mDay:Int ->
                // text date
//            textDeadline.text = ""+ mDay + "-" + (mMonth+1) + "-" + mYear
                tgl  = mDay.toString()
                bln  = mMonth.toString()
                thn  = mYear.toString()
                if(mMonth == 1){
                    HurufBulan = "Januari"
                }else if(mMonth == 2){
                    HurufBulan = "Februari"
                }else if(mMonth == 3){
                    HurufBulan = "Maret"
                }else if(mMonth == 4){
                    HurufBulan = "April"
                }else if(mMonth == 5){
                    HurufBulan= "Mei"
                }else if(mMonth == 6){
                    HurufBulan = "Juni"
                }else if(mMonth == 7){
                    HurufBulan  = "Juli"
                }else if(mMonth == 8){
                    HurufBulan  = "Agustus"
                }else if(mMonth == 9){
                    HurufBulan  = "September"
                }else if(mMonth == 10){
                    HurufBulan  = "Oktober"
                }else if(mMonth == 11){
                    HurufBulan   = "November"
                }else if(mMonth == 12){
                    HurufBulan  = "Desember"
                }else{
                    HurufBulan  = "Januari"
                }

                datepicker.text = "$tgl $HurufBulan $thn"
            },year,month,day)
            dpd.show()

        }



        database.child("DetailProyek").child(Proyek).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (snap in snapshot.children)
                    {
                        snap.ref.limitToLast(1)
                        val getdata = snap.getValue(DetailInfo::class.java)!!.id
                        val toin = getdata?.toInt()
                        val id = toin!! + 1
                        autoid.text = id.toString()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })






        fun getIDagain(){
            database = Firebase.database.reference
            database.child("DetailProyek").child(Proyek)
//                .orderByKey().limitToLast(1)
                .addListenerForSingleValueEvent(object:ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()){
                            for (snap in snapshot.children)
                            {
                                snap.ref.limitToLast(1)
                                val getdata = snap.getValue(DetailInfo::class.java)!!.id
                                val toin = getdata?.toInt()
                                val id = toin!! + 1
                                autoid.text = id.toString()
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })

        }
        ListDetailProyek = arrayListOf<DetailInfo>()
        getIDagain()
        btnadd.setOnClickListener {
//            var tanggal = tgl
//            var bulan = bln
//            var tahun = thn
            val detail = edtTugas.text.toString()
            database = Firebase.database.reference
            database.child("DetailProyek").child(Proyek)
            if(detail.isEmpty()){
                Toast.makeText(this@adminTambahTugas_DetailLagi,"Kolom Detail Kosong",Toast.LENGTH_LONG).show()

            }else{
                var idNumber : Int? = null
                database.child("DetailProyek").child(Proyek)
                    .addValueEventListener(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()){
                                for(snap in snapshot.children){
                                    val data = snap.key
//                            count = -1
                                    idNumber = data!!.toInt()

                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })
                val newID = autoid.text.toString()
                val adddetail = DetailInfo(cekbox = detail,

                    id = newID,
                    status = false,
                    tanggal = tgl,
                    bulan = bln,
                    tahun = thn
                )


                database.child("DetailProyek").child(Proyek).child(newID)
                    .setValue(adddetail)
                    .addOnCompleteListener{task->
                        if (task.isSuccessful){
                            Toast.makeText(this@adminTambahTugas_DetailLagi,"NotificationData Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
                            edtTugas.setText("")
                            getIDagain()
//                            id
//                            count++
//                            var idnumber =
                        }else{
                            Toast.makeText(this@adminTambahTugas_DetailLagi,"NotificationData Gagal Ditambahkan", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
        btndone.setOnClickListener{
            val buildMessage = AlertDialog.Builder(this@adminTambahTugas_DetailLagi)
            buildMessage.setMessage("Apakah semua data telah ditambahkan proyek ? ")
                .setTitle("Peringatan!")
                .setCancelable(false)
                .setPositiveButton("Ya"){dialog,id->
//                    val intent = Intent(this@adminTambahTugas_DetailLagi,adminBeranda::class.java)
//                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("Tidak"){dialog,id->
                    Toast.makeText(applicationContext,"Silahkan Tambahkan NotificationData Lagi",Toast.LENGTH_LONG).show()
                }
            val alert = buildMessage.create()
            alert.show()
        }

    }
}