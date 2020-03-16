package au.sjowl.scicharts.demo.base.coroutines

import au.sjowl.app.base.SLog
import au.sjowl.app.base.logLifecycle
import au.sjowl.scicharts.demo.base.lifecycle.LifecyclePresenter
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.net.UnknownHostException
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

abstract class CoroutinePresenter<V : CoroutineView> : LifecyclePresenter<V>(),
    CoroutineView,
    CoroutineScope {

    override val coroutineContext: CoroutineContext get() = Dispatchers.IO

    val baseErrorHandler = { t: Throwable ->
        SLog.e("Error in presenter rx:")
        handleExceptions(t)
        t.printStackTrace()
    }

    protected var jobs: HashMap<String, Job?> = hashMapOf()

    private val compositeDisposable = CompositeDisposable()

    private val exceptionHandler: CoroutineContext = CoroutineExceptionHandler { _, throwable ->
        SLog.e("Error in presenter coroutine: ${throwable.message}")
        throwable.printStackTrace()
        cancelJobs()
        handleExceptions(throwable)
    }

    private var compositeJob = Job()

    override fun onSetState() {
        super.onSetState()
        resetJob()
    }

    override fun onUpdateUI() {
        super.onUpdateUI()
//        resetJob()
    }

    override fun onDestroy() {
        cancelJobs()
        compositeJob.cancel()
        compositeDisposable.clear()
        logLifecycle(this, "Coroutines should be stopped now")
    }

    open fun handleExceptions(throwable: Throwable) = bg {
        SLog.e("error is ${throwable.message}, ${throwable::class.java}")
        throwable.printStackTrace()
        when (throwable) {
            is UnknownHostException -> view?.showConnectionError()
            else -> {
            }
        }
    }

    open fun cancelJobs() {
        jobs.values.forEach { job -> job?.cancel() }
    }

    @Deprecated("Use coroutines instead: Flowable.openSubscription()")
    fun cadd(d: Disposable) {
        compositeDisposable.add(d)
    }

    fun resetJob() {
        compositeJob.cancel()
        compositeJob = Job()
    }

    fun bg(
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ) = launch(compositeJob + Dispatchers.IO + exceptionHandler, start, block)

    fun ui(
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ) = launch(compositeJob + Dispatchers.Main + exceptionHandler, start, block)

    fun launch(
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ) = launch(compositeJob + coroutineContext + exceptionHandler, start, block)

    fun <T> async(
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> T
    ): Deferred<T> = async(compositeJob + coroutineContext, start, block)
}
