package au.sjowl.app.base.android.lifecycle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import au.sjowl.app.base.logLifecycle
import com.arellomobile.mvp.MvpAppCompatFragment

abstract class LifecycleFragment<V : LifecycleView> : MvpAppCompatFragment(), LifecycleView {

    abstract val presenter: LifecyclePresenter<V>

    protected abstract val layoutId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.arguments = arguments
        logLifecycle(this, "onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        logLifecycle(this, "onCreateView")
        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logLifecycle(this, "onViewCreated")
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
        super.onPause()
        logLifecycle(this, "onPause")
    }

    override fun onStop() {
        logLifecycle(this, "onStop")
        super.onStop()
    }

    override fun onDetach() {
        logLifecycle(this, "onDetach")
        super.onDetach()
    }

    override fun onDestroyView() {
        logLifecycle(this, "onDestroyView")
        super.onDestroyView()
    }

    override fun onDestroy() {
        logLifecycle(this, "onDestroyView")
        super.onDestroy()
    }
}
