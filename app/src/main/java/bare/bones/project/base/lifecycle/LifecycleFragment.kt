package bare.bones.project.base.lifecycle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import au.sjowl.app.base.logLifecycle

abstract class LifecycleFragment<P : LifecyclePresenter<V>, V : LifecycleView> :
    Fragment(),
    LifecycleView {

    abstract val presenter: P

    protected abstract val layoutId: Int

    private val v get() = this as V

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
        presenter.bind(v)
        presenter.onSetState()
    }

    override fun onStart() {
        super.onStart()
        logLifecycle(this, "onStart")
    }

    override fun onResume() {
        super.onResume()
        presenter.bind(v)
        presenter.onUpdateUI()
        logLifecycle(this, "onResume")
    }

    override fun onPause() {
        super.onPause()
        logLifecycle(this, "onPause")
        presenter.unbind()
    }

    override fun onStop() {
        logLifecycle(this, "onStop")
        super.onStop()
    }

    override fun onDetach() {
        logLifecycle(this, "onDetach")
        presenter.onDestroy()
        super.onDetach()
    }

    override fun onDestroyView() {
        logLifecycle(this, "onDestroyView")
        super.onDestroyView()
    }

    override fun onDestroy() {
        if (!activity!!.isChangingConfigurations) {
            presenter.onRealDestroy()
        }
        logLifecycle(this, "onDestroyView")
        super.onDestroy()
    }
}