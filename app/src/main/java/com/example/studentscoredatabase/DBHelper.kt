package com.example.studentscoredatabase



import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DBHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        private const val DB_NAME = "smtbiz.db"
        private const val DB_VERSION = 1

        const val TABLE_NAME = "customer"
        const val ID = "Id"
        const val NAME = "Name"
        const val EMAIL = "Email"
        const val MOBILE = "Mobile"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME ($ID INTEGER PRIMARY KEY, $NAME TEXT, $EMAIL TEXT, $MOBILE TEXT)"
        db.execSQL(createTableQuery)
        Log.i("DBHelper", "Table $TABLE_NAME created")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun resetTable() {
        val db = writableDatabase
        db.delete(TABLE_NAME, null, null)

        val initialValues = listOf(
            ContentValues().apply {
                put(NAME, "John Doe")
                put(EMAIL, "john.doe@example.com")
                put(MOBILE, "555-1234")
            },
            ContentValues().apply {
                put(NAME, "Jane Smith")
                put(EMAIL, "jane.smith@example.com")
                put(MOBILE, "555-5678")
            },
            ContentValues().apply {
                put(NAME, "Alice Lee")
                put(EMAIL, "alice.lee@example.com")
                put(MOBILE, "555-9012")
            },
            ContentValues().apply {
                put(NAME, "Bob Chen")
                put(EMAIL, "bob.chen@example.com")
                put(MOBILE, "555-3456")
            },
            ContentValues().apply {
                put(NAME, "Carol Kim")
                put(EMAIL, "carol.kim@example.com")
                put(MOBILE, "555-7890")
            }
        )

        initialValues.forEach { db.insert(TABLE_NAME, null, it) }
        db.close()
    }

    fun insertCustomer(name: String, email: String, mobile: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(NAME, name)
            put(EMAIL, email)
            put(MOBILE, mobile)
        }

        val rowId = db.insert(TABLE_NAME, null, values)
        db.close()
        return rowId
    }

    fun deleteCustomer(id: Long): Int {
        val db = writableDatabase
        val result = db.delete(TABLE_NAME, "$ID = ?", arrayOf(id.toString()))
        db.close()
        return result
    }

    fun searchCustomer(name: String): Cursor {
        val db = readableDatabase
        return db.query(TABLE_NAME, null, "$NAME LIKE ?", arrayOf("%$name%"), null, null, null)
    }

    fun getAllCustomers(): Cursor {
        val db = readableDatabase
        return db.query(TABLE_NAME, null, null, null, null, null, null)
    }

    fun updateCustomer(id: Long, name: String, email: String, mobile: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(NAME, name)
            put(EMAIL, email)
            put(MOBILE, mobile)
        }

        val result = db.update(TABLE_NAME, values, "$ID = ?", arrayOf(id.toString()))
        db.close()
        return result
    }
}
