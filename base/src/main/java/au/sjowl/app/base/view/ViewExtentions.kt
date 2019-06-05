package au.sjowl.app.base.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.transition.AutoTransition
import android.transition.Transition
import android.transition.TransitionManager
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import org.jetbrains.anko.dip
import org.jetbrains.anko.sp
import org.jetbrains.anko.topPadding
import kotlin.system.measureNanoTime

fun View.contains(px: Int, py: Int): Boolean {
    return px > x && px < x + measuredWidth && py > y && py < y + measuredHeight
}

fun View.contains(px: Float, py: Float): Boolean {
    return px > x && px < x + width && py > y && py < y + height
}

fun View.contains(event: MotionEvent): Boolean {
    return contains(event.x + x, event.y + y)
}

interface AnimProperty {
    fun setup()
    fun reverse()
}

class AnimatedPropertyF(
    var value: Float = 0f,
    var from: Float = 0f,
    var to: Float = 0f
) : AnimProperty {
    override fun setup() {
        value = from
    }

    override fun reverse() {
        val t = from
        from = to
        to = t
    }
}

class AnimatedPropertyInt(
    var value: Int = 0,
    var from: Int = 0,
    var to: Int = 0
) : AnimProperty {
    override fun setup() {
        value = from
    }

    override fun reverse() {
        val t = from
        from = to
        to = t
    }

    fun set(from: Int, to: Int, value: Int) {
        this.from = from
        this.to = to
        this.value = value
    }
}

fun Context.colorCompat(id: Int) = ContextCompat.getColor(this, id)
fun Context.drawableCompat(id: Int) = ContextCompat.getDrawable(this, id)
fun Context.dipf(dips: Float) = dip(dips) * 1f
fun Context.dipf(dips: Int) = dip(dips) * 1f
fun Context.spf(dips: Float) = sp(dips) * 1f
fun Context.spf(dips: Int) = sp(dips) * 1f

fun View.dipf(dips: Float) = dip(dips) * 1f
fun View.dipf(dips: Int) = dip(dips) * 1f
fun View.spf(dips: Float) = sp(dips) * 1f
fun View.spf(dips: Int) = sp(dips) * 1f

fun View.getTintedDrawable(id: Int, colorId: Int): Drawable? {
    return context.drawableCompat(id)?.apply {
        mutate().setColorFilter(context.colorCompat(colorId), PorterDuff.Mode.SRC_ATOP)
    }
}

fun measureDrawingMs(msg: String, block: (() -> Unit)) {
    val t = measureNanoTime {
        block.invoke()
    }
    println("$msg draw %.3f".format(t / 1000000f))
}

fun View.setVisible(toShow: Boolean) {
    visibility = if (toShow) View.VISIBLE else View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.hide() {
    visibility = View.INVISIBLE
}

fun View.scale(scale: Float) {
    scaleX = scale
    scaleY = scale
}

fun ConstraintLayout.constrain(cs: ConstraintSet, transition: Transition, block: ((cs: ConstraintSet) -> Unit)) {
    cs.clone(this)
    if (android.os.Build.VERSION.SDK_INT >= 19) {
        TransitionManager.beginDelayedTransition(this, transition)
    }
    block.invoke(cs)
    cs.applyTo(this)
}

fun ConstraintLayout.constrain(cs: ConstraintSet, block: ((cs: ConstraintSet) -> Unit)) {
    cs.clone(this)
    if (android.os.Build.VERSION.SDK_INT >= 19) {
        val transition = AutoTransition().apply {
            duration = 500L
        }
        TransitionManager.beginDelayedTransition(this, transition)
    }
    block.invoke(cs)
    cs.applyTo(this)
}

fun View.tint(color: Int) {
    setLayerType(View.LAYER_TYPE_HARDWARE, Paint().apply {
        colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    })
}

fun ImageView.tint(color: Int) = setColorFilter(color)

fun View.applyFitSystemWindowInsets() {
    if (Build.VERSION.SDK_INT >= 20) {
        setOnApplyWindowInsetsListener { v, insets ->
            v.topPadding = insets.systemWindowInsetTop
            v.invalidate()
            insets.consumeSystemWindowInsets()
            insets
        }
    }
}

fun Drawable.tinted(color: Int): Drawable {
    mutate().setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    return this
}

@SuppressLint("MissingPermission")
fun View.vibrate(ms: Int = 20) {
    val vibrator = (context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator)
    if (Build.VERSION.SDK_INT >= 26) {
        vibrator.vibrate(VibrationEffect.createOneShot(ms.toLong(), 250))
    } else {
        @Suppress("DEPRECATION")
        vibrator.vibrate(ms.toLong())
    }
}

fun View.defaultSize(measureSpec: Int, size: Int): Int {
    val specMode = View.MeasureSpec.getMode(measureSpec)
    val specSize = View.MeasureSpec.getSize(measureSpec)

    return when (specMode) {
        View.MeasureSpec.AT_MOST -> size
        View.MeasureSpec.EXACTLY -> specSize
        else -> size
    }
}

fun View.defaultSize(measureSpec: Int, size: Float): Int = defaultSize(measureSpec, size.toInt())