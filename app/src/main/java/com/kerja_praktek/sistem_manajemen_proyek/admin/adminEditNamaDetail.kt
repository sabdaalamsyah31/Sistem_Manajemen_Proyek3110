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

//        note deadline belum terisi


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
        var getTanggal = intent.getStringExtra("tanggal")
        var getBulan = intent.getStringExtra("bulan")?.toInt()
        var getTahun = intent.getStringExtra("tahun")


        var bulanHuruf :String? = null
        if(getBulan == 1){
            bulanHuruf = "Januari"
        }else if(getBulan == 2){
            bulanHuruf = "Februari"
        }else if(getBulan == 3){
            bulanHuruf = "Maret"
        }else if(getBulan == 4){
            bulanHuruf = "April"
        }else if(getBulan == 5){
            bulanHuruf= "Mei"
        }else if(getBulan == 6){
            bulanHuruf = "Juni"
        }else if(getBulan == 7){
            bulanHuruf  = "Juli"
        }else if(getBulan == 8){
            bulanHuruf  = "Agustus"
        }else if(getBulan == 9){
            bulanHuruf  = "September"
        }else if(getBulan == 10){
            bulanHuruf  = "Oktober"
        }else if(getBulan == 11){
            bulanHuruf   = "November"
        }else if(getBulan == 12){
            bulanHuruf  = "Desember"
        }else{
            bulanHuruf  = "Januari"
        }
        datepicker.text = "$getTanggal $bulanHuruf $getTahun"



        edtdetail.hint = getcekbox
        nmProyek.text = getnmProyek
        ID.text = getid.toString()

        database = Firebase.database.reference
        datepicker.setOnClickListener{
            val dpd = DatePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog_MinWidth, DatePickerDialog.OnDateSetListener{ view, mYear:Int, mMonth:Int, mDay:Int ->
                // text date
                val month = mMonth+1
                tgl  = mDay.toString()
                bln  = month.toString()
                thn  = mYear.toString()
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
                Toast.makeText(this@adminEditNamaDetail,"NotificationData Kosong Mohon Diisi", Toast.LENGTH_LONG).show()
            }else if(tgl.isEmpty()||bln.isEmpty()||thn.isEmpty()){
                Toast.makeText(this@adminEditNamaDetail,"Deadline Mohon Diisi",Toast.LENGTH_LONG).show()
            }


            else{
                val detail = DetailInfo(detailname,id = id, status = getstatus, tanggal = tgl, bulan = bln, tahun = thn)
                database.child("DetailProyek").child(proyek).child(id)
                    .setValue(detail)
                    .addOnCompleteListener{task->
                        if(task.isSuccessful){
                            Toast.makeText(this@adminEditNamaDetail,"NotificationData Berhasil Diubah", Toast.LENGTH_LONG).show()
                            finish()
                        }else{
                            Toast.makeText(this@adminEditNamaDetail,"NotificationData Gagal Dirubah", Toast.LENGTH_LONG).show()
                            edtdetail.setText("")
                        }
                    }

            }
        }




    }

}