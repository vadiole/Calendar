package vadiole.calendar.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View

class SlideTransition(
    private val animationDuration: Long = 300,
) : NavigationTransition {

    override fun animatePush(fromView: View, toView: View, onAnimationEnd: () -> Unit) {
        toView.translationX = toView.width.toFloat()
        val pushAnimation = ObjectAnimator.ofFloat(toView, View.TRANSLATION_X, 0f)
        val fromViewAnimation = ObjectAnimator.ofFloat(fromView, View.TRANSLATION_X, -fromView.width.toFloat() * bottomViewTranslationPercent)
        AnimatorSet().apply {
            duration = animationDuration
            playTogether(pushAnimation, fromViewAnimation)
            addListener(
                object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) = onAnimationEnd()
                }
            )
            start()
        }
    }

    override fun animatePop(fromView: View, toView: View, onAnimationEnd: () -> Unit) {
        toView.translationX = -toView.width.toFloat() * bottomViewTranslationPercent
        val popAnimation = ObjectAnimator.ofFloat(fromView, View.TRANSLATION_X, fromView.width.toFloat())
        val toViewAnimation = ObjectAnimator.ofFloat(toView, View.TRANSLATION_X, 0f)
        AnimatorSet().apply {
            duration = animationDuration
            playTogether(popAnimation, toViewAnimation)
            addListener(
                object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) = onAnimationEnd()
                }
            )
            start()
        }
    }

    companion object {
        private const val bottomViewTranslationPercent = 0.3f
    }
}