package com.example.flats4us21.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flats4us21.data.Offer
import com.example.flats4us21.databinding.PropertyRowBinding

class PropertyAdapter(
    private val offers : List<Offer>
    , private val onUserClick : (Offer) -> Unit
) : RecyclerView.Adapter<PropertyAdapter.MyViewHolder>() {

    inner class MyViewHolder(binding: PropertyRowBinding) : RecyclerView.ViewHolder(binding.root){
        val image = binding.imageView
        val city = binding.city
        val street = binding.street
        val size = binding.size
        val price = binding.price

        init {
            binding.root.setOnClickListener { onUserClick(offers[adapterPosition]) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            binding = PropertyRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return offers.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.image.setImageResource(offers[position].property.image)
        holder.city.text = offers[position].property.city
        holder.street.text = offers[position].property.street
        holder.size.text = "40"
        holder.price.text = offers[position].price
    }
}