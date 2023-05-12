package com.kerja_praktek.sistem_manajemen_proyek.User

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kerja_praktek.sistem_manajemen_proyek.Base.BaseActivity
import com.kerja_praktek.sistem_manajemen_proyek.Model.DetailInfo
import com.kerja_praktek.sistem_manajemen_proyek.R
import com.kerja_praktek.sistem_manajemen_proyek.User.ViewHolder.UsrDetailTugasViewHolder

class UserDetailTugas : BaseActivity() {
    private val TAG = "UserDetailTugas"

    private lateinit var database : DatabaseReference // Database

    private lateinit var tvnmProyek: TextView // nama proyek
    private lateinit var tvDeadline : TextView // Deadline Tugas
    private lateinit var tvManagerProyek : TextView // Manager Proyek
    private lateinit var tvPersen: TextView // persen
    private lateinit var anggota1 : TextView // Programmer 1
    private lateinit var anggota2 : TextView // Programmer 2
    private lateinit var anggota3 : TextView // Programmer 3
    private lateinit var anggota4 : TextView // Programmer 4
    private lateinit var rvProyek: RecyclerView // recyclerview
    private lateinit var Userlistdataproyek : ArrayList<DetailInfo>
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail_tugas)

        var namaProyek = ""

        val nama = intent.getStringExtra("namaProyek")
        val manager = intent.getStringExtra("managerProyek")
        val tanggal = intent.getStringExtra("tanggal")
        val bulan = intent.getStringExtra("bulan")
        val tahun = intent.getStringExtra("tahun")
        val programmer1 = intent.getStringExtra("programmer1")
        val programmer2 = intent.getStringExtra("programmer2")
        val programmer3 = intent.getStringExtra("programmer3")
        val programmer4 = intent.getStringExtra("programmer4")

        tvnmProyek = findViewById(R.id.NmProyek)
        tvDeadline = findViewById(R.id.usr_deadline)
        tvManagerProyek = findViewById(R.id.usr_managerproyek)
        tvPersen = findViewById(R.id.usr_persen)
        anggota1 = findViewById(R.id.user_programmer1)
        anggota2 = findViewById(R.id.user_programmer2)
        anggota3 = findViewById(R.id.user_programmer3)
        anggota4 = findViewById(R.id.user_programmer4)



        tvnmProyek.text = nama
        tvManagerProyek.text = manager
        tvDeadline.text = "$tanggal-$bulan-$tahun"
        anggota1.text = programmer1
        anggota2.text = programmer2
        anggota3.text = programmer3
        anggota4.text = programmer4

//        database
        database = Firebase.database.reference

//        recyclerView
        rvProyek = findViewById(R.id.user_rvDetailProyek)
        rvProyek.layoutManager = LinearLayoutManager(this)
        Userlistdataproyek = arrayListOf<DetailInfo>()
        namaProyek = intent.getStringExtra("namaProyek").toString()




    }

    override fun onResume() {
        super.onResume()
        usergetDetailProyek()//rendering data on this activity in resume
    }

    private fun userSetupadapter(){
        val adapter = UsrDetailTugasViewHolder(Userlistdataproyek)
        rvProyek.adapter = adapter
    }

    fun usergetDetailProyek(){
        database =  Firebase.database.reference
        val namaProyek = intent.getStringExtra("namaProyek").toString()
        database.child("DetailProyek").child(namaProyek).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                Userlistdataproyek.clear()
                if(snapshot.exists()){
                    for(snap in snapshot.children){
                        val data = snap.getValue(DetailInfo::class.java)
                        Userlistdataproyek.add(data!!)
                    }
                    userSetupadapter()
                    countpersen()
                }else{
                    Toast.makeText(applicationContext,"Detail Tugas Kosong",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun countpersen(){
        if(Userlistdataproyek.size > 0 ){
            val sizelist = Userlistdataproyek.size.toDouble()
            var count = 0.0
            for (item in Userlistdataproyek){
                if(item.status){
                    count++
                }
            }
            val result = (count / sizelist) * 100.0
            Log.d(TAG,"percentage:${result.toInt()}")
            tvPersen.text = result.toInt().toString()
        }
    }
}