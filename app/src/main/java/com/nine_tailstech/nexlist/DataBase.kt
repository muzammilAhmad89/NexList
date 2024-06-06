package com.nine_tailstech.nexlist

import android.content.ContentValues
import android.content.Context
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DataBase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    val TABLE_NAME = "Tasks" // добавляем название таблицы в переменную
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Tasks (id INTEGER PRIMARY KEY, name TEXT, description TEXT, dateAdd TEXT, dateAcc TEXT, status BOOLEAN)")
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS Tasks")
        onCreate(db)
    }
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }
    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "FeedReader.db"
    }

    // Убеждаемся, что нужная таблица существует в базе данных
    fun initialize() {
        val db = writableDatabase
        val c = db.rawQuery("SELECT DISTINCT tbl_name FROM sqlite_master WHERE tbl_name = 'Tasks'", null)
        if (c == null) { onCreate(db) }
        else if (c.count == 0) { onCreate(db) }
        c.close()
        db.close()
    }

    //  Функция добавления новой записи в базу данных
    fun addTask(name: String, description: String, dateAdd: String? = null, dateAcc: String, status: Boolean) {
        // Автоподстановка текущего времени в значение dateAdd
        val dateAdd1 = dateAdd ?: SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Calendar.getInstance().time)
        val values = ContentValues().apply {
            put("name", name)
            put("description", description)
            put("dateAdd", dateAdd1)
            put("dateAcc", dateAcc)
            put("status", status)
        }
        val db = writableDatabase
        db.insert("Tasks", null, values)
    }

    fun getTaskById(id: Int): Task? {
        val db = readableDatabase
        val projection = arrayOf("id", "name", "description", "dateAdd", "dateAcc", "status")
        val selection = "id = ?"
        val selectionArgs = arrayOf(id.toString())
        val cursor = db.query("Tasks", projection, selection, selectionArgs, null, null, null)
        var task: Task? = null
        with(cursor) {
            if (moveToNext()) {
                val taskId = getInt(getColumnIndexOrThrow("id"))
                val taskName = getString(getColumnIndexOrThrow("name"))
                val taskDescription = getString(getColumnIndexOrThrow("description"))
                val taskDateAdd = getString(getColumnIndexOrThrow("dateAdd"))
                val taskDateAcc = getString(getColumnIndexOrThrow("dateAcc"))
                val taskStatus = getInt(getColumnIndexOrThrow("status")) == 1
                task = Task(taskId, taskName, taskDescription, taskDateAdd, taskDateAcc, taskStatus)
            }
        }
        cursor.close()
        return task
    }

    fun getCount(): Int {
        val db = readableDatabase
        val count = DatabaseUtils.queryNumEntries(db, TABLE_NAME)
        db.close()
        return count.toInt()
    }

    fun deleteTaskById(id: Int) {
        val db = writableDatabase
        val selection = "id = ?"
        val selectionArgs = arrayOf(id.toString())
        db.delete("Tasks", selection, selectionArgs)
        db.close()
    }

    class Task(val id: Int, val name: String, val description: String, val dateAdd: String, val dateAcc: String, val status: Boolean)
}