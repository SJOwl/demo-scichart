@file:Suppress("unused")

package au.sjowl.app.base.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.Typeface
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Parcelable
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.Html
import android.text.InputType
import android.util.SparseArray
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.transition.AutoTransition
import androidx.transition.Transition
import androidx.transition.TransitionManager
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import au.sjowl.app.base.loge
import kotlin.system.measureNanoTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.jetbrains.anko.dip
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.sp
import org.jetbrains.anko.textAppearance
import org.jetbrains.anko.topPadding

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

fun Context.colorCompat(id: Int): Int = ContextCompat.getColor(this, id)
fun Context.drawableCompat(id: Int): Drawable = ContextCompat.getDrawable(this, id)
    ?: error("Drawable not found")

fun Context.tintedDrawable(drawableId: Int, colorId: Int): Drawable {
    return drawableCompat(drawableId).tinted(colorCompat(colorId))
}

fun Context.dipf(dips: Float): Float = dip(dips) * 1f
fun Context.dipf(dips: Int): Float = dip(dips) * 1f
fun Context.spf(dips: Float): Float = sp(dips) * 1f
fun Context.spf(dips: Int): Float = sp(dips) * 1f

fun View.dipf(dips: Float): Float = dip(dips) * 1f
fun View.dipf(dips: Int): Float = dip(dips) * 1f
fun View.spf(dips: Float): Float = sp(dips) * 1f
fun View.spf(dips: Int): Float = sp(dips) * 1f

fun View.getTintedDrawable(id: Int, colorId: Int): Drawable? {
    return context.drawableCompat(id).apply {
        mutate().setColorFilter(context.colorCompat(colorId), PorterDuff.Mode.SRC_ATOP)
    }
}

fun measureMs(msg: String, block: (() -> Unit)) {
    val t = measureNanoTime {
        block.invoke()
    }
    println("$msg %.3fms".format(t / 1000000f))
}

fun View.setVisibleOrGone(toShow: Boolean) {
    visibility = if (toShow) View.VISIBLE else View.GONE
}

