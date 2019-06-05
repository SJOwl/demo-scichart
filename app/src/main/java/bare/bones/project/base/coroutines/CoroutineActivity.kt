package bare.bones.project.base.coroutines

import bare.bones.project.base.lifecycle.LifecycleActivity
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

abstract class CoroutineActivity<P : CoroutinePresenter<V>, V : CoroutineView> : LifecycleActivity<P, V>(),
    CoroutineView {

    val t = System.currentTimeMillis()

    protected val uiDispatcher by lazy { Dispatchers.Main }

    private val exceptionHandler: CoroutineContext = CoroutineExceptionHandler { coroutineContext, throwable ->
        throwable.printStackTrace()
    }

    fun ui(
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ) = GlobalScope.launch(uiDispatcher + exceptionHandler, start, block)
}