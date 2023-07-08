package com.kerja_praktek.sistem_manajemen_proyek.User

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kerja_praktek.sistem_manajemen_proyek.Base.BaseActivity
import com.kerja_praktek.sistem_manajemen_proyek.Helper.Constant
import com.kerja_praktek.sistem_manajemen_proyek.Helper.PreferencesHelper
import com.kerja_praktek.sistem_manajemen_proyek.Model.NotesInfo
import com.kerja_praktek.sistem_manajemen_proyek.Model.NotesDates
import com.kerja_praktek.sistem_manajemen_proyek.R
import java.time.LocalDate
import java.time.LocalTime

class UserInputNote : BaseActivity() {


    private lateinit var database: DatabaseReference
    var year = LocalDate.now().year.toString()
    var date = LocalDate.now().dayOfMonth.toString()
    var month = LocalDate.now().monthValue.toString()
    var hour = LocalTime.now().hour.toString()
    var minute = LocalTime.now().minute.toString()
    var second = LocalTime.now().second.toString()
    private lateinit var sharedPref : PreferencesHelper


//    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_input_note)
        var test = findViewById<TextView>(R.id.textView19)
        var test2 = findViewById<TextView>(R.id.textView20)
        var inputnote = findViewById<EditText>(R.id.Notes)
        var nmProyek = findViewById<TextView>(R.id.Note_nmProyek)
        var proyekID = intent.getStringExtra("proyekID").toString()
        var namaProyek = intent.getStringExtra("nmProyek").toString()
        var programmer1 = intent.getStringExtra("programmer_1").toString()
        var programmer2 = intent.getStringExtra("Programmer_2").toString()
        var programmer3 = intent.getStringExtra("Programmer_3").toString()
        var programmer4 = intent.getStringExtra("Programmer_4").toString()



    sharedPref = PreferencesHelper(this)

        nmProyek.text = namaProyek



        test.text = "$hour$minute$second"
        test2.text = namaProyek
        database = Firebase.database.reference



//        button
        val addNotes = findViewById<Button>(R.id.add_Note)
        val cancel = findViewById<Button>(R.id.BatalAddnotes)

        cancel.setOnClickListener {
            Toast.makeText(this@UserInputNote,"Tambahkan Notes Dibatalkan", Toast.LENGTH_SHORT).show()
            finish()
        }


        addNotes.setOnClickListener {
            ADDNOTES()
            finish()
        }
    }

    private fun ADDNOTES() {
        addNotesDetail()
        addNotesDate()
    }

    private fun addNotesDate() {
        var proyekID = intent.getStringExtra("proyekID").toString()
        val forDate = "$date$month$year"
        val dataIDNotes = NotesDates(id = forDate, tanggal = date, bulan = month, tahun = year )

        database.child("notesinfo").child(proyekID).child(forDate).setValue(dataIDNotes)
    }

    private fun addNotesDetail() {
        sharedPref = PreferencesHelper(this)
        var inputnote = findViewById<EditText>(R.id.Notes)
        var proyekID = intent.getStringExtra("proyekID").toString()
        val forDate = "$date$month$year"
        val forTime = "$hour$minute$second"
        val notes = inputnote.text.toString()
        val nama = sharedPref.getString(Constant.PREF_NAMA).toString()
        val data = NotesInfo(id = forTime, notes = notes,tanggal = date, bulan = month, tahun = year, author = nama )
        database = Firebase.database.reference
        database.child("notes")
            .child(proyekID)
            .child(forDate)
            .child(forTime)
            .setValue(data).addOnCompleteListener{task->
                if(task.isSuccessful){
                    Toast.makeText(this@UserInputNote,"Notes Berhasil Ditambahkan",Toast.LENGTH_LONG).show()


                }
                else{
                    Toast.makeText(this@UserInputNote,"Notes Gagal Ditambahkan",Toast.LENGTH_LONG).show()

                }
            }
    }
    }
