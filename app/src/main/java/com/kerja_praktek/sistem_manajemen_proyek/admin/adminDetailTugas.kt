package com.kerja_praktek.sistem_manajemen_proyek.admin

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import java.io.File
import java.io.FileOutputStream


class adminDetailTugas : BaseActivity() {

//    animations
    private val rotateOpen : Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.rotate_open_anim)}
    private val rotateClose : Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.rotate_close_anim)}
    private val fromBottom : Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.from_bottom_anim)}
    private val toBottom : Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.to_bottom_anim)}
    private val toLeft : Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.to_left_anim)}
    private val toRight : Animation by lazy{AnimationUtils.loadAnimation(this,R.anim.to_right_anim)}

    private val TAG = "adminDetailTugas"
//    database
    private lateinit var database: DatabaseReference
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
    var PROYEKID = ""

    var pageHeight = 1120
    var pageWidth = 792

    // creating a bitmap variable
    // for storing our images
    lateinit var bmp: Bitmap
    lateinit var scaledbmp: Bitmap

    // on below line we are creating a
    // constant code for runtime permissions.
    var PERMISSION_CODE = 100

    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_detail_tugas)
        //init view
        val btnCetak = findViewById<FloatingActionButton>(R.id.printpdf)
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
        val proyekID = intent.getStringExtra("ID")
        val nama = intent.getStringExtra("namaProyek")
        val manager = intent.getStringExtra("managerProyek")
        val tanggal = intent.getStringExtra("tanggal")
        val bulan = intent.getStringExtra("bulan")
        val tahun = intent.getStringExtra("tahun")
        val programmer1 = intent.getStringExtra("programmer_1")
        val programmer2 = intent.getStringExtra("programmer_2")
        val programmer3 = intent.getStringExtra("programmer_3")
        val programmer4 = intent.getStringExtra("programmer_4")

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
        //setup data to view
        tvNamaProyek.text = nama
        tvdeadline.text = "$tanggal $bulanHuruf $tahun"
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
        PROYEKID = intent.getStringExtra("ID").toString()


//        inView()
//        DetailProyekView()
        getCekboxDetail()
//        cekboxclick()

        btnAction.setOnClickListener{
            showButton()
        }


        btnEdit.setOnClickListener{
            val ubah = Intent(this,adminEditTugas::class.java)
            ubah.putExtra("ID",proyekID)
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
            intent.putExtra("proyekID",proyekID)
            startActivity(intent)
        }

        bmp = BitmapFactory.decodeResource(resources,R.drawable._672226403878_removebg_preview)
        scaledbmp = Bitmap.createScaledBitmap(bmp,140,140,false)
        if(checkPermissions()){
            Toast.makeText(this@adminDetailTugas,"Permission Granted...", Toast.LENGTH_SHORT).show()
        }else{
            requestPermission()
        }

    }

    private fun showButton() {
        setvisibility(clicked)
        setanimation(clicked)
        clicked = !clicked
        setClikcable(clicked)
    }

    private fun setvisibility(clicked: Boolean) {
        val btnCetak = findViewById<FloatingActionButton>(R.id.printpdf)
        val btnTambah = findViewById<FloatingActionButton>(R.id.tambahdetail)
        val btnEdit = findViewById<FloatingActionButton>(R.id.btnEdit)
        val btnHapus = findViewById<FloatingActionButton>(R.id.btnHapus)
        val txt1 = findViewById<TextView>(R.id.hapus_P)
        val txt2 = findViewById<TextView>(R.id.Todolist_p)
        val txt3 = findViewById<TextView>(R.id.edit_p)
        val txt4 = findViewById<TextView>(R.id.pdf_p)

        if(!clicked){
            txt1.visibility = View.VISIBLE
            txt2.visibility = View.VISIBLE
            txt3.visibility = View.VISIBLE
            txt4.visibility = View.VISIBLE
            btnTambah.visibility = View.VISIBLE
            btnEdit.visibility = View.VISIBLE
            btnHapus.visibility = View.VISIBLE
            btnCetak.visibility = View.VISIBLE
        }else{
            btnTambah.visibility = View.INVISIBLE
            btnEdit.visibility = View.INVISIBLE
            btnHapus.visibility = View.INVISIBLE
            btnCetak.visibility = View.INVISIBLE
            txt1.visibility = View.INVISIBLE
            txt2.visibility = View.INVISIBLE
            txt3.visibility = View.INVISIBLE
            txt4.visibility = View.INVISIBLE
        }

    }

    private fun setanimation(clicked: Boolean) {
        val btnCetak = findViewById<FloatingActionButton>(R.id.printpdf)
        val btnTambah = findViewById<FloatingActionButton>(R.id.tambahdetail)
        val btnEdit = findViewById<FloatingActionButton>(R.id.btnEdit)
        val btnHapus = findViewById<FloatingActionButton>(R.id.btnHapus)
        val btnAction = findViewById<FloatingActionButton>(R.id.btn_fac)
        val txt1 = findViewById<TextView>(R.id.hapus_P)
        val txt2 = findViewById<TextView>(R.id.Todolist_p)
        val txt3 = findViewById<TextView>(R.id.edit_p)
        val txt4 = findViewById<TextView>(R.id.pdf_p)
        if(!clicked){
            txt1.startAnimation(toLeft)
            txt2.startAnimation(toLeft)
            txt3.startAnimation(toLeft)
            txt4.startAnimation(toLeft)
            btnCetak.startAnimation(fromBottom)
            btnEdit.startAnimation(fromBottom)
            btnTambah.startAnimation(fromBottom)
            btnHapus.startAnimation(fromBottom)
            btnAction.startAnimation(rotateOpen)
        }else{
            txt1.startAnimation(toBottom)
            txt2.startAnimation(toBottom)
            txt3.startAnimation(toBottom)
            txt4.startAnimation(toBottom)
            btnCetak.startAnimation(toBottom)
            btnTambah.startAnimation(toBottom)
            btnEdit.startAnimation(toBottom)
            btnHapus.startAnimation(toBottom)
            btnAction.startAnimation(rotateClose)
        }

    }

    private fun setClikcable(clicked: Boolean) {
        val btnCetak = findViewById<FloatingActionButton>(R.id.printpdf)
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
                intent.putExtra("proyekID",PROYEKID)
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
                intent.putExtra("proyekID",PROYEKID)
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
                        val proyekID = intent.getStringExtra("ID").toString()
                        val ID = listDetailProyek[position].id
//                        val nmdetail = listDetailProyek[position].cekbox
                        val delete = database.child("DetailProyek").child(proyekID).child(ID.toString())
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
        val ID = intent.getStringExtra("ID").toString()
        database.child("DetailProyek")
            .child(ID).addValueEventListener(object : ValueEventListener {
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
            .child(PROYEKID)
            .orderByChild("namaProyek")
            .equalTo("namaProyek")

        val querydetailProyek = database.child("DetailProyek")
            .child(PROYEKID)
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

    fun checkPermissions(): Boolean {
        var writeStoragePermission = ContextCompat.checkSelfPermission(
            applicationContext,
            WRITE_EXTERNAL_STORAGE
        )
        var readStoragePermission = ContextCompat.checkSelfPermission(
            applicationContext,
            READ_EXTERNAL_STORAGE
        )
        return writeStoragePermission == PackageManager.PERMISSION_GRANTED
                && readStoragePermission == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission(){
        ActivityCompat.requestPermissions(
            this, arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE),PERMISSION_CODE
        )
    }


}