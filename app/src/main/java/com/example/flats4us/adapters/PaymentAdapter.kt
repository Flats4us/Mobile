package com.example.flats4us.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.flats4us.R
import com.example.flats4us.data.Payment
import com.example.flats4us.databinding.ItemPaymentBinding


class PaymentAdapter(private val payments: List<Payment>) : RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder>() {
    private var context : Context? = null

    inner class PaymentViewHolder(val binding: ItemPaymentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val typeTextView: TextView = binding.type
        val amountTextView: TextView = binding.amount
        val isPaidTextView: TextView = binding.isPaid
        val paidAtDateTextView: TextView = binding.paidAtDate
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
        context = parent.context
        val binding = ItemPaymentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PaymentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        holder.typeTextView.text = payments[position].paymentType.displayName
        holder.amountTextView.text = payments[position].amount.toString()
        holder.isPaidTextView.text = if (payments[position].isPaid) context?.getString(R.string.yes) else context?.getString(R.string.no)
        holder.paidAtDateTextView.text = payments[position].paymentDate?.split("T")?.get(0) ?: "N/A"
    }

    override fun getItemCount() = payments.size
}
