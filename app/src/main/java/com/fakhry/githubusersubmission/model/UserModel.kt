package com.fakhry.githubusersubmission.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserModel(
    var id: Int = 0,
    var username : String? = null,
    var userUrl : String? = null,
    var idNumber : Int? = null,
    var avatarUrl : String? = null,
    var name : String? = null,
    var company : String? = null,
    var location : String? = null,
    var bio : String? = null,
    var repository : Int = 0,
    var followers : Int = 0,
    var following : Int = 0
) : Parcelable