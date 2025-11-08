package com.cleanplanet.simple

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class SuppliersAdapter(private val onSupplierClick: (Supplier) -> Unit) :
    ListAdapter<Supplier, SuppliersAdapter.SupplierViewHolder>(SupplierDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupplierViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_supplier, parent, false)
        return SupplierViewHolder(view)
    }

    override fun onBindViewHolder(holder: SupplierViewHolder, position: Int) {
        val supplier = getItem(position)
        holder.bind(supplier)
        holder.itemView.setOnClickListener { onSupplierClick(supplier) }
    }

    class SupplierViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textName: TextView = itemView.findViewById(R.id.textSupplierName)
        private val textType: TextView = itemView.findViewById(R.id.textSupplierType)
        private val textContact: TextView = itemView.findViewById(R.id.textSupplierContact)
        private val textPhone: TextView = itemView.findViewById(R.id.textSupplierPhone)
        private val textEmail: TextView = itemView.findViewById(R.id.textSupplierEmail)
        private val textRating: TextView = itemView.findViewById(R.id.textSupplierRating)

        fun bind(supplier: Supplier) {
            textName.text = supplier.name
            textType.text = "Тип: ${supplier.type}"
            textContact.text = "Контакт: ${supplier.contactPerson ?: "Не указано"}"
            textPhone.text = supplier.phone ?: "Телефон не указан"
            textEmail.text = supplier.email ?: "Email не указан"
            textRating.text = "Рейтинг: ${supplier.rating}/5.0"

            val ratingColor = when {
                supplier.rating >= 4.5 -> android.R.color.holo_green_dark
                supplier.rating >= 3.5 -> android.R.color.holo_orange_dark
                else -> android.R.color.holo_red_dark
            }
            textRating.setTextColor(ContextCompat.getColor(itemView.context, ratingColor))
        }
    }
}

class SupplierDiffCallback : DiffUtil.ItemCallback<Supplier>() {
    override fun areItemsTheSame(oldItem: Supplier, newItem: Supplier): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Supplier, newItem: Supplier): Boolean {
        return oldItem == newItem
    }
}