package com.kerja_praktek.sistem_manajemen_proyek

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.kerja_praktek.sistem_manajemen_proyek.NotifPack.Client
import com.kerja_praktek.sistem_manajemen_proyek.NotifPack.NotificationAPI
import com.kerja_praktek.sistem_manajemen_proyek.NotifPack.NotificationData
import com.kerja_praktek.sistem_manajemen_proyek.NotifPack.PushNotification
import com.kerja_praktek.sistem_manajemen_proyek.NotifPack.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val TOPIC = "/topics/myTopic"
class Send_Notif : AppCompatActivity() {
    private val TAG = "SendNotificationActivity"
//    private lateinit var notificationApi: NotificationAPI
//    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_notif)

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        val send = findViewById<Button>(R.id.btn_send)
        val title_notif = findViewById<TextView>(R.id.title_notif)
        val message_notif = findViewById<TextView>(R.id.message_notif)
//        notificationApi = Client.getClient("https://fcm.googleapis.com/fcm/send/").create(NotificationAPI::class.java)
//        database =  Firebase.database.reference
        send.setOnClickListener {
            val title:String = title_notif.text.toString()
            val message:String = message_notif.text.toString()
            if(title.isNotEmpty()&&message.isNotEmpty()){
                PushNotification(
                    NotificationData(title, message),
                    TOPIC
                ).also{
                    sendNotification(it)
                }
            }else{
                Toast.makeText(this@Send_Notif,"Failed Task", Toast.LENGTH_SHORT).show()
            }
        }
    }
   private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
       try {
           val response = RetrofitInstance.api.postNotification(notification)
           if (response.isSuccessful){
               Log.d(TAG, "Response: ${Gson().toJson(response)}")
           }else{
               Log.e(TAG, response.errorBody().toString())
           }

       }catch (e:Exception) {
           Log.e(TAG,e.toString())
       }

   }
}