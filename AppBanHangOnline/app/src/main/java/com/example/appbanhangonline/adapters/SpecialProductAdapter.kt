package com.example.appbanhangonline.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appbanhangonline.R
import com.example.appbanhangonline.data.Product
import com.example.appbanhangonline.databinding.SpecialRvItemBinding

class SpecialProductAdapter() : RecyclerView.Adapter<SpecialProductViewHolder>() {
    private val callback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SpecialRvItemBinding.inflate(inflater, parent, false)
        return SpecialProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SpecialProductViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)

        holder.itemView.setOnClickListener {
            itemClick?.onItemClick(product)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    interface OnItemClickListener {
        fun onItemClick(product: Product)
    }

    var itemClick: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClick = listener
    }

}

class SpecialProductViewHolder(private val binding: SpecialRvItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(product: Product) {
        Glide
            .with(itemView)
            .load(product.images[0])
            .into(binding.imgSpecialRvItem);

        binding.tvSpecialProductName.text = product.name
        binding.tvSpecialProductPrice.text = "$"+product.price.toString()

    }

}