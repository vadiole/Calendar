package vadiole.calendar

import android.app.Application

class CalendarApp : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        appContainer = AppContainer(this)
    }

    companion object {
        private var appContainer: AppContainer? = null
        val AppContainer: AppContainer
            get() = appContainer ?: error("App is not created yet")
        private var instance: CalendarApp? = null
    }
}