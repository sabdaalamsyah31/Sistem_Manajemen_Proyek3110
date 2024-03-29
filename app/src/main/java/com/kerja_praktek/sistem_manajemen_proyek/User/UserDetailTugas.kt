package com.kerja_praktek.sistem_manajemen_proyek.User

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kerja_praktek.sistem_manajemen_proyek.Base.BaseActivity
import com.kerja_praktek.sistem_manajemen_proyek.Model.DetailInfo
import com.kerja_praktek.sistem_manajemen_proyek.R
import com.kerja_praktek.sistem_manajemen_proyek.User.ViewHolder.UsrDetailTugasAdapter
import com.kerja_praktek.sistem_manajemen_proyek.admin.adminDetailstatus

class UserDetailTugas : BaseActivity() {
    private val TAG = "UserDetailTugas"

    private lateinit var database : DatabaseReference // Database

//    Animation source
    private val rotateOpen : Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.rotate_open_anim)}
    private val rotateClose : Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.rotate_close_anim)}
    private val fromBottom : Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.from_bottom_anim)}
    private val toBottom : Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.to_bottom_anim)}
    private val toLeft : Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.to_left_anim)}
    private val toRight : Animation by lazy{ AnimationUtils.loadAnimation(this,R.anim.to_right_anim)}

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

    private var clicked = false
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail_tugas)

        var namaProyek = ""
        val proyekID = intent.getStringExtra("proyekID")

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
        var btnNote = findViewById<FloatingActionButton>(R.id.user_note)
        var ac = findViewById<FloatingActionButton>(R.id.User_ac)

        val bulanInt = bulan?.toInt()
        var bulanHuruf = ""


        if(bulanInt == 1){
            bulanHuruf = "Januari"
        }else if(bulanInt == 2){
            bulanHuruf = "Februari"
        }else if(bulanInt == 3){
            bulanHuruf = "Maret"
        }else if(bulanInt == 4){
            bulanHuruf = "April"
        }else if(bulanInt == 5){
            bulanHuruf= "Mei"
        }else if(bulanInt == 6){
            bulanHuruf = "Juni"
        }else if(bulanInt == 7){
            bulanHuruf  = "Juli"
        }else if(bulanInt == 8){
            bulanHuruf  = "Agustus"
        }else if(bulanInt == 9){
            bulanHuruf  = "September"
        }else if(bulanInt == 10){
            bulanHuruf  = "Oktober"
        }else if(bulanInt == 11){
            bulanHuruf   = "November"
        }else if(bulanInt == 12){
            bulanHuruf  = "Desember"
        }else{
            bulanHuruf  = "Januari"
        }



        tvnmProyek.text = nama
        tvManagerProyek.text = manager
        tvDeadline.text = "$tanggal $bulanHuruf $tahun"
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


        btnNote.setOnClickListener {
            val gotoNote = Intent(this@UserDetailTugas,UserNoteBeranda::class.java)
            gotoNote.putExtra("proyekID",proyekID)
            gotoNote.putExtra("nmProyek",nama)
            gotoNote.putExtra("Programmer_1",programmer1)
            gotoNote.putExtra("Programmer_2",programmer2)
            gotoNote.putExtra("Programmer_3",programmer3)
            gotoNote.putExtra("Programmer_4",programmer4)
            startActivity(gotoNote)
        }
        ac.setOnClickListener {
            AcButton()
        }




    }



    override fun onResume() {
        super.onResume()
        usergetDetailProyek()//rendering data on this activity in resume
    }

    private fun userSetupadapter(){
        val proyekID = intent.getStringExtra("proyekID")
        val namaproyek = intent.getStringExtra("namaProyek")
        val manager = intent.getStringExtra("managerProyek")
        val Userfullname = intent.getStringExtra("UserFullname")
        val adapter = UsrDetailTugasAdapter(Userlistdataproyek)
        rvProyek.adapter = adapter

        adapter.setOnItemClickListener(object : UsrDetailTugasAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(this@UserDetailTugas, UserDetailStatus::class.java)
                intent.putExtra("proyekID",proyekID)
                intent.putExtra("nmProyek",namaproyek)
                intent.putExtra("managerProyek",manager)
                intent.putExtra("tanggal",Userlistdataproyek[position].tanggal)
                intent.putExtra("bulan",Userlistdataproyek[position].bulan)
                intent.putExtra("tahun",Userlistdataproyek[position].tahun)
                intent.putExtra("cekbox",Userlistdataproyek[position].cekbox)
                intent.putExtra("status",Userlistdataproyek[position].status)
                intent.putExtra("id",Userlistdataproyek[position].id)
                intent.putExtra("UserFullName",Userfullname.toString())

                startActivity(intent)
            }

        })
    }

    fun usergetDetailProyek(){
        database =  Firebase.database.reference
        val proyekID = intent.getStringExtra("proyekID").toString()
        database.child("DetailProyek").child(proyekID).addValueEventListener(object : ValueEventListener{
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



    private fun AcButton() {
        setvisibility(clicked)
        setanimation(clicked)
        clicked = !clicked
        setClikcable(clicked)
    }
    private fun setvisibility(clicked: Boolean){
        var noteT = findViewById<TextView>(R.id.Note_txt)
        var btnNote = findViewById<FloatingActionButton>(R.id.user_note)
        var ac = findViewById<FloatingActionButton>(R.id.User_ac)

        if(!clicked){
            noteT.visibility = View.VISIBLE
            btnNote.visibility = View.VISIBLE
        }else{
            noteT.visibility = View.INVISIBLE
            btnNote.visibility = View.INVISIBLE

        }

    }

    private fun setanimation(clicked: Boolean){
        var noteT = findViewById<TextView>(R.id.Note_txt)
        var btnNote = findViewById<FloatingActionButton>(R.id.user_note)
        var ac = findViewById<FloatingActionButton>(R.id.User_ac)

        if (!clicked) {
            noteT.startAnimation(toLeft)
            btnNote.startAnimation(fromBottom)
            ac.startAnimation(rotateOpen)
        }else{
            noteT.startAnimation(toBottom)
            btnNote.startAnimation(toBottom)
            ac.startAnimation(rotateClose)
        }

    }

    private fun setClikcable(clicked: Boolean){
        var btnNote = findViewById<FloatingActionButton>(R.id.user_note)
        var ac = findViewById<FloatingActionButton>(R.id.User_ac)
        if(!clicked){
            btnNote.isClickable = false
        } else{
            btnNote.isClickable = true
        }

    }
}