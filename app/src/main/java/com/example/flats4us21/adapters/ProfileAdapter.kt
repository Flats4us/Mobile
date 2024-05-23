package com.example.flats4us21.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.flats4us21.R
import com.example.flats4us21.URL
import com.example.flats4us21.data.Tenant
import com.example.flats4us21.databinding.PofileRowBinding

class ProfileAdapter(
    private val profiles: List<Tenant>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {
    class ProfileViewHolder(binding: PofileRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val image = binding.profilePicture
        val nameAndSurname = binding.nameAndSurname
        val email = binding.mail
        val button = binding.profileButton
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        return ProfileViewHolder(
            binding = PofileRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return profiles.size
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        holder.image.load(URL + "/" + profiles[position].profilePicture.path) {
            error(R.drawable.baseline_broken_image_24)
        }
        holder.nameAndSurname.text = profiles[position].fullName
        holder.email.text = profiles[position].email
        holder.button.setOnClickListener {
            listener.onItemClick(null, holder.itemView, position, 0)
        }
    }
}