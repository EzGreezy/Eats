package com.papb.eats

import android.app.*
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.papb.eats.R.*
import com.papb.eats.R.layout.set_alarm_dialog
import com.papb.eats.adapter.ReminderAdapter
import com.papb.eats.fragments.ReminderFragment
import com.papb.eats.fragments.SetingsFragment
import com.papb.eats.model.Reminder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_reminder.*
import kotlinx.android.synthetic.main.fragment_reminder.view.*
import kotlinx.android.synthetic.main.set_alarm_dialog.*
import kotlinx.android.synthetic.main.set_alarm_dialog.view.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var timePicker: TimePickerHelper
    private var todayList: ArrayList<Reminder> = arrayListOf()
    private var upcomingList: ArrayList<Reminder> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)

        val reminderFragment = ReminderFragment()
        val settingsFragment = SetingsFragment()

        //milih fragment apa yg jadi homepage
        makeCurrentFragment(reminderFragment)

        //warna bottom app bar
        bottom_navigation.background = null
        //menu ditengah jadi placeholder
        bottom_navigation.menu.getItem(1).isEnabled = false

        //Calendar
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val date = String.format("%02d/%02d/%04d", day, month, year)

//        addDataToList(date)
//        setReminderAdapter(rv_today, todayList)
//        setReminderAdapter(rv_upcoming, upcomingList)
//        createNotificationChannel()

        //fungsi fab klo diklik. pake yg ini ajah
        fab.setOnClickListener{
            var dialog = MaterialAlertDialogBuilder(this, style.MaterialAlertDialog_Rounded)
            var view: View = layoutInflater.inflate(R.layout.set_alarm_dialog, null)
            dialog.setView(view)
            val alertDialog = dialog.create()

            //Inisiasi view etAddDate dan etAddTime
            val etSelectTime = view.findViewById<EditText>(R.id.et_select_time)
            val etAlarmName = view.findViewById<EditText>(R.id.et_alarm_name)
            val btnSaveButton = view.findViewById<TextView>(R.id.btn_save_button)
            val btnDeleteButton = view.findViewById<TextView>(R.id.btn_delete_button)

            //fungsi button save&delete klo diklik
            btnSaveButton.setOnClickListener {
                val reminder = Reminder()
                reminder.title = etAlarmName.text.toString()
                reminder.time = etSelectTime.text.toString()
            }
            btnDeleteButton.setOnClickListener {
                alertDialog.dismiss()
            }
            alertDialog.show()

            //Inisiasi value calendar
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            //Action saat etSelectTime diklik
            etSelectTime.setOnClickListener {
                val timePickerDialog = TimePickerDialog(
                    this, R.style.DialogTheme,
                    TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                        etSelectTime.setText(String.format("%02d:%02d", selectedHour, selectedMinute))
                        val mHour = etSelectTime.text
                        val hourList = mHour.split(':').map { it.trim() }
                    },
                    hour,
                    minute,
                    true
                )
                timePickerDialog.show()
            }
        }

        //fungsi bottom navbar klo diklik pindah fragment
        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId){
                id.ic_alarm -> makeCurrentFragment(reminderFragment)
                id.ic_settings -> makeCurrentFragment(settingsFragment)
            }
            true
        }
    }


    //function onCreate baru buat alertdialog
//    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
//        super.onCreate(savedInstanceState, persistentState)
//        setContentView(layout.set_alarm_dialog)
//
//        timePicker = TimePickerHelper(this, true, true)
//        button_time.setOnClickListener {
//            showTimePickerDialog()
//        }
//
//    }

//    fun showTimePickerDialog() {
//        val cal = Calendar.getInstance()
//        val h = cal.get(Calendar.HOUR_OF_DAY)
//        val m = cal.get(Calendar.MINUTE)
//        timePicker.showDialog(h, m, object : TimePickerHelper.Callback {
//            override fun onTimeSelected(hourOfDay: Int, minute: Int) {
//                val hourStr = if (hourOfDay < 10) "0${hourOfDay}" else "${hourOfDay}"
//                val minuteStr = if (minute < 10) "0${minute}" else "${minute}"
//                tvTime.text = "${hourStr}:${minuteStr}"
//            }
//        })
//    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(id.fl_wrapper, fragment)
            commit()
        }

    private fun setReminderAdapter(recyclerView: RecyclerView?, list: java.util.ArrayList<Reminder>) {
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        val reminderAdapter = ReminderAdapter(this, list)
        recyclerView.adapter = reminderAdapter
        recyclerView.setNestedScrollingEnabled(false);
    }

    //Mungkin bisa dipake buat nyimpen data ke sqlite (dari galih)
//    private fun addDataToList(date: String) {
//        //ValueEventListener untuk mengambil data dari firebase dengan child goal user tsb
//        database
//            .child("users")
//            .child(userId)
//            .child("tasks")
//            .orderByChild("timeMillis")
//            .addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    //Hapus data dalam arraylist agar tidak jadi penumpukan data
//                    todayList.clear()
//                    upcomingList.clear()
//
//                    //Ambil semua child dalam goal dan masukan ke items
//                    var items = snapshot.children
//                    //Lakukan iterasi pada setiap item lalu buat class dan tambahkan ke list
//                    items.forEach {
//                        var task = it.getValue(Task::class.java)
//                        if (task!!.completed == false) {
//                            if (task!!.date.equals(date)) {
//                                todayList.add(task)
//                            } else {
//                                upcomingList.add(task)
//                            }
//                        }
//                    }
//                    refreshList()
//
//                    if (todayList.size == 0) {
//                        tv_no_task_today.visibility = View.VISIBLE
//                    } else {
//                        tv_no_task_today.visibility = View.GONE
//                        setNotification(todayList[0])
//                    }
//
//                    if (upcomingList.size == 0) {
//                        tv_no_task_upcoming.visibility = View.VISIBLE
//                    } else {
//                        tv_no_task_upcoming.visibility = View.GONE
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    TODO("Not yet implemented")
//                }
//
//            })
//    }

    //Bagian ini belum
//    fun refreshList() {
//        val todayAdapter = TaskAdapter(this, todayList)
//        val upcomingAdapter = TaskAdapter(this, upcomingList)
//        rv_today.adapter = todayAdapter
//        rv_upcoming.adapter = upcomingAdapter
//    }

//    private fun createNotificationChannel() {
//        var name:CharSequence = "Task Reminder"
//        var description = "Reminder for task"
//        var importance = NotificationManager.IMPORTANCE_DEFAULT
//
//        var channel = NotificationChannel("notifyTaskmaster", name, importance)
//        channel.description = description
//
//        var notificationManager = getSystemService(NotificationManager::class.java)
//        notificationManager.createNotificationChannel(channel)
//    }

//    private fun setNotification(task: Task) {
//
//        var intent = Intent(this, ReminderBroadcast::class.java)
//        intent.putExtra("title", task.title)
//        var pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//
//        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
//
//        var timeAtButtonClick: Long = System.currentTimeMillis()
//
//        alarmManager.set(AlarmManager.RTC_WAKEUP, task.timeMillis.toLong(), pendingIntent)
//
//    }


}