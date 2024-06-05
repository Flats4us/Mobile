package com.example.flats4us21.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.flats4us21.data.Payment
import com.example.flats4us21.databinding.ItemPaymentBinding

class PaymentAdapter(private val payments: List<Payment>) : RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder>() {

    inner class PaymentViewHolder(val binding: ItemPaymentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val typeTextView: TextView = binding.type
        val amountTextView: TextView = binding.amount
        val isPaidTextView: TextView = binding.isPaid
        val paidAtDateTextView: TextView = binding.paidAtDate
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
        val binding = ItemPaymentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PaymentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        holder.typeTextView.text = payments[position].paymentType.displayName
        holder.amountTextView.text = payments[position].amount.toString()
        holder.isPaidTextView.text = if (payments[position].isPaid) "Tak" else "Nie"
        holder.paidAtDateTextView.text = payments[position].paymentDate?.split("T")?.get(0) ?: "N/A"
    }

    override fun getItemCount() = payments.size
}
