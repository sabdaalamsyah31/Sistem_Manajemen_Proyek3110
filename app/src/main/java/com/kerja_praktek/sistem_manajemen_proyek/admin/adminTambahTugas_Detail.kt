package com.kerja_praktek.sistem_manajemen_proyek.admin

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.kerja_praktek.sistem_manajemen_proyek.Base.BaseActivity
import com.kerja_praktek.sistem_manajemen_proyek.Model.DetailInfo
import com.kerja_praktek.sistem_manajemen_proyek.Model.UsersInfo
import com.kerja_praktek.sistem_manajemen_proyek.NotifPack.NotificationData
import com.kerja_praktek.sistem_manajemen_proyek.NotifPack.PushNotification
import com.kerja_praktek.sistem_manajemen_proyek.NotifPack.RetrofitInstance
import com.kerja_praktek.sistem_manajemen_proyek.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import java.util.Calendar

class adminTambahTugas_Detail : BaseActivity() {


    private lateinit var database: DatabaseReference
    private lateinit var databaseID : DatabaseReference
    private lateinit var nmProyek: TextView
    private val TAG = "SendNotificationActivity"

//    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
//    @SuppressLint("SuspiciousIndentation")
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_tambah_tugas_detail)

//        date picker settings
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        var tgl = ""
        var bln = ""
        var HurufBulan = ""
        var thn = ""
        var datepicker = findViewById<Button>(R.id.datePicker1)
        val btnTambahkan = findViewById<Button>(R.id.btnTambahkan)
        val btnSelesai = findViewById<Button>(R.id.btnSelesai)
        val edtDetail = findViewById<EditText>(R.id.detail_1)
        nmProyek = findViewById(R.id.nmProyek)
