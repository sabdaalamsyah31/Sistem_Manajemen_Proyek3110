package com.kerja_praktek.sistem_manajemen_proyek.Base

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.kerja_praktek.sistem_manajemen_proyek.R

open class BaseActivity : AppCompatActivity() {
    //progressDialog
    lateinit var dialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {

        // inisialisasi view
        // inisialisasi view
        val decorView = window.decorView
        // Hide the status bar.
        // Hide the status bar.
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        // Hide ActionBar
        // Hide ActionBar
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        super.onCreate(savedInstanceState)
        dialog = ProgressDialog(this@BaseActivity)
        dialog.setMessage("Loading. Please wait...")
        dialog.isIndeterminate = true

//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
//        // Remember that you should never show the action bar if the
//        // status bar is hidden, so hide that too if necessary.
//        actionBar?.hide()
    }



    //showAlertDialog
    fun showDialog(title: String, msg: String){
        val builder = AlertDialog.Builder(this@BaseActivity)
            .setTitle(title)
            .setMessage(msg)
            .setCancelable(true)
            .setPositiveButton(getString(R.string.ok)) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }



}