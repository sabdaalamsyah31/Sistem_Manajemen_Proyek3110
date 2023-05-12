package com.kerja_praktek.sistem_manajemen_proyek.admin

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kerja_praktek.sistem_manajemen_proyek.Base.BaseActivity
import com.kerja_praktek.sistem_manajemen_proyek.Model.ProyekInfo
import com.kerja_praktek.sistem_manajemen_proyek.Model.UsersInfo
import com.kerja_praktek.sistem_manajemen_proyek.R
import java.util.*
import kotlin.collections.ArrayList
import java.util.ArrayList as ArrayList1

class adminTambahkanTugas : BaseActivity() {
    private lateinit var database: DatabaseReference

    var namaMananger:String = ""
    var anggota1:String = ""
    var anggota2:String = ""
    var anggota3:String = ""
    var anggota4:String = ""

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_tambahkan_tugas)



        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)



        val nmProyek = findViewById<EditText>(R.id.edtNamaProjek)

        val Deadline = findViewById<Button>(R.id.btn_datepicker)
        val textDeadline = findViewById<TextView>(R.id.tvDeadline)
        val judul = findViewById<TextView>(R.id.tv_judul)
        var tglDeadline :String = ""
        var blnDeadline :String = ""
        var thnDeadline :String = ""
        var HurufBulan = ""

        val btnSelanjutnya = findViewById<Button>(R.id.btnSelanjutnya)


//        var ListUser = arrayListOf<String>()


        database = Firebase.database.reference

//        catetan!! pengerjaan belum selesai error pada bagian ListUser harus menggunakan ArrayOf dan bukan ArrayList
//        tetapi data yang di terima dari firebase adalah ArrayList
//        !! AYOOO CARI SOLUSINYA!!

