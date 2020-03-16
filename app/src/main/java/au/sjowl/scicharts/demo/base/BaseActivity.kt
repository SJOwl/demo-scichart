package au.sjowl.scicharts.demo.base

import au.sjowl.app.base.android.coroutines.CoroutineActivity
import au.sjowl.app.base.navigation.MyAppNavigator
import au.sjowl.scicharts.demo.R
import org.jetbrains.anko.toast
import ru.terrakok.cicerone.NavigatorHolder

abstract class BaseActivity<V : BaseView> : CoroutineActivity<V>() {

    open val navigator by lazy {
        MyAppNavigator(this, R.id.container)
    }

    protected abstract val navigatorHolder: NavigatorHolder

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        navigatorHolder.removeNavigator()
    }

    override fun showCommonError(stringId: Int) {
        showCommonError(getString(stringId))
    }

    override fun showCommonError(message: String) {
        toast(message)
    }
}
