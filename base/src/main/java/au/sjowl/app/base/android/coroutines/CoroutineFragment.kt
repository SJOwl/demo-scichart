package au.sjowl.app.base.android.coroutines

import android.content.Context
import au.sjowl.app.base.android.lifecycle.LifecycleFragment

abstract class CoroutineFragment<V : CoroutineView> : LifecycleFragment<V>(), CoroutineView {

    val nullableContext: Context? get() = super.getContext()

    override fun getContext(): Context {
        return super.getContext() ?: throw IllegalStateException("Context is null")
    }
}
