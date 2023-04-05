package vadiole.calendar.ui

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            gravity = Gravity.TOP or Gravity.RIGHT
        }
        text = "Settings"
        setPadding(16.dp, 16.dp, 16.dp, 16.dp)
        setTextColor(Color.BLACK)
        background = ColorDrawable(Color.RED)
        gravity = Gravity.CENTER
        setOnClickListener { onSettingsClick() }
    }

    init {
        addView(textView)
        addView(settings)

        ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
            v.setPadding(
                0, insets.getInsets(WindowInsetsCompat.Type.statusBars()).top,
                0, insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
            )
            insets
        }
    }
}