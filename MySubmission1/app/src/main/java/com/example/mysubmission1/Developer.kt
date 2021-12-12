package com.example.mysubmission1

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Developer(
    var name: String = "",
    var username: String = "",
    var company: String = "",
    var location: String = "",
    var repository: String = "",
    var followers: String = "",
    var following: String = "",
    var avatar: Int = 0
) : Parcelable