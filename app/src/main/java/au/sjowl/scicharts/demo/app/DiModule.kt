package au.sjowl.scicharts.demo.app

import au.sjowl.app.base.rx.AppSchedulers
import au.sjowl.app.base.rx.DefaultSchedulers
import au.sjowl.scicharts.demo.MainActivity
import au.sjowl.scicharts.demo.base.PermissionsManager
import au.sjowl.scicharts.demo.base.RealPermissionsManager
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val schedMain = "SchedMain"
const val schedIO = "SchedIO"

val appModule = module {
    factory<Scheduler>(named(schedMain)) { AndroidSchedulers.mainThread() }
    factory<Scheduler>(named(schedIO)) { Schedulers.io() }
    single<AppSchedulers> { DefaultSchedulers() }
    single<PermissionsManager> {
        RealPermissionsManager(
            get(),
            get(named(schedMain))
        )
    }
}

val uiModule = module {

    scope(named<MainActivity>()) {
    }
}

val dataModule = module {
}

val appComponent = listOf(
    appModule,
    uiModule,
    dataModule
)
