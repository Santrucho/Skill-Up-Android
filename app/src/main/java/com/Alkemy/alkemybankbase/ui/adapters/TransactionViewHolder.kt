package com.Alkemy.alkemybankbase.ui.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.Alkemy.alkemybankbase.data.model.Transaction
import com.Alkemy.alkemybankbase.databinding.TransactionLayoutBinding

class TransactionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding = TransactionLayoutBinding.bind(view)


    fun render(transaction: Transaction, onClickListener: (Transaction) -> Unit) {
        binding.tvAmount.text = transaction.amount
        binding.tvConcept.text = transaction.concept
        binding.tvDate.text = transaction.date.take(10)
        itemView.setOnClickListener { onClickListener(transaction) }
    }
}