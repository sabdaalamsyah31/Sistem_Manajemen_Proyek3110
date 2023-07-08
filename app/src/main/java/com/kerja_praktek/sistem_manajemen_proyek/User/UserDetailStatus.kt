package com.kerja_praktek.sistem_manajemen_proyek.User

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

class UserDetailStatus : BaseActivity() {
    var tokenadmin = ""
    var tokenmanager = ""

    private lateinit var  database: DatabaseReference
    private val TAG = "SendNotificationActivity"
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail_status)

        val detail = findViewById<TextView>(R.id.Usrdetail_info)
        val statuscek = findViewById<CheckBox>(R.id.Usrstatus)
        val btnSelesai = findViewById<Button>(R.id.Usrbtn_selesai)
        val txtStatus = findViewById<TextView>(R.id.Usrtxt_status)
        val deadline = findViewById<TextView>(R.id.Usrdeadline_Detail)




        var proyekID = intent.getStringExtra("proyekID").toString()
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
        database.child("DetailProyek").child(proyekID).child(id.toString())
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val detailinfo = snapshot.getValue(DetailInfo::class.java)
                    if (detailinfo != null){
                        val resultcekbox = detailinfo.cekbox.toString()
                        val resultid = detailinfo.id.toString()
                        val resultstatus = detailinfo.status

                        txtStatus.text =resultstatus.toString()
                        statuscek.isChecked=detailinfo.status

                    }else{

                        Toast.makeText(this@UserDetailStatus,"Data Base Kosong", Toast.LENGTH_SHORT).show()
                    }




                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })



        getTokenadmin()
        getTokenmanager()


        btnSelesai.setOnClickListener{
            database= Firebase.database.reference
            var status =statuscek.isChecked
            val detailinfo = DetailInfo(cekbox = cekbox, id = id.toString(), status = status, tanggal = tanggal, bulan = RBulan, tahun = tahun)
            database.child("DetailProyek").child(proyekID).child(id.toString())
                .setValue(detailinfo)
                .addOnCompleteListener{ task->
                    if (task.isSuccessful){
                        Toast.makeText(this@UserDetailStatus,"Data Berhasil Diinput", Toast.LENGTH_LONG).show()
                        sendNotifTrueadmin()
                        sendNotifmanagerProyek()
                        finish()
                    }else{
                        Toast.makeText(this@UserDetailStatus,"Data Gagal Diinput", Toast.LENGTH_LONG).show()
                    }
                }

        }

    }

    private fun getTokenadmin(){

        database = Firebase.database.reference
        database.child("token").child("Administrator")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        tokenadmin = snapshot.getValue(String::class.java).toString()
//                        txtToken.text = token
                    }else{
//                        txtToken.text = "Token not found"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }
    private fun getTokenmanager(){
        val manager = intent.getStringExtra("managerProyek")

        database = Firebase.database.reference
        database.child("token").child(manager.toString())
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        tokenmanager = snapshot.getValue(String::class.java).toString()
//                        txtToken.text = token
                    }else{
//                        txtToken.text = "Token not found"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun sendNotifTrueadmin() {
        var namaproyek = intent.getStringExtra("nmProyek")
        var cekbox  = intent.getStringExtra("cekbox")
        val title:String = namaproyek.toString()
        val fullname = intent.getStringExtra("UserFullName").toString()
        val message:String = "Tugas $cekbox telah diupdate $fullname"
        val token = tokenadmin
        PushNotification(
            NotificationData(title, message),
            to = token
        ).also{
            sendNotification(it)
        }
    }


    private fun sendNotifmanagerProyek() {
        var namaproyek = intent.getStringExtra("nmProyek")
        var cekbox  = intent.getStringExtra("cekbox")
        var token = tokenmanager
        val title:String = namaproyek.toString()
        val fullname = intent.getStringExtra("UserFullName").toString()
        val message:String = "Tugas $cekbox telah diupdate $fullname"
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