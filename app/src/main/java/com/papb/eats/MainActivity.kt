package com.papb.eats

import android.app.AlertDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import com.papb.eats.fragments.ReminderFragment
import com.papb.eats.fragments.SetingsFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_reminder.*
import kotlinx.android.synthetic.main.fragment_reminder.view.*
import kotlinx.android.synthetic.main.set_alarm_dialog.view.*
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val reminderFragment = ReminderFragment()
        val settingsFragment = SetingsFragment()

        makeCurrentFragment(reminderFragment)

        bottom_navigation.background = null
        bottom_navigation.menu.getItem(1).isEnabled = false

        fab.setOnClickListener{
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.set_alarm_dialog, null)
            val mBuilder = AlertDialog.Builder(this, R.style.DialogTheme)
                .setView(mDialogView)
                .setTitle("Reminder")

            val mAlertDialog = mBuilder.show()
            mDialogView.save_button.setOnClickListener{
                mAlertDialog.dismiss()
                val name = mDialogView.alarm_name.text.toString()
            }

            mDialogView.delete_button.setOnClickListener{
                mAlertDialog.dismiss()
            }
        }

        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId){
                R.id.ic_alarm -> makeCurrentFragment(reminderFragment)
                R.id.ic_settings -> makeCurrentFragment(settingsFragment)
            }
            true
        }

//        Toast.makeText(this, "Welcome",Toast.LENGTH_SHORT).show()
    }


    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }

}