package com.kerja_praktek.sistem_manajemen_proyek.admin

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
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
import com.google.gson.Gson
import com.kerja_praktek.sistem_manajemen_proyek.Base.BaseActivity
import com.kerja_praktek.sistem_manajemen_proyek.Model.ProyekInfo
import com.kerja_praktek.sistem_manajemen_proyek.Model.UsersInfo
import com.kerja_praktek.sistem_manajemen_proyek.NotifPack.NotificationData
import com.kerja_praktek.sistem_manajemen_proyek.NotifPack.PushNotification
import com.kerja_praktek.sistem_manajemen_proyek.NotifPack.RetrofitInstance
import com.kerja_praktek.sistem_manajemen_proyek.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList
import java.util.ArrayList as ArrayList1

class adminTambahkanTugas : BaseActivity() {
    private lateinit var database: DatabaseReference
    private val TAG = "SendNotificationActivity"


    var namaMananger:String = ""
    var anggota1:String = ""
    var anggota2:String = ""
    var anggota3:String = ""
    var anggota4:String = ""

    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
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
        val batalkan = findViewById<Button>(R.id.btnBatalkan)

//        var ListUser = arrayListOf<String>()

//        forSpinner()


    database = Firebase.database.reference

//        catetan!! pengerjaan belum selesai error pada bagian ListUser harus menggunakan ArrayOf dan bukan ArrayList
//        tetapi data yang di terima dari firebase adalah ArrayList
//        !! AYOOO CARI SOLUSINYA!!

//        ListUser = arrayListOf<String>()


        Deadline.setOnClickListener {
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener{view, mYear:Int, mMonth:Int, mDay:Int ->
                // text date
                val month = mMonth+1
                tglDeadline = mDay.toString()
                blnDeadline = month.toString()
                thnDeadline = mYear.toString()
                if(month == 1){
                    HurufBulan = "Januari"
                }else if(month == 2){
                    HurufBulan = "Februari"
                }else if(month == 3){
                    HurufBulan = "Maret"
                }else if(month == 4){
                    HurufBulan = "April"
                }else if(month == 5){
                    HurufBulan= "Mei"
                }else if(month == 6){
                    HurufBulan = "Juni"
                }else if(month == 7){
                    HurufBulan  = "Juli"
                }else if(month == 8){
                    HurufBulan  = "Agustus"
                }else if(month == 9){
                    HurufBulan  = "September"
                }else if(month == 10){
                    HurufBulan  = "Oktober"
                }else if(month == 11){
                    HurufBulan   = "November"
                }else if(month == 12){
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
                database = Firebase.database.reference
                database.child("Proyek")
                    .child(Proyekname)
                    .setValue(Proyek)
                    .addOnCompleteListener{ task ->
                        if (task.isSuccessful){
                            Toast.makeText(this@adminTambahkanTugas,"Selanjutnya Tambahkan Detail", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@adminTambahkanTugas,adminTambahTugas_Detail::class.java)
                            intent.putExtra("namaProyek",NamaProyek)
                            intent.putExtra("Manager",Manager)
                            intent.putExtra("Programmer1",Programmer_1)
                            intent.putExtra("Programmer2",Programmer_2)
                            intent.putExtra("Programmer3",Programmer_3)
                            intent.putExtra("Programmer4",Programmer_4)
                            startActivity(intent)

                        }else{
                            Toast.makeText(this@adminTambahkanTugas,"NotificationData Gagal Ditambahkan", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
            }
        }


        batalkan.setOnClickListener {
            finish()
        }
    }

    private fun spinner() {

        database= FirebaseDatabase.getInstance().getReference("Users")
        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val UserProgrammer:MutableList<String?> = ArrayList()
                val ManagerProgrammer:MutableList<String?> = ArrayList()
                for(snap in snapshot.children){


                    val Programmer = snap.child("nama").getValue(String::class.java)
                    val Manager = snap.child("nama").getValue(String::class.java)



                    ManagerProgrammer.add(Manager)
                    UserProgrammer.add(Programmer)

                }
                UserProgrammer.add(0,"Tentukan Programmer")
                ManagerProgrammer.add(0,"Tentukan Manager Proyek")

                val anggota_1 = findViewById<View>(R.id.edt_Programmer1) as Spinner
                val anggota_2 = findViewById<View>(R.id.edt_Programmer2) as Spinner
                val anggota_3 = findViewById<View>(R.id.edt_Programmer3) as Spinner
                var anggota_4 = findViewById<View>(R.id.edt_Programmer4) as Spinner
                val nmManager = findViewById<View>(R.id.edtManagerProyek) as Spinner



                val ProgrammerarrayAdapter = object:ArrayAdapter<String?>(this@adminTambahkanTugas,android.R.layout.simple_spinner_item,UserProgrammer){
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
                ProgrammerarrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)




                val ManagerarrayAdapter = object:ArrayAdapter<String>(this@adminTambahkanTugas,android.R.layout.simple_spinner_item,ManagerProgrammer){
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



                nmManager.adapter = ManagerarrayAdapter
                nmManager.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {

                        namaMananger = UserProgrammer[position].toString()
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

                        anggota1 = UserProgrammer[position].toString()
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

                        anggota2 = UserProgrammer[position].toString()
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

                        anggota3 = UserProgrammer[position].toString()
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





//        val user = arrayOf(ListUser)

    }



    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if (response.isSuccessful){
                Log.d(TAG, "Response: ${Gson().toJson(response)}")
            }else{
                Log.e(TAG, response.errorBody().toString())
            }

        }catch (e:Exception) {
            Log.e(TAG,e.toString())
        }

    }

}