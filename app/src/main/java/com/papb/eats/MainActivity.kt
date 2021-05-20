package com.papb.eats

import android.app.*
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.papb.eats.R.*
//import com.papb.eats.adapter.ReminderAdapter
import com.papb.eats.adapter.ReminderAdapter2
import com.papb.eats.fragments.ReminderFragment
import com.papb.eats.fragments.SetingsFragment
import com.papb.eats.model.Reminder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.set_alarm_dialog.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var recyclerView: RecyclerView
    private var adapter: ReminderAdapter2? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)

        initView()
        initRecyclerView()

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
//        val date = String.format("%02d/%02d/%04d", day, month, year)


        //fungsi fab klo diklik. pake yg ini ajah
        fab.setOnClickListener{
            var dialog = MaterialAlertDialogBuilder(this, style.MaterialAlertDialog_Rounded)
            var view: View = layoutInflater.inflate(R.layout.set_alarm_dialog, null)
            dialog.setView(view)
            val alertDialog = dialog.create()

            //Inisiasi view
            val etSelectTime = view.findViewById<EditText>(R.id.et_select_time)
            val etAlarmTitle = view.findViewById<EditText>(R.id.et_alarm_title)
            val btnSaveButton = view.findViewById<TextView>(R.id.btn_save_button)
            val btnDeleteButton = view.findViewById<TextView>(R.id.btn_delete_button)

            //fungsi button save&delete klo diklik
            btnSaveButton.setOnClickListener {
//                val reminder = Reminder(time)
//                reminder.title = etAlarmTitle.text.toString()
//                reminder.time = etSelectTime.text.toString()
                addReminder()
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

    private fun addReminder(){
        val title= et_alarm_title.text.toString()
        val time= et_select_time.text.toString()

        if(title.isEmpty()){
            et_alarm_title.error = getString(string.fill_title)
        }else if(time.isEmpty()){
            et_select_time.error = getString(string.select_time)
        } else{
            dbHelper.insertData(title, time, false)
            refreshList()
        }
    }

    private fun initRecyclerView(){
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ReminderAdapter2()
        recyclerView.adapter = adapter
    }
    private fun initView(){
        recyclerView= findViewById(R.id.recyclerView)
    }


    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(id.fl_wrapper, fragment)
            commit()
        }

//    private fun setReminderAdapter(recyclerView: RecyclerView?, list: java.util.ArrayList<Reminder>) {
//        recyclerView!!.layoutManager = LinearLayoutManager(this)
//        val reminderAdapter = ReminderAdapter2(list)
//        recyclerView.adapter = reminderAdapter
//        recyclerView.setNestedScrollingEnabled(false);
//    }


    //Buat nampilin list remindernya
    private fun refreshList() {
        val reminderList = dbHelper.listOfReminder(time = String(), title = String())
        Log.e ("pppp", "${reminderList.size}")

        //display data
        adapter?.addReminders(reminderList)
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

}