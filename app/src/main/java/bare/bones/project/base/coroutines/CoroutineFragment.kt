package bare.bones.project.base.coroutines

import android.content.Context
import android.os.Bundle
import android.view.View
import bare.bones.project.base.lifecycle.LifecycleFragment
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

abstract class CoroutineFragment<P : CoroutinePresenter<V>, V : CoroutineView> : LifecycleFragment<P, V>() {

    val ui = Dispatchers.Main

    val bg = Dispatchers.IO

    protected val compositeDisposable = CompositeDisposable()

    private val exceptionHandler: CoroutineContext = CoroutineExceptionHandler { coroutineContext, throwable ->
        Timber.e("Error in fragment:")
        throwable.printStackTrace()
    }

    private var mainJob: Job = Job()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        resetJob()
    }

    override fun onDestroyView() {
        mainJob.cancel()
        super.onDestroyView()
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    override fun getContext(): Context {
        return super.getContext() ?: throw IllegalStateException("Context is null")
    }

    fun resetJob() {
        mainJob.cancel()
        mainJob = Job()
    }

    fun ui(
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ) = GlobalScope.launch(mainJob + ui + exceptionHandler, start, block) // todo do they leak?

    fun bg(
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ) = GlobalScope.launch(mainJob + bg + exceptionHandler, start, block) // todo do they leak?
}