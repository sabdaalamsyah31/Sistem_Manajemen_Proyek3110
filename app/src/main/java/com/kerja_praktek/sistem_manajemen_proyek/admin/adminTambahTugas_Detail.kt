package com.kerja_praktek.sistem_manajemen_proyek.admin

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.kerja_praktek.sistem_manajemen_proyek.Base.BaseActivity
import com.kerja_praktek.sistem_manajemen_proyek.Model.DetailInfo
import com.kerja_praktek.sistem_manajemen_proyek.NotifPack.NotificationData
import com.kerja_praktek.sistem_manajemen_proyek.NotifPack.PushNotification
import com.kerja_praktek.sistem_manajemen_proyek.NotifPack.RetrofitInstance
import com.kerja_praktek.sistem_manajemen_proyek.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

//const val TOPIC = "/topics/myTopic"
class adminTambahTugas_Detail : BaseActivity() {

    var token1 = ""
    var token2 = ""
    var token3 = ""
    var token4 = ""
    var tokenM = ""

    private lateinit var database: DatabaseReference
    private lateinit var databaseID : DatabaseReference
    private lateinit var nmProyek: TextView
    private val TAG = "SendNotificationActivity"

    @SuppressLint("SuspiciousIndentation", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_tambah_tugas_detail)
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
        nmProyek.text = intent.getStringExtra("namaProyek")

    database = Firebase.database.reference

    GetTokenP1()
    GetTokenP2()
    GetTokenP3()
    GetTokenP4()
    GetTokenM()

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
            val ID = intent.getStringExtra("ID").toString()

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
                    .child(ID)
                    .child(autoincrement.toString())
                    .setValue(detail)
                    .addOnCompleteListener{task->
                        if (task.isSuccessful){
                            Toast.makeText(this@adminTambahTugas_Detail,"Data Berhasil Ditambahkan",Toast.LENGTH_LONG).show()
                            edtDetail.setText("")
                            tanggal.isEmpty()
                            bulan.isEmpty()
                            tahun.isEmpty()
                        }else{
                            Toast.makeText(this@adminTambahTugas_Detail,"Data Gagal Ditambahkan",Toast.LENGTH_LONG).show()
                        }
                    }
            }



        }

        btnSelesai.setOnClickListener {
            val buildMessage = AlertDialog.Builder(this@adminTambahTugas_Detail)
            buildMessage.setMessage("Apakah semua data telah ditambahkan proyek ? ")
                .setTitle("Peringatan!!!")
                .setCancelable(false)
                .setPositiveButton("Ya"){dialog,id->

                    val intent = Intent(this@adminTambahTugas_Detail,adminBeranda::class.java)
                    SendNotifM()
                    SendNotifP1()
                    SendNotifP2()
                    SendNotifP3()
                    SendNotifP4()
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

    private fun GetTokenM() {
        val Manager = intent.getStringExtra("Manager").toString()


        database = Firebase.database.reference
        database.child("token").child(Manager.toString())
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        tokenM = snapshot.getValue(String::class.java).toString()
                    }else{
                        Log.e(TAG, "TOKEN TIDAK ADA!!!")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun GetTokenP4() {
        val ProgrammerName4 = intent.getStringExtra("Programmer4").toString()


        database = Firebase.database.reference
        database.child("token").child(ProgrammerName4)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        token4 = snapshot.getValue(String::class.java).toString()
                    }else{
                        Log.e(TAG, "TOKEN TIDAK ADA!!!")
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    private fun GetTokenP3() {
        val ProgrammerName3 = intent.getStringExtra("Programmer3").toString()


        database = Firebase.database.reference
        database.child("token").child(ProgrammerName3)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        token3 = snapshot.getValue(String::class.java).toString()
//                        txtToken.text = token
                    }else{
                        Log.e(TAG, "TOKEN TIDAK ADA!!!")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun GetTokenP2() {
        val ProgrammerName2 = intent.getStringExtra("Programmer2").toString()

        database = Firebase.database.reference
        database.child("token").child(ProgrammerName2)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        token2 = snapshot.getValue(String::class.java).toString()
//                        txtToken.text = token
                    }else{
                        Log.e(TAG, "TOKEN TIDAK ADA!!!")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun GetTokenP1() {
        val ProgrammerName1 = intent.getStringExtra("Programmer1").toString()
        database = Firebase.database.reference
        database.child("token").child(ProgrammerName1)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        token1 = snapshot.getValue(String::class.java).toString()
                    }else{
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun SendNotifM() {
        val NamaProyek = intent.getStringExtra("namaProyek")

        val title:String = "Project Baru"
        val message:String = "Anda menjadi Manager project $NamaProyek"
        var token = tokenM
        PushNotification(
            NotificationData(title, message),
            to = token
        ).also{
            sendNotification(it)
        }
    }
    private fun SendNotifP1() {
        val NamaProyek = intent.getStringExtra("namaProyek")

        var token = token1
        val title:String = "Project Baru"
        Log.d(TAG, "token $token")
        val message:String = "Anda menjadi programmer project $NamaProyek"
        PushNotification(
            NotificationData(title, message),
            to = token
        ).also{
            sendNotification(it)
        }
    }
    private fun SendNotifP2() {
        val NamaProyek = intent.getStringExtra("namaProyek")

        var token = token2
        val title:String = "Project Baru"
        Log.d(TAG, "token $token")
        val message:String = "Anda menjadi programmer project $NamaProyek"
        PushNotification(
            NotificationData(title, message),
            to = token
        ).also{
            sendNotification(it)
        }
    }
    private fun SendNotifP3() {
        val NamaProyek = intent.getStringExtra("namaProyek")

        val title:String = "Project Baru"
        var token = token3

        Log.d(TAG, "token $token")
        val message:String = "Anda menjadi programmer project $NamaProyek"
        PushNotification(
            NotificationData(title, message),
            to = token
        ).also{
            sendNotification(it)
        }
    }
    private fun SendNotifP4() {
        val NamaProyek = intent.getStringExtra("namaProyek")


        val title:String = "Project Baru"
        var token = token4

        Log.d(TAG, "token $token")
//        Toast.makeText(this@adminTambahTugas_Detail, "token :$token", Toast.LENGTH_SHORT).show()
        val message = "Anda menjadi programmer project $NamaProyek"
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