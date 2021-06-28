package com.papb.eats.fragments

import android.app.TimePickerDialog
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.papb.eats.DBHelper
import com.papb.eats.MainActivity
import com.papb.eats.R
import com.papb.eats.adapter.RecyclerAdapter
import kotlinx.android.synthetic.main.fragment_reminder.*
import kotlinx.android.synthetic.main.item_reminder.*
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private lateinit var recyclerView: RecyclerView
private var adapter: RecyclerAdapter = RecyclerAdapter(ArrayList(), listener = {}, completeListener = {})

/**
 * A simple [Fragment] subclass.
 * Use the [ReminderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReminderFragment : DialogFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reminder, container, false)
//        var rootView: View = inflater.inflate(R.layout.set_alarm_dialog, container, false)
//        return rootView
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ReminderFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReminderFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initRecyclerView()

    }

    public fun initRecyclerView(){

        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = RecyclerAdapter((activity as MainActivity).getReminderList(), listener = { item ->
//            Toast.makeText(activity, item.title, Toast.LENGTH_SHORT).show()
            var dialog = MaterialAlertDialogBuilder(activity as MainActivity, R.style.MaterialAlertDialog_Rounded)
            var view: View = layoutInflater.inflate(R.layout.set_alarm_dialog, null)
            dialog.setView(view)
            val alertDialog = dialog.create()

            val etSelectTime = view.findViewById<EditText>(R.id.et_select_time)
            val etAlarmTitle = view.findViewById<EditText>(R.id.et_alarm_title)
            val btnSaveButton = view.findViewById<TextView>(R.id.btn_save_button)
            val btnDeleteButton = view.findViewById<TextView>(R.id.btn_delete_button)

            etSelectTime.setText(item.time)
            etAlarmTitle.setText(item.title)

            btnSaveButton.setOnClickListener {
                val dbHelper = DBHelper(activity as MainActivity)

                val title = etAlarmTitle.text.toString()
                val time = etSelectTime.text.toString()

                dbHelper.updateData(item.id, title, time, 0)
                alertDialog.dismiss()
                fragmentManager?.beginTransaction()?.detach(this)?.attach(this)?.commit()
                (activity as MainActivity).checkNotif()
            }

            btnDeleteButton.setOnClickListener {
                alertDialog.dismiss()
                val dbHelper = DBHelper(activity as MainActivity)

                val confirmationDialog = MaterialAlertDialogBuilder(activity as MainActivity, R.style.MaterialAlertDialog_Rounded)
                var confirmationView: View = layoutInflater.inflate(R.layout.delete_dialog, null)
                confirmationDialog.setView(confirmationView)
                val deleteDialog = confirmationDialog.create()
                val btnNo = confirmationView.findViewById<TextView>(R.id.btn_no_button)
                val btnYes = confirmationView.findViewById<TextView>(R.id.btn_yes_button)

                btnNo.setOnClickListener {
                    deleteDialog.dismiss()
                    alertDialog.show()
                }

                btnYes.setOnClickListener {
                    dbHelper.deleteData(item.id)
                    deleteDialog.dismiss()
                    fragmentManager?.beginTransaction()?.detach(this)?.attach(this)?.commit()

                }
                deleteDialog.show()
                alertDialog.dismiss()
                fragmentManager?.beginTransaction()?.detach(this)?.attach(this)?.commit()

            }

            alertDialog.show()

            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            //Action saat etSelectTime diklik
            etSelectTime.setOnClickListener {
                val timePickerDialog = TimePickerDialog(
                    activity, R.style.DialogTheme,
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
        }, completeListener = { item->
            val dbHelper = DBHelper(activity as MainActivity)
            if (item.completed == 1) {
                dbHelper.updateComplete(item.id, 0)
            } else if (item.completed == 0) {
                dbHelper.updateComplete(item.id, 1)
            }
            Handler().postDelayed({
                fragmentManager?.beginTransaction()?.detach(this)?.attach(this)?.commit()
            }, 500)
        })
        recyclerView.adapter = adapter
    }

}