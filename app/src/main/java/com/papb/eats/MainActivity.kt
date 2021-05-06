package com.papb.eats

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.papb.eats.R.*
import com.papb.eats.R.layout.set_alarm_dialog
import com.papb.eats.fragments.ReminderFragment
import com.papb.eats.fragments.SetingsFragment
import com.papb.eats.model.Task
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_reminder.*
import kotlinx.android.synthetic.main.fragment_reminder.view.*
import kotlinx.android.synthetic.main.set_alarm_dialog.*
import kotlinx.android.synthetic.main.set_alarm_dialog.view.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var timePicker: TimePickerHelper

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

        //fungsi fab klo diklik. yg ini kurang bagus
//        fab.setOnClickListener{
//            val mDialogView = LayoutInflater.from(this).inflate(set_alarm_dialog, null)
//            val mBuilder = AlertDialog.Builder(this, style.DialogTheme)
//                .setView(mDialogView)
//                .setTitle("Reminder")
//
//            val mAlertDialog = mBuilder.show()
//            mDialogView.save_button.setOnClickListener{
//                mAlertDialog.dismiss()
//                val name = mDialogView.alarm_name.text.toString()
//            }
//
//            mDialogView.delete_button.setOnClickListener{
//                mAlertDialog.dismiss()
//            }
//        }

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
                val task = Task()
                task.title = etAlarmName.text.toString()
                task.time = etSelectTime.text.toString()
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


}