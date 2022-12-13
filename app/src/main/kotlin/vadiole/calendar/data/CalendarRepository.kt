package vadiole.calendar.data

import android.app.Application
import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.CalendarContract
import android.provider.CalendarContract.Calendars
import android.provider.CalendarContract.Instances
import android.text.format.DateUtils
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext

class CalendarRepository(application: Application) {

    private val contentResolver: ContentResolver = application.contentResolver

    @OptIn(DelicateCoroutinesApi::class)
    private val coroutineContext: CoroutineContext = newSingleThreadContext("CalendarIO")

    suspend fun queryEvents(): List<Event> = withContext(coroutineContext) {
        val uri: Uri = createUri()
        val selection = getQueryForSelection()
        val now = System.currentTimeMillis()
        val result = mutableListOf<Event>()
        contentResolver.query(uri, EVENT_PROJECTION, selection, null, EVENT_SORT_ORDER)?.use { cursor: Cursor ->
            while (cursor.moveToNext()) {
                val eventId: Long = cursor.getLong(INDEX_EVENT_ID)
                val allDay: Boolean = cursor.getInt(INDEX_ALL_DAY) != 0
                var start: Long = cursor.getLong(INDEX_BEGIN)
                var end: Long = cursor.getLong(INDEX_END)
                val title: String = cursor.getString(INDEX_TITLE)
                val description: String = cursor.getString(INDEX_DESCRIPTION)
                val location: String = cursor.getString(INDEX_EVENT_LOCATION)
                val startDay: Int = cursor.getInt(INDEX_START_DAY)
                val endDay: Int = cursor.getInt(INDEX_END_DAY)
                val color: Int = cursor.getInt(INDEX_COLOR)
                val selfStatus: Int = cursor.getInt(INDEX_SELF_ATTENDEE_STATUS)
                // Adjust all-day times into local timezone
                if (allDay) {
                    start = Instant.ofEpochMilli(start)
                        .atZone(ZoneId.systemDefault())
                        .with(LocalTime.MIN)
                        .toInstant()
                        .toEpochMilli()
                    end = Instant.ofEpochMilli(start)
                        .atZone(ZoneId.systemDefault())
                        .with(LocalTime.MAX)
                        .toInstant()
                        .toEpochMilli()
                }
                if (end < now) {
                    continue
                }
                result.add(
                    Event(
                        id = eventId,
                        allDay = allDay,
                        start = start,
                        end = end,
                        title = title,
                        description = description,
                        location = location,
                        startDay = startDay,
                        endDay = endDay,
                        color = color,
                        selfStatus = selfStatus,
                    )
                )
            }
        }

        result
    }

    private fun createUri(): Uri {
        val now = System.currentTimeMillis()
        // Add a day on either side to catch all-day events
        val begin = now - DateUtils.DAY_IN_MILLIS
        val end: Long = now + SEARCH_DURATION + DateUtils.DAY_IN_MILLIS
        return Uri.withAppendedPath(Instances.CONTENT_URI, "$begin/$end")
    }

    private fun getQueryForSelection(): String {
        return if (true /* hide declined */) EVENT_SELECTION_HIDE_DECLINED else EVENT_SELECTION
    }

    companion object {
        val EVENT_PROJECTION = arrayOf(
            Instances.ALL_DAY,
            Instances.BEGIN,
            Instances.END,
            Instances.TITLE,
            Instances.DESCRIPTION,
            Instances.EVENT_LOCATION,
            Instances.EVENT_ID,
            Instances.START_DAY,
            Instances.END_DAY,
            Instances.DISPLAY_COLOR,
            Instances.SELF_ATTENDEE_STATUS,
        )

        const val INDEX_ALL_DAY = 0
        const val INDEX_BEGIN = 1
        const val INDEX_END = 2
        const val INDEX_TITLE = 3
        const val INDEX_DESCRIPTION = 4
        const val INDEX_EVENT_LOCATION = 5
        const val INDEX_EVENT_ID = 6
        const val INDEX_START_DAY = 7
        const val INDEX_END_DAY = 8
        const val INDEX_COLOR = 9
        const val INDEX_SELF_ATTENDEE_STATUS = 10
        const val EVENT_MAX_COUNT = 100
        private const val SEARCH_DURATION = DateUtils.WEEK_IN_MILLIS
        private const val EVENT_SORT_ORDER = Instances.START_DAY + " ASC, " +
                Instances.START_MINUTE + " ASC, " + Instances.END_DAY + " ASC, " +
                Instances.END_MINUTE + " ASC LIMIT " + EVENT_MAX_COUNT
        private const val EVENT_SELECTION = Calendars.VISIBLE + "=1"
        private const val EVENT_SELECTION_HIDE_DECLINED = Calendars.VISIBLE + "=1 AND " +
                Instances.SELF_ATTENDEE_STATUS + "!=" + CalendarContract.Attendees.ATTENDEE_STATUS_DECLINED
    }
}