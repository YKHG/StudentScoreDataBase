package com.example.studentscoredatabase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast



class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnAddUser = findViewById<Button>(R.id.btnAddUser)
        btnAddUser.setOnClickListener {
            val db = DBHelper(this, null)
            val etSubject = findViewById<EditText>(R.id.etSubject)
            val etScore = findViewById<EditText>(R.id.etScore)
            val Subject = etSubject.text.toString()
            val Score = etScore.text.toString()

            db.addUser(Subject, Score)
            // Toast to message on the screen
            Toast.makeText(this, Subject + " added to database", Toast.LENGTH_SHORT).show()
            etSubject.text.clear()
            etScore.text.clear()
        }
        val btnPrintUsers = findViewById<Button>(R.id.btnPrintUsers)
        btnPrintUsers.setOnClickListener {
            val db = DBHelper(this, null)
            val userList = db.getAllUsers()
            val tvUserRecord = findViewById<TextView>(R.id.tvUserRecord)
            tvUserRecord.text = "### subject ###\n"
            userList.forEach {
                tvUserRecord.append("$it\n")
            }
        }

        val btnDeleteUser = findViewById<Button>(R.id.btnDeleteUser)
        btnDeleteUser.setOnClickListener {
            val db = DBHelper(this, null)
            val userName = findViewById<EditText>(R.id.etSubject).text.toString()
            val rows = db.deleteUser(userName)
            Toast.makeText(this,
                when (rows) {
                    0 -> "Nothing deleted"
                    1 -> "1 user deleted"
                    else -> "" // shouldn't happen
                },
                Toast.LENGTH_LONG).show()
        }
        val btnUpdateUser = findViewById<Button>(R.id.btnUpdateUser)
        btnUpdateUser.setOnClickListener {
            val db = DBHelper(this, null)
            val subject = findViewById<EditText>(R.id.etSubject).text.toString()
            val score = findViewById<EditText>(R.id.etScore).text.toString()
            val rows = db.updateUser(subject, score)
            Toast.makeText(this, "$rows subject updated", Toast.LENGTH_LONG).show()
        }
    }
}