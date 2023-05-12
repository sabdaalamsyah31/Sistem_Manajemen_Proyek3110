package com.kerja_praktek.sistem_manajemen_proyek

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kerja_praktek.sistem_manajemen_proyek.Base.BaseActivity
import com.kerja_praktek.sistem_manajemen_proyek.Helper.Constant
import com.kerja_praktek.sistem_manajemen_proyek.Helper.PreferencesHelper
import com.kerja_praktek.sistem_manajemen_proyek.Model.UsersInfo
import com.kerja_praktek.sistem_manajemen_proyek.User.UserBeranda
import com.kerja_praktek.sistem_manajemen_proyek.admin.adminBeranda


class Login : BaseActivity() {
    private val TAG = "Login"

    private lateinit var database: DatabaseReference
//    private lateinit var auth : FirebaseAuthr
    private lateinit var sharedPref : PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var editUser = findViewById<EditText>(R.id.edt_Username)
        var editPass = findViewById<EditText>(R.id.edt_Password)

//        Button
        var btnLogin = findViewById<Button>(R.id.btn_Login)

        database = Firebase.database.reference
//        auth = FirebaseAuth.getInstance()
        sharedPref = PreferencesHelper(this)

        fun startLogin(){
            val username = editUser.text.toString().trim()
            val password = editPass.text.toString().trim()


            if (username.isEmpty()) {
                Toast.makeText(applicationContext,"Username Kosong", Toast.LENGTH_SHORT).show()
            } else if (password.isEmpty()) {
                Toast.makeText(applicationContext,"Password Kosong", Toast.LENGTH_SHORT).show()
            }else if(username.equals("admin")&&password.equals("admin")){
                Log.d(TAG, "onCreate: $username , $password")
                val Username = "Admin"
                database.child("Admins")
                    .child(Username)
                    .addValueEventListener(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val Userdata = snapshot.getValue(UsersInfo::class.java)

                            if (Userdata == null){
                                Toast.makeText(applicationContext,"Database Kosong", Toast.LENGTH_SHORT).show()

                            }else{
                                val resultUsername = Userdata.Username.toString()
                                val resultPassword = Userdata.Password.toString()
                                val resultJabatan = Userdata.Jabatan.toString()
                                val resultNama = Userdata.Nama.toString()



                                if (username.equals(resultUsername) && password.equals(resultPassword)){
                                    sharedPref.put(Constant.PREF_USERNAME,resultUsername)
                                    sharedPref.put(Constant.PREF_PASSWORD,resultPassword)
                                    sharedPref.put(Constant.PREF_JABATAN,resultJabatan)
                                    sharedPref.put(Constant.PREF_NAMA,resultNama)
                                    sharedPref.put(Constant.PREF_IS_LOGIN,true)
                                    Toast.makeText(applicationContext,"Selamat Datang $resultUsername Sebagai $resultJabatan", Toast.LENGTH_SHORT).show()

                                    val GotoAdminBeranda = Intent(applicationContext,adminBeranda::class.java)
                                    GotoAdminBeranda.putExtra("Username",Userdata.Nama)
                                    startActivity(GotoAdminBeranda)
                                } else{
                                    Toast.makeText(applicationContext,"Data Login Salah", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.d(TAG, "onCancelled: ${error.message}")
                        }

                    })
            }


            else if(username.equals(username)&&password.equals(password)) {
                val ProgrammerUser = username
                Log.d(TAG, "onCreate: $username , $password")
                database.child("Users").child(ProgrammerUser).addValueEventListener(object:ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val Userdata = snapshot.getValue(UsersInfo::class.java)
                        if(Userdata==null){
                            Toast.makeText(applicationContext,"Username tidak terdaftar", Toast.LENGTH_SHORT).show()
                        }else{

                            val Resultusername = Userdata.Username.toString()
                            val Resultpassword = Userdata.Password.toString()
                            val Resultjabatan = Userdata.Jabatan.toString()
                            val Resultnama = Userdata.Nama.toString()



                            if (username.equals(Resultusername)&&password.equals(Resultpassword)){
                                sharedPref.put(Constant.PREF_USERNAME,Resultusername)
                                sharedPref.put(Constant.PREF_PASSWORD,Resultpassword)
                                sharedPref.put(Constant.PREF_JABATAN,Resultjabatan)
                                sharedPref.put(Constant.PREF_NAMA,Resultnama)
                                sharedPref.put(Constant.PREF_IS_LOGIN,true)
                                Toast.makeText(applicationContext,"Selamat Datang $Resultusername sebagai $Resultjabatan", Toast.LENGTH_SHORT).show()
                                val GotoUserBeranda = Intent(applicationContext,UserBeranda::class.java)
                                GotoUserBeranda.putExtra("Username",Userdata.Username)
                                startActivity(GotoUserBeranda)
                            } else{}
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
            }
            else {}
//                if(username.equals("admin")&&password.equals("admin"))
        }
        btnLogin.setOnClickListener {
            startLogin()
        }



    }
 override fun onStart(){
     super.onStart()
     if(sharedPref.getBoolean(Constant.PREF_IS_LOGIN)){
            moveIntent()
            }
        }
    private fun moveIntent(){
        val jabatan = "Administrator"
        if (sharedPref.getString(Constant.PREF_JABATAN).equals(jabatan)){
                    startActivity(Intent(this, adminBeranda::class.java))
        }else{
            startActivity(Intent(this,UserBeranda::class.java))
        }
//        sharedPref.getString(Constant.PREF_JABATAN)

//        finish()

    }
    }
