@file:Suppress("unused")

package au.sjowl.app.base.rx

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

interface AppSchedulers {
    fun io(): Scheduler
    fun main(): Scheduler
    fun computation(): Scheduler
}

class DefaultSchedulers : AppSchedulers {
    override fun io(): Scheduler = Schedulers.io()
    override fun main(): Scheduler = AndroidSchedulers.mainThread()
    override fun computation(): Scheduler = Schedulers.computation()
}

class TestSchedulers : AppSchedulers {
    override fun io(): Scheduler = Schedulers.trampoline()
    override fun main(): Scheduler = Schedulers.trampoline()
    override fun computation(): Scheduler = Schedulers.trampoline()
}
