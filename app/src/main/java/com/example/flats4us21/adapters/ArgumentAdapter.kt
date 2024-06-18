package com.example.flats4us21.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.flats4us21.data.Argument
import com.example.flats4us21.databinding.ItemArgumentBinding

class ArgumentAdapter(
    private val arguments: MutableList<Argument>,
    private val onUserClick: (Argument) -> Unit
) : RecyclerView.Adapter<ArgumentAdapter.ArgumentViewHolder>() {

    inner class ArgumentViewHolder(binding: ItemArgumentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val descriptionTextView: TextView = binding.description

        init {
            binding.root.setOnClickListener { onUserClick(arguments[bindingAdapterPosition]) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArgumentViewHolder {
        return ArgumentViewHolder(
            ItemArgumentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return arguments.size
    }

    override fun onBindViewHolder(holder: ArgumentViewHolder, position: Int) {
        holder.descriptionTextView.text = arguments[position].description
        holder.itemView.setOnClickListener {
            onUserClick(arguments[position])
        }
    }

    fun updateArguments(newArguments: List<Argument>) {
        arguments.clear()
        arguments.addAll(newArguments)
        notifyDataSetChanged()
    }
}
