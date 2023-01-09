package com.kerja_praktek.sistem_manajemen_proyek

import android.content.Intent
import android.os.Bundle
import android.view.View
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
import com.kerja_praktek.sistem_manajemen_proyek.Model.UsersInfo
import com.kerja_praktek.sistem_manajemen_proyek.admin.adminBeranda


class Login : BaseActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_login)

        var editUser = findViewById<EditText>(R.id.edt_Username)
        var editPass = findViewById<EditText>(R.id.edt_Password)

//        Button
        var btnLogin = findViewById<Button>(R.id.btn_Login)

        database = Firebase.database.reference


        btnLogin.setOnClickListener {
            val username = editUser.text.toString()
            val password = editPass.text.toString()
            if (username.isEmpty()) {
                Toast.makeText(applicationContext,"Username Kosong", Toast.LENGTH_SHORT).show()
            } else if (password.isEmpty()) {
                Toast.makeText(applicationContext,"Password Kosong", Toast.LENGTH_SHORT).show()
            }else{
                val Username = "Admin"
                database.child("Users")
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



                                if (username.equals(resultUsername) && password.equals(resultPassword)){
                                    Toast.makeText(applicationContext,"Selamat Datang $resultUsername Sebagai $resultJabatan", Toast.LENGTH_SHORT).show()

                                    val GotoAdminBeranda = Intent(applicationContext,adminBeranda::class.java)
                                    startActivity(GotoAdminBeranda)
                                } else{
                                    Toast.makeText(applicationContext,"Data Login Salah", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })
            }
        }
    }
//    btnBersihkan.setOnClickListener {
//        editEmail.setText("")
//        editPassword.setText("")
//    }

    }
