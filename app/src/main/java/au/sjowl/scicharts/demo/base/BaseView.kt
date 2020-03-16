package au.sjowl.scicharts.demo.base

import au.sjowl.app.base.android.coroutines.CoroutineView

interface BaseView : CoroutineView {
    fun progress(inProgress: Boolean = true) {}
    fun reset() {}
}
