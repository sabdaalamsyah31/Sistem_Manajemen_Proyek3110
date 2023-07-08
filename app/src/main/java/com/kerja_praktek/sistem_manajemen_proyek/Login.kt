package com.kerja_praktek.sistem_manajemen_proyek

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import com.kerja_praktek.sistem_manajemen_proyek.Base.BaseActivity
import com.kerja_praktek.sistem_manajemen_proyek.Helper.Constant
import com.kerja_praktek.sistem_manajemen_proyek.Helper.PreferencesHelper
import com.kerja_praktek.sistem_manajemen_proyek.Model.UsersInfo
//import com.kerja_praktek.sistem_manajemen_proyek.NotifPack.FirebaseIDService
import com.kerja_praktek.sistem_manajemen_proyek.User.UserBeranda
import com.kerja_praktek.sistem_manajemen_proyek.admin.adminBeranda


class Login : BaseActivity() {
    private val TAG = "Login"

    private lateinit var database: DatabaseReference
//    private lateinit var auth : FirebaseAuthr
    private lateinit var sharedPref : PreferencesHelper
    var adminU=""
    var adminP=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        var editUser = findViewById<EditText>(R.id.edt_Username)
        var editPass = findViewById<EditText>(R.id.edt_Password)

//        Button
        var btnLogin = findViewById<Button>(R.id.btn_Login)

        var token :String?

