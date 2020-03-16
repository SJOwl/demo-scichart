package au.sjowl.app.base.android.coroutines

import au.sjowl.app.base.android.lifecycle.LifecyclePresenter
import au.sjowl.app.base.logLifecycle
import au.sjowl.app.base.loge
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class CoroutinePresenter<V : CoroutineView>(
    protected val coroutineDispatchers: ICoroutineDispatchersProvider
) : LifecyclePresenter<V>(),
    CoroutineView,
    CoroutineScope {

    override val coroutineContext: CoroutineContext = coroutineDispatchers.io

    open val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { _, throwable ->
            loge("Error in presenter coroutine: ${this::class.java.simpleName} ${throwable.message}")
            throwable.printStackTrace()
            handleExceptions(throwable)
        }
    }

    val baseErrorHandler = { t: Throwable ->
        loge("Error in presenter rx:")
        handleExceptions(t)
        t.printStackTrace()
    }

    private var compositeJob = Job()

    private val jobs = arrayListOf<Job>()

    override fun onDestroy() {
        cancelJobs()
        logLifecycle(this, "Coroutines should be stopped now")
    }

    open fun handleExceptions(throwable: Throwable) {
        bg {
            loge("error is ${throwable.message}, ${throwable::class.java}")
            throwable.printStackTrace()
        }
    }

    open fun cancelJobs() {
        jobs.forEach { it.cancel() }
        compositeJob.cancel()
    }

    fun bg(
        start: CoroutineStart = CoroutineStart.DEFAULT,
        handler: CoroutineExceptionHandler = exceptionHandler,
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        val job = Job()
        launch(job + coroutineDispatchers.io + handler, start, block)
        jobs.add(job)
        return job
    }

    fun bgUnit(
        start: CoroutineStart = CoroutineStart.DEFAULT,
        handler: CoroutineExceptionHandler = exceptionHandler,
        block: suspend CoroutineScope.() -> Unit
    ) {
        bg(start, handler, block)
    }

    fun ui(
        start: CoroutineStart = CoroutineStart.DEFAULT,
        handler: CoroutineExceptionHandler = exceptionHandler,
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        val job = Job()
        launch(job + coroutineDispatchers.main + handler, start, block)
        jobs.add(job)
        return job
    }

    fun uiUnit(
        start: CoroutineStart = CoroutineStart.DEFAULT,
        handler: CoroutineExceptionHandler = exceptionHandler,
        block: suspend CoroutineScope.() -> Unit
    ) {
        ui(start, handler, block)
    }

    suspend fun asyncUi(block: suspend CoroutineScope.() -> Unit) =
        withContext(coroutineDispatchers.main, block)

    fun onUI(
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ) {
        val job = Job()
        launch(job + coroutineDispatchers.main + exceptionHandler, start, block)
        jobs.add(job)
    }

    fun <T> async(
        start: CoroutineStart = CoroutineStart.DEFAULT,
        handler: CoroutineExceptionHandler = exceptionHandler,
        block: suspend CoroutineScope.() -> T
    ): Deferred<T> = async(compositeJob + coroutineContext + handler, start, block)

    private fun resetJob() {
        compositeJob.cancel()
        compositeJob = Job()
    }
}
