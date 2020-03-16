package au.sjowl.app.base.android.coroutines

import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers

class CoroutineDispatchersProvider : ICoroutineDispatchersProvider {
    override val io: CoroutineContext = Dispatchers.IO
    override val main: CoroutineContext = Dispatchers.Main
    override val default: CoroutineContext = Dispatchers.Default
    override val unconfined: CoroutineContext = Dispatchers.Unconfined
}
