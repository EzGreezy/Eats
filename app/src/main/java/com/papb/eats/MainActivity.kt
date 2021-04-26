package com.papb.eats

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.papb.eats.fragments.ReminderFragment
import com.papb.eats.fragments.SetingsFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val reminderFragment = ReminderFragment()
        val settingsFragment = SetingsFragment()

        makeCurrentFragment(reminderFragment)

        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId){
                R.id.ic_alarm -> makeCurrentFragment(reminderFragment)
                R.id.ic_settings -> makeCurrentFragment(settingsFragment)
            }
            true
        }

        Toast.makeText(this, "Welcome",Toast.LENGTH_SHORT).show()
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }
}