//        ListUser = arrayListOf<String>()


        Deadline.setOnClickListener {
            val dpd = DatePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog_MinWidth, DatePickerDialog.OnDateSetListener{view, mYear:Int, mMonth:Int, mDay:Int ->
                // text date
                tglDeadline = mDay.toString()
                blnDeadline = mMonth.toString()
                thnDeadline = mYear.toString()
                if(mMonth == 1){
                    HurufBulan = "Januari"
                }else if(mMonth == 2){
                    HurufBulan = "Februari"
                }else if(mMonth == 3){
                    HurufBulan = "Maret"
                }else if(mMonth == 4){
                    HurufBulan = "April"
                }else if(mMonth == 5){
                    HurufBulan= "Mei"
                }else if(mMonth == 6){
                    HurufBulan = "Juni"
                }else if(mMonth == 7){
                    HurufBulan  = "Juli"
                }else if(mMonth == 8){
                    HurufBulan  = "Agustus"
                }else if(mMonth == 9){
                    HurufBulan  = "September"
                }else if(mMonth == 10){
                    HurufBulan  = "Oktober"
                }else if(mMonth == 11){
                    HurufBulan   = "November"
                }else if(mMonth == 12){
                    HurufBulan  = "Desember"
                }else{
                    HurufBulan  = "Januari"
                }
                Deadline.text = "$tglDeadline $HurufBulan $thnDeadline"
            },year,month,day)
            dpd.show()
        }
        spinner()


        btnSelanjutnya.setOnClickListener{
            val NamaProyek = nmProyek.text.toString()
            val Manager = namaMananger
//            val Deadline = textDeadline.text.toString()
//            --------------------------------
//            pengaturan Deadline tanggal
            val Tanggal= tglDeadline
            val Bulan = blnDeadline
            val Tahun = thnDeadline
//            --------------------------------
            val Programmer_1 = anggota1
            val Programmer_2 = anggota2
            val Programmer_3 = anggota3
            val Programmer_4 = anggota4
            if(NamaProyek.isEmpty()||
                Tanggal.equals("00")||
                Bulan.equals("00")||
                Tahun.equals("0000")||
                Manager!!.isEmpty()||
                Programmer_1.isEmpty()||
                Programmer_2.isEmpty()||
                Programmer_3.isEmpty()||
                Programmer_4.isEmpty())
            {
                Toast.makeText(this@adminTambahkanTugas,"Terdapat Kolom Kosong",Toast.LENGTH_SHORT).show()
            }
            else if(Manager!!.equals("Manager Proyek")||
                Programmer_1!!.equals("Programmer")||
                Programmer_2!!.equals("Programmer")||
                Programmer_3!!.equals("Programmer")||
                Programmer_4!!.equals("Programmer"))
            {
                Toast.makeText(this@adminTambahkanTugas,"Terdapat Kolom Kosong",Toast.LENGTH_SHORT).show()
            }else if(Tanggal.equals(null)||Bulan.equals(null)||Tahun.equals(null))
            {
                Toast.makeText(this@adminTambahkanTugas,"Terdapat Kolom Kosong",Toast.LENGTH_SHORT).show()
            }

            else{

                val Proyek = ProyekInfo(NamaProyek,Tanggal,Bulan,Tahun,Manager,Programmer_1,Programmer_2,Programmer_3,Programmer_4)
                val Proyekname = NamaProyek
                database.child("Proyek")
                    .child(Proyekname)
                    .setValue(Proyek)
                    .addOnCompleteListener{ task ->
                        if (task.isSuccessful){
//                            database.child("DetailProyek").child(nmProyek.toString()).setValue(nmProyek)
                            Toast.makeText(this@adminTambahkanTugas,"Selanjutnya Tambahkan Detail", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@adminTambahkanTugas,adminTambahTugas_Detail::class.java)
                            intent.putExtra("namaProyek",NamaProyek)
                            startActivity(intent)
                        }else{
                            Toast.makeText(this@adminTambahkanTugas,"Data Gagal Ditambahkan", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

    private fun spinner() {


        val anggota_1 = findViewById<Spinner>(R.id.edt_Programmer1)
        val anggota_2 = findViewById<Spinner>(R.id.edt_Programmer2)
        val anggota_3 = findViewById<Spinner>(R.id.edt_Programmer3)
        val anggota_4 = findViewById<Spinner>(R.id.edt_Programmer4)
        val nmManager = findViewById<Spinner>(R.id.edtManagerProyek)


//        val user = arrayOf(ListUser)
        val NamaProgrammer = arrayOf("Programmer",
            "Abdurahmman","Adin Comara","Aisha Stevani Alfairuzy","Sabda Alamsyah",
            "Ghufron","Bayu Grafit","Hermanto","MagangSatu",
            "MagangDua","MagangTiga","Nova Ariyanto","Panji Cahyono",
            "Tomi Kurniawan")
        val NamaManager = arrayOf("Manager Proyek",
            "Abdurahmman","Adin Comara","Aisha Stevani Alfairuzy","Sabda Alamsyah",
            "Ghufron","Bayu Grafit","Hermanto","MagangSatu",
            "MagangDua","MagangTiga","Nova Ariyanto","Panji Cahyono",
            "Tomi Kurniawan")
        val ProgrammerarrayAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,NamaProgrammer)
        ProgrammerarrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val ManagerarrayAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,NamaManager)
        ManagerarrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        nmManager!!.setAdapter(ManagerarrayAdapter)
        nmManager.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                namaMananger = NamaProgrammer[position]
//                judul.text = ListUser[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
        anggota_1.adapter = ProgrammerarrayAdapter
        anggota_1.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                anggota1 = NamaProgrammer[position]
//                Toast.makeText(applicationContext,"$anggota1", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
        anggota_2.adapter = ProgrammerarrayAdapter
        anggota_2.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                anggota2 = NamaProgrammer[position]
//                Toast.makeText(applicationContext,"$anggota2", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
        anggota_3.adapter = ProgrammerarrayAdapter
        anggota_3.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                anggota3 = NamaProgrammer[position]
//                Toast.makeText(applicationContext,"$anggota3", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
        anggota_4.adapter = ProgrammerarrayAdapter
        anggota_4.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                anggota4 = NamaProgrammer[position]
//                Toast.makeText(applicationContext,ListUser[position], Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }
}