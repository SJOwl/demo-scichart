package au.sjowl.app.base.recycler

import android.content.Context
import android.graphics.PointF
import android.util.DisplayMetrics
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

private const val MILLISECONDS_PER_INCH = 180f
private const val MAX_SCROLL_DURATION_MS = 350

class SmoothScrollLinearLayoutManager(
    context: Context,
    orientation: Int = RecyclerView.VERTICAL,
    reverseLayout: Boolean = false
) : LinearLayoutManager(context, orientation, reverseLayout) {

    override fun smoothScrollToPosition(
        recyclerView: RecyclerView,
        state: RecyclerView.State?,
        position: Int
    ) {
        val smoothScroller = object : TopSnappedSmoothScroller(recyclerView.context) {
            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float =
                MILLISECONDS_PER_INCH / displayMetrics.densityDpi

            override fun calculateTimeForScrolling(dx: Int): Int =
                super.calculateTimeForScrolling(dx).coerceAtMost(MAX_SCROLL_DURATION_MS)
        }
        smoothScroller.targetPosition = position
        startSmoothScroll(smoothScroller)
    }

    private open inner class TopSnappedSmoothScroller(context: Context) :
        LinearSmoothScroller(context) {

        override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
            return this@SmoothScrollLinearLayoutManager
                .computeScrollVectorForPosition(targetPosition)
        }

        override fun getVerticalSnapPreference(): Int {
            return SNAP_TO_START
        }
    }
}
