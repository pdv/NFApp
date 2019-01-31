package io.github.pdv.nfapp

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext

interface Bindable<in T> {
    fun bind(item: T)
}

interface Cell<in T>: AnkoComponent<ViewGroup>, Bindable<T>

class CellHolder<in T>(
    context: AnkoContext<ViewGroup>,
    val cell: Cell<T>
): RecyclerView.ViewHolder(cell.createView(context))

sealed class RowViewHolder<T>(view: View) : RecyclerView.ViewHolder(view) {
    class Header<T>(
        context: AnkoContext<ViewGroup>,
        val cell: Cell<String>
    ): RowViewHolder<T>(cell.createView(context))
    class Item<T>(
        context: AnkoContext<ViewGroup>,
        val cell: Cell<T>
    ): RowViewHolder<T>(cell.createView(context))
}

/**
 * A generic adapter that uses provided Cell constructors and a list
 * of Rows to populate a RecyclerView, with optional click listener
 */
class SectionedAdapter<T>(
    private val headerFactory: () -> Cell<String>,
    private val itemFactory: () -> Cell<T>,
    private val onItemClick: (T) -> Unit
) : RecyclerView.Adapter<RowViewHolder<T>>() {

    sealed class Row<T> {
        data class Header<T>(val title: String) : Row<T>()
        data class Item<T>(val item: T) : Row<T>()
    }

    var rows: List<Row<T>> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = rows.count()

    override fun getItemViewType(position: Int): Int = when (rows[position]) {
        is Row.Header -> 0
        is Row.Item -> 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowViewHolder<T> {
        val context = AnkoContext.create(parent.context, parent)
        return when (viewType) {
            0 -> RowViewHolder.Header(context, headerFactory())
            1 -> RowViewHolder.Item(context, itemFactory())
            else -> throw IllegalArgumentException("Unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RowViewHolder<T>, idx: Int) {
        val row = rows[idx]
        when (holder) {
            is RowViewHolder.Header -> (row as? Row.Header)?.title?.let(holder.cell::bind)
            is RowViewHolder.Item -> {
                (row as? Row.Item)?.item?.let { item ->
                    holder.cell.bind(item)
                    holder.itemView.setOnClickListener { onItemClick(item) }
                }
            }
        }
    }

}
