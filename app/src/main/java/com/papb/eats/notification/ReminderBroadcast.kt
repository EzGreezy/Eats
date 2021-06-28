package com.papb.eats.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings.Global.getString
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.papb.eats.R

class ReminderBroadcast: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.apply {
            val channelId = getStringExtra("UserId")
            val NOTIFICATION_ID = getIntExtra("NOTIFICATION_ID",0)

//            Toast.makeText(context,"Deleted ID: $channelId",Toast.LENGTH_SHORT).show()

            context?.apply {
                // Remove the notification programmatically on button click
                NotificationManagerCompat.from(this).cancel(NOTIFICATION_ID)
            }
        }
    }
}