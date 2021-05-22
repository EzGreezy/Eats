package com.papb.eats

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.EditText
import com.papb.eats.model.Reminder

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    companion object {
        val DATABASE_NAME = "Eats.db"
        val TABLE_NAME = "reminders"
        val ID = "id"
        val TITLE = "title"
        val TIME = "time"
        val COMPLETED = "isComplete"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_REMINDERS_TABLE = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TITLE + " VARCHAR(64), "
                + TIME + " VARCHAR(64), "
                + COMPLETED + " BOOLEAN" + ")")
//        db?.execSQL("CREATE TABLE $TABLE_NAME ($ID INTEGER PRIMARY KEY AUTOINCREMENT, $TITLE VARCHAR(64), $TIME VARCHAR(64), $COMPLETED BOOLEAN);")
        db.execSQL(CREATE_REMINDERS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
//        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME;")
        onCreate(db)
    }

    //Insert data ke database
    fun insertData(title: String, time: String, isCompleted: Boolean) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(TITLE, title)
        contentValues.put(TIME, time)
        contentValues.put(COMPLETED, isCompleted)
        db.insert(TABLE_NAME, null, contentValues)
    }

    //Query list data
    fun listOfReminder(time: String, title: String): ArrayList<Reminder> {
        val db = this.writableDatabase
        val res = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        val reminderList = ArrayList<Reminder>()
        while (res.moveToNext()) {
            var Reminder = Reminder(time, title)
            Reminder.id = Integer.valueOf(res.getString(0)).toString()
            Reminder.title = res.getString(1)
            Reminder.time = res.getString(2)
            reminderList.add(Reminder)
        }
        return reminderList
    }

    // Update data
    fun updateData(id: String, title: String, time: String, isCompleted: Boolean) : Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID, id)
        contentValues.put(TITLE, title)
        contentValues.put(TIME, time)
        contentValues.put(COMPLETED, isCompleted)
        db.update(TABLE_NAME, contentValues, "ID = ?", arrayOf(id))
        return true
    }

    // Delete data
    fun deleteData(id: String) : Int {
        val db = this.writableDatabase
        return db.delete(TABLE_NAME, "ID = ?", arrayOf(id))
    }
}
