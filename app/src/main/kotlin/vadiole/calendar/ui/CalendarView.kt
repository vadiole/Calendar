package vadiole.calendar.ui

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import vadiole.calendar.ResourcesOwner

class CalendarView(
    context: Context,
    onSettingsClick: () -> Unit,
) : FrameLayout(context), ResourcesOwner {
    private val textView = TextView(context).apply {
        text = "CalendarView"
        setPadding(16.dp, 16.dp, 16.dp, 16.dp)
        setTextColor(Color.BLACK)
        gravity = Gravity.CENTER
    }
    private val settings = TextView(context).apply {
        text = "Settings"
        setPadding(16.dp, 16.dp, 16.dp, 16.dp)
        setTextColor(Color.BLACK)
        gravity = Gravity.TOP or Gravity.RIGHT
        setOnClickListener { onSettingsClick() }
    }

    init {
        addView(textView)
        addView(settings)
    }
}