package com.example.appbanhangonline.adapters

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appbanhangonline.R
import com.example.appbanhangonline.data.Address
import com.example.appbanhangonline.data.CartProduct
import com.example.appbanhangonline.databinding.AddressRvItemBinding
import com.example.appbanhangonline.databinding.BillingProductsRvItemBinding
import com.example.appbanhangonline.helper.getProductPrice

class BillingProductAdapter() :
    RecyclerView.Adapter<BillingProductAdapter.BillingProductViewHolder>() {

    private val callback = object : DiffUtil.ItemCallback<CartProduct>() {
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem.product == newItem.product
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillingProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = BillingProductsRvItemBinding.inflate(inflater, parent, false)
        return BillingProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BillingProductViewHolder, position: Int) {
        val billingProduct = differ.currentList[position]
        holder.bind(billingProduct)

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    interface OnItemClickListener {
        fun onItemClick(address: Address)
    }

    var itemClick: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClick = listener
    }

    inner class BillingProductViewHolder(val binding: BillingProductsRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(billingProduct: CartProduct) {
            binding.apply {
                Glide.with(itemView).load(billingProduct.product.images[0]).into(imageCartProduct)
                tvProductCartName.text = billingProduct.product.name
                tvBillingProductQuantity.text = billingProduct.quantity.toString()
                val priceAfterPecentage =
                    billingProduct.product.offerPercentage.getProductPrice(billingProduct.product.price)
                binding.tvProductCartPrice.text = "$ ${String.format("%.2f", priceAfterPecentage)}"

                binding.imageCartProductColor.setImageDrawable(
                    ColorDrawable(
                        billingProduct.selectedColor ?: Color.TRANSPARENT
                    )
                )
            }
        }

    }

}