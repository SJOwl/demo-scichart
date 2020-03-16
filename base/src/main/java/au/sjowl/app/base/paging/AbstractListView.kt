package au.sjowl.app.base.paging

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import au.sjowl.app.base.recycler.BaseAdapter

abstract class AbstractListView(
    val adapter: BaseAdapter,
    val context: Context
) : IListView {

    abstract val recyclerView: RecyclerView?

    protected val contentItems = mutableListOf<Any>()

    abstract fun progressHeader(): Any

    abstract fun progressFooter(): Any

    override fun scrollToTop() {
        (recyclerView!!.layoutManager as LinearLayoutManager).scrollToPosition(0)
    }

    override fun newListLoading(isLoading: Boolean) {
        val items = mutableListOf<Any>().apply {
            addAll(contentItems)
        }
        if (isLoading) {
            val header = progressHeader()
            items.add(0, header)
            adapter.items = items
            scrollToTop()
        } else {
            adapter.items = items
        }
    }

    override fun nextPageLoading(isLoading: Boolean) {
        val items = mutableListOf<Any>().apply {
            addAll(contentItems)
        }
        if (isLoading) {
            val footer = progressFooter()
            items.add(footer)
        }
        adapter.items = items
    }

    override fun clear() {
        contentItems.clear()
        adapter.items = emptyList()
    }

    override fun addPage(list: List<Any>) {
        contentItems.addAll(list)
        adapter.items = contentItems

        showContentOrEmpty()
    }

    override fun setList(list: List<Any>) {
        contentItems.clear()
        contentItems.addAll(list)
        adapter.items = contentItems

        showContentOrEmpty()
    }

    override fun delete(position: Int) {
        contentItems.removeAt(position)
        showContentOrEmpty()
        adapter.items = contentItems
    }

    override fun insert(item: Any, position: Int) {
        contentItems.add(position, item)
        showContentOrEmpty()
        adapter.items = contentItems
    }

    override fun showContentOrEmpty() {
        if (contentItems.isEmpty()) {
            showEmptyContent()
        } else {
            content()
        }
    }
}
