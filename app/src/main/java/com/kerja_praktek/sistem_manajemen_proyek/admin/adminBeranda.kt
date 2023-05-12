package com.kerja_praktek.sistem_manajemen_proyek.admin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.kerja_praktek.sistem_manajemen_proyek.Base.BaseActivity
import com.kerja_praktek.sistem_manajemen_proyek.Helper.Constant
import com.kerja_praktek.sistem_manajemen_proyek.Helper.PreferencesHelper
import com.kerja_praktek.sistem_manajemen_proyek.Login
import com.kerja_praktek.sistem_manajemen_proyek.Model.ProyekInfo
import com.kerja_praktek.sistem_manajemen_proyek.R
import com.kerja_praktek.sistem_manajemen_proyek.admin.ViewHolder.adminBerandaAdapter

class adminBeranda : BaseActivity() {
    private val rotateOpen : Animation by lazy {AnimationUtils.loadAnimation(this,R.anim.rotate_open_anim)}
    private val rotateClose : Animation by lazy {AnimationUtils.loadAnimation(this,R.anim.rotate_close_anim)}
    private val fromBottom : Animation by lazy {AnimationUtils.loadAnimation(this,R.anim.from_bottom_anim)}
    private val toBottom : Animation by lazy {AnimationUtils.loadAnimation(this,R.anim.to_bottom_anim)}

    private lateinit var sharedPref : PreferencesHelper

    private lateinit var rv_item : RecyclerView
    private lateinit var database: DatabaseReference
    private lateinit var ListProyek: ArrayList<ProyekInfo>
    private var clicked = false
//    @SuppressLint("MissingInflatedId")
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_beranda)
        val logOut = findViewById<FloatingActionButton>(R.id.btn_logOut)
        val action = findViewById<FloatingActionButton>(R.id.btn_ac)
        val btnTambahkan = findViewById<FloatingActionButton>(R.id.btnTambahkanProyek)
        sharedPref = PreferencesHelper(this)
        val user = findViewById<TextView>(R.id.tv_username)
//        val namauser = intent.getStringExtra("Username")

        val username = sharedPref.getString(Constant.PREF_NAMA)
        user.text = username.toString()

        rv_item = findViewById(R.id.rv_Proyek)
        rv_item.layoutManager = LinearLayoutManager(this)
        ListProyek = arrayListOf<ProyekInfo>()

        database = FirebaseDatabase.getInstance().getReference("Proyek")

        getProyek()


        action.setOnClickListener {
        onAddButtonClicked()
        }
        btnTambahkan.setOnClickListener {
            val intent = Intent(applicationContext, adminTambahkanTugas::class.java)
            startActivity(intent)
        }
        logOut.setOnClickListener {
            sharedPref.clear()
            toLogout()
        }

    }
    private fun toLogout() {
        startActivity(Intent(this, Login::class.java))
        finish()
    }

    private fun onAddButtonClicked() {
        setvisibility(clicked)
        setanimation(clicked)
        clicked = !clicked
        setClikcable(clicked)
    }
    private fun setvisibility(clicked: Boolean) {
        val logOut = findViewById<FloatingActionButton>(R.id.btn_logOut)
        val action = findViewById<FloatingActionButton>(R.id.btn_ac)
        val btnTambahkan = findViewById<FloatingActionButton>(R.id.btnTambahkanProyek)

        if(!clicked){
            btnTambahkan.visibility = View.VISIBLE
            logOut.visibility = View.VISIBLE
        }else{
            btnTambahkan.visibility = View.INVISIBLE
            logOut.visibility = View.INVISIBLE
        }
    }
    private fun setanimation(clicked: Boolean) {
        val logOut = findViewById<FloatingActionButton>(R.id.btn_logOut)
        val action = findViewById<FloatingActionButton>(R.id.btn_ac)
        val btnTambahkan = findViewById<FloatingActionButton>(R.id.btnTambahkanProyek)
        if (!clicked) {
            btnTambahkan.startAnimation(fromBottom)
            logOut.startAnimation(fromBottom)
            action.startAnimation(rotateOpen)
        }else{
            btnTambahkan.startAnimation(toBottom)
            logOut.startAnimation(toBottom)
            action.startAnimation(rotateClose)
        }
    }

    private fun setClikcable(clicked:Boolean){
        val logOut = findViewById<FloatingActionButton>(R.id.btn_logOut)
        val action = findViewById<FloatingActionButton>(R.id.btn_ac)
        val btnTambahkan = findViewById<FloatingActionButton>(R.id.btnTambahkanProyek)
        if(!clicked){
            btnTambahkan.isClickable = false
            logOut.isClickable = false
        } else{
            btnTambahkan.isClickable = true
            logOut.isClickable = true
        }
    }



    private fun getProyek() {

//        rv_item.visibility = View.VISIBLE
        database= FirebaseDatabase.getInstance().getReference("Proyek")

        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                ListProyek.clear()
                if(snapshot.exists()){
                    for (snap in snapshot.children){
                        Log.d("TAG", "onDataChange: $snap.")
                        val data =  snap.getValue(ProyekInfo::class.java)
                        ListProyek.add(data!!)
                    }
                    val adapter = adminBerandaAdapter(ListProyek)
                    rv_item.adapter = adapter
                    adapter.setOnItemClickListener(object : adminBerandaAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@adminBeranda,adminDetailTugas::class.java)

                            intent.putExtra("namaProyek",ListProyek[position].namaProyek)
                            intent.putExtra("tanggal",ListProyek[position].tanggal)
                            intent.putExtra("bulan",ListProyek[position].bulan)
                            intent.putExtra("tahun",ListProyek[position].tahun)
                            intent.putExtra("managerProyek",ListProyek[position].manager)
                            intent.putExtra("programmer_1",ListProyek[position].programmer_1)
                            intent.putExtra("programmer_2",ListProyek[position].programmer_2)
                            intent.putExtra("programmer_3",ListProyek[position].programmer_3)
                            intent.putExtra("programmer_4",ListProyek[position].programmer_4)
                            startActivity(intent)
                        }
                    })
                }else{}
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


}
