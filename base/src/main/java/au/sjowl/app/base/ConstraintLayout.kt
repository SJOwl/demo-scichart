@file:Suppress("unused")

package au.sjowl.app.base

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.transition.Transition
import androidx.transition.TransitionManager

fun ConstraintLayout.constrain(block: ((cs: ConstraintSet) -> Unit)) {
    val cs = ConstraintSet()
    cs.clone(this)
    block.invoke(cs)
    cs.applyTo(this)
}

fun ConstraintLayout.constrain(transition: Transition, block: ((cs: ConstraintSet) -> Unit)) {
    val cs = ConstraintSet()
    cs.clone(this)
    TransitionManager.beginDelayedTransition(this, transition)
    block.invoke(cs)
    cs.applyTo(this)
}
