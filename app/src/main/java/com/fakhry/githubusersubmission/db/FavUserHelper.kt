package com.fakhry.githubusersubmission.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import com.fakhry.githubusersubmission.db.DatabaseContract.FavUserColumns.Companion.ID
import com.fakhry.githubusersubmission.db.DatabaseContract.FavUserColumns.Companion.TABLE_NAME
import com.fakhry.githubusersubmission.db.DatabaseContract.FavUserColumns.Companion.USERNAME

class FavUserHelper(context: Context) {
    private var dataBaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private var INSTANCE: FavUserHelper? = null

        //initiate database
        fun getInstance(context: Context): FavUserHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: FavUserHelper(context)
            }
    }

    init {
        dataBaseHelper = DatabaseHelper(context)
    }

    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }
    fun close() {
        dataBaseHelper.close()
        if (database.isOpen)
            database.close()
    }


    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$ID ASC")
    }

//    fun queryById(id: String): Cursor {
//        return database.query(
//            DATABASE_TABLE,
//            null,
//            "$ID = ?",
//            arrayOf(id),
//            null,
//            null,
//            null,
//            null)
//    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

//    fun update(id: String, values: ContentValues?): Int {
//        return database.update(DATABASE_TABLE, values, "$ID = ?", arrayOf(id))
//    }

    fun deleteByUsername(username: String): Int {
        return database.delete(DATABASE_TABLE, "$USERNAME = '$username'", null)
    }

}