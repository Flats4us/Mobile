package com.example.flats4us21.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.flats4us21.R
import com.example.flats4us21.URL
import com.example.flats4us21.data.Image

private const val TAG = "CoilImagesAdapter"
class CoilImagesAdapter(
    private var photos: MutableList<Image>,
    private val imageListener: (Int) -> Unit
) :
    RecyclerView.Adapter<CoilImagesAdapter.CoilImagesViewHolder>() {

    inner class CoilImagesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.photoImageView)
        val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoilImagesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_photo, parent, false)
        return CoilImagesViewHolder(view)
    }

    override fun onBindViewHolder(holder: CoilImagesViewHolder, position: Int) {
        val photo = photos[position]
        val url = URL + "/" + photo.path
        Log.i(TAG, url)
        holder.imageView.load(url) {
            error(R.drawable.baseline_broken_image_24)
        }
        holder.deleteButton.setOnClickListener {
            imageListener(position)
            photos.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount)
        }
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    fun updateData(newImageUris: MutableList<Image>) {
        photos = newImageUris
        notifyDataSetChanged()
    }
}
