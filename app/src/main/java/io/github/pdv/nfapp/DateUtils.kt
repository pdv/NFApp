package io.github.pdv.nfapp

import java.text.SimpleDateFormat
import java.util.*

fun Date.toString(format: String): String {
    val df = SimpleDateFormat(format, Locale.getDefault())
    return df.format(this)
}