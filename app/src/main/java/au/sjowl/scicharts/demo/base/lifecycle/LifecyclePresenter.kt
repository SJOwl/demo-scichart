package au.sjowl.scicharts.demo.base.lifecycle

import android.os.Bundle
import au.sjowl.app.base.logLifecycle

abstract class LifecyclePresenter<V : LifecycleView> {

    var arguments: Bundle? = null
        set(value) {
            field = value
            onSetArguments(arguments)
        }

    protected var view: V? = null

    open fun onSetArguments(arguments: Bundle?) {}

    /**
     * Called on OnViewCreated() - before view is visible
     */
    open fun onSetState() {
        logLifecycle(this, "onSetState")
    }

    /**
     * Called at onResume
     */
    open fun onUpdateUI() {
        logLifecycle(this, "onUpdateUI")
    }

    open fun onDestroy() {
        logLifecycle(this, "onDestroy")
    }

    fun bind(view: V) {
        logLifecycle(this, "bind")
        this.view = view
    }

    fun unbind() {
        logLifecycle(this, "unbind")
        view = null
    }

    fun onRealDestroy() {
        logLifecycle(this, "onRealDestroy")
    }
}
