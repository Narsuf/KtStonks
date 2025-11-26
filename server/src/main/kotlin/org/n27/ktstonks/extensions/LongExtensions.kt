package org.n27.ktstonks.extensions

import java.time.Instant
import java.time.ZoneId

fun Long.isToday(): Boolean {
    val lastUpdatedDate = Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()
    val today = Instant.now().atZone(ZoneId.systemDefault()).toLocalDate()
    return lastUpdatedDate.isEqual(today)
}
