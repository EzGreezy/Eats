package com.papb.eats.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.papb.eats.R
import com.papb.eats.model.Reminder

class ReminderAdapter2 : RecyclerView.Adapter<ReminderAdapter2.ItemViewHolder>() {

    private lateinit var reminderList: ArrayList<Reminder>

    fun addReminders(reminders: ArrayList<Reminder> = ArrayList()) {
        this.reminderList = reminders
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ItemViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.item_reminder, parent, false)
    )


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val reminder = reminderList[position]
        holder.bindItem(reminder)
    }

    override fun getItemCount(): Int {
        return reminderList.size
    }

    class ItemViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
        private var tvReminderTitle = view.findViewById<TextView>(R.id.tv_reminder_title)
        private var tvReminderTime = view.findViewById<TextView>(R.id.tv_reminder_time)
//        private var switchOnOff = view.findViewById<Switch>(R.id.switch_reminder)

        fun bindItem(reminder: Reminder){
            tvReminderTitle.text = reminder.title
            tvReminderTime.text = reminder.time

        }


    }
}