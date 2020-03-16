package au.sjowl.scicharts.demo

import au.sjowl.app.base.android.coroutines.ICoroutineDispatchersProvider
import au.sjowl.scicharts.demo.app.navigationHolderMain
import au.sjowl.scicharts.demo.app.routerMain
import au.sjowl.scicharts.demo.base.ErrorHandler
import au.sjowl.scicharts.demo.service.IPriceService
import au.sjowl.scicharts.demo.service.PriceService
import au.sjowl.scicharts.demo.ui.home.MainActivity
import au.sjowl.scicharts.demo.ui.home.MainPresenter
import com.nhaarman.mockitokotlin2.mock
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router

val testAppModule = module {
    // todo unused
//    factory<Scheduler>(named(schedMain)) { AndroidSchedulers.mainThread() }
//    factory<Scheduler>(named(schedIO)) { Schedulers.io() }
    single<ICoroutineDispatchersProvider> { CoroutineDispatchersProviderTestImpl() }
    single { ErrorHandler() }
}

val testHomeModule = module {
    single<Router>(named(routerMain)) { mock() }
    single<NavigatorHolder>(named(navigationHolderMain)) { mock() }

    factory {
        MainPresenter(
            get(),
            get(),
            get()
        )
    }
    scope(named<MainActivity>()) {}

    factory<IPriceService> { PriceService(get()) }
}

val testAppComponent = listOf(
    testAppModule,
    testHomeModule
)
