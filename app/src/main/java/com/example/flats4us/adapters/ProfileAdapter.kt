package com.example.flats4us.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.flats4us.R
import com.example.flats4us.URL
import com.example.flats4us.data.Tenant
import com.example.flats4us.databinding.PofileRowBinding

class ProfileAdapter(
    private val profiles: List<Tenant>,
    private val profileListener: (Int) -> Unit,
    private val opinionListener: ((Int) -> Unit)?,
    private val isRentFinished: Boolean
) : RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {

    class ProfileViewHolder(binding: PofileRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val image = binding.profilePicture
        val nameAndSurname = binding.nameAndSurname
        val button = binding.profileButton
        val opinionButton = binding.addOpinion
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        return ProfileViewHolder(
            PofileRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = profiles.size

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val profile = profiles[position]
        val imageUrl = profile.profilePicture?.path?.let { "$URL/$it" } ?: ""

        holder.image.load(imageUrl) {
            error(R.drawable.baseline_broken_image_24)
        }
        holder.nameAndSurname.text = profile.fullName
        holder.button.setOnClickListener {
            profileListener(position)
        }
        if(opinionListener != null){
        holder.opinionButton.setOnClickListener {
            opinionListener!!(position)
        }
            }
    }
}
