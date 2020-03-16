package au.sjowl.scicharts.demo.base

import android.os.Bundle
import ru.terrakok.cicerone.android.support.SupportAppScreen
import ru.terrakok.cicerone.commands.Replace

abstract class BaseHostActivity : BaseActivity<BaseView>() {
    abstract val homeScreen: SupportAppScreen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            navigator.applyCommands(arrayOf(Replace(homeScreen)))
        }
    }
}
