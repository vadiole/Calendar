package vadiole.calendar

import android.app.Application
import vadiole.calendar.data.CalendarRepository

class AppContainer(application: Application) {
    val calendarRepository = CalendarRepository(application)
}