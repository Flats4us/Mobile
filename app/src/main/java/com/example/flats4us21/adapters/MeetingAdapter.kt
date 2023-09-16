package com.example.flats4us21.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flats4us21.data.Meeting
import com.example.flats4us21.databinding.MeetingRowBinding
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
            binding.root.setOnClickListener{ onUserClick(meetings[adapterPosition])}
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
        holder.date.text = meetings[position].date.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")).toString()
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