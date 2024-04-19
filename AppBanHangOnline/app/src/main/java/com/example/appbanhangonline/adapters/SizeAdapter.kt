package com.example.appbanhangonline.adapters

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.appbanhangonline.databinding.SizeRvItemBinding

class SizeAdapter() : RecyclerView.Adapter<SizeAdapter.SizeAdapterViewHolder>() {

    private var selectedPosition: Int = -1

    private val callback = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, callback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizeAdapterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SizeRvItemBinding.inflate(inflater, parent, false)
        return SizeAdapterViewHolder(binding)
    }


    override fun onBindViewHolder(holder: SizeAdapterViewHolder, position: Int) {
        val size = differ.currentList[position]
        holder.bind(size, position)
        holder.itemView.setOnClickListener {
            if (selectedPosition >= 0)
                notifyItemChanged(selectedPosition)
            selectedPosition = holder.adapterPosition
            notifyItemChanged(selectedPosition)

            itemClick?.onItemClick(size)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    interface OnItemClickListener {
        fun onItemClick(size: String)
    }

    var itemClick: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClick = listener
    }


    inner class SizeAdapterViewHolder(private val binding: SizeRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(size: String, position: Int) {

            binding.tvSize.text = size
            if (position == selectedPosition) {
                binding.apply {
                    imageShadow.visibility = View.VISIBLE

                }
            } else {
                binding.apply {
                    imageShadow.visibility = View.INVISIBLE

                }
            }
        }
    }

}