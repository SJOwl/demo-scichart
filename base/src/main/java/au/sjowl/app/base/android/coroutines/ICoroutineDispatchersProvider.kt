package au.sjowl.app.base.android.coroutines

import kotlin.coroutines.CoroutineContext

interface ICoroutineDispatchersProvider {
    val io: CoroutineContext
    val main: CoroutineContext
    val default: CoroutineContext
    val unconfined: CoroutineContext
}
