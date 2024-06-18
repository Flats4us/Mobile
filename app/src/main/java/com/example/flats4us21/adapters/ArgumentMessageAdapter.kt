package com.example.flats4us21.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.flats4us21.R
import com.example.flats4us21.data.ChatMessage
import java.text.SimpleDateFormat
import java.util.Locale

class ArgumentMessageAdapter(
    private val context: Context,
    private var messages: MutableList<ChatMessage>,
    private val loggedInUserId: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_MY_MESSAGE = 1
    private val VIEW_TYPE_USER_MESSAGE = 2
    private val TIME_THRESHOLD = 5 * 60 * 1000

    private val visibleTimestamps = mutableSetOf<Int>()

    override fun getItemViewType(position: Int): Int {
        Log.d("ArgumentMessageAdapter", "sender: ${messages[position].senderId} loggedIn: $loggedInUserId result: ${messages[position].senderId == loggedInUserId} message: ${messages[position].content}")
        return if (messages[position].senderId == loggedInUserId) VIEW_TYPE_MY_MESSAGE else VIEW_TYPE_USER_MESSAGE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_MY_MESSAGE) {
            MyMessageViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_my_message, parent, false)
            )
        } else {
            UserMessageViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_user_message, parent, false)
            )
        }
    }

    override fun getItemCount(): Int = messages.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]

        if (holder is MyMessageViewHolder) {
            holder.bind(message, position)
        } else if (holder is UserMessageViewHolder) {
            holder.bind(message, position)
        }
    }

    inner class MyMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val messageTextView: TextView = view.findViewById(R.id.messageTextView)
        private val timestampTextView: TextView = view.findViewById(R.id.timestampTextView)

        fun bind(message: ChatMessage, position: Int) {
            messageTextView.text = message.content

            val shouldShowTimestamp = shouldShowTimestamp(position)
            val formattedTimestamp = formatTimestamp(message.dateTime, position)
            timestampTextView.visibility = if (shouldShowTimestamp || visibleTimestamps.contains(position)) View.VISIBLE else View.GONE
            timestampTextView.text = formattedTimestamp

            messageTextView.setOnClickListener {
                toggleTimestampVisibility(position)
            }

            itemView.setOnClickListener {
                toggleTimestampVisibility(position)
            }
        }
    }

    inner class UserMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val messageTextView: TextView = view.findViewById(R.id.messageTextView)
        private val timestampTextView: TextView = view.findViewById(R.id.timestampTextView)

        fun bind(message: ChatMessage, position: Int) {
            messageTextView.text = message.content

            val shouldShowTimestamp = shouldShowTimestamp(position)
            val formattedTimestamp = formatTimestamp(message.dateTime, position)
            timestampTextView.visibility = if (shouldShowTimestamp || visibleTimestamps.contains(position)) View.VISIBLE else View.GONE
            timestampTextView.text = formattedTimestamp

            messageTextView.setOnClickListener {
                toggleTimestampVisibility(position)
            }

            itemView.setOnClickListener {
                toggleTimestampVisibility(position)
            }
        }
    }

    private fun toggleTimestampVisibility(position: Int) {
        if (visibleTimestamps.contains(position)) {
            visibleTimestamps.remove(position)
        } else {
            visibleTimestamps.add(position)
        }
        notifyItemChanged(position)
    }

    private fun shouldShowTimestamp(position: Int): Boolean {
        if (position == 0) return true
        val previousMessageTime = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS", Locale.getDefault())
            .parse(messages[position - 1].dateTime)?.time ?: 0
        val currentMessageTime = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS", Locale.getDefault())
            .parse(messages[position].dateTime)?.time ?: 0
        return (currentMessageTime - previousMessageTime) > TIME_THRESHOLD
    }

    private fun formatTimestamp(dateTime: String, position: Int): String {
        val messageTime = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS", Locale.getDefault()).parse(dateTime)
        val currentTime = System.currentTimeMillis()

        val dateFormat: SimpleDateFormat = if (currentTime - messageTime.time > 24 * 60 * 60 * 1000) {
            SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        } else {
            SimpleDateFormat("HH:mm", Locale.getDefault())
        }
        return dateFormat.format(messageTime)
    }

    fun updateMessages(newMessages: List<ChatMessage>) {
        messages = newMessages as MutableList<ChatMessage>
        notifyDataSetChanged()
        Log.d("MessageAdapter", "Updating messages with: $messages")
    }

}