fun View.setVisible(toShow: Boolean) {
    visibility = if (toShow) View.VISIBLE else View.INVISIBLE
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

fun ConstraintLayout.constrain(
    cs: ConstraintSet,
    transition: Transition?,
    block: ((cs: ConstraintSet) -> Unit)
) {
    cs.clone(this)
    if (transition != null) {
        TransitionManager.beginDelayedTransition(this, transition)
    }
    block(cs)
    cs.applyTo(this)
}

fun ConstraintLayout.constrain(cs: ConstraintSet, block: ((cs: ConstraintSet) -> Unit)) {
    cs.clone(this)
    val transition = AutoTransition().apply {
        duration = 200
    }
    TransitionManager.beginDelayedTransition(this, transition)
    block.invoke(cs)
    cs.applyTo(this)
}

fun ViewGroup.applyTransitions() {
    val transition = AutoTransition().apply {
        duration = 150L
    }
    TransitionManager.beginDelayedTransition(this, transition)
}

fun View.tint(color: Int) {
    setLayerType(View.LAYER_TYPE_HARDWARE, Paint().apply {
        colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    })
}

fun View.tintResource(@ColorRes res: Int) {
    tint(context.colorCompat(res))
}

fun ImageView.tint(color: Int): Unit = setColorFilter(color)

fun View.applyFitSystemWindowInsets() {
    setOnApplyWindowInsetsListener { v, insets ->
        v.topPadding = insets.systemWindowInsetTop
        v.invalidate()
        insets.consumeSystemWindowInsets()
        insets
    }
}

fun Drawable.tinted(color: Int): Drawable {
    mutate().setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    return this
}

@SuppressLint("MissingPermission")
fun Context.vibrate(ms: Int = 20) {
    val vibrator = (getSystemService(Context.VIBRATOR_SERVICE) as Vibrator)
    if (Build.VERSION.SDK_INT >= 26) {
        vibrator.vibrate(VibrationEffect.createOneShot(ms.toLong(), 250))
    } else {
        @Suppress("DEPRECATION")
        vibrator.vibrate(ms.toLong())
    }
}

@SuppressLint("MissingPermission")
fun View.vibrate(ms: Int = 20): Unit = context.vibrate(ms)

fun defaultSize(measureSpec: Int, size: Int): Int {
    val specMode = View.MeasureSpec.getMode(measureSpec)
    val specSize = View.MeasureSpec.getSize(measureSpec)

    return when (specMode) {
        View.MeasureSpec.AT_MOST -> size
        View.MeasureSpec.EXACTLY -> specSize
        else -> size
    }
}

fun View.defaultSize(measureSpec: Int, size: Float): Int = defaultSize(measureSpec, size.toInt())

fun ViewGroup.inflate(layoutId: Int) {
    context.layoutInflater.inflate(layoutId, this)
}

fun View.setSelectableBackground() {
    val outValue = TypedValue()
    context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
    setBackgroundResource(outValue.resourceId)
}

fun View.setSelectableBackgroundBorderless() {
    val outValue = TypedValue()
    context.theme.resolveAttribute(
        android.R.attr.selectableItemBackgroundBorderless,
        outValue,
        true
    )
    setBackgroundResource(outValue.resourceId)
}

fun TextView.setFont(font: String) {
    typeface = Typeface.create(font, Typeface.NORMAL)
}

fun View.setOnClick(onClick: (() -> Unit)) {
    setOnClickListener { onClick.invoke() }
}

fun TextView.setStyle(styleResId: Int) {
    textAppearance = styleResId
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun Context.showKeyboardFrom(view: View?) {
    if (view == null) return
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(view, InputMethodManager.SHOW_FORCED)
}

fun View.showKeyboard() {
    context.showKeyboardFrom(this)
}

fun EditText.setReadOnly(value: Boolean, inputType: Int = InputType.TYPE_NULL) {
    isFocusable = !value
    isFocusableInTouchMode = !value
    this.inputType = inputType
}

fun Context.loadDrawable(drawableId: Int, colorId: Int): Drawable =
    drawableCompat(drawableId).apply {
        setBounds(0, 0, intrinsicWidth, intrinsicHeight)
    }.tinted(colorCompat(colorId))

fun ImageView.setIcon(drawableId: Int, colorId: Int) =
    setImageDrawable(context.loadDrawable(drawableId, colorId))

fun View.setPadding(horizontal: Int, vertical: Int) {
    setPadding(horizontal, vertical, horizontal, vertical)
}

fun ImageView.tintId(colorId: Int) {
    tint(context.colorCompat(colorId))
}

fun EditText.asPasswordInput(isPassword: Boolean = true): EditText {
    if (isPassword) {
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD
        typeface = Typeface.DEFAULT
    }
    return this
}

fun EditText.asEmailInput(): EditText {
    inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
    return this
}

fun ViewGroup.saveChildViewStates(): SparseArray<Parcelable> {
    val childViewStates = SparseArray<Parcelable>()
    children.forEach { child -> child.saveHierarchyState(childViewStates) }
    return childViewStates
}

fun ViewGroup.restoreChildViewStates(childViewStates: SparseArray<Parcelable>) {
    children.forEach { child -> child.restoreHierarchyState(childViewStates) }
}

fun Drawable.setBounds() = setBounds(0, 0, intrinsicWidth, intrinsicHeight)

fun View.alphaOrGone(alpha: Float) {
    this.alpha = alpha
    setVisibleOrGone(alpha > 0f)
}

fun ConstraintSet.setScale(@IdRes viewId: Int, scale: Float) {
    setScaleX(viewId, scale)
    setScaleY(viewId, scale)
}

/**
 * @param flags - flags from {@link #Html}
 * */
fun TextView.setHtml(source: String, flags: Int = 0) {
    val html = if (Build.VERSION.SDK_INT >= 24) {
        Html.fromHtml(source, flags)
    } else {
        Html.fromHtml(source)
    }
    setText(html, TextView.BufferType.SPANNABLE)
}

fun ImageView.animateImageResource(@DrawableRes animatedDrawable: Int) {
    setImageResource(animatedDrawable)
    (drawable as? AnimatedVectorDrawable)?.start()
    (drawable as? AnimatedVectorDrawableCompat)?.start()
}

fun View.ui(block: suspend CoroutineScope.() -> Unit) {
    GlobalScope.launch(Dispatchers.Main) { block() }
}

fun View.bg(block: suspend CoroutineScope.() -> Unit) {
    GlobalScope.launch(Dispatchers.IO) { block() }
}

fun ui(block: suspend CoroutineScope.() -> Unit): Job {
    return GlobalScope.launch(Dispatchers.Main) { block() }
}

fun bg(block: suspend CoroutineScope.() -> Unit): Job {
    return GlobalScope.launch(Dispatchers.IO) { block() }
}

fun trycatch(showError: Boolean = true, block: (() -> Unit)) {
    try {
        block.invoke()
    } catch (e: Exception) {
        if (showError) loge(e)
    }
}

suspend fun trycatchSuspend(showError: Boolean = true, block: suspend (() -> Unit)) {
    try {
        block.invoke()
    } catch (e: Exception) {
        if (showError) loge(e)
    }
}
