package au.sjowl.app.base.android.lifecycle

import android.content.Intent
import android.os.Bundle
import com.arellomobile.mvp.MvpPresenter

abstract class LifecyclePresenter<V : LifecycleView> : MvpPresenter<V>() {

    var arguments: Bundle? = null
        set(value) {
            field = value
            onSetArguments(arguments)
        }

    var intent: Intent? = null
        set(value) {
            field = value
            value?.let { onSetArgumentsIntent(value) }
        }

    protected open fun onSetArguments(arguments: Bundle?) {}

    protected open fun onSetArgumentsIntent(intent: Intent) {}
}
