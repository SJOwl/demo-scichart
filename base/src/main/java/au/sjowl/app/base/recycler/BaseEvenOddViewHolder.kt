package au.sjowl.app.base.recycler

import android.view.View

abstract class BaseEvenOddViewHolder(view: View) : BaseViewHolder(view) {
    abstract fun bindEven(item: Any)
    abstract fun bindOdd(item: Any)
}
