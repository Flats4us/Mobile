package com.example.flats4us21.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.flats4us21.R
import com.example.flats4us21.URL
import com.example.flats4us21.data.Offer
import com.example.flats4us21.databinding.PropertyRowBinding
import com.example.flats4us21.viewmodels.OfferViewModel

class PropertyAdapter(
    private val ownersOffer : Boolean,
    private var offers : List<Offer>
    , private val onUserClick : (Offer) -> Unit
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
//        holder.button.setImageResource(if(offers[position].isInterest){
//            holder.button.tag = true
//            R.drawable.observe
//        } else{
//            holder.button.tag = false
//            R.drawable.unobserve
//        })
        holder.button.setOnClickListener {
            if(holder.button.tag == true){
                holder.button.setImageResource(R.drawable.unobserve)
                holder.button.tag = false
                offerViewModel?.unwatchOffer(offers[position].offerId)
            } else {
                holder.button.setImageResource(R.drawable.observe)
                holder.button.tag = true
                offerViewModel?.watchOffer(offers[position].offerId)
            }
            notifyDataSetChanged()
        }
        if(offers[position].property.images.isNotEmpty()) {
            holder.image.load(URL + "/" + offers[position].property.images[0].path) {
                error(R.drawable.baseline_broken_image_24)
            }
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