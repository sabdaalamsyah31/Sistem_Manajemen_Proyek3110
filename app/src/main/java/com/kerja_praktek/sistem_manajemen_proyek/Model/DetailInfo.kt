package com.kerja_praktek.sistem_manajemen_proyek.Model

//@IgnoreExtraProperties
data class DetailInfo (
    var cekbox: String? = "",
    var tanggal : String? = "",
    var bulan :String? = "",
    var tahun : String? = "",
    var status: Boolean = false,
    var id: String? = ""
)