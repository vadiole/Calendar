package vadiole.calendar

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import vadiole.calendar.ui.UIViewController

class SettingsViewController(context: Context) : UIViewController() {
    override val view = TextView(context).apply {
        layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        text = "Settings"
        setTextColor(Color.BLACK)
        gravity = Gravity.CENTER
        background = ColorDrawable(Color.WHITE)
        setOnClickListener {
            navigationController?.pop(this@SettingsViewController, false)
        }
    }
}
