package com.example.appbanhangonline.adapters

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.appbanhangonline.databinding.ColorRvItemBinding

class ColorAdapter() : RecyclerView.Adapter<ColorAdapter.ColorAdapterViewHolder>() {

    private var selectedPosition: Int = -1

    private val callback = object : DiffUtil.ItemCallback<Int>() {
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, callback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorAdapterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ColorRvItemBinding.inflate(inflater, parent, false)
        return ColorAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ColorAdapterViewHolder, position: Int) {
        val color = differ.currentList[position]
        holder.bind(color, position)
        holder.itemView.setOnClickListener {
            if (selectedPosition >= 0)
                notifyItemChanged(selectedPosition)
            selectedPosition = holder.adapterPosition
            notifyItemChanged(selectedPosition)

            itemClick?.onItemClick(color)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    interface OnItemClickListener {
        fun onItemClick(color: Int)
    }

    var itemClick: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClick = listener
    }


    inner class ColorAdapterViewHolder(private val binding: ColorRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(color: Int, position: Int) {

            val imageDrawable = ColorDrawable(color)
            binding.imageColor.setImageDrawable(imageDrawable)

            if (position == selectedPosition) {
                binding.apply {
                    imageShadow.visibility = View.VISIBLE
                    imagePicked.visibility = View.VISIBLE
                }
            } else {
                binding.apply {
                    imageShadow.visibility = View.INVISIBLE
                    imagePicked.visibility = View.INVISIBLE
                }
            }
        }
    }
}

