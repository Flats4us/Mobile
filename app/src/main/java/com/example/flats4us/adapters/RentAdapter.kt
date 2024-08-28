package com.example.flats4us.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.flats4us.R
import com.example.flats4us.URL
import com.example.flats4us.data.Rent
import com.example.flats4us.databinding.RentRowBinding

class RentAdapter(
    private var rents: List<Rent>,
    private val onUserClick: (Rent) -> Unit
) : RecyclerView.Adapter<RentAdapter.MyViewHolder>() {

    inner class MyViewHolder(binding: RentRowBinding) :
        RecyclerView.ViewHolder(binding.root){
        val address = binding.address
        val image = binding.imageView

        init {
            binding.root.setOnClickListener { onUserClick(rents[bindingAdapterPosition]) }
        }
    }

    fun setRentList(rents: List<Rent>){
        this.rents = rents
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            binding = RentRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return rents.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if(rents[position].propertyImages.isNotEmpty()) {
            holder.image.load(URL + "/" + rents[position].propertyImages[0].path){
                error(R.drawable.baseline_broken_image_24)
            }
        }
        holder.address.text = rents[position].propertyAddress
        holder.itemView.setOnClickListener {
            onUserClick(rents[position])
        }
    }

}