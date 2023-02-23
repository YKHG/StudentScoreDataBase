package com.example.studentscoredatabase

import android.annotation.SuppressLint
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog


class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnAddUser = findViewById<Button>(R.id.btnAdd)
        btnAddUser.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)
            val inflater = this.layoutInflater
            val dialogView = inflater.inflate(R.layout.add_dialog, null)
            dialogBuilder.setTitle("Add Subject")
            dialogBuilder.setMessage("Enter Score")
            dialogBuilder.setView(dialogView)
            // Set a listener to each button that takes an action before dismissing the dialog
            // The dialog is automatically dismissed when a dialog button is clicked
            dialogBuilder.setPositiveButton("Yes",
                DialogInterface.OnClickListener { dialog, id ->
                    // do this if "Yes" is clicked
                    val db = DBHelper(this, null)
                    val etAddSubject = dialogView.findViewById<EditText>(R.id.etSubject)
                    val etAddScore = dialogView.findViewById<EditText>(R.id.etScore)
                    val Subject = etAddSubject.text.toString()
                    val Score = etAddScore.text.toString()
                    db.addUser(Subject, Score)
                    Toast.makeText(this, "$Subject added to database", Toast.LENGTH_SHORT).show()
                }
            )
            dialogBuilder.setNegativeButton("No",
                DialogInterface.OnClickListener { dialog, id ->
                    // do this if "No" is clicked
                    // Nothing is performed, so you can put null instead
                })
            dialogBuilder.setIcon(R.drawable.ic_launcher_background)
            dialogBuilder.show()
        }
        val btnPrintUsers = findViewById<Button>(R.id.btnPrint)
        btnPrintUsers.setOnClickListener {
            val db = DBHelper(this, null)
            val userList = db.getAll()
            val tvUserRecord = findViewById<TextView>(R.id.tvUserRecord)
            tvUserRecord.text = "### subject ###\n"
            userList.forEach {
                tvUserRecord.append("$it\n")
            }
        }

        val btnDeleteUser = findViewById<Button>(R.id.btnDelete)
            btnDeleteUser.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)
            val dialogView = this.layoutInflater.inflate(R.layout.delete_dialog, null)
            dialogBuilder.setTitle("Delete subject")
            dialogBuilder.setMessage("Enter data below")
            dialogBuilder.setView(dialogView)
            // Set a listener to each button that takes an action before dismissing the dialog
            // The dialog is automatically dismissed when a dialog button is clicked
            dialogBuilder.setPositiveButton("Yes",
                DialogInterface.OnClickListener { dialog, id ->
                    // do this if "Yes" is clicked
                    val db = DBHelper(this, null)
                    val etDeleteName = dialogView.findViewById<EditText>(R.id.etDeleteSubject)
                    val name = etDeleteName.text.toString()
                    val rows = db.delete(name)
                    Toast.makeText(
                        this,
                        when (rows) {
                            0 -> "Nothing deleted"
                            1 -> "Subject deleted"
                            else -> "" // shouldn't happen
                        },
                        Toast.LENGTH_LONG
                    ).show()
                }
            )
            dialogBuilder.setNegativeButton("No", null) // nothing to do
            dialogBuilder.setIcon(R.drawable.ic_launcher_background)
            dialogBuilder.show()
        }

        val btnUpdateUser = findViewById<Button>(R.id.btnUpdate)
        btnUpdateUser.setOnClickListener {
            val db = DBHelper(this, null)
            val subject = findViewById<EditText>(R.id.etSubject).text.toString()
            val score = findViewById<EditText>(R.id.etScore).text.toString()
            val rows = db.update(subject, score)
            Toast.makeText(this, "$rows subject updated", Toast.LENGTH_LONG).show()
        }
    }
}