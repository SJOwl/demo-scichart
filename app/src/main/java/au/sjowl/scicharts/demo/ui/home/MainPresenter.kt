package au.sjowl.scicharts.demo.ui.home

import au.sjowl.app.base.android.coroutines.ICoroutineDispatchersProvider
import au.sjowl.scicharts.demo.base.BasePresenter
import au.sjowl.scicharts.demo.base.ErrorHandler
import au.sjowl.scicharts.demo.data.PricePoint
import au.sjowl.scicharts.demo.service.IPriceService
import com.arellomobile.mvp.InjectViewState
import kotlinx.coroutines.isActive

@InjectViewState
class MainPresenter(
    coroutineDispatchers: ICoroutineDispatchersProvider,
    errorHandler: ErrorHandler,
    private val priceService: IPriceService
) : BasePresenter<IMainView>(coroutineDispatchers, errorHandler) {

    private val points = mutableListOf<PricePoint>()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.restartChart(points)
    }

    override fun attachView(view: IMainView) {
        super.attachView(view)
        bg {
            priceService.start()

            val history = priceService.getHistory(
                points.lastOrNull()?.time ?: (System.currentTimeMillis() - 20_000),
                System.currentTimeMillis()
            )
            points.addAll(history)
            asyncUi { viewState.restartChart(points) }

            val channel = priceService.observePrice()
            while (isActive) {
                val p = channel.receive()
                points.add(p)
                asyncUi { viewState.updateChartAndScrollToStart(p) }
            }
        }
    }

    override fun detachView(view: IMainView?) {
        priceService.stop()
        super.detachView(view)
    }
}
