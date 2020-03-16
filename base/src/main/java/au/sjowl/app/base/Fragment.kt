package au.sjowl.app.base

import android.app.Activity
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

fun Activity?.addFragment(
    @IdRes id: Int,
    fragment: Fragment,
    tag: String? = null,
    addToBackStack: Boolean = true
) {
    val compatActivity = this as? AppCompatActivity ?: return
    compatActivity.supportFragmentManager.beginTransaction()
        .apply {
            add(id, fragment, tag)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            if (addToBackStack) {
                addToBackStack(null)
            }
            commit()
        }
}

fun Activity?.replaceFragment(
    @IdRes id: Int,
    fragment: Fragment,
    tag: String? = null,
    addToBackStack: Boolean = false
) {
    val compatActivity = this as? AppCompatActivity ?: return
    compatActivity.supportFragmentManager.beginTransaction()
        .apply {
            replace(id, fragment, tag)
            if (addToBackStack) {
                addToBackStack(null)
            }
            commit()
        }
}

fun Fragment.addFragment(
    @IdRes id: Int,
    fragment: Fragment,
    tag: String? = null,
    addToBackStack: Boolean = true
) {
    childFragmentManager.beginTransaction()
        .apply {
            add(id, fragment, tag)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            if (addToBackStack) {
                addToBackStack(null)
            }
            commit()
        }
}

fun Fragment.replaceFragment(
    @IdRes id: Int,
    fragment: Fragment,
    tag: String? = null,
    addToBackStack: Boolean = false
) {
    childFragmentManager.beginTransaction()
        .apply {
            replace(id, fragment, tag)
            if (addToBackStack) {
                addToBackStack(null)
            }
            commit()
        }
}
