package com.kerja_praktek.sistem_manajemen_proyek.Model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class UsersInfo(
    var Username:String? ="",
    var Nama:String? = "",
    var Password:String? ="",
    var Jabatan:String? =""
)
