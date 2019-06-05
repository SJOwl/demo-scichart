package bare.bones.project.app

import bare.bones.project.MainActivity
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