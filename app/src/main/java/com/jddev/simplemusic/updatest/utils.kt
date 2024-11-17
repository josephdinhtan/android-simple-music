package com.jddev.simplemusic.updatest

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

@SuppressLint("SimpleDateFormat")
fun Long.toTime(): String {
    val seconds = this * 1000L
    val date = Date(seconds)
    val format =
        if (this < 3600) SimpleDateFormat("mm:ss").apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        else SimpleDateFormat("HH:mm:ss").apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
    return format.format(date)
}

fun currentTimeToLong(): Long {
    return System.currentTimeMillis()
}

@SuppressLint("SimpleDateFormat")
fun String.dateToLong(): Long {
    val df = SimpleDateFormat("yyyy.MM.dd HH:mm:ss")
    return df.parse(this)?.time ?: 0
}