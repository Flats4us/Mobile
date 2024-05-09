package com.example.flats4us21.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.flats4us21.R
import com.example.flats4us21.URL
import com.example.flats4us21.data.dto.Property
import com.example.flats4us21.databinding.OwnerPropertyRowBinding

private const val TAG = "ImageSliderAdapter"
class OwnerPropertiesAdapter(
    private var properties : List<Property>,
    private val onUserClick : (Property) -> Unit
)  : RecyclerView.Adapter<OwnerPropertiesAdapter.MyViewHolder>() {
    inner class MyViewHolder (binding: OwnerPropertyRowBinding) :
        RecyclerView.ViewHolder(binding.root){
        val image = binding.imageView
        val city = binding.city
        val street = binding.street
        val size = binding.size
        val room = binding.room
        val isVerified = binding.verified

        init {
            binding.root.setOnClickListener { onUserClick(properties[bindingAdapterPosition]) }
        }
    }

    fun setPropertyList(properties: List<Property>){
        this.properties = properties
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            binding = OwnerPropertyRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return properties.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if(properties[position].images.isNotEmpty()) {
            val url = URL + "/" + properties[position].images[0].path
            Log.i(TAG, url)
            holder.image.load(url) {
                error(R.drawable.baseline_broken_image_24)
            }
        }
        if(properties[position].verificationStatus == 0) {
            holder.isVerified.isVisible = true
        } else {
            holder.isVerified.isVisible = false
        }
        holder.city.text = properties[position].city
        holder.street.text = properties[position].street
        holder.size.text = properties[position].area.toString()
        holder.room.text = properties[position].numberOfRooms.toString()
        holder.itemView.setOnClickListener {
            onUserClick(properties[position])
        }
    }
}