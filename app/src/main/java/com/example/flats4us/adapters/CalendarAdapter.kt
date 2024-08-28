package com.example.flats4us.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flats4us.R
import com.example.flats4us.data.Meeting
import com.example.flats4us.databinding.CalendarCellBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CalendarAdapter(
    private val daysOfMonth: List<String>,
    private val onCellClick: OnCellClick,
    private val meetings: List<Meeting>
) : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    interface OnCellClick {
        fun onCellClick(date: String)
    }
    inner class CalendarViewHolder(val binding: CalendarCellBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val dayOfMonth = binding.cellDay

        init {
            binding.root.setOnClickListener {
                val date = daysOfMonth[bindingAdapterPosition]
                onCellClick.onCellClick(date)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CalendarCellBinding.inflate(inflater, parent, false)
        val layoutParams = binding.root.layoutParams
        layoutParams.height = (parent.height * 0.166666666).toInt()
        return CalendarViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return daysOfMonth.size
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val day = daysOfMonth[position]
        holder.dayOfMonth.text = day
        val hasMeeting = hasMeetingOnDay(day, meetings)
        if (hasMeeting) {
            holder.binding.root.setBackgroundResource(R.drawable.background_meeting)
        } else {
            holder.binding.root.setBackgroundResource(R.drawable.background_cell)
        }
        holder.binding.root.setOnClickListener {
            val date = daysOfMonth[holder.adapterPosition]
            onCellClick.onCellClick(date)
        }
    }

    private fun hasMeetingOnDay(day: String, meetings: List<Meeting>): Boolean {
        val meetingDay = day.toIntOrNull()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        if (meetingDay != null) {
            for (meeting in meetings) {
                val meetingDate = LocalDateTime.parse(meeting.date, formatter).toLocalDate()
                if (meetingDate.dayOfMonth == meetingDay) {
                    return true
                }
            }
        }
        return false
    }
}
