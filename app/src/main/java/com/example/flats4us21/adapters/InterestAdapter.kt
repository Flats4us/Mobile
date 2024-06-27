package com.example.flats4us21.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.flats4us21.R

class InterestAdapter(private val interests: List<String>) :
    RecyclerView.Adapter<InterestAdapter.InterestViewHolder>()  {
    inner class InterestViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView)  {
        val interestTextView: TextView = itemView.findViewById(R.id.interest)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InterestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.interest_row, parent, false)
        return InterestViewHolder(view)
    }

    override fun onBindViewHolder(holder: InterestViewHolder, position: Int) {
        val interest = interests[position]
        holder.interestTextView.text = interest
    }

    override fun getItemCount(): Int {
        return interests.size
    }
}