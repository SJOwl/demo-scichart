package au.sjowl.scicharts.demo.base

import au.sjowl.scicharts.demo.app.navigation.BackButtonListener
import au.sjowl.scicharts.demo.app.navigation.RouterProvider
import ru.terrakok.cicerone.Router

abstract class BaseChildFragment<P : BasePresenter<V>, V : BaseView> : BaseFragment<P, V>(),
    RouterProvider,
    BackButtonListener {

    override val router: Router get() = (parentFragment as RouterProvider).router

    override fun onBackPressed(): Boolean = false
}
