package com.kerja_praktek.sistem_manajemen_proyek.Helper

import android.content.Context
import android.content.SharedPreferences
//import kotlinx.coroutines.DefaultExecutor.shared

class PreferencesHelper(context: Context) {

    private val PREF_NAME = "SistemManajemenProyek"
    private val sharedPref : SharedPreferences
    val editor : SharedPreferences.Editor



    init {
        sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        editor = sharedPref.edit()
    }
    fun put (key:String, value:String){
        editor.putString(key,value).apply()
    }
    fun getString(key:String):String?{
        return sharedPref.getString(key,null)
    }
    fun put(key: String,value:Boolean){
        editor.putBoolean(key,value).apply()
    }
    fun getBoolean(key:String):Boolean{
        return sharedPref.getBoolean(key,false)
    }
    fun clear(){
        editor.clear().apply()
    }
}