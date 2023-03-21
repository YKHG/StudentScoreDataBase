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



class MainActivity : AppCompatActivity() {

    // Declare class variables
    private lateinit var dbHelper: DBHelper
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etMobile: EditText
    private lateinit var etDeleteId: EditText
    private lateinit var etSearchName: EditText
    private lateinit var tvResult: TextView

    // This method is called when the activity is created
    @SuppressLint("MissingInflatedId", "Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize class variables
        dbHelper = DBHelper(this)
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etMobile = findViewById(R.id.etMobile)
        etDeleteId = findViewById(R.id.etDeleteId)
        etSearchName = findViewById(R.id.etSearchName)


        // Add button click listener for adding a new user
        val btnAddUser = findViewById<Button>(R.id.btnAddUser)
        btnAddUser.setOnClickListener {
            // Get user input from EditText fields
            val name = etName.text.toString()
            val email = etEmail.text.toString()
            val mobile = etMobile.text.toString()

            // Insert the new user into the database
            val result = dbHelper.insertCustomer(name, email, mobile)

            // Show a Toast message to indicate success or failure
            if (result == -1L) {
                Toast.makeText(this, "Failed to add customer", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Customer added successfully", Toast.LENGTH_SHORT).show()
                etName.text.clear()
                etEmail.text.clear()
                etMobile.text.clear()
            }
        }

        val btnDeleteUser = findViewById<Button>(R.id.btnDeleteUser)
        btnDeleteUser.setOnClickListener {
            val id = etDeleteId.text.toString().toLongOrNull()
            if (id == null) {
                Toast.makeText(this, "Please enter a valid ID", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
// Search the database for customers with the given name
            val result = dbHelper.deleteCustomer(id)
            if (result == 0) {
                Toast.makeText(this, "Failed to delete customer", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Customer deleted successfully", Toast.LENGTH_SHORT).show()
                etDeleteId.text.clear()
            }
        }

        val btnSearchUser = findViewById<Button>(R.id.btnSearchUser)
        btnSearchUser.setOnClickListener {
            val name = etSearchName.text.toString()
            val cursor = dbHelper.searchCustomer(name)
            if (cursor.count == 0) {
                Toast.makeText(this, "No customer found with that name", Toast.LENGTH_SHORT).show()
            } else {
                cursor.moveToFirst()
                val sb = StringBuilder()
                do {
                    val id = cursor.getLong(cursor.getColumnIndex(DBHelper.ID))
                    val name = cursor.getString(cursor.getColumnIndex(DBHelper.NAME))
                    val email = cursor.getString(cursor.getColumnIndex(DBHelper.EMAIL))
                    val mobile = cursor.getString(cursor.getColumnIndex(DBHelper.MOBILE))
                    sb.append("ID: $id\nName: $name\nEmail: $email\nMobile: $mobile\n\n")
                } while (cursor.moveToNext())
                tvResult.text = sb.toString()
            }
            cursor.close()
        }
        val btnShowAll = findViewById<Button>(R.id.btnShowAll)
        btnShowAll.setOnClickListener {
            val cursor = dbHelper.getAllCustomers()
            if (cursor.count == 0) {
                Toast.makeText(this, "No customers found", Toast.LENGTH_SHORT).show()
            } else {
                val builder = StringBuilder()
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(cursor.getColumnIndexOrThrow(DBHelper.ID))
                    val name = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.NAME))
                    val email = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.EMAIL))
                    val mobile = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.MOBILE))
                    builder.append("ID: $id\nName: $name\nEmail: $email\nMobile: $mobile\n\n")
                }
                val dialogBuilder = AlertDialog.Builder(this)
                    .setTitle("All Customers")
                    .setMessage(builder.toString())
                    .setPositiveButton("OK", null)
                val dialog = dialogBuilder.create()
                dialog.show()
            }
        }
        val btnUpdate = findViewById<Button>(R.id.btnUpdate)
        btnUpdate.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)
            val inflater = this.layoutInflater
            val dialogView = inflater.inflate(R.layout.update_dialog, null)

            dialogBuilder.setTitle("Update Customer")
            dialogBuilder.setView(dialogView)

            val etUpdateId = dialogView.findViewById<EditText>(R.id.etUpdateId)
            val etUpdateName = dialogView.findViewById<EditText>(R.id.etUpdateName)
            val etUpdateEmail = dialogView.findViewById<EditText>(R.id.etUpdateEmail)
            val etUpdateMobile = dialogView.findViewById<EditText>(R.id.etUpdateMobile)

            dialogBuilder.setPositiveButton("Update", DialogInterface.OnClickListener { _, _ ->
                val db = DBHelper(this)
                val result = db.updateCustomer(
                    etUpdateId.text.toString().toLong(),
                    etUpdateName.text.toString(),
                    etUpdateEmail.text.toString(),
                    etUpdateMobile.text.toString()
                )

                if (result > 0) {
                    Toast.makeText(this, "Customer updated successfully", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Error updating customer", Toast.LENGTH_LONG).show()
                }
                db.close()
            })

            dialogBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, _ ->
                dialog.cancel()
            })

            val dialog = dialogBuilder.create()
            dialog.show()
        }
        val btnReset = findViewById<Button>(R.id.btnReset)
        btnReset.setOnClickListener {
            // Call the resetTable() method of the DBHelper class
            dbHelper.resetTable()
            Toast.makeText(this, "Table reset", Toast.LENGTH_SHORT).show()
        }

    }

}
