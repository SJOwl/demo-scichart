package au.sjowl.scicharts.demo.ui.home

import android.os.Bundle
import au.sjowl.scicharts.demo.R
import au.sjowl.scicharts.demo.app.navigationHolderMain
import au.sjowl.scicharts.demo.axisMarkerAnnotation
import au.sjowl.scicharts.demo.base.BaseActivity
import au.sjowl.scicharts.demo.data.PricePoint
import au.sjowl.scicharts.demo.dateAxis
import au.sjowl.scicharts.demo.horizontalLineAnnotation
import au.sjowl.scicharts.demo.lineSeries
import au.sjowl.scicharts.demo.modifierGroup
import au.sjowl.scicharts.demo.numericAxis
import au.sjowl.scicharts.demo.pinchZoom
import au.sjowl.scicharts.demo.textAnnotation
import au.sjowl.scicharts.demo.xyDataSeries
import au.sjowl.scicharts.demo.zoomPan
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.scichart.charting.model.dataSeries.XyDataSeries
import com.scichart.charting.visuals.SciChartSurface
import com.scichart.charting.visuals.annotations.AxisMarkerAnnotation
import com.scichart.charting.visuals.annotations.HorizontalAnchorPoint
import com.scichart.charting.visuals.annotations.HorizontalLineAnnotation
import com.scichart.charting.visuals.annotations.VerticalAnchorPoint
import com.scichart.charting.visuals.renderableSeries.IRenderableSeries
import com.scichart.core.framework.UpdateSuspender
import com.scichart.drawing.utility.ColorUtil
import com.scichart.extensions.builders.SciChartBuilder
import java.util.Collections
import kotlinx.android.synthetic.main.activity_main.chartLayout
import org.koin.android.ext.android.inject
import org.koin.androidx.scope.currentScope
import org.koin.core.qualifier.named
import ru.terrakok.cicerone.NavigatorHolder

private const val COLOR_MARKER = ColorUtil.DarkOrange

class MainActivity : BaseActivity<IMainView>(), IMainView {

    override val layoutId: Int get() = R.layout.activity_main

    @InjectPresenter
    override lateinit var presenter: MainPresenter

    override val navigatorHolder: NavigatorHolder by inject(named(navigationHolderMain))

    private val surface: SciChartSurface by lazy { SciChartSurface(this) }

    private lateinit var lineData: XyDataSeries<Long, Double>

    private lateinit var axisMarker: AxisMarkerAnnotation

    private lateinit var horizontalLine: HorizontalLineAnnotation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        chartLayout.addView(surface)
        initChart()
    }

    override fun restartChart(data: List<PricePoint>) {
        data.forEach { p -> lineData.append(p.time, p.price) }

        val sciChartBuilder = SciChartBuilder.instance()
        val lineSeries: IRenderableSeries = sciChartBuilder.lineSeries {
            withDataSeries(lineData)
            withStrokeStyle(ColorUtil.DarkRed, 1.2f, true)
        }
        surface.renderableSeries.clear()
        surface.renderableSeries.add(lineSeries)
        surface.zoomExtents()
    }

    override fun updateChartAndScrollToStart(point: PricePoint) {
        UpdateSuspender.using(surface) {
            axisMarker.y1 = point.price
            horizontalLine.y1 = point.price
            lineData.append(point.time, point.price)
            surface.zoomExtents()
        }
    }

    @ProvidePresenter
    fun providePresenter(): MainPresenter = currentScope.get()

    private fun initChart() {
        SciChartBuilder.init(this)
        val sciChartBuilder = SciChartBuilder.instance()
        lineData = sciChartBuilder.xyDataSeries {
            withFifoCapacity(100)
        }
        axisMarker = sciChartBuilder.axisMarkerAnnotation {
            withY1(0.0)
            withBackgroundColor(COLOR_MARKER)
        }
        horizontalLine = sciChartBuilder.horizontalLineAnnotation {
            withYValue(0.0)
            withStroke(1.2f, COLOR_MARKER)
        }
        Collections.addAll(surface.yAxes, sciChartBuilder.numericAxis {})
        Collections.addAll(surface.xAxes, sciChartBuilder.dateAxis {
            withTextFormatting("dd MMM")
            withSubDayTextFormatting("HH:mm:ss")
        })
        Collections.addAll(
            surface.annotations, sciChartBuilder.textAnnotation {
                withHorizontalAnchorPoint(HorizontalAnchorPoint.Center)
                withVerticalAnchorPoint(VerticalAnchorPoint.Center)
                withFontStyle(20f, ColorUtil.White)
            },
            axisMarker,
            horizontalLine
        )
        Collections.addAll(surface.chartModifiers, sciChartBuilder.modifierGroup {
            pinchZoom { withReceiveHandledEvents(true) }
            zoomPan { withReceiveHandledEvents(true) }
        })
    }
}
