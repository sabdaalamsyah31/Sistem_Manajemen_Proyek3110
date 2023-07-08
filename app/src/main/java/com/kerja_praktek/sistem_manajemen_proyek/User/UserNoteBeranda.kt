package com.kerja_praktek.sistem_manajemen_proyek.User

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kerja_praktek.sistem_manajemen_proyek.Base.BaseActivity
import com.kerja_praktek.sistem_manajemen_proyek.Model.NotesDates
import com.kerja_praktek.sistem_manajemen_proyek.R
import com.kerja_praktek.sistem_manajemen_proyek.User.ViewHolder.UsernoteBerandaAdapter

class UserNoteBeranda : BaseActivity() {

    private lateinit var rv_item : RecyclerView
    private lateinit var ListNotes :ArrayList<NotesDates>
    private lateinit var database: DatabaseReference


    @SuppressLint("MissingInflatedId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_note)

        val proyek = findViewById<TextView>(R.id.note_nmProyek)

        var proyekID = intent.getStringExtra("proyekID").toString()
        var namaProyek = intent.getStringExtra("nmProyek").toString()
        var programmer1 = intent.getStringExtra("programmer_1").toString()
        var programmer2 = intent.getStringExtra("Programmer_2").toString()
        var programmer3 = intent.getStringExtra("Programmer_3").toString()
        var programmer4 = intent.getStringExtra("Programmer_4").toString()


        proyek.text = namaProyek

        var addbutton = findViewById<FloatingActionButton>(R.id.addNotes)


        rv_item = findViewById(R.id.rv_Notes)
        rv_item.layoutManager = LinearLayoutManager(this)

        ListNotes = arrayListOf()

        database = Firebase.database.reference





        getNotesDates()




        addbutton.setOnClickListener {
            val toAddNotes = Intent(this@UserNoteBeranda,UserInputNote::class.java)
            toAddNotes.putExtra("proyekID",proyekID)
            toAddNotes.putExtra("programmer_1",programmer1)
            toAddNotes.putExtra("programmer_2",programmer2)
            toAddNotes.putExtra("programmer_3",programmer3)
            toAddNotes.putExtra("programmer_4",programmer4)
            startActivity(toAddNotes)

        }
    }

    private fun getNotesDates() {
        var proyekID = intent.getStringExtra("proyekID").toString()
        var namaProyek = intent.getStringExtra("nmProyek").toString()
        var programmer1 = intent.getStringExtra("programmer_1").toString()
        var programmer2 = intent.getStringExtra("Programmer_2").toString()
        var programmer3 = intent.getStringExtra("Programmer_3").toString()
        var programmer4 = intent.getStringExtra("Programmer_4").toString()

        database = Firebase.database.reference
        database.child("notesinfo").child(proyekID).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                ListNotes.clear()
                if(snapshot.exists()){
                    for( snap in snapshot.children){
                        Log.d("TAG", "onDataNotesChange: $snap.")

                        val data = snap.getValue(NotesDates::class.java)
                        ListNotes.add(data!!)
                    }
                    val adapternotes = UsernoteBerandaAdapter(ListNotes)
                    rv_item.adapter = adapternotes
                    adapternotes.setOnItemClickListener(object : UsernoteBerandaAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val NotesDetail = Intent(this@UserNoteBeranda,UserNotesDetail::class.java)
                            val month = ListNotes[position].bulan.toString()
                            val date = ListNotes[position].tanggal.toString()
                            val year = ListNotes[position].tahun.toString()

                            NotesDetail.putExtra("proyekID",proyekID)
                            NotesDetail.putExtra("Dates",ListNotes[position].id)
                            NotesDetail.putExtra("tanggal",date)
                            NotesDetail.putExtra("bulan",month)
                            NotesDetail.putExtra("tahun",year)
                            NotesDetail.putExtra("nmProyek",namaProyek)

                            startActivity(NotesDetail)
                        }

                    })

                }else{

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })






    }

    override fun onStart() {

        super.onStart()

        var txt = findViewById<TextView>(R.id.tambahkanNotes_TXT)
        var addbutton = findViewById<FloatingActionButton>(R.id.addNotes)

        txt.visibility = View.INVISIBLE
        addbutton.visibility = View.INVISIBLE
        val fromBottom : Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.from_bottom_anim)}
        val toBottom : Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.to_bottom_anim)}

        txt.startAnimation(fromBottom)
        addbutton.startAnimation(fromBottom)

        txt.visibility = View.VISIBLE
        addbutton.visibility = View.VISIBLE


    }
}