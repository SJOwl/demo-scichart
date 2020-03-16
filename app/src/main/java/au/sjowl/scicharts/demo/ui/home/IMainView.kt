package au.sjowl.scicharts.demo.ui.home

import au.sjowl.scicharts.demo.base.BaseView
import au.sjowl.scicharts.demo.data.PricePoint
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface IMainView : BaseView {
    fun restartChart(data: List<PricePoint>)
    fun updateChartAndScrollToStart(point: PricePoint)
}
