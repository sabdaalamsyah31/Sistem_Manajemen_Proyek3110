package com.kerja_praktek.sistem_manajemen_proyek.Model

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@IgnoreExtraProperties
@Parcelize
data class DetailInfo (
    val Detail: String? = "",
    val Status: Boolean? = null
):Parcelable