package com.kerja_praktek.sistem_manajemen_proyek.Model

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@IgnoreExtraProperties
@Parcelize
data class ProyekInfo (
    val NamaProyek: String? = "",
    val Deadline:String? = "",
    val Manager:String? = "",
    val Programmer_1 :String? = "",
    val Programmer_2 :String? = "",
    val Programmer_3 :String? = "",
    val Programmer_4 :String? = "",
    val DetailProyek:List<DetailInfo>? = null
        ):Parcelable
