package vadiole.calendar.ui

import android.content.Context
import android.widget.FrameLayout
import androidx.core.view.doOnLayout

class UINavigationController(
    context: Context,
    private val finishListener: () -> Unit,
    private val canPopListener: (Boolean) -> Unit,
) : FrameLayout(context) {
    private val viewControllers = mutableListOf<UIViewController>()

    fun push(viewController: UIViewController, animated: Boolean) {
        val lastViewController = viewControllers.lastOrNull()
        viewControllers.add(viewController)
        viewController.navigationController = this
        val view = viewController.view
        addView(view)
        view.doOnLayout {
            lastViewController?.viewWillDisappear(animated)
            lastViewController?.viewDidDisappear(animated)
            viewController.viewWillAppear(animated)
            viewController.viewDidAppear(animated)
        }
        notifyCanPop()
    }

    fun pop(viewController: UIViewController, animated: Boolean) {
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
        notifyCanPop()
    }

    fun pop(animated: Boolean) {
        if (canPop()) {
            val viewController = viewControllers.last()
            pop(viewController, animated)
        } else {
            finishListener.invoke()
        }
    }

    fun canPop(): Boolean {
        return viewControllers.size > 1
    }

    private fun notifyCanPop() {
        canPopListener.invoke(canPop())
    }
}