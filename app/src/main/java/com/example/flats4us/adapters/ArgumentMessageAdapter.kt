package com.example.flats4us.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.flats4us.R
import com.example.flats4us.URL
import com.example.flats4us.data.ChatMessage
import com.example.flats4us.data.GroupChatUser
import com.example.flats4us.databinding.ItemMyMessageBinding
import com.example.flats4us.databinding.ItemUserMessageBinding
import com.google.android.material.imageview.ShapeableImageView
import java.text.SimpleDateFormat
import java.util.Locale

private const val TAG = "ArgumentMessageAdapter"
class ArgumentMessageAdapter(
    private var messages: MutableList<ChatMessage>,
    private val loggedInUserId: Int,
    private var groupChatUsers: List<GroupChatUser>?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val MAX_WAIT_TIME = 10 * 60 * 1000
    private val visibleTimestamps = mutableSetOf<Int>()

    override fun getItemViewType(position: Int): Int {
        Log.d(TAG, "sender: ${messages[position].senderId} loggedIn: $loggedInUserId result: ${messages[position].senderId == loggedInUserId} message: ${messages[position].content}")
        return when (messages[position].senderId == loggedInUserId) {
            true -> R.layout.item_my_message
            false -> R.layout.item_user_message
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == R.layout.item_my_message) {
            MyMessageViewHolder(ItemMyMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            UserMessageViewHolder(ItemUserMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun getItemCount(): Int {
        return messages.size
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]

        if (holder is MyMessageViewHolder) {
            holder.bindData(message, position)
        } else if (holder is UserMessageViewHolder) {
            holder.bindData(message, position)
        }
    }

    inner class MyMessageViewHolder(binding: ItemMyMessageBinding) : RecyclerView.ViewHolder(binding.root) {
        private val messageTextView: TextView = binding.messageTextView
        private val timestampTextView: TextView = binding.timestampTextView

        fun bindData(message: ChatMessage, position: Int) {
            messageTextView.text = message.content
            val shouldShowTimestamp = shouldShowTimestamp(position)
            val formattedTimestamp = formatTimestamp(message.dateTime)
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

    inner class UserMessageViewHolder(binding: ItemUserMessageBinding) : RecyclerView.ViewHolder(binding.root) {
        private val messageTextView: TextView = binding.messageTextView
        private val timestampTextView: TextView = binding.timestampTextView
        private val userPhoto: ShapeableImageView = binding.userPhoto
        private val userName: TextView = binding.userName

        fun bindData(message: ChatMessage, position: Int) {
            messageTextView.text = message.content
            val shouldShowTimestamp = shouldShowTimestamp(position)
            val formattedTimestamp = formatTimestamp(message.dateTime)
            timestampTextView.visibility = if (shouldShowTimestamp || visibleTimestamps.contains(position)) View.VISIBLE else View.GONE
            timestampTextView.text = formattedTimestamp
            val user = groupChatUsers?.find { it.userId == message.senderId }
            userName.text = user?.fullName ?: ""
            if (position - 1 >= 0 && messages[position - 1].senderId == message.senderId) {
                userName.visibility = View.GONE
            } else {
                userName.visibility = View.VISIBLE
            }
            val url = "$URL/${user?.profilePicture?.path ?: ""}"
            userPhoto.load(url) {
                error(R.drawable.baseline_person_24)
            }
            if (position < messages.size - 1 && messages[position + 1].senderId == message.senderId) {
                userPhoto.visibility = View.INVISIBLE
            } else {
                userPhoto.visibility = View.VISIBLE
            }
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
        val previousMessageTime = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS", Locale.getDefault()).parse(messages[position - 1].dateTime)?.time ?: 0
        val currentMessageTime = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS", Locale.getDefault()).parse(messages[position].dateTime)?.time ?: 0
        return (currentMessageTime - previousMessageTime) > MAX_WAIT_TIME
    }

    private fun formatTimestamp(dateTime: String): String {
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
        Log.d(TAG, "Updating messages with: $messages")
    }

    fun updateChatUsers(newChatUsers: List<GroupChatUser>) {
        groupChatUsers = newChatUsers
        notifyDataSetChanged()
    }
}
