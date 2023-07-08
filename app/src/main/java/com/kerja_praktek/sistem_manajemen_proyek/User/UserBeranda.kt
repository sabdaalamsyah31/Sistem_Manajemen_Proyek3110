package com.kerja_praktek.sistem_manajemen_proyek.User

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
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
import com.kerja_praktek.sistem_manajemen_proyek.NotifPack.*
import com.kerja_praktek.sistem_manajemen_proyek.R
import com.kerja_praktek.sistem_manajemen_proyek.User.ViewHolder.UserBerandaAdapter

class UserBeranda : BaseActivity() {

    private val rotateOpen : Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.rotate_open_anim)}
    private val rotateClose : Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.rotate_close_anim)}
    private val fromBottom : Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.from_bottom_anim)}
    private val toBottom : Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.to_bottom_anim)}
    private val toLeft : Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.to_left_anim)}
    private val toRight : Animation by lazy{AnimationUtils.loadAnimation(this,R.anim.to_right_anim)}
    private lateinit var userRvBeranda : RecyclerView
    private lateinit var database: DatabaseReference
    private lateinit var userListProyek : ArrayList<ProyekInfo>
    private lateinit var notificationApi: NotificationAPI
    private var clicked = false

    private lateinit var sharedPref : PreferencesHelper

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_beranda)
        val btnAction = findViewById<FloatingActionButton>(R.id.btn_userAC)
        val btnLogOut = findViewById<FloatingActionButton>(R.id.btn_userLogout)
        val btnProfil = findViewById<FloatingActionButton>(R.id.btn_profil)
        UserGetProyekinfo()
        sharedPref = PreferencesHelper(this)

        val user = findViewById<TextView>(R.id.tv_userUsername)

//        sendNotification()
        val username = sharedPref.getString(Constant.PREF_NAMA).toString()
        user.text = username

        userRvBeranda = findViewById(R.id.user_rvProyek)
        userListProyek = arrayListOf<ProyekInfo>()
        userRvBeranda.layoutManager = LinearLayoutManager(this)
//        filterList()

        btnAction.setOnClickListener{
            onAddButtonClicked()
        }
        btnLogOut.setOnClickListener{
            sharedPref.clear()
            toLogOut()
        }
        btnProfil.setOnClickListener{
            val toProfil = Intent(applicationContext, user_editProfil::class.java)
            startActivity(toProfil)
        }
    }
    private fun toLogOut() {
        startActivity(Intent(this, Login::class.java))
        finish()
    }
//    filtering proyek user
    private fun UserGetProyekinfo(){
        database = FirebaseDatabase.getInstance().getReference("Proyek")
        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userListProyek.clear()
                if(snapshot.exists()){
                    for(snap in snapshot.children){
                        val username = sharedPref.getString(Constant.PREF_NAMA).toString()
                        val data = snap.getValue(ProyekInfo::class.java)
                        val adapter = UserBerandaAdapter(userListProyek)
                        if(data!!.programmer_1!!.contains(username)||
                            data.programmer_2!!.contains(username)||
                            data.programmer_3!!.contains(username)||
                            data.programmer_4!!.contains(username)||
                            data.manager!!.contains(username)){
                            userListProyek.add(data!!)

                        }
//                        else if(userListProyek.isEmpty()){
//                            fun pesan(){
//                                Toast.makeText(applicationContext,"Anda Belum Memiliki Proyek",Toast.LENGTH_SHORT).show()
//
//                            }
//                            pesan()
//                            toLogOut()
//                        }
                        else{
                            adapter.setFilteredList(userListProyek)
                        }
//

                    }
                    val adapter = UserBerandaAdapter(userListProyek)
                    userRvBeranda.adapter = adapter
                    adapter.setOnItemClickListener(object : UserBerandaAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@UserBeranda,UserDetailTugas::class.java)
//                            ini intent put extra place
                            intent.putExtra("proyekID",userListProyek[position].id)
                            intent.putExtra("namaProyek",userListProyek[position].namaProyek)
                            intent.putExtra("tanggal",userListProyek[position].tanggal)
                            intent.putExtra("bulan",userListProyek[position].bulan)
                            intent.putExtra("tahun",userListProyek[position].tahun)
                            intent.putExtra("managerProyek",userListProyek[position].manager)
                            intent.putExtra("programmer1",userListProyek[position].programmer_1)
                            intent.putExtra("programmer2",userListProyek[position].programmer_2)
                            intent.putExtra("programmer3",userListProyek[position].programmer_3)
                            intent.putExtra("programmer4",userListProyek[position].programmer_4)
                            intent.putExtra("UserFullname",sharedPref.getString(Constant.PREF_NAMA).toString())
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
    private fun onAddButtonClicked() {
        setvisibility(clicked)
        setanimation(clicked)
        clicked = !clicked
        setClikcable(clicked)
    }

    private fun setvisibility(clicked: Boolean) {
        val btnprofil = findViewById<FloatingActionButton>(R.id.btn_profil)
        val btnAction = findViewById<FloatingActionButton>(R.id.btn_userAC)
        val btnLogOut = findViewById<FloatingActionButton>(R.id.btn_userLogout)
        val txtlogout =  findViewById<TextView>(R.id.logout_txt)
        val txtprofil = findViewById<TextView>(R.id.profil_txt)
        if(!clicked){
            btnprofil.visibility = View.VISIBLE
            txtlogout.visibility = View.VISIBLE
            btnLogOut.visibility = View.VISIBLE
            txtprofil.visibility = View.VISIBLE
        }else{

           btnLogOut.visibility = View.INVISIBLE
            txtlogout.visibility = View.INVISIBLE
            btnprofil.visibility = View.INVISIBLE
            txtprofil.visibility = View.INVISIBLE
        }

    }

    private fun setanimation(clicked: Boolean) {
        val btnprofil = findViewById<FloatingActionButton>(R.id.btn_profil)
        val txtlogout =  findViewById<TextView>(R.id.logout_txt)
        val txtprofil = findViewById<TextView>(R.id.profil_txt)
        val btnAction = findViewById<FloatingActionButton>(R.id.btn_userAC)
        val btnLogOut = findViewById<FloatingActionButton>(R.id.btn_userLogout)
        if(!clicked){
            txtprofil.startAnimation(toLeft)
            txtlogout.startAnimation(toLeft)
            btnprofil.startAnimation(fromBottom)
            btnLogOut.startAnimation(fromBottom)
            btnAction.startAnimation(rotateOpen)
        }else{
            btnLogOut.startAnimation(toBottom)
            btnprofil.startAnimation(toBottom)
            btnAction.startAnimation(rotateClose)
            txtlogout.startAnimation(toBottom)
            txtprofil.startAnimation(toBottom)
        }
    }

    private fun setClikcable(clicked: Boolean) {
        val btnprofil = findViewById<FloatingActionButton>(R.id.btn_profil)
        val btnAction = findViewById<FloatingActionButton>(R.id.btn_userAC)
        val btnLogOut = findViewById<FloatingActionButton>(R.id.btn_userLogout)
        if (!clicked){
            btnprofil.isClickable = false
            btnLogOut.isClickable = false
        }else{
            btnprofil.isClickable = true
            btnLogOut.isClickable = true
        }
    }
}