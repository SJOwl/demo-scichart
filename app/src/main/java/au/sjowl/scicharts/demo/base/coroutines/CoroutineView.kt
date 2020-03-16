package au.sjowl.scicharts.demo.base.coroutines

import au.sjowl.app.base.SLog
import au.sjowl.scicharts.demo.base.lifecycle.LifecycleView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

interface CoroutineView :
    LifecycleView {
    suspend fun showCommonError(message: String) = GlobalScope.launch(Dispatchers.Default) { SLog.e("common error occurred $message") }
    suspend fun showConnectionError() = GlobalScope.launch(Dispatchers.Default) { SLog.e("connection error") }
    suspend fun showEmpty() = GlobalScope.launch(Dispatchers.Default) { SLog.d("Show empty") }
    suspend fun showProgress() = GlobalScope.launch(Dispatchers.Default) { SLog.d("Show progress") }
    suspend fun hideProgress() = GlobalScope.launch(Dispatchers.Default) { SLog.d("Hide progress") }
}
