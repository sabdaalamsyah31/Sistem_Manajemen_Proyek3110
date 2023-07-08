package com.kerja_praktek.sistem_manajemen_proyek.User

//import android.app.AlertDialog
import android.os.Bundle
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
import com.kerja_praktek.sistem_manajemen_proyek.R
import com.kerja_praktek.sistem_manajemen_proyek.Util.DialogUtil

class user_editProfil : BaseActivity() {

    private lateinit var sharedPref : PreferencesHelper
    private lateinit var  database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_edit_profil)
        val password_lama = findViewById<EditText>(R.id.Password_Lama)
        val password_baru = findViewById<EditText>(R.id.Password_Baru)
        val verif_password = findViewById<EditText>(R.id.Password_verif)
        val gantipassword = findViewById<Button>(R.id.btn_gantiPass)
        val batalganti = findViewById<Button>(R.id.btn_gantipassBatal)
        sharedPref = PreferencesHelper(this)
        val Pass_lama = sharedPref.getString(Constant.PREF_PASSWORD).toString()
        val username = sharedPref.getString(Constant.PREF_USERNAME).toString()
        val nama = sharedPref.getString(Constant.PREF_NAMA).toString()
        val jabatan = sharedPref.getString(Constant.PREF_JABATAN).toString()


        database = Firebase.database.reference

        gantipassword.setOnClickListener {
            var lama = password_lama.text.toString()
            var baru = password_baru.text.toString()
            var verif = verif_password.text.toString()
            if(lama.isEmpty()||baru.isEmpty()||verif.isEmpty()){
                Toast.makeText(this@user_editProfil,"Terdapat Kolom Kosong",Toast.LENGTH_SHORT).show()
            }else{
                DialogUtil().showAlertDialog(this@user_editProfil,"Apakah Yakin Mengganti Password?",
                    {// on yes
                        if(lama != Pass_lama){
                            Toast.makeText(this@user_editProfil,"Password Lama Salah",Toast.LENGTH_SHORT).show()
                        }else if(lama.equals(Pass_lama) && baru != verif){
                            Toast.makeText(this@user_editProfil,"Verifikasi Tidak Sama",Toast.LENGTH_SHORT).show()
                        }else if(lama.equals(Pass_lama) && baru.equals(verif)){
                            val data = UsersInfo(Username = username, Nama = nama,Password = baru ,Jabatan = jabatan)
                            database.child("Users")
                                .child(username)
                                .setValue(data)
                                .addOnCompleteListener{task ->
                                    if(task.isSuccessful){
                                        Toast.makeText(this@user_editProfil,"Password berhasil diubah",Toast.LENGTH_SHORT).show()
                                        sharedPref.put(Constant.PREF_PASSWORD,baru)
                                        finish()
                                    }else{
                                        Toast.makeText(this@user_editProfil,"Password gagal diubah",Toast.LENGTH_SHORT).show()
                                    }
                                }
                        }

                    }
                    ,
                    { // on no action
                        Toast.makeText(applicationContext,"DiBatalkan",Toast.LENGTH_SHORT).show()
                    }
                )

            }

        }
        batalganti.setOnClickListener {
            DialogUtil().showAlertDialog(this@user_editProfil,"Batalkan ?",
            {// on yes
                finish()
            }
            ,
            { // on no action
                Toast.makeText(applicationContext,"DiBatalkan",Toast.LENGTH_SHORT).show()
            }
        )
        }


    }
    private fun getDatalama(){
        val username = sharedPref.getString(Constant.PREF_USERNAME).toString()
        database = Firebase.database.reference
        database.child("Users").child(username).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}