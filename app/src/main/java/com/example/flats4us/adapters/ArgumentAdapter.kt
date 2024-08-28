package com.example.flats4us.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.flats4us.data.Argument
import com.example.flats4us.databinding.ItemArgumentBinding

private const val TAG = "ArgumentAdapter"
class ArgumentAdapter(
    private var arguments: MutableList<Argument>,
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
        holder.descriptionTextView.text = arguments[position].title
        holder.itemView.setOnClickListener {
            onUserClick(arguments[position])
        }
    }

    fun updateArguments(newArguments: List<Argument>) {
        arguments = newArguments as MutableList<Argument>
        notifyDataSetChanged()
        Log.d(TAG, "Updating arguments with: $arguments")
    }
}
