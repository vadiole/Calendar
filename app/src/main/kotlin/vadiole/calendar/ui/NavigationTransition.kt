package vadiole.calendar.ui

import android.view.View

interface NavigationTransition {
    fun animatePush(fromView: View, toView: View, onAnimationEnd: () -> Unit)

    fun animatePop(fromView: View, toView: View, onAnimationEnd: () -> Unit)
}