package com.fakhry.githubconsumerapp.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY = "com.fakhry.githubusersubmission"
    const val SCHEME = "content"

    internal class FavUserColumns : BaseColumns {
        companion object{
            const val TABLE_NAME = "fav_user"
            const val ID = "_id"
            const val NAME ="name"
            const val USERNAME = "username"
            const val URL = "url"
            const val AVATAR ="avatar"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}