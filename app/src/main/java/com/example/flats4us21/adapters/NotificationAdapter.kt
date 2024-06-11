package com.example.flats4us21.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.flats4us21.R
import com.example.flats4us21.data.Notification
import com.example.flats4us21.databinding.NotificationRowBinding

//TODO: Correct changing fragment
class NotificationAdapter(
    private val notifications: MutableList<Notification>,
    private val onUserClick : (Notification) -> Unit
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    inner class NotificationViewHolder(binding: NotificationRowBinding) :
        RecyclerView.ViewHolder(binding.root){
        val titleTextView: TextView = itemView.findViewById(R.id.title)
        val descriptionTextView: TextView = itemView.findViewById(R.id.description)

        init {
            binding.root.setOnClickListener { onUserClick(notifications[bindingAdapterPosition]) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        return NotificationViewHolder(
            binding = NotificationRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return notifications.size
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification : Notification = notifications[position]
        holder.titleTextView.text = notification.title
        holder.descriptionTextView.text = notification.body
    }
}