        database = Firebase.database.reference
//        auth = FirebaseAuth.getInstance()
        sharedPref = PreferencesHelper(this)
        Firebase.messaging.isAutoInitEnabled = true
        getAdmin()
        fun getToken() {
            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(OnCompleteListener { task ->

                if (!task.isSuccessful) {
                    Log.w(TAG, "Fetching FCM registration Usertoken failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new FCM registration Usertoken
                token = task.result

                // Log and toast
                val msg = ("ini adalah Tokennya : $token")
                Log.d(TAG, msg)
//                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
            })
        }



        fun startLogin(){
            val username = editUser.text.toString().trim()
            val password = editPass.text.toString().trim()


            if (username.isEmpty()) {
                Toast.makeText(this@Login,"Username Kosong", Toast.LENGTH_SHORT).show()
            } else if (password.isEmpty()) {
                Toast.makeText(this@Login,"Password Kosong", Toast.LENGTH_SHORT).show()
            }else if(username.equals(adminU) && password.equals(adminP) ){
                Log.d(TAG, "onCreate: $username , $password")
                val Username = "Admin"
                database.child("Admins")
                    .child(Username)
                    .addValueEventListener(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val Userdata = snapshot.getValue(UsersInfo::class.java)

                            if (Userdata == null){
                                Toast.makeText(this@Login,"Database Kosong", Toast.LENGTH_SHORT).show()

                            }else{
                                val resultUsername = Userdata.Username.toString()
                                val resultPassword = Userdata.Password.toString()
                                val resultJabatan = Userdata.Jabatan.toString()
                                val resultNama = Userdata.Nama.toString()

                                if(password != resultPassword){
                                    Toast.makeText(this@Login,"Password Salah", Toast.LENGTH_SHORT).show()

                                } else if (username.equals(resultUsername) && password.equals(resultPassword)){
                                    sharedPref.put(Constant.PREF_USERNAME,resultUsername)
                                    sharedPref.put(Constant.PREF_PASSWORD,resultPassword)
                                    sharedPref.put(Constant.PREF_JABATAN,resultJabatan)
                                    sharedPref.put(Constant.PREF_NAMA,resultNama)
                                    sharedPref.put(Constant.PREF_IS_LOGIN,true)
                                    Toast.makeText(applicationContext,"Selamat Datang $resultNama Sebagai $resultJabatan", Toast.LENGTH_SHORT).show()
                                    FirebaseMessaging.getInstance().getToken().addOnCompleteListener(OnCompleteListener { task ->

                                        if (!task.isSuccessful) {
                                            Log.w(TAG, "Fetching FCM registration Usertoken failed", task.exception)
                                            return@OnCompleteListener
                                        }

                                        // Get new FCM registration Usertoken
                                        token = task.result
                                        database.child("token").child(resultNama).setValue(token).addOnCompleteListener{ task->
                                            if(!task.isSuccessful){

                                            }else{
//                                            val msg = ("ini adalah Tokennya : $Usertoken")
//                                            Log.d(TAG, msg)
//                                            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                                            }
                                        }

                                        // Log and toast

                                    })

                                    val GotoAdminBeranda = Intent(this@Login,adminBeranda::class.java)

                                    GotoAdminBeranda.putExtra("Username",Userdata.Nama)
                                    startActivity(GotoAdminBeranda)
                                } else{
                                    Toast.makeText(this@Login,"Data Login Salah", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.d(TAG, "onCancelled: ${error.message}")
                        }

                    })
            }
// masukan notification masih belum selesai
// tambahkan fitur upload foto screenshot untuk dikirim programmer untuk bukti program atau progress sudah di selesaikan
// tambahkan fitur get image dan visibility button untuk melihat foto dari detail kalau sudah terselesaikan atau foto sudah ada
//  logika kalau foto belum ada dan ceklist di isi maka tidak bisa di selesaikan karena foto harus tersedia
//  fitur mengedit foto untuk programmer mengajukan dn juga fitur comment dari admin atau manager proyek

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

                            if(password != Resultpassword){
                                Toast.makeText(applicationContext,"Password Salah", Toast.LENGTH_SHORT).show()

                            } else if (username.equals(Resultusername)&&password.equals(Resultpassword)&&Resultjabatan.equals("Programmer")||Resultjabatan.equals("Magang")){

                                getToken()
                                sharedPref.put(Constant.PREF_USERNAME,Resultusername)
                                sharedPref.put(Constant.PREF_PASSWORD,Resultpassword)
                                sharedPref.put(Constant.PREF_JABATAN,Resultjabatan)
                                sharedPref.put(Constant.PREF_NAMA,Resultnama)
                                sharedPref.put(Constant.PREF_IS_LOGIN,true)
                                FirebaseMessaging.getInstance().getToken().addOnCompleteListener(OnCompleteListener { task ->

                                    if (!task.isSuccessful) {
                                        Log.w(TAG, "Fetching FCM registration Usertoken failed", task.exception)
                                        return@OnCompleteListener
                                    }

                                    // Get new FCM registration Usertoken
                                    token = task.result
                                    database.child("token").child(Resultnama).setValue(token).addOnCompleteListener{ task->
                                        if(!task.isSuccessful){

                                        }else{
//                                            val msg = ("ini adalah Tokennya : $Usertoken")
//                                            Log.d(TAG, msg)
//                                            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                                        }
                                    }

                                    // Log and toast

                                })
                                Toast.makeText(applicationContext,"Selamat Datang $Resultnama sebagai $Resultjabatan", Toast.LENGTH_SHORT).show()
                                val GotoUserBeranda = Intent(applicationContext,UserBeranda::class.java)
                                GotoUserBeranda.putExtra("Username",Userdata.Username)
                                startActivity(GotoUserBeranda)

                            } else if(password != Resultpassword){
                                Toast.makeText(applicationContext,"Password Salah", Toast.LENGTH_SHORT).show()

                            } else if(username.equals(Resultusername)&&password.equals(Resultpassword)&&Resultjabatan.equals("Manager Proyek")){
                                sharedPref.put(Constant.PREF_USERNAME,Resultusername)
                                sharedPref.put(Constant.PREF_PASSWORD,Resultpassword)
                                sharedPref.put(Constant.PREF_JABATAN,Resultjabatan)
                                sharedPref.put(Constant.PREF_NAMA,Resultnama)
                                sharedPref.put(Constant.PREF_IS_LOGIN,true)
                                Toast.makeText(applicationContext,"Selamat Datang $Resultnama sebagai $Resultjabatan", Toast.LENGTH_SHORT).show()
                                val GotoAdminBeranda = Intent(applicationContext,adminBeranda::class.java)
//                                FirebaseIDService()
                                GotoAdminBeranda.putExtra("Username",Userdata.Nama)
                                startActivity(GotoAdminBeranda)

                            }
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
        val Admin = "Administrator"
        val Manager = "Manager Proyek"
        if (sharedPref.getString(Constant.PREF_JABATAN).equals(Admin)||sharedPref.getString(Constant.PREF_JABATAN).equals(Manager)){
                    startActivity(Intent(this, adminBeranda::class.java))
        }else{
            startActivity(Intent(this,UserBeranda::class.java))
        }
//        sharedPref.getString(Constants.PREF_JABATAN)

//        finish()

    }
    private fun getAdmin(){
        database.child("Admins")
            .child("Admin")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userdata = snapshot.getValue(UsersInfo::class.java)
                    if (userdata == null){

                    }else{
                        adminU = userdata.Username.toString()
                        adminP = userdata.Password.toString()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }


    }
