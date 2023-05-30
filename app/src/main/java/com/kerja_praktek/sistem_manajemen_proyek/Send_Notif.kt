package com.kerja_praktek.sistem_manajemen_proyek

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.kerja_praktek.sistem_manajemen_proyek.NotifPack.Client
import com.kerja_praktek.sistem_manajemen_proyek.NotifPack.NotificationAPI
import com.kerja_praktek.sistem_manajemen_proyek.NotifPack.NotificationData
import com.kerja_praktek.sistem_manajemen_proyek.NotifPack.PushNotification
import com.kerja_praktek.sistem_manajemen_proyek.NotifPack.RetrofitInstance
import com.kerja_praktek.sistem_manajemen_proyek.NotifPack.Usertoken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val TOPIC = "/topics/myTopic"
class Send_Notif : AppCompatActivity() {
    private val TAG = "SendNotificationActivity"
    private lateinit var  database : DatabaseReference
    var token = ""
//    private lateinit var notificationApi: NotificationAPI
//    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_notif)
        val send = findViewById<Button>(R.id.btn_send)

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)



//        notificationApi = Client.getClient("https://fcm.googleapis.com/fcm/send/").create(NotificationAPI::class.java)
//        database =  Firebase.database.reference
    val title_notif = findViewById<TextView>(R.id.title_notif)
    val message_notif = findViewById<TextView>(R.id.message_notif)
    var txtToken = findViewById<TextView>(R.id.txt_token)

    database = Firebase.database.reference
    database.child("token").child("Aisha661")
        .addValueEventListener(object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()){
                token = snapshot.getValue(String::class.java).toString()
                txtToken.text = token
            }else{
                txtToken.text = "Token not found"
            }
        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }

    })

        send.setOnClickListener {
            SendNotifP1()
            SendNotifP2()
            SendNotifP3()
            SendNotifP4()



        }
    }

    private fun SendNotifP1() {

        val title_notif = findViewById<TextView>(R.id.title_notif)
        val message_notif = findViewById<TextView>(R.id.message_notif)
        val title:String = "Title Coba"
        val token2 = "fv8U3bUBQzCueMdNQ_cR_z:APA91bHn2U70SOfqBkyw3Q_ob-xRzk5k-vrCDcK3oi-hkQ-ZJhkbBY883Zvbcty_jtWQ9qVY0biAg-58o7EB4W6bgc996zfKb9pdGYNjXbetqDMvt9pCAvRECuFOfZzbtyGEc2oozOPO"
        val message:String = "Message Coba"
        PushNotification(
            NotificationData(title, message),
            to = token
        ).also{
            sendNotification(it)
        }
    }
    private fun SendNotifP2() {

        val title_notif = findViewById<TextView>(R.id.title_notif)
        val message_notif = findViewById<TextView>(R.id.message_notif)
        val title:String = "Title Coba"
        val token2 = "fv8U3bUBQzCueMdNQ_cR_z:APA91bHn2U70SOfqBkyw3Q_ob-xRzk5k-vrCDcK3oi-hkQ-ZJhkbBY883Zvbcty_jtWQ9qVY0biAg-58o7EB4W6bgc996zfKb9pdGYNjXbetqDMvt9pCAvRECuFOfZzbtyGEc2oozOPO"
        val message:String = "Message Coba"
        PushNotification(
            NotificationData(title, message),
            to = token2
        ).also{
            sendNotification(it)
        }
    }
    private fun SendNotifP3() {

        val title_notif = findViewById<TextView>(R.id.title_notif)
        val message_notif = findViewById<TextView>(R.id.message_notif)
        val title:String = "Title Coba"
        val token2 = "dbmgg7nKSCeOQ7Khwj9zPY:APA91bHFFlzTMZNKpusNQFLg5jsUByf9Tl56e3ANZbfZDjE213sPS4T-bnt-ZzjLkZ6gCcxO1qFziMqzO_lp_AVEDcWVY0KI_DHCKIsc98ImLM4HQ2XLyNLTh_HXYNzZx5Lge9iH2-lh"
        val message:String = "Message Coba"
        PushNotification(
            NotificationData(title, message),
            to = token2
        ).also{
            sendNotification(it)
        }
    }
    private fun SendNotifP4() {

        val title_notif = findViewById<TextView>(R.id.title_notif)
        val message_notif = findViewById<TextView>(R.id.message_notif)
        val title:String = "Title Coba"
        val token2 = "fv8U3bUBQzCueMdNQ_cR_z:APA91bHn2U70SOfqBkyw3Q_ob-xRzk5k-vrCDcK3oi-hkQ-ZJhkbBY883Zvbcty_jtWQ9qVY0biAg-58o7EB4W6bgc996zfKb9pdGYNjXbetqDMvt9pCAvRECuFOfZzbtyGEc2oozOPO"
        val message:String = "Message Coba"
        PushNotification(
            NotificationData(title, message),
            to = token2
        ).also{
            sendNotification(it)
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