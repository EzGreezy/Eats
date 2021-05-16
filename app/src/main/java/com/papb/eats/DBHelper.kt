package com.papb.eats

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.papb.eats.model.Task

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    companion object {
        val DATABASE_NAME = "Eats"
        val TABLE_NAME = "task"
        val ID = "id"
        val TITLE = "title"
        val TIME = "time"
        val ACTIVE = "isActive"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE $TABLE_NAME ($ID INTEGER PRIMARY KEY AUTOINCREMENT, $TITLE VARCHAR(64), $TIME VARCHAR(64), $ACTIVE BOOLEAN)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    //Insert data ke database
    fun insertData(title: String, time: String, isActive: Boolean) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(TITLE, title)
        contentValues.put(TIME, time)
        contentValues.put(ACTIVE, isActive)
        db.insert(TABLE_NAME, null, contentValues)
    }

    //List task blm jadi masih nunggu Model
//    fun listOfTask(): ArrayList<Task> {
//        val db = this.writableDatabase
//        val res = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
//        val taskList = ArrayList<Task>()
//        while (res.moveToNext()) {
//            var Task = Task()
//            Task.id = Integer.valueOf(res.getString(0)).toString()
//            Task.title = res.getString(1)
//            Task.time = res.getString(2)
//
//        }
//    }

}
