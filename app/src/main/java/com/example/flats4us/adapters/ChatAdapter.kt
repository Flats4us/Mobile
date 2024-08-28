package com.example.flats4us.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.flats4us.data.Chat
import com.example.flats4us.databinding.ItemChatBinding

class ChatAdapter(
    private val chats: MutableList<Chat>,
    private val onUserClick: (Chat) -> Unit
) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    inner class ChatViewHolder(binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val emailTextView: TextView = binding.email

        init {
            binding.root.setOnClickListener { onUserClick(chats[bindingAdapterPosition]) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder(
            ItemChatBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.emailTextView.text = chats[position].otherUsername
        holder.itemView.setOnClickListener {
            onUserClick(chats[position])
        }
    }

    fun updateChats(newChats: List<Chat>) {
        chats.clear()
        chats.addAll(newChats)
        notifyDataSetChanged()
    }
}
