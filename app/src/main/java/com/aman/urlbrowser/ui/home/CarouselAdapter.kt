package com.aman.urlbrowser.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aman.urlbrowser.databinding.ItemCarouselBinding

class CarouselAdapter(private val images: List<Int>) :
    RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val binding = ItemCarouselBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CarouselViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int = images.size

    class CarouselViewHolder(private val binding: ItemCarouselBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(imageRes: Int) {
            binding.ivCarousel.setImageResource(imageRes)
        }
    }
}
