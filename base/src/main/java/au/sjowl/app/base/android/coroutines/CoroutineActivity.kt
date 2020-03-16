package au.sjowl.app.base.android.coroutines

import au.sjowl.app.base.android.lifecycle.LifecycleActivity
import au.sjowl.app.base.loge
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class CoroutineActivity<V : CoroutineView> : LifecycleActivity<V>(), CoroutineView,
    CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.Main

    val ui by lazy { Dispatchers.Main }

    private val bg by lazy { Dispatchers.IO }

    private val exceptionHandler: CoroutineContext =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            loge("Error in fragment:")
            throwable.printStackTrace()
        }

    private var mainJob: Job = Job()

    override fun onResume() {
        super.onResume()
        resetJob()
    }

    override fun onPause() {
        mainJob.cancel()
        super.onPause()
    }

    fun ui(
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ) = launch(mainJob + ui + exceptionHandler, start, block)

    fun bg(
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ) = launch(mainJob + bg + exceptionHandler, start, block)

    fun onUI(
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ) {
        launch(mainJob + coroutineContext + exceptionHandler, start, block)
    }

    private fun resetJob() {
        mainJob.cancel()
        mainJob = Job()
    }
}
