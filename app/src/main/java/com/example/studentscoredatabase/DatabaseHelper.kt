package com.example.studentscoredatabase

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        private val DB_NAME = "student_management "
        private val DB_VERSION = 1
        val TABLE_NAME = " student_score"
        val ID = "id"
        val Subject = "Subject"
        val Score = "Score"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        val query = (
                "CREATE TABLE $TABLE_NAME (" +
                        "$ID INTEGER PRIMARY KEY," +
                        "$Subject TEXT," +
                        "$Score TEXT" + ")"
                )
        db?.execSQL(query) // nullable
    }
    // Called when the database needs to be upgraded
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int){
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME") // non-null assertion.
        //Error if null at compile time

        onCreate(db)
    }
    // This method is to add a User record in DB
    fun addUser(subject: String, score: String) {
        // This ContentValues class is used to store a set of values
        val values = ContentValues()
        // insert key-value pairs
        values.put(Subject, subject)
        values.put(Score, score)
        // create a writable DB variable of our database to insert record
        val db = this.writableDatabase
        // insert all values into DB
        db.insert(TABLE_NAME, null, values)
        // close DB
        db.close()
    }
    // This method is get all User records from DB
    fun getAll(): ArrayList<User> {
        // create a readable DB variable of our database to read record

        val db = this.readableDatabase
        // read all records from DB and get the cursor
        val cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null)
        val userList = ArrayList<User>() // User ArrayList
        if (cursor.moveToFirst()) {
            do { // add all users to the list
                userList.add(
                    User(
                        cursor.getInt(cursor.getColumnIndexOrThrow(ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Subject)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Score))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return userList
    }

    fun delete(subject: String): Int {
        // create a writable DB variable of our database to delete record
        val db = this.writableDatabase
        // delete a user by NAME
        val rows = db.delete(TABLE_NAME, "name=?", arrayOf(subject))
        db.close();
        return rows // 0 or 1
    }
    fun update(subject: String, score: String): Int {
        // create a writable DB variable of our database to update record
        val db = this.writableDatabase
        // This ContentValues class is used to store a set of values
        val values = ContentValues()
        values.put(Score, score)
        val rows = db.update(TABLE_NAME, values, "name=?", arrayOf(subject))
        db.close()
        return rows // rows updated
    }
    // This method is to recreated DB and tables
    fun recreateDatabaseAndTables() {
    }


}