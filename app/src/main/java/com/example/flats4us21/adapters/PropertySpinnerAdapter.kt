package com.example.flats4us21.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.flats4us21.R
import com.example.flats4us21.data.dto.Property

class PropertySpinnerAdapter(
    context: Context,
    private val items: List<Property>
) : ArrayAdapter<Property>(context, R.layout.property_dropdown_item, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createCustomView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createCustomView(position, convertView, parent)
    }

    private fun createCustomView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.property_dropdown_item, parent, false)

        val item = items[position]

        val imageView = view.findViewById<ImageView>(R.id.imageView)
        val cityTextView = view.findViewById<TextView>(R.id.city)
        val streetTextView = view.findViewById<TextView>(R.id.street)
        val buildingNumberTextView = view.findViewById<TextView>(R.id.building)

        imageView.setImageBitmap(item.images[0])
        cityTextView.text = item.city
        streetTextView.text = item.street
        buildingNumberTextView.text = item.buildingNumber

        return view
    }
}