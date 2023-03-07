package com.example.studentscoredatabase

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    var context: Context
    init {
        this.context = context
    }

    companion object {
        private val DB_NAME = "student_score_database"
        private val DB_VERSION = 1
        val TABLE_NAME = "score_table"
        val ID = "id"
        val SUBJECT = "subject"
        val SCORE = "score"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val query = (
                "CREATE TABLE $TABLE_NAME (" +
                        "$ID INTEGER PRIMARY KEY," +
                        "$SUBJECT TEXT," +
                        "$SCORE INTEGER" + ")"
                )
        db?.execSQL(query)

        Log.i("onCreate", "TABLE CREATED!!!")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)

        Log.i("onUpgrade", "DATABASE UPGRADED!!!")
    }

    fun addScore(subject: String, score: Int) {
        val values = ContentValues()
        values.put(SUBJECT, subject)
        values.put(SCORE, score)

        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getAllScores(): ArrayList<Score> {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null)
        val scoreList = ArrayList<Score>()

        if (cursor.moveToFirst()) {
            do {
                scoreList.add(
                    Score(
                        cursor.getInt(cursor.getColumnIndexOrThrow(ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(SUBJECT)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(SCORE))
                    )
                )
            } while (cursor.moveToNext())
        }

        cursor.close()
        return scoreList
    }

    fun deleteScore(subject: String): Int {
        val db = this.writableDatabase
        val rows = db.delete(TABLE_NAME, "$SUBJECT=?", arrayOf(subject))
        db.close()

        return rows
    }

    fun updateScore(subject: String, score: String): Int {
        val values = ContentValues()
        values.put(SCORE, score)

        val db = this.writableDatabase
        val rows = db.update(TABLE_NAME, values, "$SUBJECT=?", arrayOf(subject))
        db.close()

        return rows
    }
}