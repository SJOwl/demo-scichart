package au.sjowl.app.base.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import au.sjowl.app.base.logd
import au.sjowl.app.base.logv
import com.hannesdorfmann.adapterdelegates4.AdapterDelegatesManager

/**
 * V - ViewHolder class
 * D - model data class
 *
 * Usage example:
 *
 * class PlacesAdapter : BaseAdapter({delegatesManager ->
 *      delegatesManager
 *          .addDelegate(PlacesAdapterDelegate())
 *  })
 */
abstract class BaseAdapter(
    initDelegates: (delegatesManager: AdapterDelegatesManager<List<Any>>) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    open val diffutilsCallback = BaseDiffutilCallback<Any>()

    open var items: List<Any> = ArrayList()
        set(value) {
            field = value
            itemsInvalidate(value)
        }

    protected val delegatesManager = AdapterDelegatesManager<List<Any>>()

    private var _items: MutableList<Any> = ArrayList()

    override fun getItemCount(): Int = _items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegatesManager.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegatesManager.onBindViewHolder(_items, position, holder)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        delegatesManager.onBindViewHolder(_items, position, holder, payloads)
    }

    override fun getItemViewType(position: Int): Int {
        logv("get item view type for ${_items[position]::class.java}")
        return delegatesManager.getItemViewType(_items, position)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        delegatesManager.onViewRecycled(holder)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        delegatesManager.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        delegatesManager.onViewDetachedFromWindow(holder)
    }

    override fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean {
        return delegatesManager.onFailedToRecycleView(holder)
    }

    fun itemsInvalidate(items: List<Any>) {
        diffutilsCallback.oldList = _items
        diffutilsCallback.newList = items
        val diffRes = DiffUtil.calculateDiff(diffutilsCallback, true)
        _items = ArrayList(items)
        diffRes.dispatchUpdatesTo(this@BaseAdapter)
    }

    fun update(item: Any, position: Int) {
        logd("update item $item at $position")
//        if (position !in _items.indices) {
//            loge("Try to update $position at ${_items.indices}")
//            IllegalAccessException("Try to update $position at ${_items.indices}").printStackTrace()
//            return
//        }
        _items[position] = item
        notifyItemChanged(position)
    }

    fun insert(item: Any, position: Int) {
        _items.add(position, item)
        notifyItemInserted(position)
    }

    fun delete(position: Int) {
        if (position !in _items.indices) return
        _items.removeAt(position)
        notifyItemRemoved(position)
    }

    init {
        initDelegates(delegatesManager)
    }
}
