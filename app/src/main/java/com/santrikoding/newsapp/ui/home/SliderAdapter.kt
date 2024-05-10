package com.santrikoding.newsapp.ui.home

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.santrikoding.newsapp.R
import com.santrikoding.newsapp.data.remote.model.Sliders
import com.santrikoding.newsapp.databinding.ItemSliderBinding

class SliderAdapter : ListAdapter<Sliders, SliderAdapter.SliderViewHolder>(diffCallback) {

    class SliderViewHolder(private val binding: ItemSliderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Sliders) {
            binding.imageSlider.load(item.image) {
                crossfade(true)
                crossfade(400)
                error(R.drawable.ic_broken_image)
            }
        }
    }


    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Sliders>() {
            override fun areItemsTheSame(oldItem: Sliders, newItem: Sliders): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Sliders, newItem: Sliders): Boolean =
                oldItem == newItem

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder =
        SliderViewHolder(
            ItemSliderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.bind(getItem(position))
        Log.d("TAG", "Binding slider at position $position")

    }
}