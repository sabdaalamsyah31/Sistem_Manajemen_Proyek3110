package com.kerja_praktek.sistem_manajemen_proyek.admin

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kerja_praktek.sistem_manajemen_proyek.Base.BaseActivity
import com.kerja_praktek.sistem_manajemen_proyek.Model.DetailInfo
import com.kerja_praktek.sistem_manajemen_proyek.R
import java.util.Calendar

class adminEditNamaDetail : BaseActivity() {
    private lateinit var database : DatabaseReference
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_edit_nama_detail)


        var edtdetail = findViewById<EditText>(R.id.edt_DetailNama)
        var ID = findViewById<TextView>(R.id.editID)
        var nmProyek = findViewById<TextView>(R.id.nmProyek_onDetail)
        var selesai = findViewById<Button>(R.id.bt_selesai)
        var datepicker = findViewById<Button>(R.id.datePicker2)

        var tgl = ""
        var bln = ""
        var HurufBulan = ""
        var thn = ""

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        var getnmProyek = intent.getStringExtra("nmProyek")
        var getcekbox = intent.getStringExtra("cekbox")
        var getid = intent.getStringExtra("id")
        var getstatus = intent.getBooleanExtra("Status", false)
        var getTanggal = intent.getStringExtra("Tanggal")
        var getBulan = intent.getStringExtra("Bulan")
        var getTahun = intent.getStringExtra("Tahun")
        datepicker.text = "$getTanggal $getBulan $getTahun"



        edtdetail.hint = getcekbox
        nmProyek.text = getnmProyek
        ID.text = getid.toString()

        database = Firebase.database.reference
        datepicker.setOnClickListener{
            val dpd = DatePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog_MinWidth, DatePickerDialog.OnDateSetListener{ view, mYear:Int, mMonth:Int, mDay:Int ->
                // text date
//            textDeadline.text = ""+ mDay + "-" + (mMonth+1) + "-" + mYear
                tgl  = mDay.toString()
                bln  = mMonth.toString()
                thn  = mYear.toString()
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

                datepicker.text = "$tgl $HurufBulan $thn"
            },year,month,day)
            dpd.show()
        }

        selesai.setOnClickListener {

            val proyek = getnmProyek.toString()
            val id = getid.toString()
//            val status = getstatus.toBoolean()
            var detailname =  edtdetail.text.toString()
            if (detailname.isEmpty()){
                Toast.makeText(this@adminEditNamaDetail,"Data Kosong Mohon Diisi", Toast.LENGTH_LONG).show()
            }else{
                val detail = DetailInfo(detailname,id = id, status = getstatus, tanggal = tgl, bulan = bln, tahun = thn)
                database.child("DetailProyek").child(proyek).child(id)
                    .setValue(detail)
                    .addOnCompleteListener{task->
                        if(task.isSuccessful){
                            Toast.makeText(this@adminEditNamaDetail,"Data Berhasil Diubah", Toast.LENGTH_LONG).show()
                            finish()
                        }else{
                            Toast.makeText(this@adminEditNamaDetail,"Data Gagal Dirubah", Toast.LENGTH_LONG).show()
                            edtdetail.setText("")
                        }
                    }

            }
        }




    }

}