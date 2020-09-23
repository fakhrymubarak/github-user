package com.fakhry.githubusersubmission.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FavUserModel(
    var id: Int = 0,
    var username: String? = null,
    var userUrl: String? = null,
    var idNumber: Int? = null,
    var avatarUrl: String? = null
) : Parcelable