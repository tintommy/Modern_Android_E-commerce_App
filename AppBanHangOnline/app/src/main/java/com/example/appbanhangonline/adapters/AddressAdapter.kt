package com.example.appbanhangonline.adapters

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.appbanhangonline.R
import com.example.appbanhangonline.data.Address
import com.example.appbanhangonline.data.Product
import com.example.appbanhangonline.databinding.AddressRvItemBinding
import com.example.appbanhangonline.databinding.BestDealsRvItemBinding

class AddressAdapter() : RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {
    var selectedAddress = -1

    private val callback = object : DiffUtil.ItemCallback<Address>() {
        override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem.addressTittle == newItem.addressTittle && oldItem.fullName == newItem.fullName
        }

        override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AddressRvItemBinding.inflate(inflater, parent, false)
        return AddressViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val address = differ.currentList[position]
        holder.bind(address, selectedAddress == position)
        holder.binding.buttonAddress.setOnClickListener {
            if (selectedAddress >= 0) {
                notifyItemChanged(selectedAddress)
            }
            selectedAddress = holder.adapterPosition
            notifyItemChanged(selectedAddress)

        }

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

    inner class AddressViewHolder(val binding: AddressRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(address: Address, isSelected: Boolean) {
            binding.buttonAddress.text = address.addressTittle
            itemClick!!.onItemClick(address)
            if (isSelected) {
                binding.buttonAddress.background =
                    ColorDrawable(itemView.context.resources.getColor(R.color.g_blue))
                binding.buttonAddress.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
            } else {
                binding.buttonAddress.background =
                    ColorDrawable(itemView.context.resources.getColor(R.color.g_white))
                binding.buttonAddress.setTextColor(ContextCompat.getColor(itemView.context, R.color.g_gray500))
            }


        }

    }


}