package io.github.pdv.nfapp

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.Date

sealed class Gender : Parcelable {
    @Parcelize object Male : Gender()
    @Parcelize object Female : Gender()
    @Parcelize data class Other(val name: String) : Gender()
}

@Parcelize
data class Person(
    val name: String,
    val score: Int,
    val date: Date,
    val gender: Gender
) : Parcelable