package bare.bones.project.base.lifecycle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import au.sjowl.app.base.logLifecycle
import au.sjowl.app.base.slog

abstract class LifecycleActivity<P : LifecyclePresenter<V>, V : LifecycleView> : AppCompatActivity(),
    LifecycleView {

    abstract val presenter: P

    abstract val layoutId: Int

    private val v get() = this as V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        logLifecycle(this, "onCreate")
        slog("lifecycle ${savedInstanceState == null}")
        presenter.bind(v)
        presenter.onSetState()
    }

    override fun onStart() {
        super.onStart()
        logLifecycle(this, "onStart")
    }

    override fun onResume() {
        super.onResume()
        logLifecycle(this, "onResume")
        presenter.bind(v)
    }

    override fun onPause() {
        logLifecycle(this, "onPause")
        presenter.unbind()
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