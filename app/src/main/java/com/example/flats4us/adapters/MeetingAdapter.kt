package com.example.flats4us.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flats4us.data.Meeting
import com.example.flats4us.databinding.MeetingRowBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MeetingAdapter(
    private val onUserClick: (meeting: Meeting) -> Unit
) : RecyclerView.Adapter<MeetingAdapter.MeetingViewHolder>() {
    private var meetings: List<Meeting> = emptyList()

    inner class MeetingViewHolder(binding: MeetingRowBinding) :
        RecyclerView.ViewHolder(binding.root){
        val date = binding.time
        val reason = binding.reason

        init {
            binding.root.setOnClickListener{ onUserClick(meetings[bindingAdapterPosition])}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeetingViewHolder {
        return MeetingViewHolder(
            binding = MeetingRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return meetings.size
    }

    override fun onBindViewHolder(holder: MeetingViewHolder, position: Int) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val meetingDate = LocalDateTime.parse(meetings[position].date, formatter).toLocalTime()
        holder.date.text = meetingDate.format(DateTimeFormatter.ofPattern("HH:mm")).toString()
        holder.reason.text = meetings[position].reason
        holder.itemView.setOnClickListener{
            onUserClick(meetings[position])
        }
    }
    fun updateData(newMeetings: List<Meeting>) {
        meetings = newMeetings
        notifyDataSetChanged()
    }
}