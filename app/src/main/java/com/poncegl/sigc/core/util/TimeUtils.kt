package com.poncegl.sigc.core.util

import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatTimeAMPM(localTime: LocalTime): String {
    val formatter = DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault())
    return localTime.format(formatter)
}
