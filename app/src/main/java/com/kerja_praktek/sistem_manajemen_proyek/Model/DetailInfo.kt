package com.kerja_praktek.sistem_manajemen_proyek.Model

//@IgnoreExtraProperties
data class DetailInfo (
    var cekbox: String? = "",
    var tanggal : String? = null,
    var bulan :String? = null,
    var tahun : String? = null,
    var status: Boolean = false,
    var id: String? = ""
)