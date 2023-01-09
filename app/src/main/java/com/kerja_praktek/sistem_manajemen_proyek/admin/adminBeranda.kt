package com.kerja_praktek.sistem_manajemen_proyek.admin

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.kerja_praktek.sistem_manajemen_proyek.Base.BaseActivity
import com.kerja_praktek.sistem_manajemen_proyek.Model.ProyekInfo
import com.kerja_praktek.sistem_manajemen_proyek.R
import com.kerja_praktek.sistem_manajemen_proyek.admin.ViewHolder.adminBerandaAdapter

class adminBeranda : BaseActivity() {
    private lateinit var rv_item : RecyclerView
    private lateinit var database: DatabaseReference
    private lateinit var ListProyek: ArrayList<ProyekInfo>
//    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_beranda)



        val btnTambahkan = findViewById<ImageButton>(R.id.btnTambahkanProyek)

        rv_item = findViewById(R.id.rv_Proyek)
        rv_item.layoutManager = LinearLayoutManager(this)
        ListProyek = arrayListOf<ProyekInfo>()

        database = FirebaseDatabase.getInstance().getReference("Proyek")

        getProyek()



        btnTambahkan.setOnClickListener {
            val intent = Intent(applicationContext, adminTambahkanTugas::class.java)
            startActivity(intent)
        }
    }

    private fun getProyek() {

//        rv_item.visibility = View.VISIBLE
        database= FirebaseDatabase.getInstance().getReference("Proyek")

        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                ListProyek.clear()
                if(snapshot.exists()){
                    for (snap in snapshot.children){
                        val data =  snap.getValue(ProyekInfo::class.java)
                        ListProyek.add(data!!)
                    }
                    val adapter = adminBerandaAdapter(ListProyek)
                    rv_item.adapter = adapter
                    adapter.setOnItemClickListener(object : adminBerandaAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@adminBeranda,adminDetailTugas::class.java)

                            intent.putExtra("namaProyek",ListProyek[position].namaProyek)
                            intent.putExtra("deadline",ListProyek[position].deadline)
                            intent.putExtra("managerProyek",ListProyek[position].manager)
                            intent.putExtra("programmer_1",ListProyek[position].programmer_1)
                            intent.putExtra("programmer_2",ListProyek[position].programmer_2)
                            intent.putExtra("programmer_3",ListProyek[position].programmer_3)
                            intent.putExtra("programmer_4",ListProyek[position].programmer_4)
                            startActivity(intent)
                        }
                    })
                }else{}
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


}
