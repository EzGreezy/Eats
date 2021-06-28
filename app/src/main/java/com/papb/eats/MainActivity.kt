package com.papb.eats

//import com.papb.eats.adapter.ReminderAdapter
import android.app.*
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.papb.eats.R.*
import com.papb.eats.adapter.RecyclerAdapter
import com.papb.eats.fragments.ReminderFragment
import com.papb.eats.fragments.SetingsFragment
import com.papb.eats.model.Reminder
import com.papb.eats.notification.NotificationReceiver
import com.papb.eats.notification.ReminderBroadcast
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private var adapter: RecyclerAdapter = RecyclerAdapter(ArrayList(), listener = {}, completeListener = {})

    var reminderFragment = ReminderFragment()
    val settingsFragment = SetingsFragment()

    private val CHANNEL_ID = "Eats channel"
    private val NOTIFICATION_ID = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)

        createNotificationChannel()


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
                //pake ini aja ya pit, sudah bisa wkwkwk
                val dbHelper = DBHelper(this)

                val title = etAlarmTitle.text.toString()
                val time = etSelectTime.text.toString()

                if(title.equals("")){
                    etAlarmTitle.error = getString(R.string.fill_title)
                }else if(time.equals("")){
                   etSelectTime.error = getString(R.string.select_time)
                }else{
                    dbHelper.insertData(title, time, 0)

                    alertDialog.dismiss()
                    supportFragmentManager.beginTransaction().detach(reminderFragment).attach(reminderFragment).commit()
                }
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

    override fun onResume() {

        var handler= Handler()
        var runnable: Runnable? = null
        var delay = 10000

        handler.postDelayed(Runnable {
            handler.postDelayed(runnable!!, delay.toLong())

            val timeFormat = SimpleDateFormat("HH:mm")
            val currentTime = timeFormat.format(Date())

            val reminders = getReminderList()

            val dbHelper = DBHelper(this)


//            for (item in reminders) {
//                if (item.time == currentTime) {
//                    if (item.completed == 0) {
//                        sendNotification(item.title)
//                        dbHelper.updateComplete(item.id, 1)
//                        supportFragmentManager.beginTransaction().detach(reminderFragment).attach(reminderFragment).commitAllowingStateLoss()
//                    }
//                }
//            }

            for (item in reminders) {
                if (item.completed == 0) {
                    setNotification(item, reminders.indexOf(item))
                }
            }
            supportFragmentManager.beginTransaction().detach(reminderFragment).attach(reminderFragment).commitAllowingStateLoss()

        }.also { runnable = it }, delay.toLong())
        super.onResume()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Eats Notification"
            val descriptionText = getString(R.string.don_t_forget_to_eat2)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("CHANNEL_ID", name,importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    public fun sendNotification(title: String): Notification? {
        val intent = Intent(this, ReminderBroadcast::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
//        val pendingIntent = PendingIntent.getBroadcast(this, code, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        //code buka gmaps
        val formattedTitle = title.replace(" ","+")
        val gmmIntentUri = Uri.parse("geo:0,0?q="+formattedTitle)
        val mapsIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapsIntent.setPackage("com.google.android.apps.maps")
        val pendingMapsIntent = PendingIntent.getActivity(this,0, mapsIntent, 0)

        //code delete notif
        val deleteIntent = Intent(this, ReminderBroadcast::class.java)
        deleteIntent.apply {
            action = "Delete"
            putExtra("channelId","100")
            putExtra("NOTIFICATION_ID", NOTIFICATION_ID)
        }
        val deletePendingIntent = PendingIntent.getBroadcast(this,0, deleteIntent, 0)

        val builder = NotificationCompat.Builder(this,CHANNEL_ID)
            .setSmallIcon(R.drawable.eats_logo_only)
            .setContentTitle(getString(R.string.don_t_forget_to_eat2) + " " + title)
            .setContentText(getString(R.string.find_resto))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .addAction(R.mipmap.ic_launcher, getString(string.selfcook), deletePendingIntent)
            .addAction(R.mipmap.ic_launcher, getString(R.string.open_gmaps), pendingMapsIntent)
//            .addInvisibleAction(R.mipmap.ic_launcher, getString(R.string.open_gmaps), pendingIntent)
//            .addInvisibleAction(R.mipmap.ic_launcher, getString(R.string.open_gmaps), pendingIntent)
//            .setDeleteIntent()
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)){
//            builder.setAutoCancel(true)
            notify(NOTIFICATION_ID, builder.build())
        }

        return builder.build()

//        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
//        if(reminder.timeMillis.toLong() >= System.currentTimeMillis()){
//            alarmManager.set(AlarmManager.RTC_WAKEUP, reminder.timeMillis.toLong(), pendingIntent)
//        }
    }


    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(id.fl_wrapper, fragment)
            commit()
        }

    //Buat nampilin list remindernya
    public fun refreshList() {
        val dbHelper = DBHelper(this)

        val reminderList = dbHelper.listOfReminder()
        Log.e ("pppp", "${reminderList.size}")

        //display data
        adapter = RecyclerAdapter(reminderList, listener = {}, completeListener = {})
    }


    public fun getReminderList() : ArrayList<Reminder> {
        val dbHelper = DBHelper(this)
        val reminders = dbHelper.listOfReminder()
        return reminders
    }

    fun timeCalc(time: String): Long {
        val hourToMinutes = time.substring(0,1).toLong()*60
        val minutes = time.substring(3,4).toLong()
        val totalMinutes = hourToMinutes+minutes

        val timeInMillis = totalMinutes*60000
        return timeInMillis
    }

    private fun setNotification(reminder: Reminder, code: Int) {
        val dbHelper = DBHelper(this)

        val intent = Intent(this, NotificationReceiver::class.java)
        intent.putExtra("title", reminder.title)
        val pendingIntent = PendingIntent.getBroadcast(this,code,intent,PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val timeInMillis = System.currentTimeMillis()+timeDifference(reminder.time)
        alarmManager.set(AlarmManager.RTC_WAKEUP,timeInMillis,pendingIntent)
        dbHelper.updateComplete(reminder.id,1)

    }

    fun timeDifference(time: String): Long {
        val timeFormat = SimpleDateFormat("HH:mm")
        val currentTime = timeFormat.format(Date())
        var currentHour = currentTime.substring(0,2).toInt()
        var currentMinute = currentTime.substring(3,5).toInt()

        var timeHour = time.substring(0,2).toInt()
        var timeMinute = time.substring(3,5).toInt()

        var hour = 0
        var minute = 0

        if (timeHour<currentHour) {
            timeHour += 24
        }

        if (timeMinute<currentMinute){
            timeHour -= 1
            timeMinute += 60

        }
        minute = timeMinute-currentMinute
        hour = timeHour-currentHour

        val totalMinutes = hour*60 + minute

        return totalMinutes.toLong()*60000
    }
}