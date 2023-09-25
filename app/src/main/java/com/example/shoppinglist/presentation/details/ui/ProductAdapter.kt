package com.example.shoppinglist.presentation.details.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.databinding.ProductItemBinding
import com.example.shoppinglist.domain.models.Product

class ProductAdapter(
    private val onItemLongClickListener: (shoppingList: Product) -> Unit

) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(DiffCallBack) {

    inner class ProductViewHolder(val binding: ProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: Product,
            onItemLongClickListener: (shoppingList: Product) -> Unit
        ) {
            with(binding) {

                root.setOnLongClickListener {
                    onItemLongClickListener(item)
                    true
                }

                productName.text = item.name
                productQuantity.text = getRightEndingGoods(item.quantity.toInt())

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ProductItemBinding.inflate(inflater, parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(
            item = item,
            onItemLongClickListener = onItemLongClickListener
        )
    }


    private fun getRightEndingGoods(numberOfTracks: Int): String {
        val preLastDigit = numberOfTracks % 100 / 10

        if (preLastDigit == 1) {
            return "$numberOfTracks единиц"
        }

        return when (numberOfTracks % 10) {
            1 -> "$numberOfTracks единица"
            2 -> "$numberOfTracks единицы"
            3 -> "$numberOfTracks единицы"
            4 -> "$numberOfTracks единицы"
            else -> "$numberOfTracks единиц"
        }
    }
    companion object {
        val DiffCallBack = object : DiffUtil.ItemCallback<Product>() {

            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem == newItem
            }
        }
    }
}