package au.sjowl.app.base.android.coroutines

import au.sjowl.app.base.android.lifecycle.LifecycleView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface CoroutineView : LifecycleView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showCommonError(stringId: Int) {
    }

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showCommonError(message: String) {
    }
}
