package com.example.studentscoredatabase

import android.annotation.SuppressLint
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.studentscoredatabase.R.id.etDeleteName


class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnAddUser = findViewById<Button>(R.id.btnAddUser)
        btnAddUser.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)
            val inflater = this.layoutInflater
            val dialogView = inflater.inflate(R.layout.add_dialog, null)

            dialogBuilder.setTitle("Add New Subject")
            dialogBuilder.setMessage("Enter data below")
            dialogBuilder.setView(dialogView)

            // Set a listener to each button that takes an action before dismissing the dialog
            // The dialog is automatically dismissed when a dialog button is clicked
            dialogBuilder.setPositiveButton("Yes",
                DialogInterface.OnClickListener { dialog, id ->
                    // do this if "Yes" is clicked
                    val db = DBHelper(this, null)
                    val etAddName = dialogView.findViewById<EditText>(R.id.etAddName)
                    val etAddAge = dialogView.findViewById<EditText>(R.id.etAddAge)
                    val subject = etAddName.text.toString()
                    val score = etAddAge.text.toString().toInt()
                    db.addScore(subject, score)
                    Toast.makeText(this, "$subject added to database", Toast.LENGTH_SHORT).show()
                }
            )
            dialogBuilder.setNegativeButton("No",
                DialogInterface.OnClickListener { dialog, id ->
                    // do this if "No" is clicked
                    // Nothing is performed, so you can put null instead
                })
            dialogBuilder.setIcon(android.R.drawable.ic_dialog_alert)
            dialogBuilder.show()
        }

        val btnPrintUsers = findViewById<Button>(R.id.btnPrintUsers)
        btnPrintUsers.setOnClickListener {
            val db = DBHelper(this, null)
            val userList = db.getAllScores()

            val tvUserRecord = findViewById<TextView>(R.id.tvUserRecord)
            tvUserRecord.text = "### Subject:Score ###\n"

            userList.forEach {
                tvUserRecord.append("${it.subject}:${it.score}\n")
            }
        }

        val btnDeleteUser = findViewById<Button>(R.id.btnDeleteUser)
        btnDeleteUser.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)
            val dialogView = this.layoutInflater.inflate(R.layout.delete_dialog, null)

            dialogBuilder.setTitle("Delete User")
            dialogBuilder.setMessage("Enter data below")
            dialogBuilder.setView(dialogView)

            // Set a listener to each button that takes an action before dismissing the dialog
            // The dialog is automatically dismissed when a dialog button is clicked
            dialogBuilder.setPositiveButton("Yes",
                DialogInterface.OnClickListener { dialog, id ->
                    // do this if "Yes" is clicked
                    val db = DBHelper(this, null)
                    val etDeleteName = dialogView.findViewById<EditText>(etDeleteName)
                    val subject = etDeleteName.text.toString()
                    val rows = db.deleteScore(subject)
                    Toast.makeText(
                        this,
                        when (rows) {
                            0 -> "Nothing deleted"
                            1 -> "1 subject deleted"
                            else -> "" // shouldn't happen
                        },
                        Toast.LENGTH_LONG
                    ).show()
                }
            )
            dialogBuilder.setNegativeButton("No", null) // nothing to do
            dialogBuilder.setIcon(android.R.drawable.ic_dialog_alert)
            dialogBuilder.show()
        }

        val btnUpdateUser = findViewById<Button>(R.id.btnUpdateUser)
        btnUpdateUser.setOnClickListener {
            val dialogView = this.layoutInflater.inflate(R.layout.update_dialog, null)

            /*
            Kotlin is both a functional programming and an OOP language
             */
            AlertDialog.Builder(this)
                .setTitle("Update Subject")
                .setMessage("Enter data below")
                .setView(dialogView)

                // Set a listener to each button that takes an action before dismissing the dialog
                // The dialog is automatically dismissed when a dialog button is clicked
                .setPositiveButton(
                    "Yes",
                    DialogInterface.OnClickListener { dialog, id ->
                        // do this if "Yes" is clicked
                        val db = DBHelper(this, null)
                        val etUpdateName = dialogView.findViewById<EditText>(R.id.etUpdateName)
                        val etUpdateAge = dialogView.findViewById<EditText>(R.id.etUpdateAge)
                        val Subject = etUpdateName.text.toString()
                        val Score= etUpdateAge.text.toString()
                        val rows = db.updateScore(Subject, Score)
                        Toast.makeText(this, "$rows Subject updated", Toast.LENGTH_LONG).show()
                    }
                )
                .setNegativeButton("No", null) // nothing to do
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
        }
    }
}