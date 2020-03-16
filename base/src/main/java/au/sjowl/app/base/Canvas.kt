package au.sjowl.app.base

import android.graphics.Canvas
import android.graphics.Paint

fun Canvas.drawCompatRoundRect(left: Float, top: Float, right: Float, bottom: Float, rx: Float, ry: Float, paint: Paint) {
    if (android.os.Build.VERSION.SDK_INT >= 21) {
        drawRoundRect(left, top, right, bottom, rx, ry, paint)
    } else {
        drawRect(left, top, right, bottom, paint)
    }
}
