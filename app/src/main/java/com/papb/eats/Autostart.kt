package com.papb.eats

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class Autostart: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("Autostart", "BOOT_COMPLETED broadcast received. Executing starter service.")

        val intent = Intent(context, ReminderService::class.java)
        context?.startService(intent)
    }
}

// Ini buat nyoba aktivin app di background, tapi masih gagal