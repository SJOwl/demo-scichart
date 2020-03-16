package au.sjowl.app.base.navigation

import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import au.sjowl.app.base.R
import org.jetbrains.anko.toast

open class SingleActivityNavigator : MyAppNavigator {

    constructor(
        activity: FragmentActivity,
        fragmentManager: FragmentManager,
        containerId: Int
    ) : super(activity, fragmentManager, containerId) {
        this.activity = activity
    }

    constructor(activity: FragmentActivity, containerId: Int) : super(activity, containerId) {
        this.activity = activity
    }

    private var activity: FragmentActivity? = null

    private var lastTimeBackPressed = 0L

    private val doubleBackPressThresholdMs = 1500

    /**
     * I do not want kill app by finishing activity. On iOS we can't do it. So should be here.
     * This method does nothing, because having ART we do not need kill apps.
     * Apps should persist as much as possible. Any cold start takes much longer time
     * in comparison with hot one.
     */
    override fun activityBack() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastTimeBackPressed < doubleBackPressThresholdMs) {
            activity?.finish()
        } else {
            lastTimeBackPressed = currentTime
            val message =
                activity?.getString(R.string.to_close_the_application_press_back)?.toString()
                    .orEmpty()
            activity?.toast(message)
        }
    }
}
