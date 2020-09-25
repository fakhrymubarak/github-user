package com.fakhry.githubusersubmission.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.fakhry.githubusersubmission.db.DatabaseContract.AUTHORITY
import com.fakhry.githubusersubmission.db.DatabaseContract.FavUserColumns.Companion.CONTENT_URI
import com.fakhry.githubusersubmission.db.DatabaseContract.FavUserColumns.Companion.TABLE_NAME
import com.fakhry.githubusersubmission.db.FavUserHelper

class FavoriteProvider : ContentProvider() {

    companion object {
        internal val TAG = this::class.java.simpleName
        private const val FAVORITE = 1
        private const val FAVORITE_ID = 2
        private lateinit var favUserHelper: FavUserHelper

        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, FAVORITE)
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/*", FAVORITE_ID)
        }
    }

    override fun onCreate(): Boolean {
        favUserHelper = FavUserHelper.getInstance(context as Context)
        favUserHelper.open()
        return true
    }

    override fun query(
        uri: Uri,
        strings: Array<String>?,
        s: String?,
        strings1: Array<String>?,
        s1: String?,
    ): Cursor? {
        return when (sUriMatcher.match(uri)) {
            FAVORITE -> favUserHelper.queryAll()
            FAVORITE_ID -> favUserHelper.queryByUsername(uri.lastPathSegment.toString())
            else -> null
        }
    }


    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        Log.d(TAG, "Trying to INSERT data  user 2 : ${sUriMatcher.match(uri)}")
        val added: Long = when (FAVORITE) {
            sUriMatcher.match(uri) -> favUserHelper.insert(contentValues)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }


    override fun update(
        uri: Uri,
        contentValues: ContentValues?,
        s: String?,
        strings: Array<String>?,
    ): Int {
        val updated: Int = when (FAVORITE_ID) {
            sUriMatcher.match(uri) -> favUserHelper.update(uri.lastPathSegment.toString(),
                contentValues)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return updated
    }

    override fun delete(uri: Uri, s: String?, strings: Array<String>?): Int {
        val deleted: Int = when (FAVORITE_ID) {
            sUriMatcher.match(uri) -> {
                favUserHelper.deleteByUsername(uri.lastPathSegment.toString())
            }
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }
}
