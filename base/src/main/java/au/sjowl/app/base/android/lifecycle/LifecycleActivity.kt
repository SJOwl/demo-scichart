package au.sjowl.app.base.android.lifecycle

import android.os.Bundle
import au.sjowl.app.base.logLifecycle
import au.sjowl.app.base.logd
import com.arellomobile.mvp.MvpAppCompatActivity

abstract class LifecycleActivity<V : LifecycleView> : MvpAppCompatActivity(),
    LifecycleView {

    abstract val presenter: LifecyclePresenter<V>

    abstract val layoutId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent?.let { presenter.intent = it }
        setContentView(layoutId)
        logLifecycle(this, "onCreate")
        logd("lifecycle ${savedInstanceState == null}")
    }

    override fun onStart() {
        super.onStart()
        logLifecycle(this, "onStart")
    }

    override fun onResume() {
        super.onResume()
        logLifecycle(this, "onResume")
    }

    override fun onPause() {
        logLifecycle(this, "onPause")
        super.onPause()
    }

    override fun onStop() {
        logLifecycle(this, "onStop")
        super.onStop()
    }

    override fun onDestroy() {
        logLifecycle(this, "onDestroy")
        super.onDestroy()
    }
}
