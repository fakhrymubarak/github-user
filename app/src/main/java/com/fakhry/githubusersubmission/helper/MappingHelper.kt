package com.fakhry.githubusersubmission.helper

import android.database.Cursor
import com.fakhry.githubusersubmission.db.DatabaseContract
import com.fakhry.githubusersubmission.model.UserModel

object MappingHelper {
    fun mapCursorToArrayList(favUserCursor: Cursor?): ArrayList<UserModel> {
        val favUserList = ArrayList<UserModel>()

        favUserCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndex(DatabaseContract.FavUserColumns.ID))
                val name = getString(getColumnIndex(DatabaseContract.FavUserColumns.NAME))
                val username = getString(getColumnIndex(DatabaseContract.FavUserColumns.USERNAME))
                val userUrl = getString(getColumnIndex(DatabaseContract.FavUserColumns.URL))
                val avatar = getString(getColumnIndex(DatabaseContract.FavUserColumns.AVATAR))
                favUserList.add(UserModel(id, name, username, userUrl, avatar))
            }
        }
        return favUserList
    }
}