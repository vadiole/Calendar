package vadiole.calendar.data

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

// Migrate to static kotlin extension when available, see https://youtrack.jetbrains.com/issue/KT-11968
object LocalDataTime {
    fun ofEpochMilli(epochMilli: Long): LocalDateTime {
        val instant = Instant.ofEpochMilli(epochMilli)
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
    }
}
