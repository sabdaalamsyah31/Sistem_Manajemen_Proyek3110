package com.kerja_praktek.sistem_manajemen_proyek.admin

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
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
import com.kerja_praktek.sistem_manajemen_proyek.Register
import com.kerja_praktek.sistem_manajemen_proyek.Send_Notif
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
        var btnRegister = findViewById<FloatingActionButton>(R.id.btn_Daftar)
        var btnNotif = findViewById<FloatingActionButton>(R.id.btn_notif)
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
        btnRegister.setOnClickListener {
            val toRegister = Intent(applicationContext, Register::class.java)
            startActivity(toRegister)
        }
        btnNotif.setOnClickListener {
            val toSendNotif = Intent(applicationContext, Send_Notif::class.java)
            startActivity(toSendNotif)
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
        val btnRegister = findViewById<FloatingActionButton>(R.id.btn_Daftar)

        if(!clicked){
            btnTambahkan.visibility = View.VISIBLE
            logOut.visibility = View.VISIBLE
            btnRegister.visibility = View.VISIBLE
        }else{
            btnTambahkan.visibility = View.INVISIBLE
            logOut.visibility = View.INVISIBLE
            btnRegister.visibility = View.INVISIBLE
        }
    }
    private fun setanimation(clicked: Boolean) {
        val logOut = findViewById<FloatingActionButton>(R.id.btn_logOut)
        val action = findViewById<FloatingActionButton>(R.id.btn_ac)
        val btnTambahkan = findViewById<FloatingActionButton>(R.id.btnTambahkanProyek)
        val btnRegister = findViewById<FloatingActionButton>(R.id.btn_Daftar)
        if (!clicked) {
            btnTambahkan.startAnimation(fromBottom)
            logOut.startAnimation(fromBottom)
            btnRegister.startAnimation(fromBottom)
            action.startAnimation(rotateOpen)
        }else{
            btnTambahkan.startAnimation(toBottom)
            logOut.startAnimation(toBottom)
            btnRegister.startAnimation(toBottom)
            action.startAnimation(rotateClose)
        }
    }

    private fun setClikcable(clicked:Boolean){
        val logOut = findViewById<FloatingActionButton>(R.id.btn_logOut)
        val action = findViewById<FloatingActionButton>(R.id.btn_ac)
        val btnTambahkan = findViewById<FloatingActionButton>(R.id.btnTambahkanProyek)
        val btnRegister = findViewById<FloatingActionButton>(R.id.btn_Daftar)
        if(!clicked){
            btnTambahkan.isClickable = false
            logOut.isClickable = false
            btnRegister.isClickable = false
        } else{
            btnTambahkan.isClickable = true
            logOut.isClickable = true
            btnRegister.isClickable = true
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

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }



    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

}
