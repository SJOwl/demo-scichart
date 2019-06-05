package bare.bones.project.base.rx

import au.sjowl.app.base.rx.AppSchedulers
import bare.bones.project.base.coroutines.CoroutineActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.koin.androidx.scope.currentScope

abstract class RxActivity<P : RxPresenter<V>, V : RxView> : CoroutineActivity<P, V>() {

    protected val schedulers: AppSchedulers by currentScope.inject()

    private val composite = CompositeDisposable()

    override fun onPause() {
        composite.clear()
        super.onPause()
    }

    fun add(disposable: Disposable) = composite.add(disposable)
}