//        ListUser = arrayListOf<UsersInfo>()
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

        datepicker.setOnClickListener {
            val dpd = DatePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog_MinWidth, DatePickerDialog.OnDateSetListener{ view, mYear:Int, mMonth:Int, mDay:Int ->
                // text date
                val bulan = mMonth+1
                tgl  = mDay.toString()
                bln  = bulan.toString()
                thn  = mYear.toString()
                if(bulan == 1){
                    HurufBulan = "Januari"
                }else if(bulan == 2){
                    HurufBulan = "Februari"
                }else if(bulan == 3){
                    HurufBulan = "Maret"
                }else if(bulan == 4){
                    HurufBulan = "April"
                }else if(bulan == 5){
                    HurufBulan= "Mei"
                }else if(bulan == 6){
                    HurufBulan = "Juni"
                }else if(bulan == 7){
                    HurufBulan  = "Juli"
                }else if(bulan == 8){
                    HurufBulan  = "Agustus"
                }else if(bulan == 9){
                    HurufBulan  = "September"
                }else if(bulan == 10){
                    HurufBulan  = "Oktober"
                }else if(bulan == 11){
                    HurufBulan   = "November"
                }else if(bulan == 12){
                    HurufBulan  = "Desember"
                }else{
                    HurufBulan  = "Januari"
                }

                datepicker.text = "$tgl $HurufBulan $thn"
            },year,month,day)
            dpd.show()
        }

        var autoincrement = 0

        btnTambahkan.setOnClickListener{
            database = Firebase.database.reference
            autoincrement++
            val adddetail = edtDetail.text.toString()
            var tanggal = tgl
            var bulan = bln
            var tahun = thn
//            val id = database.addValueEventListener()
            val Proyek = nmProyek.text.toString()

            if (adddetail.isEmpty()) {
                Toast.makeText(this@adminTambahTugas_Detail,"Tolong Tambahkan Detail Proyek", Toast.LENGTH_SHORT).show()

            }else if(tanggal.isEmpty()||bulan.isEmpty()||tahun.isEmpty()) {
                Toast.makeText(this@adminTambahTugas_Detail,"Tolong Tambahkan Deadline", Toast.LENGTH_SHORT).show()
            }

            else{
                val detail = DetailInfo(cekbox = adddetail,status = false, tanggal = tanggal.toString(), bulan = bulan.toString(), tahun = tahun.toString(),
                    id = autoincrement.toString(),
                )
                database.child("DetailProyek")
                    .child(Proyek)
                    .child(autoincrement.toString())
                    .setValue(detail)
                    .addOnCompleteListener{task->
                        if (task.isSuccessful){
                            Toast.makeText(this@adminTambahTugas_Detail,"NotificationData Berhasil Ditambahkan",Toast.LENGTH_LONG).show()
                            edtDetail.setText("")
                            tanggal.isEmpty()
                            bulan.isEmpty()
                            tahun.isEmpty()
                        }else{
                            Toast.makeText(this@adminTambahTugas_Detail,"NotificationData Gagal Ditambahkan",Toast.LENGTH_LONG).show()
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
                    SendNotifP1()
                    SendNotifP2()
                    SendNotifP3()
                    SendNotifP4()
                    val intent = Intent(this@adminTambahTugas_Detail,adminBeranda::class.java)
                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("Tidak"){dialog,id->
                    Toast.makeText(applicationContext,"Silahkan Tambahkan NotificationData Lagi",Toast.LENGTH_LONG).show()
                }
            val alert = buildMessage.create()
            alert.show()
        }




    }
    private fun SendNotifP1() {

//        val title_notif = findViewById<TextView>(R.id.title_notif)
//        val message_notif = findViewById<TextView>(R.id.message_notif)
        val ProgrammerName = intent.getStringExtra("Programmer1")

        var token = ""
        database = Firebase.database.reference
        database.child("token").child(ProgrammerName.toString())
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        token = snapshot.getValue(String::class.java).toString()
//                        txtToken.text = token
                    }else{
//                        txtToken.text = "Token not found"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        val title:String = "Title Coba"
        val message:String = "Message Coba"
        PushNotification(
            NotificationData(title, message),
            to = token
        ).also{
            sendNotification(it)
        }
    }
    private fun SendNotifP2() {

//        val title_notif = findViewById<TextView>(R.id.title_notif)
//        val message_notif = findViewById<TextView>(R.id.message_notif)
        val ProgrammerName = intent.getStringExtra("Programmer2")
        var token = ""
        database = Firebase.database.reference
        database.child("token").child(ProgrammerName.toString())
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        token = snapshot.getValue(String::class.java).toString()
//                        txtToken.text = token
                    }else{
//                        txtToken.text = "Token not found"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        val title:String = "Title Coba"
        val message:String = "Message Coba"
        PushNotification(
            NotificationData(title, message),
            to = token
        ).also{
            sendNotification(it)
        }
    }
    private fun SendNotifP3() {

//        val title_notif = findViewById<TextView>(R.id.title_notif)
//        val message_notif = findViewById<TextView>(R.id.message_notif)
        val ProgrammerName = intent.getStringExtra("Programmer3")

        var token = ""
        database = Firebase.database.reference
        database.child("token").child(ProgrammerName.toString())
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        token = snapshot.getValue(String::class.java).toString()
//                        txtToken.text = token
                    }else{
//                        txtToken.text = "Token not found"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        val title:String = "Title Coba"
        val message:String = "Message Coba"
        PushNotification(
            NotificationData(title, message),
            to = token
        ).also{
            sendNotification(it)
        }
    }
    private fun SendNotifP4() {

//        val title_notif = findViewById<TextView>(R.id.title_notif)
//        val message_notif = findViewById<TextView>(R.id.message_notif)
        val ProgrammerName = intent.getStringExtra("Programmer4")

        var token = ""
        database = Firebase.database.reference
        database.child("token").child(ProgrammerName.toString())
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        token = snapshot.getValue(String::class.java).toString()
//                        txtToken.text = token
                    }else{
//                        txtToken.text = "Token not found"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        val title:String = "Title Coba"
        val message:String = "Message Coba"
        PushNotification(
            NotificationData(title, message),
            to = token
        ).also{
            sendNotification(it)
        }
    }

    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if (response.isSuccessful){
                Log.d(TAG, "Response: ${Gson().toJson(response)}")
            }else{
                Log.e(TAG, response.errorBody().toString())
            }

        }catch (e:Exception) {
            Log.e(TAG,e.toString())
        }

    }
}