package au.sjowl.scicharts.demo

import au.sjowl.app.base.android.coroutines.ICoroutineDispatchersProvider
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers

class CoroutineDispatchersProviderTestImpl :
    ICoroutineDispatchersProvider {
    override val io: CoroutineContext = Dispatchers.Unconfined
    override val main: CoroutineContext = Dispatchers.Unconfined
    override val default: CoroutineContext = Dispatchers.Unconfined
    override val unconfined: CoroutineContext = Dispatchers.Unconfined
}
