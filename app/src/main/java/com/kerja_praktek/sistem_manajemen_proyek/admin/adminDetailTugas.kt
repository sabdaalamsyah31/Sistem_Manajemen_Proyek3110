package com.kerja_praktek.sistem_manajemen_proyek.admin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kerja_praktek.sistem_manajemen_proyek.Base.BaseActivity
import com.kerja_praktek.sistem_manajemen_proyek.Model.DetailInfo
import com.kerja_praktek.sistem_manajemen_proyek.R
import com.kerja_praktek.sistem_manajemen_proyek.Util.DialogUtil
import com.kerja_praktek.sistem_manajemen_proyek.admin.ViewHolder.adminDetailtugasAdapter

class adminDetailTugas : BaseActivity() {

//    animations
    private val rotateOpen : Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.rotate_open_anim)}
    private val rotateClose : Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.rotate_close_anim)}
    private val fromBottom : Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.from_bottom_anim)}
    private val toBottom : Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.to_bottom_anim)}

    private val TAG = "adminDetailTugas"
//    database
    private lateinit var database: DatabaseReference
//    private lateinit var viewHolder: adminDetailtugasAdapter.ViewHolder
    //view
    private lateinit var tvPersen : TextView //camelCase
    private lateinit var tvNamaProyek: TextView
    private lateinit var tvManagerProyek: TextView
    private lateinit var tvdeadline: TextView
    private lateinit var tvProgrammer_1: TextView
    private lateinit var tvProgrammer_2: TextView
    private lateinit var tvProgrammer_3: TextView
    private lateinit var tvProgrammer_4: TextView
    private lateinit var btnAction:FloatingActionButton
    private lateinit var btnHapus:FloatingActionButton
    private lateinit var btnEdit:FloatingActionButton
    private lateinit var rvProyekDetail:RecyclerView
    private lateinit var listDetailProyek:ArrayList<DetailInfo> //camelCase
    private var clicked = false
    //variable
    var namaproyek = ""

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_detail_tugas)
        //init view
        var btnTambah = findViewById<ImageButton>(R.id.tambahdetail)
        tvPersen = findViewById(R.id.txt_persen)
        btnAction = findViewById(R.id.btn_fac)
        btnHapus = findViewById(R.id.btnHapus)
        btnEdit = findViewById(R.id.btnEdit)
        tvNamaProyek = findViewById(R.id.tvNamaProyek)
        tvManagerProyek = findViewById(R.id.tvManagerProyek)
        tvdeadline = findViewById(R.id.deadline)
        tvProgrammer_1 = findViewById(R.id.tv_Programmer_1)
        tvProgrammer_2 = findViewById(R.id.tv_Programmer_2)
        tvProgrammer_3 = findViewById(R.id.tv_Programmer_3)
        tvProgrammer_4 = findViewById(R.id.tv_Programmer_4)
//        btnHapus = findViewById(R.id.btnkumai)
//        btnEdit = findViewById(R.id.btnhilir)
        //get data
        val nama = intent.getStringExtra("namaProyek")
        val manager = intent.getStringExtra("managerProyek")
        val tanggal = intent.getStringExtra("tanggal")
        val bulan = intent.getStringExtra("bulan")
        val tahun = intent.getStringExtra("tahun")
        val programmer1 = intent.getStringExtra("programmer_1")
        val programmer2 = intent.getStringExtra("programmer_2")
        val programmer3 = intent.getStringExtra("programmer_3")
        val programmer4 = intent.getStringExtra("programmer_4")
        //setup data to view
        tvNamaProyek.text = nama
        tvdeadline.text = "$tanggal-$bulan-$tahun"
        tvManagerProyek.text = manager
        tvProgrammer_1.text = programmer1
        tvProgrammer_2.text = programmer2
        tvProgrammer_3.text = programmer3
        tvProgrammer_4.text = programmer4

//        setup Database
        database = Firebase.database.reference
        //setup rycleView
        rvProyekDetail = findViewById(R.id.rvProyekDetail)
        rvProyekDetail.layoutManager = LinearLayoutManager(this)
        listDetailProyek = arrayListOf<DetailInfo>()
        namaproyek = intent.getStringExtra("namaProyek").toString()

