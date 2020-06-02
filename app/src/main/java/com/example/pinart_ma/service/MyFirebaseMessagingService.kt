package com.example.pinart_ma.service

import android.preference.PreferenceManager
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService(){

    val TAG = "FCM Service"

    /*
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.from)

        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.notification!!.body)
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }*/

    override fun onNewToken(p0: String?) {
        super.onNewToken(p0)

        Log.d("TOKEN", "The Token refreshed $p0")
        p0?.let { saveDataCacheFake("toke-fire-base", it) };

    }


    fun saveDataCacheFake(key:String, data:String){
        val myPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val myEditor = myPreferences.edit()
        myEditor.putString(key, data);
        myEditor.commit();
    }

}