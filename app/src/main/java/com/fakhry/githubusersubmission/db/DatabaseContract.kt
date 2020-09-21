package com.fakhry.githubusersubmission.db

import android.provider.BaseColumns

internal class DatabaseContract {
    internal class FavUserColumns : BaseColumns {
        companion object{
            const val TABLE_NAME = "fav_user"
            const val ID = "_id"
            const val USERNAME = "username"
            const val URL = "url"
            const val AVATAR ="avatar"
            const val ID_NUMBER ="id_number"
        }
    }
}