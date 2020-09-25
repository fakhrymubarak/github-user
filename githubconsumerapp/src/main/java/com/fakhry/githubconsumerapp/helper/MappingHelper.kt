package com.fakhry.githubconsumerapp.helper

import android.database.Cursor
import com.fakhry.githubconsumerapp.db.DatabaseContract
import com.fakhry.githubconsumerapp.model.UserModel

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

//    fun mapCursorToObject(favUserCursor: Cursor?): UserModel {
//        val favUserList = UserModel()
//
//        favUserCursor?.apply {
//            while (moveToNext()) {
//                val id = getInt(getColumnIndex(DatabaseContract.FavUserColumns.ID))
//                val name = getString(getColumnIndex(DatabaseContract.FavUserColumns.NAME))
//                val username = getString(getColumnIndex(DatabaseContract.FavUserColumns.USERNAME))
//                val userUrl = getString(getColumnIndex(DatabaseContract.FavUserColumns.URL))
//                val avatar = getString(getColumnIndex(DatabaseContract.FavUserColumns.AVATAR))
//                UserModel(id, name, username, userUrl, avatar)
//            }
//        }
//        return favUserList
//    }
}