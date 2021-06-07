package com.papb.eats.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.papb.eats.DBHelper
import com.papb.eats.MainActivity
import com.papb.eats.model.Reminder
import kotlinx.android.synthetic.main.item_reminder.view.*

class ReminderHolder(view : View) : RecyclerView.ViewHolder(view) {
    private val tvReminderTime = view.tv_reminder_time
    private val tvReminderTitle = view.tv_reminder_title
    private val reminderSwitch = view.switch_reminder
    private val btnDelete = view.delete_button

    fun bindItem(reminder: Reminder) {
        tvReminderTime.text = reminder.time
        tvReminderTitle.text = reminder.title


        reminderSwitch.isChecked = reminder.completed == 0
    }

}