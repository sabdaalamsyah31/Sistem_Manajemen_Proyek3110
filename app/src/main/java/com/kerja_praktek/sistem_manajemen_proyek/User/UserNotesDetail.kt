package com.kerja_praktek.sistem_manajemen_proyek.User

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kerja_praktek.sistem_manajemen_proyek.Base.BaseActivity
import com.kerja_praktek.sistem_manajemen_proyek.Helper.Constant
import com.kerja_praktek.sistem_manajemen_proyek.Helper.PreferencesHelper
import com.kerja_praktek.sistem_manajemen_proyek.Model.NotesDates
import com.kerja_praktek.sistem_manajemen_proyek.Model.NotesInfo
import com.kerja_praktek.sistem_manajemen_proyek.R
import com.kerja_praktek.sistem_manajemen_proyek.User.ViewHolder.UsernoteDetailAdapter
import com.kerja_praktek.sistem_manajemen_proyek.Util.DialogUtil

class UserNotesDetail : BaseActivity() {
    private lateinit var rv_item : RecyclerView
    private lateinit var database : DatabaseReference
    private lateinit var ListNotesDetail :ArrayList<NotesInfo>
    private lateinit var sharedPref : PreferencesHelper

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_notes_detail)
        val proyek = findViewById<TextView>(R.id.notesDetailnmProyek)
        val tglProyek = findViewById<TextView>(R.id.notesDates)
        val nmProyek  = intent.getStringExtra("nmProyek").toString()

        proyek.text = nmProyek


        rv_item = findViewById(R.id.rv_NotesDetail)
        rv_item.layoutManager = LinearLayoutManager(this)

        ListNotesDetail = arrayListOf()

        database = Firebase.database.reference


        val date = intent.getStringExtra("tanggal").toString()
        val month = intent.getStringExtra("bulan")
        val year = intent.getStringExtra("tahun").toString()
        var BulanHuruf = ""
        if (month == "1") {
            BulanHuruf = "Januari"
        } else if (month == "2") {
            BulanHuruf = "Februari"
        } else if (month == "3") {
            BulanHuruf = "Maret"
        } else if (month == "4") {
            BulanHuruf = "April"
        } else if (month == "5") {
            BulanHuruf = "Mei"
        } else if (month == "6") {
            BulanHuruf = "Juni"
        } else if (month == "7") {
            BulanHuruf = "Juli"
        } else if (month == "8") {
            BulanHuruf = "Agustus"
        } else if (month == "9") {
            BulanHuruf = "September"
        } else if (month == "10") {
            BulanHuruf = "Oktober"
        } else if (month == "11") {
            BulanHuruf = "November"
        } else if (month == "12") {
            BulanHuruf = "Desember"
        }

        tglProyek.text = "$date $BulanHuruf $year"



        getNotesDetail()


    }

    private fun getNotesDetail() {
        var proyekID = intent.getStringExtra("proyekID").toString()
        var dates = intent.getStringExtra("Dates").toString()
        database = Firebase.database.reference
        database.child("notes").child(proyekID).child(dates).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(snap in snapshot.children){
                        Log.d("TAG", "onDataNotesChange: $snap.")
                        val data = snap.getValue(NotesInfo::class.java)
                        ListNotesDetail.add(data!!)
                    }
                    val adapterNotesDetail = UsernoteDetailAdapter(ListNotesDetail)
                    rv_item.adapter = adapterNotesDetail

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun setupAdapter (){
        sharedPref = PreferencesHelper(this)
        val adapterNotesDetail = UsernoteDetailAdapter(ListNotesDetail)
        rv_item.adapter = adapterNotesDetail

        adapterNotesDetail.setOnDeleteClickListener(object:UsernoteDetailAdapter.DeleteClickListener{
            override fun onDeleteClick(position: Int) {
                val nama = sharedPref.getString(Constant.PREF_NAMA).toString()
                val authorNT = ListNotesDetail[position].author.toString()
                if(nama.equals(authorNT)){
                    Toast.makeText(this@UserNotesDetail,"test",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this@UserNotesDetail,"test",Toast.LENGTH_SHORT).show()
                }
            }


        })

    }

    private fun Dnote() {
        TODO("Not yet implemented")
    }
}