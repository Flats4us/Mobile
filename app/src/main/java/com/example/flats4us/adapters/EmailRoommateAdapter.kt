package com.example.flats4us.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.flats4us.R

class EmailRoommateAdapter (private val emails: MutableList<String>) :
    RecyclerView.Adapter<EmailRoommateAdapter.LinkViewHolder>() {

        inner class LinkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val emailRoommate: TextView = itemView.findViewById(R.id.link)
            val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinkViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.link_row, parent, false)
            return LinkViewHolder(view)
        }

        override fun getItemCount(): Int {
            return emails.size
        }

        override fun onBindViewHolder(holder: LinkViewHolder, position: Int) {
            val link = emails[position]
            holder.emailRoommate.text = link
            holder.deleteButton.setOnClickListener{
                emails.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, itemCount)
            }
        }
}