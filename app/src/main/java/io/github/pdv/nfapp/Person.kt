package io.github.pdv.nfapp

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
data class Person(
    val name: String,
    val score: Int,
    val date: Date
) : Parcelable