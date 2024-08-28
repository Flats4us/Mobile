package com.example.flats4us.adapters

import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.flats4us.R
import com.example.flats4us.URL
import com.example.flats4us.data.Image

private const val TAG = "NewImageSliderAdapter"
class NewImageSliderAdapter(
    private val imageObjects: List<Image>,
    private val bitmaps: List<Bitmap>
) : RecyclerView.Adapter<NewImageSliderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image_slider, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position < imageObjects.size) {
            val photo = imageObjects[position]
            val url = URL + "/" + photo.path
            Log.i(TAG, url)
            holder.imageView.load(url) {
                error(R.drawable.baseline_broken_image_24)
            }
        } else {
            val bitmapPosition = position - imageObjects.size
            val bitmap = bitmaps[bitmapPosition]
            holder.imageView.setImageBitmap(bitmap)
        }
    }

    override fun getItemCount(): Int {
        return imageObjects.size + bitmaps.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }
}
