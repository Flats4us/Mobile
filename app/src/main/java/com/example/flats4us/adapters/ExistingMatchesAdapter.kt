package com.example.flats4us.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.flats4us.R
import com.example.flats4us.URL
import com.example.flats4us.data.StudentForMatcher
import com.example.flats4us.databinding.ItemExistingMatchesBinding

class ExistingMatchesAdapter (
    private val profiles: List<StudentForMatcher>,
    private val chatListener: (Int) -> Unit,
) : RecyclerView.Adapter<ExistingMatchesAdapter.ExistingMatchesViewHolder>() {

    class ExistingMatchesViewHolder(binding: ItemExistingMatchesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val image = binding.profilePicture
        val nameAndSurname = binding.nameAndAge
        val button = binding.chatButton
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExistingMatchesViewHolder {
        return ExistingMatchesViewHolder(
            ItemExistingMatchesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = profiles.size

    override fun onBindViewHolder(holder: ExistingMatchesViewHolder, position: Int) {
        val profile = profiles[position]
        val imageUrl = profile.profilePicture?.path?.let { "$URL/$it" } ?: ""

        holder.image.load(imageUrl) {
            error(R.drawable.baseline_broken_image_24)
        }
        holder.nameAndSurname.text = "${profile.name}, ${profile.age}"
        holder.button.setOnClickListener {
            chatListener(position)
        }
    }
}