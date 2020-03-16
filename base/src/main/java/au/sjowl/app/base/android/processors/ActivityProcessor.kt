@file:Suppress("unused")

package au.sjowl.app.base.android.processors

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import au.sjowl.app.base.logd

interface IActivityProcessor {
    /**
     * @return true if was consumed
     */
    fun processResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean
    fun attach(activity: Activity)
    fun attach(fragment: Fragment)
    fun detach(fragment: Fragment)
    fun detach(activity: Activity)
}

abstract class ActivityProcessor : IActivityProcessor {

    protected var fragment by WeakReferenceDelegate<Fragment>()

    protected var activity by WeakReferenceDelegate<Activity>()

    override fun attach(activity: Activity) {
        logd("attach ${this::class.java.simpleName} to activity")
        this.activity = activity
    }

    override fun attach(fragment: Fragment) {
        logd("attach ${this::class.java.simpleName} to fragment")
        this.fragment = fragment
    }

    override fun detach(fragment: Fragment) {
        if (this.fragment === fragment) {
            this.fragment = null
        }
        logd("detach ${this::class.java.simpleName}")
    }

    override fun detach(activity: Activity) {
        if (this.activity === activity) {
            this.activity = null
        }
        logd("detach ${this::class.java.simpleName}")
    }

    protected fun startActivityForResult(intent: Intent, code: Int) {
        when {
            activity != null -> activity?.startActivityForResult(intent, code)
            fragment != null -> fragment?.startActivityForResult(intent, code)
            else -> throw IllegalArgumentException("No attached fragment or activity")
        }
    }
}
