package au.sjowl.scicharts.demo.app.navigation

import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import bare.bones.project.R
import org.jetbrains.anko.toast
import ru.terrakok.cicerone.android.support.SupportAppNavigator

open class SingleActivityNavigator : SupportAppNavigator {

    constructor(activity: FragmentActivity?, fragmentManager: FragmentManager?, containerId: Int) : super(activity, fragmentManager, containerId) {
        this.activity = activity
    }

    constructor(activity: FragmentActivity?, containerId: Int) : super(activity, containerId) {
        this.activity = activity
    }

    private var activity: FragmentActivity? = null

    private var lastTimeBackPressed = 0L

    private val doubleBackPressThresholdMs = 1500

    override fun activityBack() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastTimeBackPressed < doubleBackPressThresholdMs) {
            activity?.finish()
        } else {
            lastTimeBackPressed = currentTime
            val message = activity?.getString(R.string.pressBackTwice)?.toString().orEmpty()
            activity?.toast(message)
        }
    }
}
