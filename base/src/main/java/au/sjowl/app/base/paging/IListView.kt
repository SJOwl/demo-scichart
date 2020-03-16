package au.sjowl.app.base.paging

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface IListView {

    @StateStrategyType(SkipStrategy::class)
    fun clear()
    @StateStrategyType(SkipStrategy::class)
    fun delete(position: Int)
    @StateStrategyType(SkipStrategy::class)
    fun insert(item: Any, position: Int)
    @StateStrategyType(SkipStrategy::class)
    fun networkError()
    @StateStrategyType(SkipStrategy::class)
    fun newListLoading(isLoading: Boolean)
    @StateStrategyType(SkipStrategy::class)
    fun nextPageLoading(isLoading: Boolean)
    @StateStrategyType(SkipStrategy::class)
    fun scrollToTop()

    @StateStrategyType(SkipStrategy::class)
    fun setList(list: List<Any>)

    @StateStrategyType(SkipStrategy::class)
    fun addPage(list: List<Any>)
    fun content()
    fun showContentOrEmpty()
    fun showEmptyContent()
}
