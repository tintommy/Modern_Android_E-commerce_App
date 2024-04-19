package com.example.appbanhangonline.adapters

import android.graphics.Paint
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appbanhangonline.data.Product
import com.example.appbanhangonline.databinding.BestDealsRvItemBinding
import com.example.appbanhangonline.databinding.SpecialRvItemBinding

class BestDealAdapter() : RecyclerView.Adapter<BestDealViewHolder>() {
    private val callback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestDealViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = BestDealsRvItemBinding.inflate(inflater, parent, false)
        return BestDealViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BestDealViewHolder, position: Int) {
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

class BestDealViewHolder(private val binding: BestDealsRvItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(product: Product) {
        Glide
            .with(itemView)
            .load(product.images[0])
            .into(binding.imgBestDeal);

        val remainingPricePercentage = 1f - (product.offerPercentage?.div(100))!!
        val priceAfterOffer = remainingPricePercentage * product.price
        binding.tvNewPrice.text = "$"+String.format("%.2f", priceAfterOffer)


        binding.tvOldPrice.text = "$"+product.price.toString()
        binding.tvOldPrice.paintFlags= Paint.STRIKE_THRU_TEXT_FLAG
        binding.tvBestDealName.text = product.name


    }

}
