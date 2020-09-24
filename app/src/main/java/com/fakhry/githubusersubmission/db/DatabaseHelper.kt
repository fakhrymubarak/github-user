package com.fakhry.githubusersubmission.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.fakhry.githubusersubmission.db.DatabaseContract.FavUserColumns.Companion.AVATAR
import com.fakhry.githubusersubmission.db.DatabaseContract.FavUserColumns.Companion.ID
import com.fakhry.githubusersubmission.db.DatabaseContract.FavUserColumns.Companion.NAME
import com.fakhry.githubusersubmission.db.DatabaseContract.FavUserColumns.Companion.TABLE_NAME
import com.fakhry.githubusersubmission.db.DatabaseContract.FavUserColumns.Companion.URL
import com.fakhry.githubusersubmission.db.DatabaseContract.FavUserColumns.Companion.USERNAME

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION)  {
    companion object {
        private const val DATABASE_NAME = "dbfavuser"

        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE_FAV_USER = "CREATE TABLE $TABLE_NAME" +
                " ($ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " $NAME TEXT NOT NULL," +
                " $USERNAME TEXT NOT NULL," +
                " $URL TEXT NOT NULL," +
                " $AVATAR TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_FAV_USER)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}