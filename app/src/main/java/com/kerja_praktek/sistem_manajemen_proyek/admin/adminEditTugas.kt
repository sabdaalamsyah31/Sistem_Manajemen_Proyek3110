package com.kerja_praktek.sistem_manajemen_proyek.admin

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
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
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kerja_praktek.sistem_manajemen_proyek.Base.BaseActivity
import com.kerja_praktek.sistem_manajemen_proyek.Model.DetailInfo
import com.kerja_praktek.sistem_manajemen_proyek.Model.ProyekInfo
import com.kerja_praktek.sistem_manajemen_proyek.R
import com.kerja_praktek.sistem_manajemen_proyek.admin.ViewHolder.adminDetailtugasAdapter
import java.util.*
import kotlin.collections.ArrayList

class adminEditTugas : BaseActivity() {
    private lateinit var ListDetailProyek:ArrayList<DetailInfo>
    private lateinit var database:DatabaseReference


    var namaMananger:String = ""
    var anggota1:String = ""
    var anggota2:String = ""
    var anggota3:String = ""
    var anggota4:String = ""
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_edit_tugas)
        val btnbatal = findViewById<Button>(R.id.btnBatal)
        val btnselesai = findViewById<Button>(R.id.btn_Selesai)

        val edtnmProyek = findViewById<TextView>(R.id.txt_namaProyek)
        val btnDeadline = findViewById<Button>(R.id.btn_editdatepicker)
        val edtDeadline = findViewById<TextView>(R.id.tv_Deadline)


        var tglDeadline :String = ""
        var blnDeadline :String = ""
        var thnDeadline :String = ""


        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val ID = intent.getStringExtra("ID").toString()
        val nama = intent.getStringExtra("namaProyek").toString()
//        val deadline = intent.getStringExtra("deadline").toString()
        var tanggal = intent.getStringExtra("tanggal")
        var bulan = intent.getStringExtra("bulan")?.toInt()
        var bulanHuruf =""
        var tahun = intent.getStringExtra("tahun")

        if(bulan == 1){
            bulanHuruf = "Januari"
        }else if(bulan == 2){
            bulanHuruf = "Februari"
        }else if(bulan == 3){
            bulanHuruf = "Maret"
        }else if(bulan == 4){
            bulanHuruf = "April"
        }else if(bulan == 5){
            bulanHuruf= "Mei"
        }else if(bulan == 6){
            bulanHuruf = "Juni"
        }else if(bulan == 7){
            bulanHuruf  = "Juli"
        }else if(bulan == 8){
            bulanHuruf  = "Agustus"
        }else if(bulan == 9){
            bulanHuruf  = "September"
        }else if(bulan == 10){
            bulanHuruf  = "Oktober"
        }else if(bulan == 11){
            bulanHuruf   = "November"
        }else if(bulan == 12){
            bulanHuruf  = "Desember"
        }else{
            bulanHuruf  = "Januari"
        }



        spinner()



        edtnmProyek.text = nama
        btnDeadline.text = "$tanggal $bulanHuruf $tahun"







        ListDetailProyek = arrayListOf<DetailInfo>()

