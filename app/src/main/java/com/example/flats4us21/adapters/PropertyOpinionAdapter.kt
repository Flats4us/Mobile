package com.example.flats4us21.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flats4us21.data.PropertyOpinion
import com.example.flats4us21.databinding.ItemPropertyOpinionBinding

class PropertyOpinionAdapter(
    private var opinions: List<PropertyOpinion>
) : RecyclerView.Adapter<PropertyOpinionAdapter.MyViewHolder>() {

    inner class MyViewHolder(binding: ItemPropertyOpinionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val image = binding.userPhoto
        val nameAndSurname = binding.nameAndSurname
        val rating = binding.ratingBar
        val numberRating = binding.numberRating
        val layoutService = binding.layoutService
        val layoutLocation = binding.layoutLocation
        val layoutEquipment = binding.layoutEquipment
        val layoutQualityForMoney = binding.layoutQualityForMoney
        val description = binding.opinionDescription
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            binding = ItemPropertyOpinionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return opinions.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.rating.rating = opinions[position].rating.toFloat()
        holder.numberRating.text = opinions[position].rating.toFloat().toString()
        if (opinions[position].service != 0) {
            holder.layoutService.visibility = View.VISIBLE
        } else {
            holder.layoutService.visibility = View.GONE
        }
        if (opinions[position].location != 0) {
            holder.layoutLocation.visibility = View.VISIBLE
        } else {
            holder.layoutLocation.visibility = View.GONE
        }
        if (opinions[position].equipment != 0) {
            holder.layoutEquipment.visibility = View.VISIBLE
        } else {
            holder.layoutEquipment.visibility = View.GONE
        }
        if (opinions[position].qualityForMoney != 0) {
            holder.layoutQualityForMoney.visibility = View.VISIBLE
        } else {
            holder.layoutQualityForMoney.visibility = View.GONE
        }

        holder.description.text = opinions[position].description

    }
}