package au.sjowl.app.base.recycler

abstract class BaseEvenOddRecyclerViewAdapter<D : Any, V : BaseViewHolder> :
    BaseRecyclerViewAdapter<D, V>() {

    override fun onBindViewHolder(holder: V, position: Int) {
        (holder as BaseEvenOddViewHolder)
        val item = items[position]
        if (position % 2 == 0) {
            holder.bindEven(item)
        } else {
            holder.bindOdd(item)
        }

        holder.bind(item)
    }
}
