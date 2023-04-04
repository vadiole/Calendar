package vadiole.calendar

import android.content.Context
import vadiole.calendar.ui.CalendarView
import vadiole.calendar.ui.UIViewController

class CalendarViewController(private val context: Context) : UIViewController() {
    override val view = CalendarView(context, onSettingsClick = ::navigateToSettings)

    private fun navigateToSettings() {
        navigationController?.push(SettingsViewController(context), false)
    }
}

