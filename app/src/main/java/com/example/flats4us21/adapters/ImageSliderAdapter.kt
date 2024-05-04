package com.example.flats4us21.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.flats4us21.R
import com.example.flats4us21.URL
import com.example.flats4us21.data.Image
import com.example.flats4us21.databinding.ItemImageSliderBinding

private const val TAG = "ImageSliderAdapter"
class ImageSliderAdapter(private val images: List<Image>) :
    RecyclerView.Adapter<ImageSliderAdapter.MyViewHolder>() {

    inner class MyViewHolder(binding: ItemImageSliderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val imageView: ImageView = binding.imageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            binding = ItemImageSliderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if(images.isNotEmpty()) {
            val url = URL + "/" + images[position].path
            Log.i(TAG, url)
            holder.imageView.load(url) {
                error(R.drawable.baseline_broken_image_24)
            }
        }
    }
}
