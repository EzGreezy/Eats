package com.papb.eats.notification

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.startActivity
import com.papb.eats.MainActivity
import com.papb.eats.R

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val title = intent?.getStringExtra("title")
        val formattedTitle = title?.replace(" ","+")
        val gmmIntentUri = Uri.parse("geo:0,0?q="+formattedTitle)
        val mapsIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapsIntent.setPackage("com.google.android.apps.maps")
        val pendingMapsIntent = PendingIntent.getActivity(context,0, mapsIntent, 0)

        val contentTitle = context?.getString(R.string.don_t_forget_to_eat2)
        val contentText = context?.getString(R.string.find_resto)
        val contentAction = context?.getString(R.string.open_gmaps)

        val builder = NotificationCompat.Builder(context!!, "Eats channel")
            .setSmallIcon(R.drawable.eats_logo_only)
            .setContentTitle(contentTitle+" "+title)
            .setContentText(contentText)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(R.mipmap.ic_launcher, contentAction, pendingMapsIntent)
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(200, builder.build())
        Toast.makeText(context,title,Toast.LENGTH_SHORT).show()
    }


}