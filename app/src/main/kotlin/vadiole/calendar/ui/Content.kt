package vadiole.calendar.ui

import android.content.Context
import android.widget.FrameLayout
import android.widget.ScrollView
import android.widget.TextView
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import vadiole.calendar.ResourcesOwner
import vadiole.calendar.data.CalendarRepository

class Content(
    context: Context,
    calendarRepository: CalendarRepository,
) : FrameLayout(context), ResourcesOwner {

    private val textView = TextView(context).apply {
        setPadding(16.dp, 16.dp, 16.dp, 16.dp)
    }
    private val scope = MainScope()

    init {
        scope.launch {
            val events = calendarRepository.queryEvents()
            val text = events.joinToString("\n\n\n") { event ->
                "Title: ${event.title}\nLocation: ${event.location}\nDescription: ${event.description?.take(100)}"
            }
            textView.text = text
        }

        addView(
            ScrollView(context).apply {
                addView(textView)
            }
        )
    }
}