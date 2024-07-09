package com.example.flats4us21.adapters

import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.flats4us21.R
import com.example.flats4us21.data.Notification
import com.example.flats4us21.data.utils.QuestionTranslator
import com.example.flats4us21.databinding.NotificationRowBinding
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class NotificationAdapter(
    private var notifications: MutableList<Notification>,
    private val context: Context,
    private val onUserClick : (Notification) -> Unit
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    inner class NotificationViewHolder(binding: NotificationRowBinding) :
        RecyclerView.ViewHolder(binding.root){
        val titleTextView: TextView = itemView.findViewById(R.id.title)
        val descriptionTextView: TextView = itemView.findViewById(R.id.description)
        val timeTextView: TextView = itemView.findViewById(R.id.time)

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
        holder.titleTextView.text = QuestionTranslator.translateNotification(notification.title, context)
        holder.descriptionTextView.text = QuestionTranslator.translateNotification(notification.body, context)
        if(notification.read) {
            holder.titleTextView.setTypeface(null, Typeface.NORMAL)
            holder.descriptionTextView.setTypeface(null, Typeface.NORMAL)
        } else {
            holder.titleTextView.setTypeface(null, Typeface.BOLD)
            holder.descriptionTextView.setTypeface(null, Typeface.BOLD)
        }

        val localDateTime = LocalDateTime.parse(notification.dateTime, DateTimeFormatter.ISO_DATE_TIME)

        val localDate: LocalDate = localDateTime.toLocalDate()

        val currentDate: LocalDate = LocalDate.now()

        val daysBetween = ChronoUnit.DAYS.between(localDate, currentDate)
        holder.timeTextView.text = if(daysBetween < 1) "dzisiaj" else "$daysBetween dni temu"
    }

    fun updateNotifications(newNotifications: List<Notification>) {
        Log.d("NotificationAdapter", newNotifications.toString())
        notifications = newNotifications as MutableList<Notification>
        Log.d("NotificationAdapter", notifications.toString())
        notifyDataSetChanged()
    }
}