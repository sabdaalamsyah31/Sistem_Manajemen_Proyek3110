package com.kerja_praktek.sistem_manajemen_proyek.admin

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
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
import com.kerja_praktek.sistem_manajemen_proyek.Model.DetailInfo
import com.kerja_praktek.sistem_manajemen_proyek.R
import com.kerja_praktek.sistem_manajemen_proyek.admin.ViewHolder.adminDetailtugasAdapter

class adminDetailTugas : BaseActivity() {
//    database
    private lateinit var database: DatabaseReference

//    private lateinit var viewHolder: adminDetailtugasAdapter.ViewHolder
    private lateinit var Persen : TextView
    private lateinit var tvNamaProyek: TextView
    private lateinit var tvManagerProyek: TextView
    private lateinit var tvdeadline: TextView
    private lateinit var tvProgrammer_1: TextView
    private lateinit var tvProgrammer_2: TextView
    private lateinit var tvProgrammer_3: TextView
    private lateinit var tvProgrammer_4: TextView
    private lateinit var btnHapus:ImageButton
    private lateinit var btnEdit:ImageButton
    private lateinit var rvProyekDetail:RecyclerView
    private lateinit var ListDetailProyek:ArrayList<DetailInfo>
//    @SuppressLint("MissingInflatedId")
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_detail_tugas)

        var btnTambah = findViewById<ImageButton>(R.id.tambahDetail)

//        val tambahdetail = findViewById<ImageButton>(R.id.tambahDetail)
        Persen = findViewById(R.id.txt_persen)
        btnHapus = findViewById(R.id.btnHapus)
        btnEdit = findViewById(R.id.btnEdit)
        tvNamaProyek = findViewById(R.id.tvNamaProyek)
        tvManagerProyek = findViewById(R.id.tvManagerProyek)
        tvdeadline = findViewById(R.id.deadline)
        tvProgrammer_1 = findViewById(R.id.tv_Programmer_1)
        tvProgrammer_2 = findViewById(R.id.tv_Programmer_2)
        tvProgrammer_3 = findViewById(R.id.tv_Programmer_3)
        tvProgrammer_4 = findViewById(R.id.tv_Programmer_4)
        btnHapus = findViewById(R.id.btnHapus)
        btnEdit = findViewById(R.id.btnEdit)

        val nama = intent.getStringExtra("namaProyek")
        val manager = intent.getStringExtra("managerProyek")
        val deadline = intent.getStringExtra("deadline")
        val programmer1 = intent.getStringExtra("programmer_1")
        val programmer2 = intent.getStringExtra("programmer_2")
        val programmer3 = intent.getStringExtra("programmer_3")
        val programmer4 = intent.getStringExtra("programmer_4")

    tvNamaProyek.text = nama
    tvdeadline.text = deadline
    tvManagerProyek.text = manager
    tvProgrammer_1.text = programmer1
    tvProgrammer_2.text = programmer2
    tvProgrammer_3.text = programmer3
    tvProgrammer_4.text = programmer4

//        setup Database
        database = Firebase.database.reference

        rvProyekDetail = findViewById(R.id.rvProyekDetail)
        rvProyekDetail.layoutManager = LinearLayoutManager(this)
        ListDetailProyek = arrayListOf<DetailInfo>()
        val namaproyek = intent.getStringExtra("namaProyek").toString()

//        inView()
//        DetailProyekView()
        getCekboxDetail()
        percentage()
