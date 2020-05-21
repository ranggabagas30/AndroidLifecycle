package com.excercise.androidlifecycle.timer.countdown

import java.text.SimpleDateFormat
import java.util.*

object TimeUtil {
    fun toTimeFormat(millis: Long): String {
        val seconds = millis / 1000
        return String.format("%02d:%02d:%02d", (seconds / 3600), (seconds % 3600) / 60, (seconds % 60))
    }

    fun toTimeFormat2(millis: Long): String {
        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return sdf.format(Date(millis))
    }
}