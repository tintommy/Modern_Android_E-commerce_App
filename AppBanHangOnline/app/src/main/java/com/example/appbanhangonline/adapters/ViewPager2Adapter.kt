package com.example.appbanhangonline.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appbanhangonline.databinding.ViewpagerImageItemBinding

class ViewPager2Adapter : RecyclerView.Adapter<ViewPager2AdapterViewHolder>() {
    private val callback = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, callback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPager2AdapterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewpagerImageItemBinding.inflate(inflater, parent, false)
        return ViewPager2AdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewPager2AdapterViewHolder, position: Int) {
        val image = differ.currentList[position]
        holder.bind(image)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}

class ViewPager2AdapterViewHolder(val binding: ViewpagerImageItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(imagePath: String) {
        Glide.with(itemView).load(imagePath).into(binding.imageProductDetails)
    }
}