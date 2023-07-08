package com.kerja_praktek.sistem_manajemen_proyek

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kerja_praktek.sistem_manajemen_proyek.Base.BaseActivity
import com.kerja_praktek.sistem_manajemen_proyek.Model.UsersInfo

class Register : BaseActivity() {
    var Jabatan:String? = null

    private lateinit var database : DatabaseReference
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val batal = findViewById<Button>(R.id.reg_btnBatalkan)
        val register = findViewById<Button>(R.id.btnRegister)
        val Nama = findViewById<EditText>(R.id.reg_nama)
        val username = findViewById<EditText>(R.id.reg_Username)
        val password = findViewById<EditText>(R.id.reg_password)



        Spinner()



        database = Firebase.database.reference
        batal.setOnClickListener {
            finish()

        }
        register.setOnClickListener {
            val nama = Nama.text.toString()
            val password = password.text.toString()
            val username = username.text.toString()
            val akun = UsersInfo(Nama = nama, Password = password, Username = username, Jabatan = Jabatan)
            if(nama.isEmpty()||username.isEmpty()||password.isEmpty()|| Jabatan.isNullOrEmpty()){
                Toast.makeText(this@Register,"Terdapat Kolom Kosong",Toast.LENGTH_SHORT).show()
            }else{


                database.child("Users")
                    .child(nama)
                    .setValue(akun)
                    .addOnCompleteListener{task->
                        if(task.isSuccessful){
                            Toast.makeText(this@Register,"Akun berhasil di tambahkan",Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this@Register,"Data gagal di tambahkan",Toast.LENGTH_SHORT).show()
                        }
                    }

            }



        }
    }

    private fun Spinner() {
        val regJabatan = findViewById<View>(R.id.reg_Jabatan) as Spinner
        val namaJabatan = arrayOf("Tentukan Jabatan","Programmer","Magang","Manager Proyek")

        val JabatanAdapter = object: ArrayAdapter<String?>(this@Register,android.R.layout.simple_spinner_item,namaJabatan){
            override fun isEnabled(position: Int): Boolean {
//                        data dari firebase sudah masuk tapi hint belum bisa di atasi belum terapat Hint "Tentukan Programmer"
                position == 0
                return position != 0
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view : TextView = super.getDropDownView(position, convertView, parent) as TextView
//                set color hint
                if(position == 0){
                    view.setTextColor(Color.GRAY)
                }else{

                }
                return view
            }
        }
        JabatanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        regJabatan.adapter = JabatanAdapter
        regJabatan.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(position == 0){

                }else{
                    Jabatan = namaJabatan[position]
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }
}