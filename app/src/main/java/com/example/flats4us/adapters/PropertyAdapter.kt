package com.example.flats4us.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.flats4us.DataStoreManager
import com.example.flats4us.R
import com.example.flats4us.URL
import com.example.flats4us.data.Offer
import com.example.flats4us.databinding.PropertyRowBinding
import com.example.flats4us.viewmodels.OfferViewModel

class PropertyAdapter(
    private val ownersOffer : Boolean,
    private var offers : List<Offer>,
    private val onUserClick : (Offer) -> Unit
) : RecyclerView.Adapter<PropertyAdapter.MyViewHolder>() {

    private var offerViewModel: OfferViewModel? = null
    inner class MyViewHolder(binding: PropertyRowBinding) :
        RecyclerView.ViewHolder(binding.root){
        val button = binding.addButton
        val image = binding.imageView
        val promotion = binding.promotion
        val city = binding.city
        val street = binding.street
        val size = binding.size
        val price = binding.price
        val room = binding.room

        init {
            binding.root.setOnClickListener { onUserClick(offers[bindingAdapterPosition]) }
        }
    }

    fun setOfferList(offers: List<Offer>){
        this.offers = offers
    }
    fun setViewModel(viewModel: OfferViewModel) {
        offerViewModel = viewModel
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
        if(ownersOffer){
            holder.button.visibility = View.GONE
        }
        holder.button.isVisible = DataStoreManager.userRole.value?.let { it == "Student" } ?: false
        var isObserved = offers[position].isInterest
        if(isObserved){
            holder.button.tag = true
            holder.button.setImageResource(R.drawable.observe)
        } else{
            holder.button.tag = false
            holder.button.setImageResource(R.drawable.unobserve)
        }
        holder.button.setOnClickListener {
            if(isObserved){
                holder.button.setImageResource(R.drawable.unobserve)
                offerViewModel?.unwatchOffer(offers[position].offerId)
            } else {
                holder.button.setImageResource(R.drawable.observe)
                offerViewModel?.watchOffer(offers[position].offerId)
            }
            isObserved = !isObserved
        }
        if(offers[position].property.images.isNotEmpty()) {
            holder.image.load(URL + "/" + offers[position].property.images[0].path){
                error(R.drawable.baseline_broken_image_24)
            }
        }
        holder.promotion.setOnClickListener{
            Toast.makeText(it.context, "Oferta promowana", Toast.LENGTH_SHORT).show()
        }
        if(offers[position].isPromoted){
            holder.promotion.isVisible = true
        } else {
            holder.promotion.isVisible = false
        }
        holder.city.text = offers[position].property.city
        holder.street.text = offers[position].property.street
        holder.size.text = offers[position].property.area.toString()
        holder.price.text = offers[position].price
        holder.room.text = offers[position].property.numberOfRooms.toString()
        holder.itemView.setOnClickListener {
            onUserClick(offers[position])
        }
    }
}