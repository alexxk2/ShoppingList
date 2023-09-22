package com.example.shoppinglist.presentation.lists.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemLongClickListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.ListItemBinding
import com.example.shoppinglist.domain.models.ShoppingList

class ListsAdapter(
    private val context: Context,
    private val onItemClickListener: (shoppingList: ShoppingList) -> Unit,
    private val onMenuClickListener: (shoppingList: ShoppingList) -> Unit,
    private val onItemLongClickListener: (shoppingList: ShoppingList) -> Unit

) : ListAdapter<ShoppingList, ListsAdapter.ListsViewHolder>(DiffCallBack) {

    inner class ListsViewHolder(val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: ShoppingList,
            onItemClickListener: (shoppingList: ShoppingList) -> Unit,
            onMenuClickListener: (shoppingList: ShoppingList) -> Unit,
            onItemLongClickListener: (shoppingList: ShoppingList) -> Unit
        ) {
            with(binding) {
                root.setOnClickListener { onItemClickListener(item) }
                listMenuButton.setOnClickListener { onMenuClickListener(item) }
                root.setOnLongClickListener {
                    onItemLongClickListener(item)
                    true
                }

                listName.text = item.name
                listCreatedDate.text = context.getString(R.string.list_created_date, item.created)

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(inflater, parent, false)
        return ListsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListsViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(
            item = item,
            onItemClickListener = onItemClickListener,
            onMenuClickListener = onMenuClickListener,
            onItemLongClickListener = onItemLongClickListener
        )
    }


    companion object {
        val DiffCallBack = object : DiffUtil.ItemCallback<ShoppingList>() {

            override fun areItemsTheSame(oldItem: ShoppingList, newItem: ShoppingList): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: ShoppingList, newItem: ShoppingList): Boolean {
                return oldItem == newItem
            }
        }
    }
}