//        inView()
//        DetailProyekView()
        getCekboxDetail()
//        cekboxclick()

        btnAction.setOnClickListener{
            showButton()
        }


        btnEdit.setOnClickListener{
            val ubah = Intent(this,adminEditTugas::class.java)
            ubah.putExtra("namaProyek",nama)
            ubah.putExtra("tanggal",tanggal)
            ubah.putExtra("bulan",bulan)
            ubah.putExtra("tahun",tahun)
            ubah.putExtra("managerProyek",manager)
            ubah.putExtra("programmer_1",programmer1)
            ubah.putExtra("programmer_2",programmer2)
            ubah.putExtra("programmer_3",programmer3)
            ubah.putExtra("programmer_4",programmer4)
            startActivityForResult(ubah,10)

        }

        btnHapus.setOnClickListener{
            DialogUtil().showAlertDialog(this@adminDetailTugas,"Apakah Yakin Menghapus Proyek $namaproyek ?",
                {// on yes
                    goDelete()
                    finish()
                }
            ,
                { // on no action
                    Toast.makeText(applicationContext,"DiBatalkan",Toast.LENGTH_SHORT).show()
                }
            )
        }


        btnTambah.setOnClickListener {
            val intent = Intent(this@adminDetailTugas,adminTambahTugas_DetailLagi::class.java)
            intent.putExtra("nmProyek",nama)
            startActivity(intent)
        }

    }

    private fun showButton() {
        setvisibility(clicked)
        setanimation(clicked)
        clicked = !clicked
        setClikcable(clicked)
    }

    private fun setvisibility(clicked: Boolean) {
        val btnTambah = findViewById<FloatingActionButton>(R.id.tambahdetail)
        val btnEdit = findViewById<FloatingActionButton>(R.id.btnEdit)
        val btnHapus = findViewById<FloatingActionButton>(R.id.btnHapus)

        if(!clicked){
            btnTambah.visibility = View.VISIBLE
            btnEdit.visibility = View.VISIBLE
            btnHapus.visibility = View.VISIBLE
        }else{
            btnTambah.visibility = View.INVISIBLE
            btnEdit.visibility = View.INVISIBLE
            btnHapus.visibility = View.INVISIBLE
        }

    }

    private fun setanimation(clicked: Boolean) {
        val btnTambah = findViewById<FloatingActionButton>(R.id.tambahdetail)
        val btnEdit = findViewById<FloatingActionButton>(R.id.btnEdit)
        val btnHapus = findViewById<FloatingActionButton>(R.id.btnHapus)
        val btnAction = findViewById<FloatingActionButton>(R.id.btn_fac)

        if(!clicked){
            btnEdit.startAnimation(fromBottom)
            btnTambah.startAnimation(fromBottom)
            btnHapus.startAnimation(fromBottom)
            btnAction.startAnimation(rotateOpen)
        }else{
            btnTambah.startAnimation(toBottom)
            btnEdit.startAnimation(toBottom)
            btnHapus.startAnimation(toBottom)
            btnAction.startAnimation(rotateClose)
        }

    }

    private fun setClikcable(clicked: Boolean) {
        val btnTambah = findViewById<FloatingActionButton>(R.id.tambahdetail)
        val btnEdit = findViewById<FloatingActionButton>(R.id.btnEdit)
        val btnHapus = findViewById<FloatingActionButton>(R.id.btnHapus)

        if(!clicked){
            btnTambah.isClickable = false
            btnEdit.isClickable = false
            btnHapus.isClickable = false
        }else{
            btnTambah.isClickable = true
            btnEdit.isClickable = true
            btnHapus.isClickable = true
        }
    }

    override fun onResume() {
        super.onResume()
        getCekboxDetail()//rendering data on this activity in resume
    }

    private fun setupAdapter(){
        val adapter = adminDetailtugasAdapter(listDetailProyek)
        rvProyekDetail.adapter = adapter
        adapter.setOnItemClickListener(object : adminDetailtugasAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(this@adminDetailTugas,adminDetailstatus::class.java)
                intent.putExtra("nmProyek",namaproyek)
                intent.putExtra("tanggal",listDetailProyek[position].tanggal)
                intent.putExtra("bulan",listDetailProyek[position].bulan)
                intent.putExtra("tahun",listDetailProyek[position].tahun)
                intent.putExtra("cekbox",listDetailProyek[position].cekbox)
                intent.putExtra("status",listDetailProyek[position].status)
                intent.putExtra("id",listDetailProyek[position].id)
                startActivity(intent)

            }
        })

        adapter.setOnEditClickListener(object : adminDetailtugasAdapter.btneditClickListener{
            override fun onbtneditClick(position: Int) {
                val intent = Intent(this@adminDetailTugas,adminEditNamaDetail::class.java)
                intent.putExtra("nmProyek",namaproyek)
                intent.putExtra("tanggal",listDetailProyek[position].tanggal)
                intent.putExtra("bulan",listDetailProyek[position].bulan)
                intent.putExtra("tahun",listDetailProyek[position].tahun)
                intent.putExtra("cekbox",listDetailProyek[position].cekbox)
                intent.putExtra("id",listDetailProyek[position].id)
                intent.putExtra("Status",listDetailProyek[position].status)
                startActivity(intent)
            }

        })

        adapter.setOnHapusClickListener(object :adminDetailtugasAdapter.btnhapusClickListener{
            override fun onbtnhapusClick(position: Int) {
                val detail = listDetailProyek[position]
                DialogUtil().showAlertDialog(this@adminDetailTugas,"Apakah Yakin Menghapus Detail Proyek ${detail.cekbox} ?",
                    {// on yes
                        Toast.makeText(this@adminDetailTugas,"Detail Dihapus",Toast.LENGTH_SHORT).show()
                        val namadetailProyek = namaproyek
                        val ID = listDetailProyek[position].id
//                        val nmdetail = listDetailProyek[position].cekbox
                        val delete = database.child("DetailProyek").child(namadetailProyek).child(ID.toString())
                        delete.addValueEventListener(object : ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                snapshot.ref.removeValue()
                                getCekboxDetail()
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                        })
                    }
                    ,
                    { // on no action
                        Toast.makeText(applicationContext,"DiBatalkan",Toast.LENGTH_SHORT).show()
                    }
                )

            }

        })
    }

    private fun getCekboxDetail() {
// note pada database nama child harus sama dengan nama proyek
        database = Firebase.database.reference
        val namaproyek = intent.getStringExtra("namaProyek").toString()
        database.child("DetailProyek")
            .child(namaproyek).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    listDetailProyek.clear()
                    if (snapshot.exists()) {
                        for (snap in snapshot.children) {
                            val data = snap.getValue(DetailInfo::class.java)
                            listDetailProyek.add(data!!)
                        }
                        setupAdapter()
                        percentage()
                    }
                }
                override fun onCancelled(error: DatabaseError) {

                }

            })
    }

    private fun percentage(){
        if (listDetailProyek.size > 0) {
            val sizeList = listDetailProyek.size.toDouble()
            var countDone = 0.0
            for (item in listDetailProyek){
                if (item.status){// status == true
                    countDone++
                }
            }
            val result = (countDone / sizeList) * 100.0
            Log.d(TAG, "percentage: ${result.toInt()}")
            tvPersen.text = result.toInt().toString()
        }
    }

    fun goDelete(){
        val querynamaproyek = database.child("Proyek")
            .child(namaproyek)
            .orderByChild("namaProyek")
            .equalTo("namaProyek")

        val querydetailProyek = database.child("DetailProyek")
            .child(namaproyek)
            .orderByChild("namaDetail")
            .equalTo("namaDetail")

        querydetailProyek.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.ref.removeValue()
                //render new list
                getCekboxDetail()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        querynamaproyek.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.ref.removeValue()
                getCekboxDetail()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}