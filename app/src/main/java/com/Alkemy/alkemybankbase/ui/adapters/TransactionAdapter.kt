package com.Alkemy.alkemybankbase.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.Alkemy.alkemybankbase.R
import com.Alkemy.alkemybankbase.data.model.Transaction

class TransactionAdapter(
    private val transactionList: List<Transaction>,
    private val onClickListener: (Transaction) -> Unit
) :
    RecyclerView.Adapter<TransactionViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TransactionViewHolder(
            layoutInflater.inflate(
                R.layout.transaction_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val item = transactionList[position]
        holder.render(item, onClickListener)
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }
}
