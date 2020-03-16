package au.sjowl.scicharts.demo

import com.scichart.charting.model.dataSeries.XyDataSeries
import com.scichart.charting.modifiers.ModifierGroup
import com.scichart.charting.visuals.annotations.AxisMarkerAnnotation
import com.scichart.charting.visuals.annotations.HorizontalLineAnnotation
import com.scichart.charting.visuals.annotations.TextAnnotation
import com.scichart.charting.visuals.axes.IAxis
import com.scichart.charting.visuals.renderableSeries.FastLineRenderableSeries
import com.scichart.extensions.builders.AnnotationBuilder
import com.scichart.extensions.builders.AxisBuilder
import com.scichart.extensions.builders.DataSeriesBuilder
import com.scichart.extensions.builders.ModifierGroupBuilder
import com.scichart.extensions.builders.RenderableSeriesBuilder
import com.scichart.extensions.builders.SciChartBuilder

fun SciChartBuilder.numericAxis(initialize: AxisBuilder.NumericAxisBuilder.() -> Unit): IAxis {
    val axis = newNumericAxis()
    axis.initialize()
    return axis.build()
}

fun SciChartBuilder.dateAxis(initialize: AxisBuilder.DateAxisBuilder.() -> Unit): IAxis {
    val axis = newDateAxis()
    axis.initialize()
    return axis.build()
}

fun SciChartBuilder.textAnnotation(block: AnnotationBuilder.TextAnnotationBuilder.() -> Unit): TextAnnotation? {
    val t = newTextAnnotation()
    t.block()
    return t.build()
}

inline fun <reified TX : Comparable<TX>, reified TY : Comparable<TY>> SciChartBuilder.xyDataSeries(
    block: DataSeriesBuilder.XyDataSeriesBuilder<TX, TY>.() -> Unit
): XyDataSeries<TX, TY> {
    val t = xyDataSeries<TX, TY>()
    t.block()
    return t.build()
}

fun SciChartBuilder.lineSeries(block: RenderableSeriesBuilder.FastLineRenderableSeriesBuilder.() -> Unit): FastLineRenderableSeries {
    val t = newLineSeries()
    t.block()
    return t.build()
}

fun SciChartBuilder.modifierGroup(block: ModifierGroupBuilder.() -> Unit): ModifierGroup {
    val t = newModifierGroup()
    t.block()
    return t.build()
}

fun ModifierGroupBuilder.pinchZoom(block: ModifierGroupBuilder.PinchZoomModifierBuilder.() -> Unit): ModifierGroupBuilder {
    val t = withPinchZoomModifier()
    t.block()
    return t.build()
}

fun ModifierGroupBuilder.zoomPan(block: ModifierGroupBuilder.ZoomPanModifierBuilder.() -> Unit): ModifierGroupBuilder {
    val t = withZoomPanModifier()
    t.block()
    return t.build()
}

fun SciChartBuilder.axisMarkerAnnotation(block: AnnotationBuilder.AxisMarkerAnnotationBuilder.() -> Unit): AxisMarkerAnnotation {
    val t = newAxisMarkerAnnotation()
    t.block()
    return t.build()
}

fun SciChartBuilder.horizontalLineAnnotation(block: AnnotationBuilder.HorizontalLineAnnotationBuilder.() -> Unit): HorizontalLineAnnotation {
    val t = newHorizontalLineAnnotation()
    t.block()
    return t.build()
}

inline fun <reified TX : Comparable<TX>, reified TY : Comparable<TY>> SciChartBuilder.xyDataSeries(): DataSeriesBuilder.XyDataSeriesBuilder<TX, TY> {
    return newXyDataSeries(TX::class.javaObjectType, TY::class.javaObjectType)
}
