package com.kerja_praktek.sistem_manajemen_proyek.admin

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kerja_praktek.sistem_manajemen_proyek.Base.BaseActivity
import com.kerja_praktek.sistem_manajemen_proyek.Helper.Constant
import com.kerja_praktek.sistem_manajemen_proyek.Helper.PreferencesHelper
import com.kerja_praktek.sistem_manajemen_proyek.Login
import com.kerja_praktek.sistem_manajemen_proyek.Model.ProyekInfo
import com.kerja_praktek.sistem_manajemen_proyek.R
import com.kerja_praktek.sistem_manajemen_proyek.Register
import com.kerja_praktek.sistem_manajemen_proyek.admin.ViewHolder.adminBerandaAdapter

class adminBeranda : BaseActivity() {
    private val rotateOpen : Animation by lazy {AnimationUtils.loadAnimation(this,R.anim.rotate_open_anim)}
    private val rotateClose : Animation by lazy {AnimationUtils.loadAnimation(this,R.anim.rotate_close_anim)}
    private val fromBottom : Animation by lazy {AnimationUtils.loadAnimation(this,R.anim.from_bottom_anim)}
    private val toBottom : Animation by lazy {AnimationUtils.loadAnimation(this,R.anim.to_bottom_anim)}
    private val toLeft : Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.to_left_anim)}
    private val toRight : Animation by lazy{AnimationUtils.loadAnimation(this,R.anim.to_right_anim)}

    private lateinit var sharedPref : PreferencesHelper

    private lateinit var rv_item : RecyclerView
    private lateinit var database: DatabaseReference
    private lateinit var ListProyek: ArrayList<ProyekInfo>
    private var clicked = false
    var state = false
    val PERMISSIONS = arrayOf(

//        Make Permissions
        Manifest.permission.POST_NOTIFICATIONS,
        Manifest.permission.INTERNET,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_beranda)
        val logOut = findViewById<FloatingActionButton>(R.id.btn_logOut)
        val action = findViewById<FloatingActionButton>(R.id.btn_ac)
        val btnTambahkan = findViewById<FloatingActionButton>(R.id.btnTambahkanProyek)
        var btnRegister = findViewById<FloatingActionButton>(R.id.btn_Daftar)
        sharedPref = PreferencesHelper(this)
        val user = findViewById<TextView>(R.id.tv_username)

        val username = sharedPref.getString(Constant.PREF_NAMA)
        user.text = username.toString()

        rv_item = findViewById(R.id.rv_Proyek)
        rv_item.layoutManager = LinearLayoutManager(this)
        ListProyek = arrayListOf<ProyekInfo>()

        validate()

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
    }



    private fun validate() {
        if(!hasPermission(*PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS,0)
        }else{

        }
    }
    private fun hasPermission(vararg permissions: String): Boolean {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M && permissions != null){
            for(permission in permissions){
                if(ContextCompat.checkSelfPermission(
                        this,
                        permission!!
                )!= PackageManager.PERMISSION_GRANTED
                ){
                    return false
                }
            }

        }
            return true
    }
    private fun checkPermissionRequest(vararg permissions: String?): Boolean {
        var status = false
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M && permissions != null){
            for (permission in permissions){
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, permission!!)){
                    status = true
                }
            }
        }
        return status
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode){
            0 ->{
                if(checkPermissionRequest(*PERMISSIONS)){
                    showDialogPermission()
                    
                }
            }
        }
    }

    fun showDialogPermission() {
        AlertDialog.Builder(this)
            .setTitle("Izin Ditolak")
            .setMessage("Login membutuhkan izin membaca keadaan ponsel, Lokasi, Penyimpanan, dan Telepon.\nPergi ke setting untuk mengaktifkan.")
            .setCancelable(true)
            .setOnCancelListener(DialogInterface.OnCancelListener {
                finish()
            })
            .setNeutralButton(
                "Pergi ke Setting"
            ) { dialogInterface, i ->
                state = true
                startActivity(
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:$packageName")
                    )
                )
            }.show()
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
        val txt1 = findViewById<TextView>(R.id.out_txt)
        val txt2 = findViewById<TextView>(R.id.register_txt)
        val txt3 = findViewById<TextView>(R.id.plus_txt)

        if(!clicked){
            txt1.visibility = View.VISIBLE
            txt2.visibility = View.VISIBLE
            txt3.visibility = View.VISIBLE
            btnTambahkan.visibility = View.VISIBLE
            logOut.visibility = View.VISIBLE
            btnRegister.visibility = View.VISIBLE
        }else{
            txt1.visibility = View.INVISIBLE
            txt2.visibility = View.INVISIBLE
            txt3.visibility = View.INVISIBLE
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
        val txt1 = findViewById<TextView>(R.id.out_txt)
        val txt2 = findViewById<TextView>(R.id.register_txt)
        val txt3 = findViewById<TextView>(R.id.plus_txt)
        if (!clicked) {
            txt1.startAnimation(toLeft)
            txt2.startAnimation(toLeft)
            txt3.startAnimation(toLeft)
            btnTambahkan.startAnimation(fromBottom)
            logOut.startAnimation(fromBottom)
            btnRegister.startAnimation(fromBottom)
            action.startAnimation(rotateOpen)
        }else{
            txt1.startAnimation(toBottom)
            txt2.startAnimation(toBottom)
            txt3.startAnimation(toBottom)
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
                            intent.putExtra("ID", ListProyek[position].id)
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
                }else{

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    override fun onRestart() {
        super.onRestart()
        //Log.d(TAG, "onRestart: ")
        validate()
    }
}
