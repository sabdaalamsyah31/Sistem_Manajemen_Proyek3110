package com.kerja_praktek.sistem_manajemen_proyek.User

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.kerja_praktek.sistem_manajemen_proyek.Base.BaseActivity
import com.kerja_praktek.sistem_manajemen_proyek.Helper.Constant
import com.kerja_praktek.sistem_manajemen_proyek.Helper.PreferencesHelper
import com.kerja_praktek.sistem_manajemen_proyek.Login
import com.kerja_praktek.sistem_manajemen_proyek.Model.ProyekInfo
import com.kerja_praktek.sistem_manajemen_proyek.R
import com.kerja_praktek.sistem_manajemen_proyek.User.ViewHolder.UserBerandaAdapter
import java.util.Locale

class UserBeranda : BaseActivity() {

    private val rotateOpen : Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.rotate_open_anim)}
    private val rotateClose : Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.rotate_close_anim)}
    private val fromBottom : Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.from_bottom_anim)}
    private val toBottom : Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.to_bottom_anim)}
    private lateinit var userRvBeranda : RecyclerView
    private lateinit var database: DatabaseReference
    private lateinit var userListProyek : ArrayList<ProyekInfo>
//    private lateinit var adapter:UserBerandaAdapter
    private var clicked = false

    private lateinit var sharedPref : PreferencesHelper

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_beranda)
        val btnAction = findViewById<FloatingActionButton>(R.id.btn_userAC)
        val btnLogOut = findViewById<FloatingActionButton>(R.id.btn_userLogout)
        UserGetProyekinfo()
        sharedPref = PreferencesHelper(this)
        val user = findViewById<TextView>(R.id.tv_userUsername)
//        val namauser = intent.getStringExtra("Username")

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



    }

    private fun toLogOut() {
        startActivity(Intent(this, Login::class.java))
        finish()
    }
//    filtering proyek user
    private fun filterList(){
        val username = sharedPref.getString(Constant.PREF_USERNAME).toString()
        val filteredList = ArrayList<ProyekInfo>()
        val adapter = UserBerandaAdapter(userListProyek)
        for(i in userListProyek){
            if(i.programmer_1!!.lowercase(Locale.ROOT).contains(username)|| i.programmer_2!!.lowercase(Locale.ROOT).contains(username)||i
                    .programmer_3!!.lowercase(Locale.ROOT).contains(username)||i.programmer_4!!.lowercase(Locale.ROOT).contains(username)||
                    i.manager!!.lowercase(Locale.ROOT).contains(username)){
                filteredList.add(i)
            }else if(filteredList.isEmpty()){
                Toast.makeText(this,"Anda Belum Memiliki Proyek",Toast.LENGTH_SHORT).show()
            }
            else{
                adapter.setFilteredList(filteredList)
            }
        }
    }

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
                            intent.putExtra("namaProyek",userListProyek[position].namaProyek)
                            intent.putExtra("tanggal",userListProyek[position].tanggal)
                            intent.putExtra("bulan",userListProyek[position].bulan)
                            intent.putExtra("tahun",userListProyek[position].tahun)
                            intent.putExtra("managerProyek",userListProyek[position].manager)
                            intent.putExtra("programmer1",userListProyek[position].programmer_1)
                            intent.putExtra("programmer2",userListProyek[position].programmer_2)
                            intent.putExtra("programmer3",userListProyek[position].programmer_3)
                            intent.putExtra("programmer4",userListProyek[position].programmer_4)
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
        val btnAction = findViewById<FloatingActionButton>(R.id.btn_userAC)
        val btnLogOut = findViewById<FloatingActionButton>(R.id.btn_userLogout)
        if(!clicked){

            btnLogOut.visibility = View.VISIBLE
        }else{

           btnLogOut.visibility = View.INVISIBLE
        }

    }

    private fun setanimation(clicked: Boolean) {
        val btnAction = findViewById<FloatingActionButton>(R.id.btn_userAC)
        val btnLogOut = findViewById<FloatingActionButton>(R.id.btn_userLogout)
        if(!clicked){
            btnLogOut.startAnimation(fromBottom)
            btnAction.startAnimation(rotateOpen)
        }else{
            btnLogOut.startAnimation(toBottom)
            btnAction.startAnimation(rotateClose)
        }
    }

    private fun setClikcable(clicked: Boolean) {
        val btnAction = findViewById<FloatingActionButton>(R.id.btn_userAC)
        val btnLogOut = findViewById<FloatingActionButton>(R.id.btn_userLogout)
        if (!clicked){
            btnLogOut.isClickable = false
        }else{
            btnLogOut.isClickable = true
        }
    }
}