package com.example.flats4us21.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.flats4us21.R
import com.example.flats4us21.data.Meeting
import com.example.flats4us21.databinding.CalendarCellBinding

class CalendarAdapter(
    private val daysOfMonth: List<String>,
    private val onCellClickListener: OnCellClickListener,
    private val meetingsLiveData: LiveData<List<Meeting>>,
    private val lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    interface OnCellClickListener {
        fun onCellClick(date: String)
    }
    inner class CalendarViewHolder(val binding: CalendarCellBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val dayOfMonth = binding.cellDay

        init {
            binding.root.setOnClickListener {
                val date = daysOfMonth[adapterPosition]
                onCellClickListener.onCellClick(date)
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

        meetingsLiveData.observe(lifecycleOwner) { meetings ->
            val hasMeeting = hasMeetingOnDay(day, meetings)
            if (hasMeeting) {
                holder.binding.root.setBackgroundResource(R.drawable.background_meeting)
            } else {
                holder.binding.root.setBackgroundResource(R.drawable.background_cell)
            }
        }

        holder.binding.root.setOnClickListener {
            val date = daysOfMonth[holder.adapterPosition]
            onCellClickListener.onCellClick(date)
        }
    }

    private fun hasMeetingOnDay(day: String, meetings: List<Meeting>): Boolean {
        val meetingDay = day.toIntOrNull()
        if (meetingDay != null) {
            for (meeting in meetings) {
                if (meeting.date.dayOfMonth == meetingDay) {
                    return true
                }
            }
        }
        return false
    }
}
