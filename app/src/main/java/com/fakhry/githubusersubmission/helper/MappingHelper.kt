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
                val username = getString(getColumnIndex(DatabaseContract.FavUserColumns.USERNAME))
                val userUrl = getString(getColumnIndex(DatabaseContract.FavUserColumns.URL))
                val idNumber = getInt(getColumnIndex(DatabaseContract.FavUserColumns.ID_NUMBER))
                val avatar = getString(getColumnIndex(DatabaseContract.FavUserColumns.AVATAR))
                favUserList.add(UserModel(id, username, userUrl, idNumber, avatar))
            }
        }
        return favUserList
    }
}