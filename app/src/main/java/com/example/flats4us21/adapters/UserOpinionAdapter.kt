package com.example.flats4us21.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.flats4us21.R
import com.example.flats4us21.URL
import com.example.flats4us21.data.UserOpinion
import com.example.flats4us21.databinding.ItemUserOpinionBinding

class UserOpinionAdapter (
    private var opinions : List<UserOpinion>,
) : RecyclerView.Adapter<UserOpinionAdapter.MyViewHolder>() {

    inner class MyViewHolder(binding: ItemUserOpinionBinding) :
        RecyclerView.ViewHolder(binding.root){
        val image = binding.userPhoto
        val nameAndSurname = binding.nameAndSurname
        val date = binding.date
        val rating = binding.ratingBar
        val numberRating = binding.numberRating
        val description = binding.opinionDescription
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            binding = ItemUserOpinionBinding.inflate(
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
        val imageUrl = opinions[position].sourceUserProfilePicture?.path?.let { "$URL/$it" } ?: ""

        holder.image.load(imageUrl) {
            error(R.drawable.baseline_broken_image_24)
        }
        holder.nameAndSurname.text = opinions[position].sourceUserName
        holder.date.text = opinions[position].date.split("T")[0]
        holder.rating.rating = (opinions[position].rating.toFloat() / 2)
        holder.numberRating.text = opinions[position].rating.toFloat().toString()

        holder.description.text = opinions[position].description

    }
}