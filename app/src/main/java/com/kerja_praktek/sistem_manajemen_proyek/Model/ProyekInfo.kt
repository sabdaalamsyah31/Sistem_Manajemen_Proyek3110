package com.kerja_praktek.sistem_manajemen_proyek.Model

import com.google.firebase.database.IgnoreExtraProperties

//@IgnoreExtraProperties
data class ProyekInfo(
    val namaProyek: String? = "",
    val deadline:String? = "",
    val manager:String? = "",
    val programmer_1:String? = "",
    val programmer_2:String? = "",
    val programmer_3:String? = "",
    val programmer_4:String? = "",
//    val detail : List<DetailInfo>? = null
        )
