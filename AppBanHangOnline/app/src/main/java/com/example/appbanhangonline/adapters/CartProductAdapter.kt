package com.example.appbanhangonline.adapters

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appbanhangonline.data.CartProduct
import com.example.appbanhangonline.data.Product
import com.example.appbanhangonline.databinding.CartProductLayoutBinding
import com.example.appbanhangonline.databinding.SpecialRvItemBinding
import com.example.appbanhangonline.helper.getProductPrice
import org.checkerframework.checker.units.qual.C

class CartProductAdapter : RecyclerView.Adapter<CartProductViewHolder>() {
    private val callback = object : DiffUtil.ItemCallback<CartProduct>() {
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem.product.id == newItem.product.id
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CartProductLayoutBinding.inflate(inflater, parent, false)
        return CartProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartProductViewHolder, position: Int) {
        val cartProduct = differ.currentList[position]
        holder.bind(cartProduct)

        holder.itemView.setOnClickListener {
            click?.onItemClick(cartProduct)
        }

        holder.binding.imagePlus.setOnClickListener {
            click?.onPlusClick(cartProduct)
        }
        holder.binding.imageMinus.setOnClickListener {
            click?.onMinusClick(cartProduct)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    interface OnItemClickListener {
        fun onItemClick(cartProduct: CartProduct)
        fun onPlusClick(cartProduct: CartProduct)
        fun onMinusClick(cartProduct: CartProduct)

    }

    var click: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        click = listener
    }

}

class CartProductViewHolder(val binding: CartProductLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(cartProduct: CartProduct) {
        Glide.with(itemView).load(cartProduct.product.images[0]).into(binding.imageCartProduct);

        binding.tvProductCartName.text = cartProduct.product.name
        binding.tvQuantity.text = cartProduct.quantity.toString()
        binding.tvProductCartPrice.text = "$" + cartProduct.product.price.toString()
        val priceAfterPecentage =
            cartProduct.product.offerPercentage.getProductPrice(cartProduct.product.price)
        binding.tvProductCartPrice.text = "$ ${String.format("%.2f", priceAfterPecentage)}"

        binding.imageCartProductColor.setImageDrawable(
            ColorDrawable(
                cartProduct.selectedColor ?: Color.TRANSPARENT
            )
        )

    }

}