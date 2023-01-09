package com.kerja_praktek.sistem_manajemen_proyek.Util

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.kerja_praktek.sistem_manajemen_proyek.R
import io.reactivex.functions.Action

/**
 * Created by Ridza Achyar on 09/01/23.
 */
class DialogUtil {

    //General alert dialog with action yes and no
    fun showAlertDialog(context: Context,message: String, yesAction: Action?, noAction: Action? = null){
        val buildMessage = AlertDialog.Builder(context)
        buildMessage.setMessage(message)
            .setTitle(context.getString(R.string.notif_title_warning))
            .setCancelable(false)
            .setPositiveButton("YA") { _ , _ ->
                yesAction?.run()
            }
            .setNegativeButton("TIDAK"){ _ , _ ->
                noAction?.run()
            }
        val alert = buildMessage.create()
        alert.show()
    }
}