package com.example.flats4us21.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flats4us21.data.UserOpinion
import com.example.flats4us21.databinding.ItemUserOpinionBinding

class UserOpinionAdapter (
    private var opinions : List<UserOpinion>,
) : RecyclerView.Adapter<UserOpinionAdapter.MyViewHolder>() {

    inner class MyViewHolder(binding: ItemUserOpinionBinding) :
        RecyclerView.ViewHolder(binding.root){
        val image = binding.userPhoto
        val nameAndSurname = binding.nameAndSurname
        val rating = binding.ratingBar
        val numberRating = binding.numberRating
        val layoutHelpful = binding.layoutHelpful
        val layoutCooperative = binding.layoutCooperative
        val layoutTidy = binding.layoutTidy
        val layoutFriendly = binding.layoutFriendly
        val layoutRespectingPrivacy = binding.layoutRespectingPrivacy
        val layoutCommunicative = binding.layoutCommunicative
        val layoutUnfair = binding.layoutUnfair
        val layoutLackOfHygiene = binding.layoutLackOfHygiene
        val layoutUntidy = binding.layoutUntidy
        val layoutConflicting = binding.layoutConflicting
        val layoutNoisy = binding.layoutNoisy
        val layoutNotFollowingTheArrangements = binding.layoutNotFollowingTheArrangements
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
        holder.rating.rating = opinions[position].rating.toFloat()
        holder.numberRating.text = opinions[position].rating.toFloat().toString()
        if(opinions[position].helpful){
            holder.layoutHelpful.visibility = View.VISIBLE
        } else {
            holder.layoutHelpful.visibility = View.GONE
        }
        if(opinions[position].cooperative){
            holder.layoutCooperative.visibility = View.VISIBLE
        } else {
            holder.layoutCooperative.visibility = View.GONE
        }
        if(opinions[position].tidy){
            holder.layoutTidy.visibility = View.VISIBLE
        } else {
            holder.layoutTidy.visibility = View.GONE
        }
        if(opinions[position].friendly){
            holder.layoutFriendly.visibility = View.VISIBLE
        } else {
            holder.layoutFriendly.visibility = View.GONE
        }
        if(opinions[position].respectingPrivacy){
            holder.layoutRespectingPrivacy.visibility = View.VISIBLE
        } else {
            holder.layoutRespectingPrivacy.visibility = View.GONE
        }
        if(opinions[position].communicative){
            holder.layoutCommunicative.visibility = View.VISIBLE
        } else {
            holder.layoutCommunicative.visibility = View.GONE
        }
        if(opinions[position].unfair){
            holder.layoutUnfair.visibility = View.VISIBLE
        } else {
            holder.layoutUnfair.visibility = View.GONE
        }
        if(opinions[position].lackOfHygiene){
            holder.layoutLackOfHygiene.visibility = View.VISIBLE
        } else {
            holder.layoutLackOfHygiene.visibility = View.GONE
        }
        if(opinions[position].untidy){
            holder.layoutUntidy.visibility = View.VISIBLE
        } else {
            holder.layoutUntidy.visibility = View.GONE
        }
        if(opinions[position].conflicting){
            holder.layoutConflicting.visibility = View.VISIBLE
        } else {
            holder.layoutConflicting.visibility = View.GONE
        }
        if(opinions[position].noisy){
            holder.layoutNoisy.visibility = View.VISIBLE
        } else {
            holder.layoutNoisy.visibility = View.GONE
        }
        if(opinions[position].notFollowingTheArrangements){
            holder.layoutNotFollowingTheArrangements.visibility = View.VISIBLE
        } else {
            holder.layoutNotFollowingTheArrangements.visibility = View.GONE
        }
        holder.description.text = opinions[position].description

    }
}