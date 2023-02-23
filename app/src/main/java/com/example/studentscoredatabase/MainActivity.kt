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

        val btnAdd = findViewById<Button>(R.id.btnAddUser)
        btnAdd.setOnClickListener {
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
                    val etAddSubject = dialogView.findViewById<EditText>(R.id.etADDSubject)
                    val etAddScore = dialogView.findViewById<EditText>(R.id.etADDScore)
                    val Subject = etAddSubject.text.toString()
                    val Score = etAddScore.text.toString()
                    db.add(Subject, Score)
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
        val btnPrintUsers = findViewById<Button>(R.id.btnPrintUser)
        btnPrintUsers.setOnClickListener {
            val db = DBHelper(this, null)
            val userList = db.getAll()
            val tvRecord = findViewById<TextView>(R.id.tvUserRecord)
            tvRecord.text = "### subject ###\n"
            userList.forEach {
                tvRecord.append("$it\n")
            }
        }

        val btnDeleteSubject = findViewById<Button>(R.id.btnDeleteUser)
        btnDeleteSubject.setOnClickListener {
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
                    val etDeleteSubject = dialogView.findViewById<EditText>(R.id.etDeleteSubject)
                    val name = etDeleteSubject.text.toString()
                    val rows = db.delete(name)
                    Toast.makeText(
                        this,
                        when (rows) {
                            0 -> "Nothing deleted"
                            1 -> "subject deleted"
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

        val btnUpdateUser = findViewById<Button>(R.id.btnUpdateUser)
        btnUpdateUser.setOnClickListener {
            val dialogView = this.layoutInflater.inflate(R.layout.update_dialog, null)
            /*
            Kotlin is both a functional programming and an OOP language
            */
            AlertDialog.Builder(this)
                .setTitle("Update User")
                .setMessage("Enter data below")
                .setView(dialogView)
                // Set a listener to each button that takes an action before dismissing the dialog
                // The dialog is automatically dismissed when a dialog button is clicked
                .setPositiveButton(
                    "Yes",
                    DialogInterface.OnClickListener { dialog, id ->
                        // do this if "Yes" is clicked
                        val db = DBHelper(this, null)
                        val etUpdateName = dialogView.findViewById<EditText>(R.id.etUpdateSubject)
                        val etUpdateAge = dialogView.findViewById<EditText>(R.id.etUpdateScore)

                        val name = etUpdateName.text.toString()
                        val age = etUpdateAge.text.toString()
                        val rows = db.update(name, age)
                        Toast.makeText(this, "$rows users updated", Toast.LENGTH_LONG).show()
                    }
                )
                .setNegativeButton("No", null) // nothing to do
                .setIcon(R.drawable.ic_launcher_background)
                .show()
        }
    }
}