package vadiole.calendar

import android.app.Activity
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.window.OnBackInvokedCallback
import android.window.OnBackInvokedDispatcher
import androidx.annotation.RequiresApi
import androidx.core.view.WindowCompat
import androidx.core.view.WindowCompat.setDecorFitsSystemWindows
import vadiole.calendar.ui.UINavigationController

class CalendarActivity : Activity() {

    private var backInvokedHandler: (() -> Unit)? = null
    private var backCallbackEnabled = false

    @RequiresApi(Build.VERSION_CODES.S)
    private val onBackInvokedCallback: OnBackInvokedCallback = OnBackInvokedCallback {
        backInvokedHandler?.invoke()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDecorFitsSystemWindows(window, false)

        setContentView(
            UINavigationController(
                context = this,
                finishListener = {
                    @Suppress("DEPRECATION") // It's okay to use onBackPressed() to close the activity
                    onBackPressed()
                },
                canPopListener = { canPop ->
                    setBackCallbackEnabled(enabled = canPop)
                }
            ).apply {
                push(CalendarViewController(context), false)
                backInvokedHandler = {
                    pop(animated = true)
                }
            }
        )
    }

    @Suppress("DEPRECATION", "OVERRIDE_DEPRECATION")
    override fun onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return super.onBackPressed()
        } else {
            if (backCallbackEnabled) {
                backInvokedHandler?.invoke()
            } else {
                super.onBackPressed()
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        updateSystemBars(newConfig)
        window.decorView.setBackgroundColor(getColor(R.color.window_background))
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (hasFocus) {
            updateSystemBars(resources.configuration)
        }
    }

    private fun updateSystemBars(configuration: Configuration) {
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        val isLightMode = configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK != Configuration.UI_MODE_NIGHT_YES
        insetsController.isAppearanceLightStatusBars = isLightMode
        insetsController.isAppearanceLightNavigationBars = isLightMode
    }

    private fun setBackCallbackEnabled(enabled: Boolean) {
        backCallbackEnabled = enabled
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (enabled) {
                onBackInvokedDispatcher.registerOnBackInvokedCallback(OnBackInvokedDispatcher.PRIORITY_DEFAULT, onBackInvokedCallback)
            } else {
                onBackInvokedDispatcher.unregisterOnBackInvokedCallback(onBackInvokedCallback)
            }
        }
    }
}