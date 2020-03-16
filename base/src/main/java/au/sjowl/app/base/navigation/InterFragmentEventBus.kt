package au.sjowl.app.base.navigation

import au.sjowl.app.base.logd
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

abstract class BaseEvent

/**
 * Use only for inter fragment communications
 */
class InterFragmentEventBus {

    private val publisher = PublishSubject.create<BaseEvent>()

    fun postEvent(event: BaseEvent) {
        logd("post event: ${event::class.java}")
        publisher.onNext(event)
    }

    fun onEvent(): Observable<BaseEvent> = publisher
}