//        cekboxclick()

        btnEdit.setOnClickListener{
            val ubah = Intent(this,adminEditTugas::class.java)
            ubah.putExtra("namaProyek",nama)
            ubah.putExtra("deadline",deadline)
            ubah.putExtra("managerProyek",manager)
            ubah.putExtra("programmer_1",programmer1)
            ubah.putExtra("programmer_2",programmer2)
            ubah.putExtra("programmer_3",programmer3)
            ubah.putExtra("programmer_4",programmer4)
            startActivityForResult(ubah,10)

        }

        btnHapus.setOnClickListener{
            val buildMessage = AlertDialog.Builder(this@adminDetailTugas)
            buildMessage.setMessage("Apakah Yakin Menghapus Proyek $nama ?")
                .setTitle("Peringatan !!!")
                .setCancelable(false)
                .setPositiveButton("YA"){dialog,id->
                    val querynamaproyek = database.child("Proyek")
                        .child(nama.toString())
                        .orderByChild("namaProyek")
                        .equalTo("namaProyek")

                    val querydetailProyek = database.child("DetailProyek")
                        .child(nama.toString())
                        .orderByChild("namaDetail")
                        .equalTo("namaDetail")
                    querydetailProyek.addValueEventListener(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            snapshot.ref.removeValue()
                            finish()
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })

                    querynamaproyek.addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            snapshot.ref.removeValue()
                            finish()
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })
                }
                .setNegativeButton("TIDAK"){dialog,id->
                    Toast.makeText(applicationContext,"DiBatalkan",Toast.LENGTH_SHORT).show()
                }
            val alert = buildMessage.create()
            alert.show()
        }


    btnTambah.setOnClickListener {
        val intent = Intent(this@adminDetailTugas,adminTambahTugas_DetailLagi::class.java)
        intent.putExtra("nmProyek",nama)
        startActivity(intent)

    }
    }
    private fun getCekboxDetail() {
// note pada database nama child harus sama dengan nama proyek
        database = Firebase.database.reference
        val namaproyek = intent.getStringExtra("namaProyek").toString()
        database.child("DetailProyek")
            .child(namaproyek).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    ListDetailProyek.clear()
                    if (snapshot.exists()) {
                        for (snap in snapshot.children) {
                            val data = snap.getValue(DetailInfo::class.java)
                            ListDetailProyek.add(data!!)
                        }
                        val adapter = adminDetailtugasAdapter(ListDetailProyek)
//                        val viewHolder = adminDetailtugasAdapter.ViewHolder(ListDetailProyek)
                        rvProyekDetail.adapter = adapter
                        adapter.setOnItemClickListener(object : adminDetailtugasAdapter.onItemClickListener{
                            override fun onItemClick(position: Int) {
                                val intent = Intent(this@adminDetailTugas,adminDetailstatus::class.java)
                                intent.putExtra("nmProyek",namaproyek)
                                intent.putExtra("cekbox",ListDetailProyek[position].cekbox)
                                intent.putExtra("status",ListDetailProyek[position].status)
                                intent.putExtra("id",ListDetailProyek[position].id)
                                startActivity(intent)

                                }
                            })

                        adapter.setOnEditClickListener(object : adminDetailtugasAdapter.btneditClickListener{
                            override fun onbtneditClick(position: Int) {
                                val intent = Intent(this@adminDetailTugas,adminEditNamaDetail::class.java)
                                intent.putExtra("nmProyek",namaproyek)
                                intent.putExtra("cekbox",ListDetailProyek[position].cekbox)
                                intent.putExtra("id",ListDetailProyek[position].id)
                                intent.putExtra("Status",ListDetailProyek[position].status)
                                startActivity(intent)
                            }

                        })

                        adapter.setOnHapusClickListener(object :adminDetailtugasAdapter.btnhapusClickListener{
                            override fun onbtnhapusClick(position: Int) {
                                Toast.makeText(this@adminDetailTugas,"Detail Dihapus",Toast.LENGTH_SHORT).show()


                                val namadetailProyek = namaproyek
                                val ID = ListDetailProyek[position].id
                                val nmdetail = ListDetailProyek[position].cekbox
                                val delete = database.child("DetailProyek").child(namadetailProyek).child(ID.toString())
                                delete.addValueEventListener(object : ValueEventListener{
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        snapshot.ref.removeValue()

                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        TODO("Not yet implemented")
                                    }

                                })

                            }

                        })
                    }
                }
                override fun onCancelled(error: DatabaseError) {

                }

            })


    }
    private fun percentage(){
        val adapter = adminDetailtugasAdapter(ListDetailProyek)
       val itemcount = adapter.itemCount
        Persen.text = itemcount.toString()
    }
}



