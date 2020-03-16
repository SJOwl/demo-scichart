package au.sjowl.scicharts.demo.base

import android.os.Bundle
import android.view.View
import au.sjowl.scicharts.demo.app.navigation.BackButtonListener
import au.sjowl.scicharts.demo.app.navigation.LocalCiceroneHolder
import au.sjowl.scicharts.demo.app.navigation.RouterProvider
import au.sjowl.scicharts.demo.app.navigation.SingleActivityNavigator
import org.koin.android.ext.android.inject
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen

interface TabContainer {
    fun openRootScreen()
}

abstract class BaseContainerFragment<P : BasePresenter<V>, V : BaseView> : BaseFragment<P, V>(),
    RouterProvider,
    BackButtonListener,
    TabContainer {

    abstract val containerResId: Int

    abstract val containerName: String

    override val router: Router get() = cicerone.router

    private val navigator: Navigator by lazy {
        SingleActivityNavigator(
            activity,
            childFragmentManager,
            containerResId
        )
    }

    private val ciceroneHolder: LocalCiceroneHolder by inject()

    private val cicerone: Cicerone<Router> get() = ciceroneHolder.getCicerone(containerName)

    abstract fun getScreen(): Screen

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        openRootScreen()
    }

    override fun onResume() {
        super.onResume()
        cicerone.navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        cicerone.navigatorHolder.removeNavigator()
        super.onPause()
    }

    override fun onBackPressed(): Boolean {
        val fragment = childFragmentManager.findFragmentById(containerResId)
        val consumedByChildFragment = fragment != null &&
            fragment is BackButtonListener &&
            (fragment as BackButtonListener).onBackPressed()

        if (!consumedByChildFragment) {
            router.exit()
        }

        return !consumedByChildFragment
    }
}
