package vadiole.calendar.ui

import android.view.View

abstract class UIViewController {
    var navigationController: UINavigationController? = null
    abstract val view: View
    open fun viewWillAppear(animated: Boolean) = Unit
    open fun viewWillDisappear(animated: Boolean) = Unit
    open fun viewDidAppear(animated: Boolean) = Unit
    open fun viewDidDisappear(animated: Boolean) = Unit
}