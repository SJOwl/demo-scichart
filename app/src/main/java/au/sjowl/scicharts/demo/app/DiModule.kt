package au.sjowl.scicharts.demo.app

import android.content.Context
import android.content.res.Resources
import au.sjowl.app.base.android.coroutines.CoroutineDispatchersProvider
import au.sjowl.app.base.android.coroutines.ICoroutineDispatchersProvider
import au.sjowl.app.base.navigation.LocalCiceroneHolder
import au.sjowl.scicharts.demo.base.ErrorHandler
import au.sjowl.scicharts.demo.service.IPriceService
import au.sjowl.scicharts.demo.service.PriceService
import au.sjowl.scicharts.demo.ui.home.MainActivity
import au.sjowl.scicharts.demo.ui.home.MainPresenter
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router

const val routerMain = "routerMain"
const val navigationHolderMain = "navigationHolderMain"

val appModule = module {
    single<ICoroutineDispatchersProvider> { CoroutineDispatchersProvider() }
    single { ErrorHandler() }

    single<Resources> { get<Context>().resources }
    single { LocalCiceroneHolder() }
    single<Cicerone<Router>> { Cicerone.create() }
}

val homeModule = module {
    single<Router>(named(routerMain)) { get<Cicerone<Router>>().router }
    single<NavigatorHolder>(named(navigationHolderMain)) { get<Cicerone<Router>>().navigatorHolder }

    factory { MainPresenter(get(), get(), get()) }
    scope(named<MainActivity>()) {}

    factory<IPriceService> { PriceService(get()) }
}

val appComponent = listOf(
    appModule,
    homeModule
)
