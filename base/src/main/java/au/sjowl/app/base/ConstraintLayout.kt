package au.sjowl.app.base

import android.transition.Transition
import android.transition.TransitionManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet

fun ConstraintLayout.constrain(block: ((cs: ConstraintSet) -> Unit)) {
    val cs = ConstraintSet()
    cs.clone(this)
    block.invoke(cs)
    cs.applyTo(this)
}

fun ConstraintLayout.constrain(transition: Transition, block: ((cs: ConstraintSet) -> Unit)) {
    val cs = ConstraintSet()
    cs.clone(this)
    if (android.os.Build.VERSION.SDK_INT >= 19) {
        TransitionManager.beginDelayedTransition(this, transition)
    }
    block.invoke(cs)
    cs.applyTo(this)
}