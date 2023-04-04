package vadiole.calendar.ui

import android.content.Context
import android.widget.FrameLayout

class UINavigationController(context: Context) : FrameLayout(context) {
    val viewControllers = mutableListOf<UIViewController>()

    fun push(viewController: UIViewController, animation: Boolean) {
        viewControllers.add(viewController)
        val view = viewController.view
        viewController.navigationController = this
        addView(view)
    }

    fun pop(viewController: UIViewController, animation: Boolean) {
        val view = viewController.view
        require(viewControllers.contains(viewController)) { "$viewController is not attached to $this" }
        require(indexOfChild(view) > -1) { "$viewControllers's view is not attached to window" }

        val wasRemoved = viewControllers.remove(viewController)
        if (wasRemoved) {
            if (view.isAttachedToWindow) {
                view.handler.removeCallbacksAndMessages(null)
                removeView(view)
            }
        }
    }
}