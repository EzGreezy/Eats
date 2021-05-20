package com.papb.eats.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.papb.eats.R
import com.papb.eats.model.Reminder
import java.util.prefs.Preferences

class ReminderAdapter(mContext: Context, private var reminderList: ArrayList<Reminder>)
    : RecyclerView.Adapter<ReminderAdapter.ItemViewHolder>() {

    private var mContext = mContext

    fun addReminders(reminders: ArrayList<Reminder> = ArrayList()) {
        this.reminderList = reminders
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_reminder, parent, false)
        return ItemViewHolder(adapterLayout, mContext)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val reminder = reminderList[position]
        holder.bindItem(reminder)
    }

    override fun getItemCount(): Int {
        return reminderList.size
    }

    class ItemViewHolder(private val view: View, context: Context): RecyclerView.ViewHolder(view) {
        private var tvReminderTitle = view.findViewById<TextView>(R.id.tv_reminder_title)
        private var tvReminderTime = view.findViewById<TextView>(R.id.tv_reminder_time)
//        private var switchOnOff = view.findViewById<Switch>(R.id.switch_reminder)

//        val tvReminderTitle: TextView = view.findViewById(R.id.tv_reminder_title)
//        val tvReminderTime: TextView = view.findViewById(R.id.tv_reminder_time)
//        val switchOnOff: Switch = view.findViewById(R.id.switch_reminder)
//        val context = context

        fun bindItem(reminder: Reminder){
            tvReminderTitle.text = reminder.title
            tvReminderTime.text = reminder.time

//            tvReminderTitle.setText(reminder.title)
//            tvReminderTime.setText(reminder.time)

//            database = FirebaseDatabase.getInstance().reference
//            preferences = Preferences(context)
//            auth = Firebase.auth
//            user = auth.currentUser

//            if(reminder.completed == false){
//                btnCheck.setImageResource(R.drawable.ic_uncompleted)
//            } else{
//                btnCheck.setImageResource(R.drawable.ic_completed)
//            }
//
//            switchOnOff.setOnClickListener {
//                val item = database
//                    .child("users")
//                    .child(user.uid)
//                    .child("tasks")
//                    .child(reminder.id.toString())
//                item.removeValue()
//                Toast.makeText(context,"Reminder ${reminder.title} is On", Toast.LENGTH_LONG).show()
//            }
//
//            btnCheck.setOnClickListener {
//                task.completed = !task.completed
//                database.child("users")
//                    .child(user.uid)
//                    .child("tasks")
//                    .child(task.id.toString())
//                    .child("completed")
//                    .setValue(task.completed)
//            }

        }


    }
}