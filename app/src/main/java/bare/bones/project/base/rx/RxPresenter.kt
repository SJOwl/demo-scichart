package bare.bones.project.base.rx

import bare.bones.project.base.coroutines.CoroutinePresenter
import bare.bones.project.base.coroutines.CoroutineView
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