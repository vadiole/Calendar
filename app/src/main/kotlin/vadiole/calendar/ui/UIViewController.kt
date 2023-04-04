package vadiole.calendar.ui

import android.view.View

abstract class UIViewController {
    var navigationController: UINavigationController? = null
    abstract val view: View
}