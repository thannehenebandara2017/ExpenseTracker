package com.inex.expensetracker.views.addnewtransaction.selectionlist

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inex.expensetracker.R
import com.inex.expensetracker.model.SelectionModel
import com.inex.expensetracker.utils.inflate
import kotlinx.android.synthetic.main.list_item_category.view.*

class SelectionListAdapter(
    val onClickItem: (item: SelectionModel) -> Unit,
    val onLongPressedItem: (item: SelectionModel) -> Unit
) : RecyclerView.Adapter<SelectionListAdapter.ItemViewHolder>() {
    private var list: ArrayList<SelectionModel> = ArrayList()

    fun setDataSet(_list: ArrayList<SelectionModel>) {
        list.clear()
        list = _list
        notifyDataSetChanged()
    }

    inner class ItemViewHolder(parent: ViewGroup) :
        RecyclerView.ViewHolder(parent.inflate(R.layout.list_item_category)) {
        fun bind(item: SelectionModel) = with(itemView) {
            txt_transaction_name.text = item.name
            itemView.setOnClickListener {
                onClickItem(item)
            }
            itemView.setOnLongClickListener {
                onLongPressedItem(item)
                return@setOnLongClickListener true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ItemViewHolder(parent)

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) =
        holder.bind(list[position])
}