package com.papb.eats

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Build.VERSION_CODES
import android.util.Log

class Restarter: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("Broadcast Listened", "Service tried to stop")

        if (Build.VERSION.SDK_INT >= VERSION_CODES.O) {
            context?.startForegroundService(Intent(context,ReminderService::class.java))
        } else {
            context?.startService(Intent(context,ReminderService::class.java))
        }

    }
}

// Ini buat nyoba aktivin app di background, tapi masih gagal