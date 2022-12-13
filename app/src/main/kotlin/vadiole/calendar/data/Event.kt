package vadiole.calendar.data

import android.provider.CalendarContract
import java.time.LocalDateTime

class Event(
    val id: Long,
    val allDay: Boolean,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val title: String,
    val description: String?,
    val location: String?,
    val startDay: Int,
    val endDay: Int,
    val color: Int?,
    val selfStatus: AttendeeStatus,
) {
    constructor(
        id: Long,
        allDay: Boolean,
        start: Long,
        end: Long,
        title: String,
        description: String?,
        location: String?,
        startDay: Int,
        endDay: Int,
        color: Int?,
        selfStatus: Int,
    ) : this(
        id = id,
        allDay = allDay,
        start = LocalDataTime.ofEpochMilli(start),
        end = LocalDataTime.ofEpochMilli(end),
        title = title,
        description = description,
        location = location,
        startDay = startDay,
        endDay = endDay,
        color = color,
        selfStatus = AttendeeStatus.from(selfStatus),
    )
}

enum class AttendeeStatus {
    None,
    Accepted,
    Maybe,
    Declined;

    companion object {
        fun from(rawStatus: Int): AttendeeStatus = when (rawStatus) {
            CalendarContract.Attendees.ATTENDEE_STATUS_ACCEPTED -> Accepted
            CalendarContract.Attendees.ATTENDEE_STATUS_TENTATIVE -> Maybe
            CalendarContract.Attendees.ATTENDEE_STATUS_DECLINED -> Declined
            else -> None
        }
    }
}


