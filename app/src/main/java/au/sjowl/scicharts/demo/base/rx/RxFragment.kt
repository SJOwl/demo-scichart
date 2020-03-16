package au.sjowl.scicharts.demo.base.rx

import au.sjowl.scicharts.demo.base.coroutines.CoroutineFragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class RxFragment<P : RxPresenter<V>, V : RxView> : CoroutineFragment<P, V>() {

    private val composite = CompositeDisposable()

    override fun onPause() {
        composite.clear()
        super.onPause()
    }

    fun add(disposable: Disposable) = composite.add(disposable)
}
