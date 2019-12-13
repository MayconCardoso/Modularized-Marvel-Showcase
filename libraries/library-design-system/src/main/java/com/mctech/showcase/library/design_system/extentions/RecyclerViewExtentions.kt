package com.mctech.showcase.library.design_system.extentions

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Encapsulate the creation of recycler views in order to reduce all the necessary boilerplate
 * when creating a list.
 */
fun <T, VDB : ViewDataBinding> createDefaultRecyclerView(
        recyclerView : RecyclerView,
        items: List<T> = listOf(),
        layoutOrientation: Int = RecyclerView.VERTICAL,

        // Prepare view binding
        viewBindingCreator : (parent : ViewGroup, inflater : LayoutInflater) -> VDB,

        // Delegate to bind the item on  the view holder.
        prepareHolder: (item : T, viewBinding : VDB) -> Unit
) {
    val context = recyclerView.context

    recyclerView.setHasFixedSize(true)
    recyclerView.itemAnimator = DefaultItemAnimator()

    recyclerView.layoutManager = LinearLayoutManager(context).apply {
        orientation = layoutOrientation
    }

    recyclerView.adapter = object : BaseRecyclerAdapter<T, VDB, BaseRecyclerAdapterHolder<T, VDB>>(context, items.toMutableList()) {
        override fun prepareViewHolder(parent: ViewGroup) = object : BaseRecyclerAdapterHolder<T, VDB>(
            viewBindingCreator.invoke(parent, LayoutInflater.from(context))
        ){
            override fun attach(position: Int, item: T, binding: VDB) {
                prepareHolder.invoke(item, binding)
            }
        }
    }
}


/**
 * Encapsulate the list update computation
 */
fun <T> refreshItems(recyclerView: RecyclerView, newItems : List<T>, callback : DiffUtil.ItemCallback<T>) {
    recyclerView.adapter?.let {
        val oldItems = (it as BaseRecyclerAdapter<T, *, *>).items
        val result = DiffUtil.calculateDiff(object : DiffUtil.Callback(){

            override fun getOldListSize() = oldItems.size
            override fun getNewListSize() = newItems.size

            override fun areItemsTheSame(old: Int, new: Int) = callback.areItemsTheSame(
                oldItems[old],
                newItems[new]
            )

            override fun areContentsTheSame(old: Int, new: Int) = callback.areContentsTheSame(
                oldItems[old],
                newItems[new]
            )
        })

        oldItems.clear()
        oldItems.addAll(newItems)

        result.dispatchUpdatesTo(it)
    }
}


/**
 * Default adapter to create lists without boilerplate.
 */
private abstract class BaseRecyclerAdapter<T, VDB : ViewDataBinding, VH : BaseRecyclerAdapterHolder<T, VDB>>(
    val context: Context,
    val items: MutableList<T>
) : RecyclerView.Adapter<VH>() {
    abstract fun prepareViewHolder(parent: ViewGroup): VH

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: VH, position: Int){
        holder.bind(position, items[position])
    }

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : VH {
        return prepareViewHolder(parent)
    }
}

/**
 * Default view holder to create lists without boilerplate
 */
private abstract class BaseRecyclerAdapterHolder<T, VDB : ViewDataBinding>(private val binding: VDB) : RecyclerView.ViewHolder(binding.root) {
    abstract fun attach(position: Int, item: T, binding: VDB)

    fun bind(position: Int, item: T) {
        attach(position, item, binding)
        binding.executePendingBindings()
    }
}

/**
 * Handle the pagination when the list is almost in the and.
 */
class LoadNextPageScrollMonitor(private val loadNextPageHandler : () -> Unit) : RecyclerView.OnScrollListener() {
    private var lastItemVisiblePositionOnList = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val layoutManager           = recyclerView.layoutManager as LinearLayoutManager
        val lastItemVisiblePosition = layoutManager.findLastVisibleItemPosition()

        // It is at last but one.
        if(isScrollingDown(lastItemVisiblePosition) && recyclerView.shouldLoadMoreItems()){
            loadNextPageHandler.invoke()
        }

        lastItemVisiblePositionOnList = lastItemVisiblePosition
    }

    private fun isScrollingDown(lastItemVisiblePosition: Int) = lastItemVisiblePosition > lastItemVisiblePositionOnList

    private fun RecyclerView.shouldLoadMoreItems() : Boolean{
        val layoutManager                        = layoutManager as LinearLayoutManager

        val totalItemCount                       = layoutManager.itemCount
        val lastCompletelyVisibleItemPosition    = layoutManager.findLastCompletelyVisibleItemPosition()

        return lastCompletelyVisibleItemPosition == totalItemCount - 3
    }
}

