package au.sjowl.app.base.recycler

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(item: Any)
    open fun bind(item: Any, payload: Bundle) = bind(item)
}
