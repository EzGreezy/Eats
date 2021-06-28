package com.papb.eats.adapter

import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.papb.eats.DBHelper
import com.papb.eats.MainActivity
import com.papb.eats.R
import com.papb.eats.model.Reminder
import kotlinx.android.synthetic.main.item_reminder.view.*

class ReminderHolder(view : View) : RecyclerView.ViewHolder(view) {
    private val tvReminderTime = view.tv_reminder_time
    private val tvReminderTitle = view.tv_reminder_title
    val reminderSwitch = view.switch_reminder

    fun bindItem(reminder: Reminder) {
        tvReminderTime.text = reminder.time
        tvReminderTitle.text = reminder.title

        reminderSwitch.isChecked = reminder.completed == 0

    }

}