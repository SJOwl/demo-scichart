package au.sjowl.scicharts.demo.base

import au.sjowl.app.base.android.coroutines.CoroutinePresenter
import au.sjowl.app.base.android.coroutines.ICoroutineDispatchersProvider
import au.sjowl.app.base.logd
import au.sjowl.app.base.loge
import au.sjowl.scicharts.demo.R
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.io.IOException
import java.net.UnknownHostException
import kotlinx.coroutines.CoroutineExceptionHandler
import org.koin.core.KoinComponent

abstract class BasePresenter<V : BaseView>(
    coroutineDispatchers: ICoroutineDispatchersProvider,
    protected val errorHandler: ErrorHandler
) : CoroutinePresenter<V>(coroutineDispatchers), KoinComponent {

    protected open val cHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { coroutineContext, throwable ->
            // todo
            loge(throwable)
        }
    }

    private val compositeDisposable by lazy { CompositeDisposable() }

    private val defaultOnErrorConsumer: (Throwable) -> Unit by lazy { OnErrorConsumer() }

    private var startTimeMs = System.currentTimeMillis()

    override fun attachView(view: V) {
        super.attachView(view)
        startView()
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    override fun showCommonError(message: String) {
        (viewState as BaseView).showCommonError(message)
    }

    override fun showCommonError(stringId: Int) {
        (viewState as BaseView).showCommonError(stringId)
    }

    open fun startView() {
        logd("start seing $this")
        startTimeMs = System.currentTimeMillis()
    }

    open fun stopView() {
        logd("stop seing $this")
    }

    protected fun Completable.compositeSubscribe(
        onSuccess: () -> Unit = {},
        onError: (Throwable) -> Unit = defaultOnErrorConsumer
    ) = subscribe(onSuccess, onError)
        .composite()

    protected fun Completable.justSubscribe(
        onSuccess: () -> Unit = {},
        onError: (Throwable) -> Unit = defaultOnErrorConsumer
    ) = subscribe(onSuccess, onError)

    protected fun <T : Any> Flowable<T>.compositeSubscribe(
        onNext: (T) -> Unit = {},
        onError: (Throwable) -> Unit = defaultOnErrorConsumer,
        onComplete: () -> Unit = {}
    ) = subscribe(onNext, onError, onComplete)
        .composite()

    protected fun <T : Any> Single<T>.compositeSubscribe(
        onSuccess: (T) -> Unit = {},
        onError: (Throwable) -> Unit = defaultOnErrorConsumer
    ) = subscribe(onSuccess, onError)
        .composite()

    protected fun <T : Any> Maybe<T>.compositeSubscribe(
        onSuccess: (T) -> Unit = {},
        onError: (Throwable) -> Unit = defaultOnErrorConsumer
    ) = subscribe(onSuccess, onError)
        .composite()

    protected fun <T : Any> Single<T>.justSubscribe(
        onSuccess: (T) -> Unit = {},
        onError: (Throwable) -> Unit = defaultOnErrorConsumer
    ) = subscribe(onSuccess, onError)

    protected fun <T : Any> Observable<T>.compositeSubscribe(
        onNext: (T) -> Unit = {},
        onError: (Throwable) -> Unit = defaultOnErrorConsumer,
        onComplete: () -> Unit = {}
    ) = subscribe(onNext, onError, onComplete)
        .composite()

    protected fun <T : Any> Observable<T>.justSubscribe(
        onNext: (T) -> Unit = {},
        onError: (Throwable) -> Unit = defaultOnErrorConsumer,
        onComplete: () -> Unit = {}
    ): Disposable = subscribe(onNext, onError, onComplete)

    private fun Disposable.composite(): Disposable {
        compositeDisposable.add(this)
        return this
    }

    open inner class OnErrorConsumer : BaseOnErrorConsumer() {

        override fun onError(error: Throwable) {
            loge(error)
            when (error) {
                is IOException,
                is UnknownHostException -> onNoConnectionError()
                else -> onOtherError(error)
            }
        }

        override fun onNoConnectionError() {
            showCommonError(R.string.no_internet_connection)
        }

        override fun onOtherError(error: Throwable) {
            errorHandler.proceed(error) { message: String -> showCommonError(message) }
        }
    }

    abstract class BaseOnErrorConsumer : (Throwable) -> Unit {

        abstract fun onError(error: Throwable)

        abstract fun onNoConnectionError()

        abstract fun onOtherError(error: Throwable)

        override fun invoke(error: Throwable) {
            onError(error)
        }
    }
}
