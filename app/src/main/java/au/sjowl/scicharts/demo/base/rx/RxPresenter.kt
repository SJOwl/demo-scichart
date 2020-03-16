package au.sjowl.scicharts.demo.base.rx

import au.sjowl.scicharts.demo.base.coroutines.CoroutinePresenter
import au.sjowl.scicharts.demo.base.coroutines.CoroutineView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class RxPresenter<V : CoroutineView> : CoroutinePresenter<V>() {

    private val composite = CompositeDisposable()

    override fun onDestroy() {
        composite.clear()
        super.onDestroy()
    }

    fun add(disposable: Disposable) = composite.add(disposable)
}