//        getCekboxDetail()
        btnDeadline.setOnClickListener {
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener{ view, mYear, mMonth, mDay ->
                val month = mMonth+1
                tglDeadline = mDay.toString()
                blnDeadline = month.toString()
                thnDeadline = mYear.toString()

                if(month == 1){
                    bulanHuruf = "Januari"
                }else if(month == 2){
                    bulanHuruf = "Februari"
                }else if(month == 3){
                    bulanHuruf = "Maret"
                }else if(month == 4){
                    bulanHuruf = "April"
                }else if(month == 5){
                    bulanHuruf= "Mei"
                }else if(month == 6){
                    bulanHuruf = "Juni"
                }else if(month == 7){
                    bulanHuruf  = "Juli"
                }else if(month == 8){
                    bulanHuruf  = "Agustus"
                }else if(month == 9){
                    bulanHuruf  = "September"
                }else if(month == 10){
                    bulanHuruf  = "Oktober"
                }else if(month == 11){
                    bulanHuruf   = "November"
                }else if(month == 12){
                    bulanHuruf  = "Desember"
                }else{
                    bulanHuruf  = "Januari"
                }

                // text date
                btnDeadline.text = "$tglDeadline $bulanHuruf $thnDeadline"
            },year,month,day)
            dpd.show()
        }

        database = Firebase.database.reference

        btnbatal.setOnClickListener {
            finish()
        }

        btnselesai.setOnClickListener {
            val nama = edtnmProyek.text.toString()
//            val deadline = edtDeadline.text.toString()
            val tanggal = tglDeadline
            val bulan = blnDeadline
            val tahun = thnDeadline
            val manager = namaMananger
            val programmer1 = anggota1
            val programmer2 = anggota2
            val programmer3 = anggota3
            val programmer4 = anggota4
            val detail = "detail"
            val detailProyek =database.child("Proyek").child(nama)

            if(nama.isEmpty()||manager.isEmpty()||programmer1.isEmpty()||programmer2.isEmpty()||programmer3.isEmpty()||programmer4.isEmpty())
            {
                Toast.makeText(this@adminEditTugas,"Kolom Data Kosong",Toast.LENGTH_SHORT).show()
            }else if(tanggal.isEmpty()||bulan.isEmpty()||tahun.isEmpty()){
                Toast.makeText(this@adminEditTugas,"Kolom Deadline Tugas Kosong",Toast.LENGTH_SHORT).show()
            }


            else{
                val proyek = ProyekInfo(id = ID , namaProyek = nama, tanggal = tanggal, bulan = bulan,
                    tahun = tahun, manager = manager, programmer_1 = programmer1, programmer_2 = programmer2,
                    programmer_3 = programmer3, programmer_4 = programmer4,
//                    ListDetailProyek
                )
                val proyekname = nama

                database.child("Proyek")
                    .child(ID)
                    .setValue(proyek)
                    .addOnCompleteListener{ task ->
                        if(task.isSuccessful){
                            Toast.makeText(this@adminEditTugas, "Data Proyek Berhasil Diubah",Toast.LENGTH_SHORT).show()
                            val beranda = Intent(this@adminEditTugas,adminBeranda::class.java)
                            startActivity(beranda)
                            finish()
                        }else{
                            Toast.makeText(this@adminEditTugas, "Data Gagal Diubah",Toast.LENGTH_SHORT).show()
                            setResult(Activity.RESULT_CANCELED)
                            finish()
                        }

                    }
            }

        }



    }

    private fun spinner() {
        database = FirebaseDatabase.getInstance().getReference("Users")
        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {


                val UserProgrammer:MutableList<String?> = ArrayList()
                val ManagerProgrammer:MutableList<String?> = ArrayList()
                for(snap in snapshot.children){


                    val Programmer = snap.child("nama").getValue(String::class.java)
//                    val Manager = snap.child("nama").getValue(String::class.java)



                    ManagerProgrammer.add(Programmer)
                    UserProgrammer.add(Programmer)

                }
                UserProgrammer.add(0,"Tentukan Programmer")
                ManagerProgrammer.add(0,"Tentukan Manager Proyek")



                val edtManager = findViewById<View>(R.id.edt_managerProyek) as Spinner
                val edtProgrammer1 = findViewById<View>(R.id.edt_Programmer_1) as Spinner
                val edtProgrammer2 = findViewById<View>(R.id.edt_Programmer_2) as Spinner
                val edtProgrammer3 = findViewById<View>(R.id.edt_Programmer_3) as Spinner
                val edtProgrammer4 = findViewById<View>(R.id.edt_Programmer_4) as Spinner





                val nama = intent.getStringExtra("namaProyek").toString()
//        val deadline = intent.getStringExtra("deadline").toString()
                val tanggal = intent.getStringExtra("tanggal")
                val bulan = intent.getStringExtra("bulan")
                val tahun = intent.getStringExtra("tahun")
                val manager = intent.getStringExtra("managerProyek").toString()
                val programmer1 = intent.getStringExtra("programmer_1").toString()
                val programmer2 = intent.getStringExtra("programmer_2").toString()
                val programmer3 = intent.getStringExtra("programmer_3").toString()
                val programmer4 = intent.getStringExtra("programmer_4").toString()

                val ProgrammerarrayAdapter = object:
                    ArrayAdapter<String>(this@adminEditTugas,android.R.layout.simple_spinner_item,UserProgrammer){
                    override fun isEnabled(position: Int): Boolean {
//
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
                ProgrammerarrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)



                val ManagerarrayAdapter = object:ArrayAdapter<String>(this@adminEditTugas,android.R.layout.simple_spinner_item,ManagerProgrammer){
                    override fun isEnabled(position: Int): Boolean {
//
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
                ManagerarrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


                edtManager.adapter = ManagerarrayAdapter
                edtManager.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {

                        namaMananger = ManagerProgrammer[position].toString()
//                judul.text = ListUser[position]
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }
                edtProgrammer1.adapter = ProgrammerarrayAdapter
                edtProgrammer1.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {

                        anggota1 = UserProgrammer[position].toString()
//                Toast.makeText(applicationContext,"$anggota1", Toast.LENGTH_SHORT).show()
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }
                edtProgrammer2.adapter = ProgrammerarrayAdapter
                edtProgrammer2.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {

                        anggota2 = UserProgrammer[position].toString()
//                Toast.makeText(applicationContext,"$anggota2", Toast.LENGTH_SHORT).show()
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }
                edtProgrammer3.adapter = ProgrammerarrayAdapter
                edtProgrammer3.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {

                        anggota3 = UserProgrammer[position].toString()
//                Toast.makeText(applicationContext,"$anggota3", Toast.LENGTH_SHORT).show()
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }
                edtProgrammer4.adapter = ProgrammerarrayAdapter
                edtProgrammer4.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        val default = arrayOf("Programmer")
                        if(position == 0){

                        }
                        anggota4 = UserProgrammer[position].toString()
//                Toast.makeText(applicationContext,ListUser[position], Toast.LENGTH_SHORT).show()
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })



    }





//function tidak terpakai
    private fun getCekboxDetail() {
        database = Firebase.database.reference
// note pada database nama child harus sama dengan nama proyek
        val namaproyek = intent.getStringExtra("namaProyek").toString()
        val child = "detail"
        database.child("Proyek")
            .child(namaproyek).child(child).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    ListDetailProyek.clear()
                    if(snapshot.exists()){
                        for(snap in snapshot.children){
                            val data = snap.getValue(DetailInfo::class.java)
                            ListDetailProyek.add(data!!)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        return
    }


}