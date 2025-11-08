package com.cleanplanet.simple

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class ServiceHistoryAdapter : ListAdapter<ServiceRecord, ServiceHistoryAdapter.ServiceHistoryViewHolder>(
    ServiceHistoryDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceHistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_service_history, parent, false)
        return ServiceHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceHistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ServiceHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textDate: TextView = itemView.findViewById(R.id.textServiceDate)
        private val textPartner: TextView = itemView.findViewById(R.id.textServicePartner)
        private val textService: TextView = itemView.findViewById(R.id.textServiceName)
        private val textQuantity: TextView = itemView.findViewById(R.id.textServiceQuantity)
        private val textPrice: TextView = itemView.findViewById(R.id.textServicePrice)

        fun bind(record: ServiceRecord) {
            textDate.text = record.date
            textPartner.text = record.partnerName
            textService.text = record.serviceName
            textQuantity.text = "Кол-во: ${record.quantity}"
            textPrice.text = "Стоимость: ${record.totalPrice} руб."
        }
    }
}

class ServiceHistoryDiffCallback : DiffUtil.ItemCallback<ServiceRecord>() {
    override fun areItemsTheSame(oldItem: ServiceRecord, newItem: ServiceRecord): Boolean {
        return oldItem.date == newItem.date && oldItem.partnerName == newItem.partnerName
    }

    override fun areContentsTheSame(oldItem: ServiceRecord, newItem: ServiceRecord): Boolean {
        return oldItem == newItem
    }
}