package com.papb.eats

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import java.text.SimpleDateFormat
import java.util.*

// Ini service app yang jalan di background
class ReminderService: Service() {


    private val mainActivity = MainActivity()
    var handler: Handler = Handler()
    var runnable: Runnable? = null
    var delay = 10000

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        callNotification()
        return START_STICKY

    }


    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        val restartServiceIntent = Intent(applicationContext,this.javaClass)
        restartServiceIntent.setPackage(packageName)
        startService(restartServiceIntent)
        super.onTaskRemoved(rootIntent)
    }

    override fun onDestroy() {
        super.onDestroy()

        val broadcastIntent = Intent()
            .setAction("restartservice")
            .setClass(this, Restarter::class.java)
        this.sendBroadcast(broadcastIntent)
    }


    private fun callNotification() {
        handler.postDelayed(Runnable {
            handler.postDelayed(runnable!!, delay.toLong())

            val timeFormat = SimpleDateFormat("HH:mm")
            val currentTime = timeFormat.format(Date())

//            val reminders = mainActivity.getReminderList()

            val dbHelper = DBHelper(this)

            val reminders = dbHelper.listOfReminder()


            for (item in reminders) {
                if (item.time == currentTime) {
                    if (item.completed == 0) {
                        mainActivity.sendNotification(item.title)
                        dbHelper.updateComplete(item.id, 1)
                    }
                }
            }

        }.also { runnable = it }, delay.toLong())
    }
}